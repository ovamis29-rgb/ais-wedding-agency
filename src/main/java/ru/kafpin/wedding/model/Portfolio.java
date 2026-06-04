package ru.kafpin.wedding.model;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Portfolio {
    @NonNull
    private Long id;
    @NonNull
    private String description;
    @NonNull
    private String imgUrl;
    @NonNull
    private Contractor contractor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
