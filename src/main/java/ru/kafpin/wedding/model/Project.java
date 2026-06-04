package ru.kafpin.wedding.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class Project {
    @NonNull
    private Long id;
    @NonNull
    private LocalDateTime weddingDate;
    @NonNull
    private String status;
    private String wishesForWedding;
    private List<Guest> guests;
    private List<Task> tasks;
    private List<ProjectClient> projectClients;
    private List<Contractor> contractors;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Override
    public String toString(){
        if(weddingDate!=null && projectClients!=null && !projectClients.isEmpty()){
            return weddingDate+" | "+projectClients.getFirst()+" & "+projectClients.getLast();
        }
        else {
            return weddingDate.toString();
        }
    }
}
