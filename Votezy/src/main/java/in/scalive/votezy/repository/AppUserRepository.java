package in.scalive.votezy.repository;

import in.scalive.votezy.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    // ✅ Used by AuthService to find user by email during login
    Optional<AppUser> findByEmail(String email);

    // ✅ Used during registration to check duplicate email
    boolean existsByEmail(String email);
}