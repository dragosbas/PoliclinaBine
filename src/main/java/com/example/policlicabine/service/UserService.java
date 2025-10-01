package com.example.policlicabine.service;

import com.example.policlicabine.common.Result;
import com.example.policlicabine.dto.UserDto;
import com.example.policlicabine.entity.User;
import com.example.policlicabine.entity.enums.UserRole;
import com.example.policlicabine.event.UserCreated;
import com.example.policlicabine.mapper.UserMapper;
import com.example.policlicabine.repository.UserRepository;
import com.example.policlicabine.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing User entities.
 *
 * Architecture:
 * - Extends BaseServiceImpl for common CRUD operations (findById, validateExists, getEntityById)
 * - Only uses UserRepository (single responsibility)
 * - Provides validation methods for other services
 * - Follows service-to-service communication pattern
 *
 * Inherited Methods (from BaseServiceImpl):
 * - findById(UUID) → Result&lt;UserDto&gt;
 * - validateExists(UUID) → Result&lt;Void&gt;
 * - getEntityById(UUID) → User
 * - getEntitiesByIds(List&lt;UUID&gt;) → List&lt;User&gt;
 * - findAll() → Result&lt;List&lt;UserDto&gt;&gt;
 */
@Service
@Slf4j
@Transactional
public class UserService extends BaseServiceImpl<User, UserDto, UUID> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(UserRepository userRepository, UserMapper userMapper,
                      ApplicationEventPublisher eventPublisher) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.eventPublisher = eventPublisher;
    }

    @Override
    protected UserDto toDto(User entity) {
        return userMapper.toDto(entity);
    }

    @Override
    protected String getEntityName() {
        return "User";
    }

    @Override
    protected void updateEntityFromDto(User entity, UserDto dto) {
        // Update mutable fields (NOT userId or username - username is immutable)
        if (dto.getFullName() != null) {
            entity.setFullName(dto.getFullName().trim());
        }
        if (dto.getRole() != null) {
            entity.setRole(dto.getRole());
        }
    }

    /**
     * Creates a new user.
     *
     * @param username Username (must be unique)
     * @param fullName Full name
     * @param role User role
     * @return Result containing UserDto or error message
     */
    public Result<UserDto> createUser(String username, String fullName, UserRole role) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Result.failure("Username is required");
            }
            if (role == null) {
                return Result.failure("User role is required");
            }

            // Check for duplicate username
            if (userRepository.existsByUsername(username.trim())) {
                return Result.failure("Username already exists");
            }

            User user = User.builder()
                .username(username.trim())
                .fullName(fullName != null ? fullName.trim() : null)
                .role(role)
                .build();

            User savedUser = userRepository.save(user);

            // Publish domain event
            eventPublisher.publishEvent(new UserCreated(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getFullName(),
                savedUser.getRole()
            ));

            log.info("User created: {} with role {}", username, role);

            return Result.success(userMapper.toDto(savedUser));

        } catch (Exception e) {
            log.error("Error creating user", e);
            return Result.failure("Failed to create user: " + e.getMessage());
        }
    }

    /**
     * Finds a user by ID.
     * Delegates to inherited findById() method from BaseServiceImpl.
     *
     * @param userId User identifier
     * @return Result containing UserDto or error message
     */
    @Transactional(readOnly = true)
    public Result<UserDto> findUserById(UUID userId) {
        return findById(userId);
    }

    /**
     * Finds a user by username.
     *
     * @param username Username
     * @return Result containing UserDto or error message
     */
    @Transactional(readOnly = true)
    public Result<UserDto> findUserByUsername(String username) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return Result.failure("Username is required");
            }

            User user = userRepository.findByUsername(username.trim()).orElse(null);
            if (user == null) {
                return Result.failure("User not found with username: " + username);
            }

            return Result.success(userMapper.toDto(user));

        } catch (Exception e) {
            log.error("Error finding user by username", e);
            return Result.failure("Failed to find user: " + e.getMessage());
        }
    }

    /**
     * Finds all users with a specific role.
     *
     * @param role User role
     * @return Result containing list of UserDto or error message
     */
    @Transactional(readOnly = true)
    public Result<List<UserDto>> findUsersByRole(UserRole role) {
        try {
            if (role == null) {
                return Result.failure("Role is required");
            }

            List<User> users = userRepository.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());

            List<UserDto> userDtos = users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());

            return Result.success(userDtos);

        } catch (Exception e) {
            log.error("Error finding users by role", e);
            return Result.failure("Failed to find users: " + e.getMessage());
        }
    }

    // ============= INTERNAL METHODS FOR SERVICE-TO-SERVICE COMMUNICATION =============
    // Note: The following methods are inherited from BaseServiceImpl:
    // - getEntityById(UUID) → User
    // - validateExists(UUID) → Result<Void>
    // - getEntitiesByIds(List<UUID>) → List<User>

    /**
     * INTERNAL: Validates that a user exists.
     * Delegates to inherited validateExists() method from BaseServiceImpl.
     * Used by other services (e.g., BillingService) to validate user references.
     *
     * @param userId User identifier
     * @return Result success if user exists, failure with message otherwise
     */
    @Transactional(readOnly = true)
    public Result<Void> validateUserExists(UUID userId) {
        return validateExists(userId);
    }
}
