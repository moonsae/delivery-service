package org.delivery.api.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.user.business.UserBusiness;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/open-api/user")
public class UserOpenApiController {
    private final UserBusiness userBusiness;

    // 사용자 가입 요청
    //user를 요청하면 UserResponse를 받음
    @PostMapping("/register")
    public Api<UserResponse> register(
            @Valid // << valid 어노테이션 때문에 UserRegisterRequest 다 검증
            @RequestBody Api<UserRegisterRequest> request
    ){
        var response = userBusiness.register(request.getBody());
        return Api.OK(response);

    }

    //로그인
    @PostMapping("/login")
    public Api<TokenResponse> login(
            @Valid
            @RequestBody Api<UserLoginRequest> request
    ){
        var response = userBusiness.login(request.getBody());
        return Api.OK(response);
    }
}
