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
public class ContractorService {
    private Long id;
    @NonNull
    private String service;
    private BigDecimal price;
    private BigDecimal prepayment;
    @NonNull
    private Contractor contractor;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @Override
    public String toString(){
        if(prepayment!=null){
            return service+"|"+price+"|"+prepayment;
        }else {
            return service+"|"+price;
        }
    }
}
