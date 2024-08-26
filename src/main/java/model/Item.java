package model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String code;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Integer qtnInHand;
}
