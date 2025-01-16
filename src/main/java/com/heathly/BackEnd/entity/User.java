package com.heathly.BackEnd.entity;


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
    // nullable (có thể vô hiệu) = false ( không được phép null,
    //                                    nếu lưu null vào db nó sẽ ném ra 1 ngoaij lệ exception)
    // unique (độc nhất)  = true ( nội dung sau không được trùng với nội dung trước
    @Column(nullable = false, unique = true, name = "UserID")
    @Id
    private String userId;

    @Column(nullable = false, name = "Username")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @Column(nullable = false,name = "PasswordHash")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Column(nullable = false,name = "FullName")
    @NotBlank(message = "Full name cannot be blank")
    private String fullName;


    @Column(unique = true,name = "Email")
    @Email(message = "Email should be valid")
    private String email;

    @Column(unique = true,name = "PhoneNumber")
    private String phoneNumber;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

    //updatable = false   ( có nghĩa không cho phép lưu lại sửa đổi kế tiếp, giống như
    // hằng
    @Column(nullable = false, updatable = false, name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(nullable = false,name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, INACTIVE
    }

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
        STUDENT, PARENT, PSYCHOLOGIST, MANAGER,Staff
    }
}
