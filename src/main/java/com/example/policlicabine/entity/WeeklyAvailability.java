package com.example.policlicabine.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "doctor_availability", indexes = {
    @Index(name = "idx_avail_doctor", columnList = "doctor_id"),
    @Index(name = "idx_avail_day", columnList = "dayOfWeek")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyAvailability {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private Integer dayOfWeek; // 1=Monday, 7=Sunday

    @Column(nullable = false)
    private String startTime; // Format: "HH:mm"

    @Column(nullable = false)
    private String endTime; // Format: "HH:mm"

    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveTo;

    @PrePersist
    void generateId() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeeklyAvailability)) return false;
        WeeklyAvailability that = (WeeklyAvailability) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "WeeklyAvailability{" +
                "id=" + id +
                ", dayOfWeek=" + dayOfWeek +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
