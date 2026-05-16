//package in.scalive.votezy.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import in.scalive.votezy.entity.Voter;
//
//public interface VoterRepository extends JpaRepository<Voter,Long>{
//	boolean existsByEmail(String email);
//
//}
package in.scalive.votezy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import in.scalive.votezy.entity.Voter;

public interface VoterRepository extends JpaRepository<Voter, Long> {

    // ✅ EXISTING - prevents duplicate voter registration
    boolean existsByEmail(String email);

    // ✅ NEW - prevents same email voting twice in same election
    boolean existsByEmailAndVotedInElection(String email, String electionName);
    
    List<Voter> findByVotedInElection(String electionName);
}