package chatApi.model;

import chatApi.DTO.UserDto;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String login;
    @ToString.Exclude
    private String password;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(UserDto userDTO) {
        this.username = userDTO.getUsername();
        this.login = userDTO.getLogin();
        this.password = userDTO.getPassword();
        this.role = Role.USER_ROLE;
        this.status = Status.ACTIVE;
    }
}
