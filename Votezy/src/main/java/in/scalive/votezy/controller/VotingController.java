//package in.scalive.votezy.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import in.scalive.votezy.dto.VoteRequestDTO;
//import in.scalive.votezy.dto.VoteResponseDTO;
//import in.scalive.votezy.entity.Vote;
//import in.scalive.votezy.service.VotingService;
//import jakarta.validation.Valid;
//
//@RestController
//@RequestMapping("/api/votes")
//@CrossOrigin
//public class VotingController {
//	
//	private VotingService votingService;
//
//	@Autowired
//	public VotingController(VotingService votingService) {
//		
//		this.votingService = votingService;
//	}
//	
//	@PostMapping("/cast")
//	public ResponseEntity<VoteResponseDTO> castVote(@RequestBody @Valid VoteRequestDTO voteRequest)
//	{
//		Vote vote=votingService.castVote(voteRequest.getVoterId(),voteRequest.getCandidateId());
//		VoteResponseDTO voteResponse=new VoteResponseDTO("Vote casted successfully",true,vote.getVoterId(),vote.getCandidateId());
//		return new ResponseEntity<>(voteResponse,HttpStatus.CREATED);
//	}
//	
//	@GetMapping
//	public ResponseEntity<List<Vote>>getAllVotes(){
//	   List<Vote>voteList=votingService.getAllVotes();      //receive
//		return new ResponseEntity<>(voteList,HttpStatus.OK);
//		
//	}
//	
//	
//
//}
package in.scalive.votezy.controller;

import in.scalive.votezy.dto.VoteRequestDTO;
import in.scalive.votezy.dto.VoteResponseDTO;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.service.VotingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin
public class VotingController {

    private final VotingService votingService;

    public VotingController(VotingService votingService) {
        this.votingService = votingService;
    }

    // ✅ Cast vote - requires ROLE_USER + JWT token in header
    // POST /api/votes/cast
    // Header: Authorization: Bearer <token>
    // Body: { "voterId": 1, "candidateId": 2, "electionName": "General2024" }
    @PostMapping("/cast")
    public ResponseEntity<VoteResponseDTO> castVote(@RequestBody @Valid VoteRequestDTO voteRequest) {
        Vote vote = votingService.castVote(
                voteRequest.getVoterId(),
                voteRequest.getCandidateId(),
                voteRequest.getElectionName()
        );
        VoteResponseDTO voteResponse = new VoteResponseDTO(
                "Vote casted successfully", true,
                vote.getVoterId(), vote.getCandidateId()
        );
        return new ResponseEntity<>(voteResponse, HttpStatus.CREATED);
    }

    // Get all votes
    @GetMapping
    public ResponseEntity<List<Vote>> getAllVotes() {
        List<Vote> voteList = votingService.getAllVotes();
        return new ResponseEntity<>(voteList, HttpStatus.OK);
    }
}