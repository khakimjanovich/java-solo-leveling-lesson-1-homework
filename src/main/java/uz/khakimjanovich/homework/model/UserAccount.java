package uz.khakimjanovich.homework.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    protected UserAccount() {
    }

    public UserAccount(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }

    public Long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String displayName() {
        return displayName;
    }

    public Instant createdAt() {
        return createdAt;
    }
}

