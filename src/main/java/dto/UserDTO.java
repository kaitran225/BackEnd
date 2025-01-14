package dto;

import entity.User;
import entity.User.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password; // Lưu ý đổi tên từ passwordHash thành password trong DTO

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "/(84[3|5|7|8|9])+([0-9]{8})\\b/g;", message = "Phone number cannot be blank")
    private String phoneNumber;

    private Role role;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor để chuyển từ Entity sang DTO
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        // Không copy passwordHash vì lý do bảo mật
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    // Method để chuyển từ DTO sang Entity
    public User toEntity() {
        User user = new User();
        user.setUserId(this.userId);
        user.setUsername(this.username);
        // Password sẽ được mã hóa ở service layer
        user.setFullName(this.fullName);
        user.setEmail(this.email);
        user.setPhoneNumber(this.phoneNumber);
        user.setRole(this.role);
        user.setCreatedAt(this.createdAt != null ? this.createdAt : LocalDateTime.now());
        user.setUpdatedAt(this.updatedAt != null ? this.updatedAt : LocalDateTime.now());
        return user;
    }
}