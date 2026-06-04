package ru.kafpin.wedding.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Guest {
    @NonNull
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    private String middleName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
