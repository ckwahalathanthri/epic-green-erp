package lk.epicgreen.erp.token.entity.Util;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class TokenHashUtil {
    public  String sha256Encoding(String input){
        try{
            MessageDigest md=MessageDigest.getInstance("SHA-256");
            byte[] digest=md.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        }catch(Exception err){
            throw  new IllegalStateException(err);
        }
    }
}
