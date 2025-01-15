package entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Users")
@AllArgsConstructor  // contructor full tham số
@NoArgsConstructor   // contructor ko tham số
public class User {
    @Id  // // khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) ////AUTO_INCREMENT
    private String userId;

    @Column(nullable = false, unique = true)

    // nullable (có thể vô hiệu) = false ( không được phép null,
    //                                    nếu lưu null vào db nó sẽ ném ra 1 ngoaij lệ exception)
    // unique (độc nhất)  = true ( nội dung sau không được trùng với nội dung trước
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "Password cannot be blank")
    private String passwordHash;

    @Column(nullable = false)
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Column(unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = " /(84[3|5|7|8|9])+([0-9]{8})\\b/g;", message = "Phone number cannot be blank")
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    //updatable = false   ( có nghĩa không cho phép lưu lại sửa đổi kế tiếp, giống như
    // hằng
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Role {
        STUDENT, PARENT, PSYCHOLOGIST, MANAGER
    }
}
