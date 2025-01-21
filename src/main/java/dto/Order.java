package dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private String id;
    private LocalDate date;
    private String custId;
    private Double totDiscount;
    private Double billTotal;
    private List<OrderProducts> orderProducts;
}
