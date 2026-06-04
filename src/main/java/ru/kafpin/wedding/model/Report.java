package ru.kafpin.wedding.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Report {
    @NonNull
    private Long id;
    @NonNull
    private BigDecimal totalBudget;
    @NonNull
    private Project project;
    private Long completedTasksCount;
    private Long amountOfGuests;
    private List<UsedContractor> contractors;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
