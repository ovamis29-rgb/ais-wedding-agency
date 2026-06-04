package ru.kafpin.wedding.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contractor {
    @NonNull
    private Long id;
    @NonNull
    private String phoneNumber;
    private String email;
    @NonNull
    private String name;
    private String lastname;
    private List<Portfolio> portfolio;
    private List<ContractorService> services;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Override
    public String toString() {
        if(lastname!=null){
            return name+" "+lastname+" "+phoneNumber;
        }
        else{
            return name+" "+phoneNumber;
        }
    }
}
