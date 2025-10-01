package com.example.policlicabine.service;

import com.example.policlicabine.common.Result;
import com.example.policlicabine.dto.AnswerDto;
import com.example.policlicabine.entity.Answer;
import com.example.policlicabine.entity.AppointmentSession;
import com.example.policlicabine.entity.Consultation;
import com.example.policlicabine.entity.Question;
import com.example.policlicabine.event.AnswerDeleted;
import com.example.policlicabine.event.AnswerSaved;
import com.example.policlicabine.event.AnswerUpdated;
import com.example.policlicabine.mapper.AnswerMapper;
import com.example.policlicabine.repository.AnswerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing Answer entities.
 * Answers link questions to appointment sessions with proper validation.
 *
 * Architecture:
 * - Only uses AnswerRepository (single responsibility)
 * - Calls AppointmentSessionService and QuestionService for validation and entity access
 * - Uses EntityGraph to prevent N+1 queries when loading answers
 * - Uses EntityManager.getReference() to avoid unnecessary DB hits
 * - Follows service-to-service communication pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AnswerService {

    // Only our repository - single responsibility principle
    private final AnswerRepository answerRepository;

    // Services for validation and entity access
    private final AppointmentSessionService appointmentSessionService;
    private final QuestionService questionService;

    private final AnswerMapper answerMapper;
    private final ApplicationEventPublisher eventPublisher;

    // EntityManager for creating entity references without DB hits
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Saves an answer to a question for a specific session.
     * Validates that the question belongs to one of the session's consultations.
     *
     * Architecture notes:
     * - Uses QuestionService to get question entity with consultation (EntityGraph)
     * - Uses AppointmentSessionService to validate question belongs to session
     * - Uses EntityManager.getReference() for session FK (no DB hit)
     *
     * @param sessionId AppointmentSession identifier
     * @param questionId Question identifier
     * @param answerText The answer text
     * @return Result containing AnswerDto or error message
     */
    public Result<AnswerDto> saveAnswer(UUID sessionId, UUID questionId, String answerText) {
        try {
            if (sessionId == null) {
                return Result.failure("Session ID is required");
            }

            if (questionId == null) {
                return Result.failure("Question ID is required");
            }

            // Answer text can be empty (unanswered questions are allowed)

            // Get question entity with consultation loaded (EntityGraph)
            Question question = questionService.getEntityWithConsultation(questionId);
            if (question == null) {
                return Result.failure("Question not found");
            }

            Consultation questionConsultation = question.getConsultation();
            if (questionConsultation == null) {
                return Result.failure("Question has no consultation");
            }

            // Critical validation: Question must belong to one of the session's consultations
            Result<Void> validationResult = appointmentSessionService
                .validateQuestionBelongsToSession(sessionId, questionConsultation.getConsultationId());
            if (validationResult.isFailure()) {
                return Result.failure(validationResult.getErrorMessage());
            }

            // Use EntityManager.getReference() for session FK (no DB hit)
            AppointmentSession sessionRef = entityManager.getReference(AppointmentSession.class, sessionId);

            Answer answer = Answer.builder()
                .session(sessionRef)
                .question(question)
                .consultation(questionConsultation)
                .answerText(answerText != null ? answerText.trim() : null)
                .build();

            Answer savedAnswer = answerRepository.save(answer);

            // Publish domain event
            eventPublisher.publishEvent(new AnswerSaved(
                savedAnswer.getAnswerId(),
                sessionId,
                questionId,
                questionConsultation.getConsultationId(),
                savedAnswer.getAnswerText()
            ));

            log.info("Answer saved: {} for session {} and question {}",
                savedAnswer.getAnswerId(), sessionId, questionId);

            return Result.success(answerMapper.toDto(savedAnswer));

        } catch (Exception e) {
            log.error("Error saving answer", e);
            return Result.failure("Failed to save answer: " + e.getMessage());
        }
    }

    /**
     * Retrieves all answers for a specific session.
     *
     * Architecture notes:
     * - Uses EntityGraph to load all relationships for DTO mapping (prevents N+1 queries)
     *
     * @param sessionId Session identifier
     * @return Result containing list of AnswerDto or error message
     */
    @Transactional(readOnly = true)
    public Result<List<AnswerDto>> getAnswersForSession(UUID sessionId) {
        try {
            if (sessionId == null) {
                return Result.failure("Session ID is required");
            }

            // Use EntityGraph to load all relationships (session, question, consultation)
            List<Answer> answers = answerRepository.findWithRelationshipsBySessionSessionId(sessionId);

            List<AnswerDto> answerDtos = answers.stream()
                .map(answerMapper::toDto)
                .collect(Collectors.toList());

            return Result.success(answerDtos);

        } catch (Exception e) {
            log.error("Error getting answers for session", e);
            return Result.failure("Failed to get answers: " + e.getMessage());
        }
    }

    /**
     * Retrieves all answers for a specific session and consultation combination.
     * Useful for organizing answers by consultation type.
     *
     * Architecture notes:
     * - Uses EntityGraph to load all relationships for DTO mapping (prevents N+1 queries)
     *
     * @param sessionId Session identifier
     * @param consultationId Consultation identifier
     * @return Result containing list of AnswerDto or error message
     */
    @Transactional(readOnly = true)
    public Result<List<AnswerDto>> getAnswersForSessionAndConsultation(
            UUID sessionId, UUID consultationId) {
        try {
            if (sessionId == null) {
                return Result.failure("Session ID is required");
            }
            if (consultationId == null) {
                return Result.failure("Consultation ID is required");
            }

            // Use EntityGraph to load all relationships (session, question, consultation)
            List<Answer> answers = answerRepository
                .findWithRelationshipsBySessionSessionIdAndConsultationConsultationId(sessionId, consultationId);

            List<AnswerDto> answerDtos = answers.stream()
                .map(answerMapper::toDto)
                .collect(Collectors.toList());

            return Result.success(answerDtos);

        } catch (Exception e) {
            log.error("Error getting answers for session and consultation", e);
            return Result.failure("Failed to get answers: " + e.getMessage());
        }
    }

    /**
     * Updates an existing answer's text.
     *
     * @param answerId Answer identifier
     * @param newAnswerText New answer text
     * @return Result containing updated AnswerDto or error message
     */
    public Result<AnswerDto> updateAnswer(UUID answerId, String newAnswerText) {
        try {
            if (answerId == null) {
                return Result.failure("Answer ID is required");
            }

            Answer answer = answerRepository.findById(answerId)
                .orElse(null);
            if (answer == null) {
                return Result.failure("Answer not found");
            }

            // Store old text for event
            String oldAnswerText = answer.getAnswerText();

            answer.setAnswerText(newAnswerText != null ? newAnswerText.trim() : null);
            Answer savedAnswer = answerRepository.save(answer);

            // Publish domain event
            eventPublisher.publishEvent(new AnswerUpdated(
                answerId,
                oldAnswerText,
                newAnswerText != null ? newAnswerText.trim() : null
            ));

            log.info("Answer updated: {}", answerId);

            return Result.success(answerMapper.toDto(savedAnswer));

        } catch (Exception e) {
            log.error("Error updating answer", e);
            return Result.failure("Failed to update answer: " + e.getMessage());
        }
    }

    /**
     * Deletes an answer.
     *
     * @param answerId Answer identifier
     * @return Result containing success flag or error message
     */
    public Result<Void> deleteAnswer(UUID answerId) {
        try {
            if (answerId == null) {
                return Result.failure("Answer ID is required");
            }

            // Get answer for event data before deletion
            Answer answer = answerRepository.findById(answerId).orElse(null);
            if (answer == null) {
                return Result.failure("Answer not found");
            }

            // Store data for event before deletion
            UUID sessionId = answer.getSession() != null ? answer.getSession().getSessionId() : null;
            UUID questionId = answer.getQuestion() != null ? answer.getQuestion().getQuestionId() : null;

            answerRepository.deleteById(answerId);

            // Publish domain event
            eventPublisher.publishEvent(new AnswerDeleted(
                answerId,
                sessionId,
                questionId
            ));

            log.info("Answer deleted: {}", answerId);

            return Result.success(null);

        } catch (Exception e) {
            log.error("Error deleting answer", e);
            return Result.failure("Failed to delete answer: " + e.getMessage());
        }
    }
}
