package com.welab.k8s_backend_user.domain.event;

import com.welab.k8s_backend_user.domain.SiteUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SiteUserInfoEvent {
    public static final String  Topic = "userinfo";

    private String action;
//    비밀번호는 넣으면 안된다.
    private String userId;
    private String phoneNumber;
    private LocalDateTime eventTime;

//   엔티티에서 event로 변환해줘야되는거니까
    public static SiteUserInfoEvent fromEntity(String action, SiteUser siteUser){
        SiteUserInfoEvent message = new SiteUserInfoEvent();
//        회원정보가 바뀔때 마다 action이 실행
        message.action = action;
        message.userId = siteUser.getUserId();
        message.phoneNumber = siteUser.getPhoneNumber();
        message.eventTime = LocalDateTime.now();
        return message;
    }
}
