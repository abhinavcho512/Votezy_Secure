package in.scalive.votezy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.DuplicateResourceException;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;

@Service
public class VoterService {
	private VoterRepository voterRepository;
	private CandidateRepository candidateRepository; // if voting k bad voter ko delete krdo => vote km => candidate vote count km hua 
	
	// ci
	@Autowired
	public VoterService(VoterRepository voterRepository, CandidateRepository candidateRepository) {
		
		this.voterRepository = voterRepository;
		this.candidateRepository = candidateRepository;
	}
	public Voter registerVoter(Voter voter)   // registerVoter method m kya pas hoga =>voter object and return voter itself
	{
	     if(voterRepository.existsByEmail(voter.getEmail()))	
	    		 {
	    	      // throw duplicate error
	    	        throw new DuplicateResourceException("Voter with email id:"+voter.getEmail()+"already exists");
	    		 }
	     return voterRepository.save(voter); // genuine voter
	}
	 // return sare voter ka data
	public List<Voter>getAllVoters()
	{
		return voterRepository.findAll();
	}
	 // return single voter information
	public Voter getVoterById(Long id)

{
		Voter voter=voterRepository.findById(id).orElse(null);
		if(voter==null)
		{
			throw new ResourceNotFoundException("Voter with id:"+id+"not found");
		}
		return voter;
		
}
	
	public Voter updateVoter(Long id,Voter updatedVoter)
	{
		Voter voter=voterRepository.findById(id).orElse(null); // return optional so else
		if(voter==null) {
			throw new ResourceNotFoundException("Voter with id:"+id+"not found");
			
		} 
		// update voter record
		if(updatedVoter.getName()!=null)
		{
		voter.setName(updatedVoter.getName());
		}
		
		if(updatedVoter.getEmail()!=null)
		{
		voter.setEmail(updatedVoter.getEmail());
		}
		return voterRepository.save(voter);
		
	}
	 @Transactional  // voter delete kiya pr vote nhi hti uski as order cancel liya
	public void deleteVoter(Long id)
	{
		Voter voter=voterRepository.findById(id).orElse(null);  // voter mil gya id se
		if(voter==null)  // voter nhi h 
		{
			throw new ResourceNotFoundException("Cannot delete voter with id: +"+id+"as id does not exists");  
			
		}
		Vote vote=voter.getVote();// voter ne vote di
		if(vote!=null)  // vote di h to
		{
			Candidate candidate=vote.getCandidate();   // vote for cand
			candidate.setVoteCount(candidate.getVoteCount()-1);  
			candidateRepository.save(candidate);  // for undo =>@Transactional
			
		}
		voterRepository.delete(voter);
	}
	

}
