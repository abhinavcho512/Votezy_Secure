package in.scalive.votezy.controller;

import in.scalive.votezy.entity.Election;
import in.scalive.votezy.repository.ElectionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/election")
@CrossOrigin
public class ElectionController {

    private final ElectionRepository electionRepository;

    public ElectionController(ElectionRepository electionRepository) {
        this.electionRepository = electionRepository;
    }

    // ✅ ADMIN: Create election with end date
    // POST /api/election/create
    // Body: { "electionName": "General2024", "endDate": "2025-12-31T23:59:59" }
    @PostMapping("/create")
    public ResponseEntity<Election> createElection(@RequestBody @Valid Election election) {
        Election saved = electionRepository.save(election);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // ✅ Get all elections
    @GetMapping
    public ResponseEntity<List<Election>> getAllElections() {
        return ResponseEntity.ok(electionRepository.findAll());
    }
}