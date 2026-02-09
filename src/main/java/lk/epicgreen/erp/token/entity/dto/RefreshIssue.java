package lk.epicgreen.erp.token.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshIssue {
    private String rawRefreshToken;
    private String tokenId;

    public RefreshIssue(String rawRefreshToken, String tokenId) {
        this.rawRefreshToken = rawRefreshToken;
        this.tokenId = tokenId;
    }
}
