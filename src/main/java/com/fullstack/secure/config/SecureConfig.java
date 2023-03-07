package com.fullstack.secure.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/*
스프링에서 시큐어 작업을 할때 필요한 Bean 등을 여기서 정의 후에
스프링커네이너가 로드시 해당 객체를 Bean 으로 생성해서 메모리에
올리고, 관리하게끔 저의 합니다. 이후 Service 나 Repository 등에서
해당 Bean 등을 사용하는 방식이 일반적입니다.
 */
@Log4j2
@Configuration
public class SecureConfig {

    //InMemoryUser 객체를 빈으로 생성하고,
    //기본적으로 스프링에서는 각 계정마다 사용되는 암호를 반드시 Encrypt 시키도록
    //강제화 되어 있습니다.
    //때문에 PasswordEncoder 라는 인터페이스 객체또한 빈으로 올려서 사용하도록 할 예정입니다.
    //@Bean 어노테이션을 적용후 사용될 타입을 리턴하는 메서드로 정의 하시면 기본 빈 설정은
    //끝납니다.


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //AuthcationManager 타입의 InMemoryUserDetail의 객체를 bean으로 생성해 기본 인증에 필요한 값들을 설정 후 메모리 올리는 작업.
    //여기서 기억해야할 것은 실제 사용자의 인증 정보를 가지고 있는 객체는 UserDetailService타입이다.

    @Bean
    public InMemoryUserDetailsManager userDetailsManager() {

        UserDetails user = User.builder()
                .username("asdf") //스프링에서는 Username이라는 속성은 unique라는 속성은 unique한 ID로 취??
                .password(passwordEncoder().encode("123"))
                .roles("User").build();//User 권한 부여
        log.info("생성된 유저 : " + user);
        System.out.println(user);
        //얘 탭치니까 완성된거;;
        //UserDetail 객체를 bean으로 생성해 기본 인증에 필요한 값들을 설정 후 메모리 올리는 작업.
        //여기서 기억해야할 것은 실제 사용자의 인증 정보를 가지고 있는 객체는 UserDetailService타입이다.
    return new InMemoryUserDetailsManager(user);

    }
    /*
    Authorization설정을 변경하는 작업
    Config에서 하는 방법을 적용해 인증시 실행되는 FilterChain을 수정해 그 내용을 적용해보자.
    여기서 주의깊게 봐야하는 건 인가작업 후 수행하는 객체인데, HttpSecurity을 사용해서
    인가작업, 로그인 후 리디렉션, 로그아웃, 세션 및 쿠키 삭제 등을 할 수 있다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((auth)->{
            auth.antMatchers("sample/exAll").permitAll(); //권한없는 애들도 능가
            auth.antMatchers("sample/exAdmin").hasAnyRole("admin");
        });

        http.logout().deleteCookies("JSESSIONID");

    return http.build();
    }
}