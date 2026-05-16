package in.scalive.votezy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.service.CandidateService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/candidate")
@CrossOrigin
public class CandidateController {
	
	private CandidateService candidateService;

	@Autowired
	public CandidateController(CandidateService candidateService) {
		
		this.candidateService = candidateService;
	}
	
	@PostMapping("/add")
	public ResponseEntity<Candidate>addCandidate(@RequestBody @Valid Candidate candidate)
	{
		Candidate savedCandidate=candidateService.addCandidate(candidate);
		return new ResponseEntity<>(savedCandidate,HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Candidate>getCandidateById(@PathVariable Long id)
	{
		Candidate candidate=candidateService.getCandidateById(id);
		return new ResponseEntity<>(candidate,HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<Candidate>>getAllCandidates()
	{
		List<Candidate>candidateList=this.candidateService.getAllCandidates();
		return new ResponseEntity<>(candidateList,HttpStatus.OK);
		
	}
	
	 @PutMapping("/update/{id}")
	    public ResponseEntity<Candidate>updateCandidate(@PathVariable Long id,@RequestBody Candidate candidate)  // we use @PathVariable => becaause id is cumpolsory to pass in PutMapping
	    {
	    	Candidate updatedCandidate=candidateService.updateCandidate(id, candidate);
	    	return new ResponseEntity<>(updatedCandidate,HttpStatus.OK);
	    }
		
	    @DeleteMapping("/delete/{id}")
	    public ResponseEntity<String>deleteCandidate(@PathVariable Long id)
	    {
	    	candidateService.deleteCandidate(id);
	    	return new ResponseEntity<>("Candidate with id: "+id+"deleted successfully",HttpStatus.OK);
	    }
		
		

}
