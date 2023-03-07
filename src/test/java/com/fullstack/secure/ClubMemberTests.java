package com.fullstack.secure.club;

import com.fullstack.secure.club.entity.ClubMember;
import com.fullstack.secure.club.entity.ClubMemberRole;

import com.fullstack.secure.club.repository.ClubMemberRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private ClubMemberRepository clubmemberRepository;
    //암호는 반드시 Encoding 되어야 합니다. 스프링 Security 사용시엔..
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {
        //1-80 USER;
        //81-90 MEMBER;
        //나머지 ADMIN 부여

        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@asdf.com")
                    .name("이름" + i)
                    .password(passwordEncoder.encode("123"))
                    .fromSocial(false)
                    .build();
            //Role 적용..
            clubMember.addMemberRole(ClubMemberRole.USER);

            if (i > 80) {
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 90) {
                clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }
            clubmemberRepository.save(clubMember);
        });
    }

    @Test
    public void testRead() {
        Optional<ClubMember> result =
                clubmemberRepository.findByEmail("user100@asdf.com", false);
        System.out.println(result.get());
        /*
        스프링 시큐어 사용할 때 기억해야할 것
        1. 개발자가 원하는 방식으로 로그인을 처리하려면 반드시 구현해야하는 인터페이스가 있는데, UserDetailInterface
        2.이 인터페이스에는 사용자의 정보를 이름(username)으로 받아 정보객체를 리턴하는 UserDetail이라는 객체 리턴
        3. UserDetail 내에는 사용자의 인증정보 및 인가정보등을 확인할 수 있는 메서드가 다수 포함되어있다.
        4. 2번에서 사용된 username은 특정 객체에 속한 username이 아니라 pk로 사용되는 값을 의미.
        5. 4의 의미를 되돌려보면 스프링에는 USER라는 객체가 있고 그 객체엔 사용자의 식별정보(user)가 있고 내부의 암호를 담은 필드가 있다.
        6. 다시 순서를 정의해보면, 일단 User의 이름으로 User가 있는지 찾고 존재한다면 암호를 검증하고(틀리면 인증 오류 메시지 출력),
        인증되면 이후 Permit해주는 형식이다.
         */

    }
}