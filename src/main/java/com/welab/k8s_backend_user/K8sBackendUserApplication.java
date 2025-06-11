package com.welab.k8s_backend_user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableFeignClient
@SpringBootApplication
public class K8sBackendUserApplication {
	public static void main(String[] args) {
		SpringApplication.run(K8sBackendUserApplication.class, args);
	}

}
