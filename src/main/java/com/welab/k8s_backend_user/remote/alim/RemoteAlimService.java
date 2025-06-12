package com.welab.k8s_backend_user.remote.alim;

import com.welab.k8s_backend_user.common.dto.ApiResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

//유레카에서 서비스 알림 목록을 가져왔을 때
//name은 규칙이 따로 없다. (단지 app.yml에다가 특정 feignclient에 등록하고 싶을 때 이름을 써주면된다.)
//url은 서비스 명칭이다. -> 서비스 자체가 추상화기때문에 다음과 같이 포트번호와 함께 써주면되낟.
@FeignClient(name = "backend-alim",
        url = "http://k8s-backend-alim-service:8080",
        path = "/api/alim/v1")
public interface RemoteAlimService {
    @GetMapping(value = "/hello")
    public ApiResponseDto<String> hello();
}
