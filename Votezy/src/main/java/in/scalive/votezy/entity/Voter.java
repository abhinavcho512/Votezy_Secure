//package in.scalive.votezy.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.OneToOne;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.Data;
//
//@Entity  // to map this java class into Jpa table
//@Data  // to use lombok
//public class Voter {
//	@Id // for pk
//	@GeneratedValue(strategy = GenerationType.IDENTITY)  // to auto update id and table
//	private Long id;
//	
//	@NotBlank(message="Name is required")
//	private String name;
//	
//	@NotBlank(message="Email is required")
//	@Email(message="Invalid mail format")
//	private String email;
//	
//	private boolean hasVoted=false;  // same as id no need of @
//	
//	@OneToOne(mappedBy ="voter",cascade = CascadeType.ALL) // voter ->owner h, if no voter->no vote (cascading)
//	@JsonIgnore
//	private Vote vote; // vote ka object        null h initially
//
//}

package in.scalive.votezy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
public class Voter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid mail format")
    private String email;

    private boolean hasVoted = false;

    // ✅ NEW: tracks which election this voter voted in
    private String votedInElection;

    @OneToOne(mappedBy = "voter", cascade = CascadeType.ALL)
    @JsonIgnore
    private Vote vote;
}