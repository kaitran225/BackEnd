package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Entity
@Table(name = "Students")
@AllArgsConstructor
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    private Integer grade;

    @Size(max = 20, message = "Class name cannot exceed 20 characters")
    private String studentClass;

    @Size(max = 100, message = "School name cannot exceed 100 characters")
    private String schoolName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}