package ru.kafpin.wedding.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectClient {
    private Long id;
    @NonNull
    private Project project;
    @NonNull
    private Client client;
    @NonNull
    private String role;
    @Override
    public String toString(){
        return client.getName()+" "+client.getLastname();
    }
}
