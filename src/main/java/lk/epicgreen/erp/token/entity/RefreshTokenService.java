package lk.epicgreen.erp.token.entity;


import lk.epicgreen.erp.token.entity.Util.TokenHashUtil;
import lk.epicgreen.erp.token.entity.repository.RefreshTokenRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepo refreshTokenRepo;
    private final TokenHashUtil tokenHashUtil;
    private final int refreshDays=7;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo,TokenHashUtil tokenHashUtil) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.tokenHashUtil=tokenHashUtil;
    }

    public Token createSession(Long userId,String refreshToken){
        Token t=new Token();
        String hashedTkn=tokenHashUtil.sha256Encoding(refreshToken);
        t.setUserId(userId);
        t.setRefreshToken(hashedTkn);
        t.setIssuedAt(java.time.LocalDateTime.now());
        t.setExpiresAt(java.time.LocalDateTime.now().plusDays(refreshDays));
        t.setRevokedAt(null);
        t.setReason_revoked(null);
        return refreshTokenRepo.save(t);

    }

    public Token validateActiveRefreshToken(String refreshToken){
        Token token=refreshTokenRepo.findByRefreshToken(refreshToken);

        if(!token.isActive()){
            throw  new RuntimeException("Refresh token expired or revoked");
        }
        return token;
    }

    public void revokeToken(Long userId,String reason){
        List<Token> active=refreshTokenRepo.findAllByUserIdAndRevokedAtIsNull(userId);

        for(Token t:active){
            t.setRevokedAt(java.time.LocalDateTime.now());
            t.setReason_revoked(reason);
            refreshTokenRepo.save(t);
        }
    }
}
