package in.scalive.votezy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.scalive.votezy.entity.Candidate;
import in.scalive.votezy.entity.ElectionResult;
import in.scalive.votezy.exception.ResourceNotFoundException;
import in.scalive.votezy.repository.CandidateRepository;
import in.scalive.votezy.repository.ElectionResultRepository;
import in.scalive.votezy.repository.VoteRepository;
import in.scalive.votezy.repository.VoterRepository;

@Service
public class ElectionResultService {

    private CandidateRepository candidateRepository;
    private ElectionResultRepository electionResultRepository;
    private VoteRepository voteRepository;
    private VoterRepository voterRepository; // ✅ NEW

    @Autowired
    public ElectionResultService(CandidateRepository candidateRepository,
            ElectionResultRepository electionResultRepository,
            VoteRepository voteRepository,
            VoterRepository voterRepository) {
        this.candidateRepository = candidateRepository;
        this.electionResultRepository = electionResultRepository;
        this.voteRepository = voteRepository;
        this.voterRepository = voterRepository;
    }

    public ElectionResult declareElectionResult(String electionName) {
        Optional<ElectionResult> existingResult = electionResultRepository.findByElectionName(electionName);
        if (existingResult.isPresent()) {
            return existingResult.get();
        }

        // ✅ Count voters who voted in this specific election
        List<in.scalive.votezy.entity.Voter> voters = voterRepository.findByVotedInElection(electionName);
        if (voters.isEmpty()) {
            throw new IllegalStateException("Cannot declare result - no votes cast for: " + electionName);
        }

        List<Candidate> allCandidates = candidateRepository.findAllByOrderByVoteCountDesc();
        if (allCandidates.isEmpty()) {
            throw new ResourceNotFoundException("No candidates available");
        }

        Candidate winner = allCandidates.get(0);
        int totalVotes = voters.size(); // ✅ count by election

        ElectionResult result = new ElectionResult();
        result.setElectionName(electionName);
        result.setWinner(winner);
        result.setTotalVotes(totalVotes);

        return electionResultRepository.save(result);
    }

    public List<ElectionResult> getAllResults() {
        return electionResultRepository.findAll();
    }
}