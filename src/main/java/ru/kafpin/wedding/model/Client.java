package ru.kafpin.wedding.model;
import lombok.*;

import java.time.*;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Client {
    @NonNull
    private Long id;
    @NonNull
    private String phoneNumber;
    private String email;
    @NonNull
    private String name;
    @NonNull
    private String lastname;
    private String middleName;
    @NonNull
    private String gender;
    @NonNull
    private LocalDate dateOfRegistration;
    @NonNull
    private String placeOfBirth;
    @NonNull
    private String series;
    @NonNull
    private String number;
    @NonNull
    private String issuedBy;
    @NonNull
    private LocalDate whenIssued;
    @NonNull
    private String registrationAdress;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Override
    public String toString() {
        if (middleName != null && !middleName.trim().isEmpty()) {
            return lastname + " " + name + " " + middleName;
        } else {
            return lastname + " " + name;
        }
    }
}
