//package in.scalive.votezy.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import in.scalive.votezy.entity.Candidate;
//import in.scalive.votezy.entity.Vote;
//import in.scalive.votezy.entity.Voter;
//import in.scalive.votezy.exception.ResourceNotFoundException;
//import in.scalive.votezy.exception.VoteNotAllowedException;
//import in.scalive.votezy.repository.CandidateRepository;
//import in.scalive.votezy.repository.VoteRepository;
//import in.scalive.votezy.repository.VoterRepository;
//import jakarta.transaction.Transactional;
//
//@Service
//public class VotingService {
//	
//	private VoteRepository voteRepository;
//	private CandidateRepository candidateRepository; // vote count-1 in candidate
//	private VoterRepository voterRepository;  // to cast vote
//	public VotingService(VoteRepository voteRepository, CandidateRepository candidateRepository,
//			VoterRepository voterRepository) {
//		
//		this.voteRepository = voteRepository;
//		this.candidateRepository = candidateRepository;
//		this.voterRepository = voterRepository;
//	}
//	
//	@Transactional
//	public Vote castVote(Long voterId,Long candidateId)
//	{
//		// check voter is present or not
//		if(!voterRepository.existsById(voterId))
//		{
//			throw new ResourceNotFoundException("Voter not found with ID:"+voterId);
//			
//		}
//		
//		if(!candidateRepository.existsById(candidateId))
//		{
//			throw new ResourceNotFoundException("Candidate not found with ID:"+candidateId);
//			
//		}
//		
//		// fetch voter 
//		Voter voter=voterRepository.findById(voterId).get();
//		if(voter.isHasVoted())  // vote kr di kya ? -> if yes throw exception
//			{
//			throw new VoteNotAllowedException("Voter ID:"+voterId+"has already casted vote");
//			
//			}
//		
//		Candidate candidate=candidateRepository.findById(candidateId).get();  // candidate fetch)
//		Vote vote=new Vote(); // vote ka object bnaya hmne
//		vote.setVoter(voter);  // voter dala
//		vote.setCandidate(candidate);  // candidate dala
//		voteRepository.save(vote);  // ? 57 and 63 same line meaning
//		
//		candidate.setVoteCount(candidate.getVoteCount()+1);  // vc m +1 vote dali candidate ko
//		candidateRepository.save(candidate);
//		
//		voter.setHasVoted(true);
//		voterRepository.save(voter);
//		
//		return vote;
//	}
//	
//	public List<Vote> getAllVotes()
//	{
//	  return voteRepository.findAll();
//
//}
//}
package in.scalive.votezy.service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.Election;
import in.scalive.votezy.entity.Vote;
import in.scalive.votezy.entity.Voter;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.exception.VoteNotAllowedException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.ElectionRepository;
import in.scalive.votezy.repository.VoteRepository;
import in.scalive.votezy.repository.VoterRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotingService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;

    public VotingService(VoteRepository voteRepository,
                         CandidateRepository candidateRepository,
                         VoterRepository voterRepository,
                         ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
        this.electionRepository = electionRepository;
    }

    @Transactional
    public Vote castVote(Long voterId, Long candidateId, String electionName) {

        // 1. Check voter exists
        Voter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new ResourceNotFoundException("Voter not found with ID: " + voterId));

        // 2. Check candidate exists
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + candidateId));

        // 3. ✅ Check election exists
        Election election = electionRepository.findByElectionName(electionName)
                .orElseThrow(() -> new ResourceNotFoundException("Election not found: " + electionName));

        // 4. ✅ Check election expiry
        if (LocalDateTime.now().isAfter(election.getEndDate())) {
            throw new VoteNotAllowedException(
                "Voting for '" + electionName + "' has ended on " + election.getEndDate()
            );
        }

        // 5. Check voter already voted
        if (voter.isHasVoted()) {
            throw new VoteNotAllowedException("Voter ID: " + voterId + " has already cast a vote");
        }

        // 6. ✅ Duplicate email check
        if (voterRepository.existsByEmailAndVotedInElection(voter.getEmail(), electionName)) {
            throw new VoteNotAllowedException(
                "Email '" + voter.getEmail() + "' has already voted in: " + electionName
            );
        }

        // 7. Cast the vote
        Vote vote = new Vote();
        vote.setVoter(voter);
        vote.setCandidate(candidate);
        voteRepository.save(vote);

        // 8. Update candidate vote count
        candidate.setVoteCount(candidate.getVoteCount() + 1);
        candidateRepository.save(candidate);

        // 9. Mark voter as voted
        voter.setHasVoted(true);
        voter.setVotedInElection(electionName);
        voterRepository.save(voter);

        return vote;
    }

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }
}
