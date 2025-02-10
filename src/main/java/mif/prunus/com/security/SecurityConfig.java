package mif.prunus.com.security;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mif.prunus.com.util.LoginUtil;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Autowired
	private final FormAuthenticationDetailSource authenticationDetailSource;// 추가 파라미터 처리

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.httpBasic(httpBasic -> httpBasic.disable())
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/", "/front/**", "/user/login", "/logout", "/expired", "/invalid").permitAll()
				// .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
				.requestMatchers("/user/**").hasRole("USER")
				.requestMatchers("/admin/**").hasRole("ADMIN")
				// .anyRequest().authenticated()
			)
			// invalidSessionUrl <-> authenticationEntryPoint 혼용 안됨.
			// session 사용시 authenticationEntryPoint 주석, loginPage("/invalid") 사용
			// session 미사용시 authenticationEntryPoint 주석 해제, loginPage("/invalid") 타지 않음.
			/*
			.sessionManagement(sessionManagement -> sessionManagement
				.maximumSessions(1)// 최대 허용 가능 세션 수, -1: 무제한 로그인 세션 허용
				.maxSessionsPreventsLogin(false) // 동시 로그인 차단, false:기존 세션 만료 true가 2번 전략, false가 1번전략
				.expiredUrl("/expired")// 세션이 만료된 경우 이동할 페이지
			)
			//.sessionManagement(session -> session.invalidSessionUrl("/invalid"))// 세션이 유효하지 않을때 이동할 페이지
			.sessionManagement(session -> session.sessionFixation().newSession())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
			//.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			 */
			/**
			 * <pre>
			📌 SessionCreationPolicy.ALWAYS: Spring Security가 항상 세션을 생성하도록 합니다. 이 정책은 애플리케이션이 인증 상태를 세션을 통해 반드시 관리해야 할 때 유용합니다.
			📌 SessionCreationPolicy.NEVER: Spring Security가 세션을 생성하지 않지만, 이미 생성된 세션이 있다면 사용할 수 있습니다. 이 설정은 세션을 생성하지 않음으로써 서버 자원을 절약하고자 할 때 유용할 수 있습니다. 그러나, 이 정책을 사용하면 인증 정보가 요청마다 재제공되어야 할 수 있습니다.
			📌 SessionCreationPolicy.IF_REQUIRED: 이것은 기본 정책으로, Spring Security가 필요한 경우에만 세션을 생성합니다. 예를 들어, 인증 과정에서 세션을 사용해야 한다면 세션을 생성합니다. 이 정책은 세션 사용을 최소화하면서 필요한 경우에는 세션을 활용하고자 할 때 적합합니다.
			📌 SessionCreationPolicy.STATELESS: Spring Security가 어떠한 상황에서도 세션을 생성하거나 사용하지 않도록 합니다. 이 정책은 REST API와 같이 세션을 사용하지 않는 서비스에서 유용합니다. 모든 요청은 자체적으로 인증 정보를 포함해야 하며, 서버는 요청 간 인증 상태를 유지하지 않습니다.
			                                    이로 인해 애플리케이션의 확장성이 증가하고 서버 자원이 절약될 수 있습니다.=> JWT 토큰에서 사용하며, 모든 사용자의 정보나 어떤 추가적인 그런 사항들을 다 토큰에 저장하고 세션 저장 없이 인증을 받음.
			 * </pre>
			 */
			// 401 403 관련 예외처리
			.exceptionHandling((exceptionConfig) -> exceptionConfig
					//.authenticationEntryPoint(unauthorizedEntryPoint) // 권한 정보 없음.
					.accessDeniedHandler(accessDeniedHandler) // 권한 없음.
			)
			// .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			// mif.prunus.com.security.AccountService..java loadUserByUsername 에서 로그인 정보 처리.
			.formLogin(form -> form
				//.loginPage("/login")
				.loginPage("/invalid")
				.usernameParameter("userId")
				.passwordParameter("userPwd")
				.authenticationDetailsSource(authenticationDetailSource) // 추가 파라미터 처리
				.loginProcessingUrl("/user/loginProc") // 로그인 처리  URL, default: /login_proc, 원칙적으로 post 방식만 지원
				.defaultSuccessUrl("/", true)
				.successHandler(onAuthenticationSuccess)// 로그인 성공 후 핸들러
				.failureHandler(onAuthenticationFailure)// 로그인 실패 후 핸들러
				.permitAll()
			)
			// .authenticationProvider(authenticationProvider)
			.logout(form -> form // 로그아웃 기능 작동함
				.logoutUrl("/logout") // 로그아웃 처리 URL, default: /logout, 원칙적으로 post 방식만 지원
				.logoutSuccessUrl("/") // 로그아웃 성공 후 이동페이지
				.deleteCookies("JSESSIONID", "remember-me") // 로그아웃 후 쿠키 삭제
				// .addLogoutHandler( ...생략... ) // 로그아웃 핸들러
				.logoutSuccessHandler(onLogoutSuccess) // 로그아웃 성공 후 핸들러
			);
			// password 체크 로직
			// org.springframework.security.authentication.dao.DaoAuthenticationProvider.additionalAuthenticationChecks
		return http.build();
	}

	/**
	 * 로그아웃 성공 후 핸들러
	 */
	public final LogoutSuccessHandler onLogoutSuccess = (request, response, authentication) -> {
		ErrorResponse success = new ErrorResponse(HttpStatus.OK, "SUCCESS");
		response.setStatus(success.getStatus().value());
		String json = new ObjectMapper().writeValueAsString(success);
		log.info("LogoutSuccessHandler json: {}", json);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};
	/**
	 * 로그인 성공 후 핸들러
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final AuthenticationSuccessHandler onAuthenticationSuccess = (request, response, authentication) -> {

		ErrorResponse success = new ErrorResponse(HttpStatus.OK, "SUCCESS");
		log.info("AuthenticationSuccessHandler authentication: {}", authentication.getName());

		FormWebAuthenticationDetails authenticationDetails = (FormWebAuthenticationDetails) authentication.getDetails();
		String secretKey = authenticationDetails.getSecretKey();
		log.info("AuthenticationSuccessHandler secretKey: {}", secretKey);
		response.setStatus(success.getStatus().value());
		//String json = new ObjectMapper().writeValueAsString(success);
		
		Map rsMap = new HashMap();
		rsMap.put("status", success.getStatus());
		rsMap.put("message", success.getMessage());
		rsMap.put("isLogin", LoginUtil.isLogin());
		rsMap.put("userDetails", LoginUtil.getUserDetails());

		String json = new ObjectMapper().writeValueAsString(rsMap);
		log.info("AuthenticationSuccessHandler json: {}", json);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};
	/**
	 * 로그인 실패 후 핸들러
	 */
	public final AuthenticationFailureHandler onAuthenticationFailure = (request, response, authException) -> {
		ErrorResponse fail = new ErrorResponse(HttpStatus.OK, "Spring security login Fail ...");
		log.info("AuthenticationFailureHandler exception: {}", authException.getMessage());
		response.setStatus(fail.getStatus().value());
		String json = new ObjectMapper().writeValueAsString(fail);
		log.info("AuthenticationFailureHandler json: {}", json);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};
	/**
	 * 인증 실패 후 핸들러
	 */
	public final AuthenticationEntryPoint unauthorizedEntryPoint = (request, response, authException) -> {
		ErrorResponse fail = new ErrorResponse(HttpStatus.UNAUTHORIZED, "Spring security unauthorized... EntryPoint ");
		log.info("AuthenticationEntryPoint exception: {}", fail.getMessage());
		response.setStatus(fail.getStatus().value());
		String json = new ObjectMapper().writeValueAsString(fail);
		log.info("AuthenticationEntryPoint json: {}", json);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

	/**
	 * 인증 성공 후 권한 없음 핸들러
	 */
	public final AccessDeniedHandler accessDeniedHandler = (request, response, accessDeniedException) -> {
		ErrorResponse fail = new ErrorResponse(HttpStatus.FORBIDDEN, "Spring security forbidden... AccessDenied ");
		log.info("AccessDeniedHandler exception: {}", fail.getMessage());
		response.setStatus(fail.getStatus().value());
		String json = new ObjectMapper().writeValueAsString(fail);
		log.info("AccessDeniedHandler json: {}", json);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.flush();
	};

	@Getter
	@RequiredArgsConstructor
	public class ErrorResponse {

		private final HttpStatus status;
		private final String message;
	}
}
