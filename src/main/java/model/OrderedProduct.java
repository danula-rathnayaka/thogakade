package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderedProduct {
    private String itemCode;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Integer orderQty;
    private Integer discount;
}
