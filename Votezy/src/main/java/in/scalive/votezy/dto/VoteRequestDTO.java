//package in.scalive.votezy.dto;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.Data;
//
//@Data
//public class VoteRequestDTO {
//	@NotNull(message="Voter ID is required")
//	 private Long voterId;
//	@NotNull(message="Candidate ID is required")
//	 private Long candidateId;
//
//}
package in.scalive.votezy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequestDTO {

    @NotNull(message = "Voter ID is required")
    private Long voterId;

    @NotNull(message = "Candidate ID is required")
    private Long candidateId;

    // ✅ NEW: required to check election expiry and duplicate email
    @NotBlank(message = "Election name is required")
    private String electionName;
}