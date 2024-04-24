package com.jubasbackend.domain.entity;

import com.jubasbackend.domain.entity.enums.Rating;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "tb_feedback")
public class Feedback {

    @Id
    @Column(name = "appointment_id")
    private UUID id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String comment;

    private Rating rating;

    private Instant createdAt;

}
