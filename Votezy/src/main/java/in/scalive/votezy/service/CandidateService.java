package in.scalive.votezy.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;

@Service
public class CandidateService {

	private CandidateRepository candidateRepository;

	@Autowired
	public CandidateService(CandidateRepository candidateRepository) {
		
		this.candidateRepository = candidateRepository;
	}
	
	// methods->
	
	public Candidate addCandidate(Candidate candidate)
	{
		return candidateRepository.save(candidate);   // for register/add=> use .save()
	}
	
	public List<Candidate> getAllCandidates(){
		return candidateRepository.findAll();  // for getAll=> use findAll()
	}
	
	public Candidate getCandidateById(Long id)
	{
		Candidate candidate=candidateRepository.findById(id).orElse(null); // return optionl so use else
		
		if(candidate==null) {
			throw new ResourceNotFoundException("Candidate with id:"+id+"does not found");
		}
		return candidate;
		
	}
	
	public Candidate updateCandidate(Long id,Candidate updatedCandidate)
	{
		Candidate candidate=getCandidateById(id);  // candidate mila
		if(updatedCandidate.getName()!=null)  // update candidate ka name milra h 
			{
			candidate.setName(updatedCandidate.getName());  // set or update new name
			}
		
		if(updatedCandidate.getParty()!=null)
		{
		candidate.setParty(updatedCandidate.getParty());
		}
		
		return candidateRepository.save(candidate);
	}
	
	
	public void deleteCandidate(Long id) 
	{
		
		// Direct mt delete kro candiate ko uske child vote ko bhi delete kro
		Candidate candidate=getCandidateById(id);  // candidate nikal liya id se
		List<Vote>votes=candidate.getVote();  // vote nikal liye candidate se
				
		// Votes bi htado bhai candidate k liye  => vote also related with voter so thats why we have to break bw cand and vote  
		
		for(Vote v:votes)  // candidate =>null & candidate only null hoga pr delete nhi
		{
			v.setCandidate(null);  // break relationship bw candidate and vote so that as parent(candidate) delete->vote delete while taking candidate =>null
		}
	
		candidate.getVote().clear();  // sb votes ko hta dege (child(vote)) ko vote diya jo ye reh na jaye
		candidateRepository.delete(candidate);  // delete candidate
	
	
	
	}
	
	
}
