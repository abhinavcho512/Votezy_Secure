package in.scalive.votezy.service;

import in.scalive.votezy.entity.AppUser;
import in.scalive.votezy.repository.AppUserRepository;
import in.scalive.votezy.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AppUserRepository appUserRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ REGISTER
    public String register(AppUser user) {
        // Duplicate email check
        if (appUserRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }

        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 
        // ✅ Always force ROLE_USER - no one can self-assign ROLE_ADMIN
          user.setRole("ROLE_USER");

        appUserRepository.save(user);
        return "User registered successfully!";
    }

    // ✅ LOGIN
    public Map<String, String> login(String email, String password) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        return Map.of(
                "token", token,
                "role", user.getRole(),
                "email", user.getEmail()
        );
    }
}