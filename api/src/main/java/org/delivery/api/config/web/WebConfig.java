package org.delivery.api.config.web;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.UserSession;
import org.delivery.api.interceptor.AuthorizationInterceptor;
import org.delivery.api.resolver.UserSessionResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;
    private final UserSessionResolver userSessionResolver;
    /**
     * .excludePathPatterns("/api/user/register")
     * 이렇게 작성하면 개많아짐
     * 그래서 룰을 정하자
     * open-api를 시작하는 애들은 검증 안할 것
     * 나머지는 검증할 것
     * */
    private List<String> OPEN_API = List.of(
            "/open-api/**"
    );
    //기본적으로 빼줘야 하는 주소들
    private List<String> DEFAULT_EXCLUDE = List.of(
            "/",
            "favicorn.ico",
            "/error"
    );
    //기본적으로 빼줘야 하는 주소들
    private List<String> SWAGGER = List.of(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    );
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authorizationInterceptor)
                .excludePathPatterns(OPEN_API)
                .excludePathPatterns(DEFAULT_EXCLUDE)
                .excludePathPatterns(SWAGGER)
                ;
        // 이 주소들이 아니면 다 검증하겠다.
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userSessionResolver);
    }
}
