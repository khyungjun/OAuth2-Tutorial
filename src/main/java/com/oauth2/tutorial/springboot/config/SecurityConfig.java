package com.oauth2.tutorial.springboot.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.web.SecurityFilterChain;

import com.oauth2.tutorial.springboot.handler.OAuth2FailureHandler;
import com.oauth2.tutorial.springboot.handler.OAuth2SuccessHandler;
import com.oauth2.tutorial.springboot.service.CustomOAuth2UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 기능을 사용할 수 있다. 간단히 말해서, Spring Security를 활성화시키는 어노테이션. Web 보안을 활성화. @EnableWebSecurity을 통해 기본 스프링 필터체인에 시큐리티를 등록한다.
@Configuration
public class SecurityConfig { // 공식문서를 살펴보면 Spring Security 5.7.0 부터는 더이상 WebSecurityConfigurerAdapter를 확장해서 사용하지 않고 Bean을 주입하는 방식으로 사용하도록 설정방식이 아주 약간 변경된 것을 확인할 수 있다.

	private final OAuth2FailureHandler oAuth2FailureHandler;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final CustomOAuth2UserService customOAuth2UserService;
	
	/**
     * Http Security 설정
     */
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    	return httpSecurity
    			.formLogin(formLogin -> formLogin.disable()) // form 로그인 해제 (UsernamePasswordAuthenticationFilter 비활성화)
    			.httpBasic(httpBasic -> httpBasic.disable()) // username, password 헤더 로그인 방식 해제 (BasicAuthenticationFilter 비활성화)
    			.csrf(csrf -> csrf
    					.ignoringRequestMatchers(PathRequest.toH2Console()) // /h2-console 관련 URL들에 대해 인증을 면제
    					.disable())
    	        .headers(headers -> headers.frameOptions(FrameOptionsConfig::sameOrigin)) // /h2-console 관련 URL들에 대해 인증을 면제
    			.authorizeHttpRequests(authorizeRequests ->
	                authorizeRequests
                    	.requestMatchers("/", "/css/**", "/images/**", "/js/**", "/profile").permitAll()
                    	.requestMatchers(PathRequest.toH2Console()).permitAll() // /h2-console 관련 URL들에 대해 인증을 면제
	                    .anyRequest().authenticated() // 위의 인증 면제 리소스를 제외한 모든 리소스는 인증을 해야 접근을 허용
	            )
				.logout(logout -> logout.logoutSuccessUrl("/")) // logout().logoutSuccessUrl("/") : 로그아웃 기능에 대한 여러 설정의 진입점이다. 로그아웃 성공 시 "/" 주소로 이동한다.
			    .oauth2Login(oauth2Login -> // OAuth2 로그인 기능에 대한 여러 설정의 진입점이다.
			    	oauth2Login
//			    		.loginPage("/loginForm") // 인증이 필요한 URL에 접근하면 호출될 URL
//			    		.defaultSuccessUrl("/") // 로그인 성공시 이동할 URL
//			    		.failureUrl("/loginForm") // 로그인 실패시 이동할 URL
//				    	.successHandler(oAuth2SuccessHandler) // 소셜 로그인 동의하고 계속하기를 눌렀을 때 Handler 설정
//		                .failureHandler(oAuth2FailureHandler) // 소셜 로그인 실패 시 핸들러 설정
				    	.userInfoEndpoint(userInfoEndpoint -> // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다. 
				    		userInfoEndpoint
				    			.userService(customOAuth2UserService) // userService : 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다. 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시할 수 있다.
				    	)	
		    	)	
    			.build();
    }
}