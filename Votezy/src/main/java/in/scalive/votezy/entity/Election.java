package in.scalive.votezy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Election name is required")
    @Column(unique = true)
    private String electionName;

    // ✅ Voting blocked after this date and time
    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    private boolean active = true;
}