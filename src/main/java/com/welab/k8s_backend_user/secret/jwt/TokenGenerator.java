package com.welab.k8s_backend_user.secret.jwt;

import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;
import com.welab.k8s_backend_user.secret.jwt.props.JwtConfigProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenGenerator {
    private final JwtConfigProperties jwtConfigProperties;
    private volatile SecretKey secretKey;

    //    JWT에서는 대칭키도 사용한다. Token을 발급하는 쪽과 검증하는 쪽이 같기때문에 대칭키를 사용한다.
    private SecretKey getSecretKey() {
//        null이면 secretKey를 만들어줘야된다.
        if (secretKey == null) {
//            같이 생성하지 않고 순차적으로 생성하기 위해
            synchronized (this) {
//                다시 secretKey가 null인지 확인
                if (secretKey == null) {
//                    secretKey에 jwtConfig에서 secretKey를 가져와서 대칭키 hash함수를 이용한 key를 가져와서 확인
                    secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfigProperties.getSecretKey()));
                }
            }
        }
        return secretKey;
    }

    public TokenDto.AccessToken generateAccessToken(String userId, String deviceType) {
        TokenDto.JwtToken jwtToken = this.generateJwtToken(userId, deviceType, false);
        return new TokenDto.AccessToken(jwtToken);
    }

    public TokenDto.AccessRefreshToken generateAccessRefreshToken(String userId, String deviceType) {
        TokenDto.JwtToken accessJwtToken = this.generateJwtToken(userId, deviceType, false);
        TokenDto.JwtToken refreshJwtToken = this.generateJwtToken(userId, deviceType, true);
        return new TokenDto.AccessRefreshToken(accessJwtToken, refreshJwtToken);
    }

    //    claims를 가지고 토큰을 만드는 것(권한과 Role까지 넣어줘서 접근제한을 해줘야된다.)
    private TokenDto.JwtToken generateJwtToken(String userId, String deviceType,
                                               boolean refreshToken) {
        int tokenExpiresIn = tokenExpiresIn(refreshToken, deviceType);
        String tokenType = refreshToken ? "refresh" : "access";
        String token = Jwts.builder()
                .issuer("welab")
                .subject(userId)
                .claim("userId", userId)
                .claim("deviceType", deviceType)
                .claim("tokenType", tokenType)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExpiresIn * 1000L))
                .signWith(getSecretKey())
                .header().add("typ", "JWT")
                .and()
                .compact();
        return new TokenDto.JwtToken(token, tokenExpiresIn);
    }

    private int tokenExpiresIn(boolean refreshToken, String deviceType) {
        int expiresIn = 60 * 15;
        if (!refreshToken) {
            return 60 * 15;
        }
        if (refreshToken) {
            if (deviceType != null) {
                if (deviceType.equals("WEB")) {
                    expiresIn = jwtConfigProperties.getExpiresIn();
                } else if (deviceType.equals("MOBILE")) {
                    expiresIn = jwtConfigProperties.getMobileExpiresIn();
                } else if (deviceType.equals("TABLET")) {
                    expiresIn = jwtConfigProperties.getTabletExpiresIn();
                }
            }
        }
        return expiresIn;
    }
}
