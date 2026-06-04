package ru.kafpin.wedding.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Task {
    @NonNull
    private Long id;
    @NonNull
    private ContractorService contractorService;
    @NonNull
    private BigDecimal price;
    @NonNull
    private String status;
    @NonNull
    private LocalDateTime deadline;
    private Integer priority;
    @NonNull
    private Project project;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getGoal() {
        return contractorService != null ? contractorService.getService() : "";
    }
}
