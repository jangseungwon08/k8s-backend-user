package com.welab.k8s_backend_user.domain.dto;

import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUserRegisterDto {
    @NotBlank(message = "아이디를 입력하세요")
    private String userId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;

    @NotBlank(message = "휴대폰 번호를 입력하세요")
    private String phoneNumber;

//    입력받은 Dto를 엔티티로 변환해주는 메서드
    public SiteUser toEntity(){
        SiteUser siteUser = new SiteUser();

        siteUser.setUserId(this.userId);
        siteUser.setPassword(SecureHashUtils.hash(this.password));
        siteUser.setPhoneNumber(this.phoneNumber);
        return siteUser;
    }

}
