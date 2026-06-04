package ru.kafpin.wedding.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class UsedContractor {
    private Long id;
    @NonNull
    private Contractor contractor;
    @NonNull
    private Project project;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
