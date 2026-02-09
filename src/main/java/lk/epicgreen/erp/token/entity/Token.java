package lk.epicgreen.erp.token.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_sessions")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "refreshToken", nullable = false, unique = true, length = 150)
    private String refreshToken;

    @Column(name="user_id",nullable = false)
    private Long userId;

    @Column(name = "issued_at", nullable = false,  length = 100)
    private LocalDateTime issuedAt;

    @Column(name = "expires_at", nullable = false,length = 100)
    private LocalDateTime expiresAt;

    @Column(name = "revoked_at", length = 100)
    private LocalDateTime revokedAt;

    @Column(name = "reason_revoked", unique = true, length = 100)
    private String reason_revoked;

    public boolean isActive(){
        return revokedAt == null && expiresAt.isAfter(LocalDateTime.now());
    }


}
