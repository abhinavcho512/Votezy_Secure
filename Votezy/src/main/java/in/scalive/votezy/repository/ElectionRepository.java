package in.scalive.votezy.repository;

import in.scalive.votezy.entity.Election;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ElectionRepository extends JpaRepository<Election, Long> {

    // ✅ Used in VotingService to check election expiry
    Optional<Election> findByElectionName(String electionName);
}