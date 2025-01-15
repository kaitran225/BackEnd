package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Entity
@Table(name = "Psychologists")
@AllArgsConstructor
@NoArgsConstructor
public class Psychologist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String psychologistId;


    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Size(max = 100, message = "Specialization cannot exceed 100 characters")
    private String specialization;

    @Min(value = 0, message = "Years of experience cannot be negative")
    private Integer yearsOfExperience;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String availableHours; // JSON string

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE, ON_LEAVE, INACTIVE
    }
}