package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProducts {
    private String orderId;
    private String ItemCode;
    private Integer orderQty;
    private Double discount;
}
