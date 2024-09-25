package com.jjery.springcrud.config;

import com.jjery.springcrud.filter.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final JwtRequestFilter jwtRequestFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
            .csrf(CsrfConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)  // Bearer 방식을 사용하므로 httpBasic 비활성화
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // 세션 비활성화
            .authorizeHttpRequests(request ->
                    request
                            // 토큰 없이 접근 가능한 경로 (게시글 조회와 회원가입/로그인 관련 요청)
                            .requestMatchers(
                                    "/",
                                    "/swagger-ui/**",
                                    "/v3/api-docs/**",
                                    "/api/users/register",
                                    "/api/users/login",
                                    "/api/posts",  // 전체 게시글 조회 허용
                                    "/api/posts/{id}",  // 특정 게시글 조회 허용
                                    "/api/posts/{category}"  // 카테고리별 게시글 조회 허용
                            ).permitAll()

                            // 권한이 필요한 경로 (POST, PUT, DELETE 요청들)
                            .requestMatchers("/api/posts/**").hasAnyRole("USER", "ADMIN")  // 게시글 작성, 수정, 삭제는 인증 필요
                            .requestMatchers("/api/v1/user/**").hasRole("USER")  // 사용자 관련 API 접근은 USER 권한 필요
                            .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")  // 관리자 관련 API 접근은 ADMIN 권한 필요
                            .requestMatchers(HttpMethod.POST, "/api/posts/**").hasRole("USER")
                            .requestMatchers(HttpMethod.DELETE, "/api/posts/**").hasRole("USER")
                            .requestMatchers(HttpMethod.PUT, "/api/posts/**").hasRole("USER")
                            .requestMatchers(HttpMethod.PATCH, "/api/posts/**").hasRole("USER")

                            // 그 외 모든 요청은 인증 필요
                            .anyRequest().authenticated())
            .cors(Customizer.withDefaults());

    // JWT 필터 추가
    httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOriginPattern("*"); // 모든 도메인 허용
    configuration.addAllowedMethod("*"); // 모든 HTTP 메서드 허용
    configuration.addAllowedHeader("*"); // 모든 헤더 허용
    configuration.setAllowCredentials(true); // 자격 증명 허용

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
