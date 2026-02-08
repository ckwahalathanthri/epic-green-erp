package lk.epicgreen.erp.token.entity.repository;

import lk.epicgreen.erp.token.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepo extends JpaRepository<Token,Long> {
    Optional<Token> findById(Long id);
    void deleteByExpiresAtBefore(LocalDateTime t);

    Token findByRefreshToken(String refreshToken);
    List<Token> findAllByUserIdAndRevokedAtIsNull(Long userId);
}
