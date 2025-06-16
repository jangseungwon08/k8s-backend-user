package com.welab.k8s_backend_user.service;

import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.NotFound;
import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.domain.dto.SiteUserLoginDto;
import com.welab.k8s_backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.domain.event.SiteUserInfoEvent;
import com.welab.k8s_backend_user.domain.repository.SiteUserRepository;
import com.welab.k8s_backend_user.event.producer.KafkaMessageProducer;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import com.welab.k8s_backend_user.secret.jwt.TokenGenerator;
import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final KafkaMessageProducer kafkaMessageProducer;
    private final TokenGenerator tokenGenerator;

    @Transactional
    public void registerUser(SiteUserRegisterDto dto) {
        SiteUser siteUser = dto.toEntity();
        siteUserRepository.save(siteUser);

        log.info("DB 저장 완료");
        SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity("Create", siteUser);
        kafkaMessageProducer.send(SiteUserInfoEvent.Topic, event);
    }

    @Transactional(readOnly = true)
    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto){
        SiteUser siteUser = siteUserRepository.findByUserId(loginDto.getUserId());
        if(siteUser == null){
            throw new BadParameter("아이디 또는 비밀번호를 확인하세요");
        }

//        SQL인젝션 방어를 위해서 DB밖에서 하는 것이 좋다.
        if(!SecureHashUtils.matches(loginDto.getPassword(),siteUser.getPassword())){
            throw new BadParameter("아이디 또는 비밀번호가 틀렸습니다.");
        }
//        토큰 제너레이터의 refresh토큰값을 리턴해준다.
        return tokenGenerator.generateAccessRefreshToken(loginDto.getUserId(), "WEB");
    }
}
