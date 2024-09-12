package model;

import lombok.*;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String id;
    private String custId;
    private LocalDate date;
    private Integer totDiscount;
    private Double billTotal;
}
