package com.welab.k8s_backend_user.secret.jwt.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(value = "jwt", ignoreUnknownFields = true)
public class JwtConfigProperties {
//    refresh토큰의 만료시간
    private Integer expiresIn;
    private Integer mobileExpiresIn;
    private Integer tabletExpiresIn;
    private String secretKey;
}
