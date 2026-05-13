package uz.khakimjanovich.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.khakimjanovich.homework.model.UserAccount;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    boolean existsByUsername(String username);

    Optional<UserAccount> findByUsername(String username);
}
