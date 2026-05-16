package in.scalive.votezy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Vote {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(name="voter_id",unique=true) //for use fk in vote table & owner btane ke liye @JoinColumn lege
	@JsonIgnore
	private Voter voter; // voter_id pas hogi
	
	@ManyToOne
	@JoinColumn(name="candidate_id") // id unique nhi hogi=> kyuki candidate same hoga overall
	@JsonIgnore
	private Candidate candidate;
	
	
	// @JsonProperty-> 2 getters
	
	@JsonProperty("voterId")
	public Long getVoterId()
	{
		return voter!=null? voter.getId():null;  // voter h to voter info do
	}
	
	@JsonProperty("candidateId")
	public Long getCandidateId()
	{
		return candidate!=null? candidate.getId():null;  // voter h to voter info do
	}


}
