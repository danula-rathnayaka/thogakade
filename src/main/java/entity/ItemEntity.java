package entity;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemEntity {
    private String code;
    private String description;
    private String packSize;
    private Double unitPrice;
    private Integer qtnInHand;
}
