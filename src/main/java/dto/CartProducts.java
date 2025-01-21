package dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CartProducts {
    private String itemCode;
    private String description;
    private String packSize;
    private Integer orderQty;
    private Double unitPrice;
    private Double netTotal;
    private Double discount;
    private Double total;
}
