package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String id;
    private String username;
    private String name;
    private String password_hash;
    private Role role;
    private Timestamp lastLogin;
    private Timestamp lastLogout;

    public enum Role {
        MANAGER,
        CASHIER
    }
}
