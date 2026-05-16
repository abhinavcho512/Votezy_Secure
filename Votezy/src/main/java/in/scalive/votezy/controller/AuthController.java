package in.scalive.votezy.controller;

import in.scalive.votezy.entity.AppUser;
import in.scalive.votezy.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ✅ REGISTER
    // POST /api/auth/register
    // Body: { "name": "Abhinav", "email": "a@a.com", "password": "123456" }
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid AppUser user) {
        String message = authService.register(user);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // ✅ LOGIN
    // POST /api/auth/login
    // Body: { "email": "a@a.com", "password": "123456" }
    // Returns: { "token": "eyJ...", "role": "ROLE_USER", "email": "a@a.com" }
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        Map<String, String> response = authService.login(
                credentials.get("email"),
                credentials.get("password")
        );
        return ResponseEntity.ok(response);
    }
}