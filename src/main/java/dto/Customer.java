package dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private String id;
    private String title;
    private String name;
    private LocalDate dob;
    private Double salary;
    private String address;
    private String city;
    private String province;
    private String postalCode;
}
