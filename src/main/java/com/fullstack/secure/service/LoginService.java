package com.fullstack.secure.service;

import org.springframework.stereotype.Service;

import com.fullstack.secure.entity.Member;
import com.fullstack.secure.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/*
 * Service 객체가 구현할 내용 : MemberRepository에서 회원 조회 후 파라미터로 넘어오는 passwod와 비교해서
 * 같으면 Member 인스턴스를 넘기고, 아닌 경우 null을 리턴하도록 정의합니다.
 */
@RequiredArgsConstructor
@Service
public class LoginService {

	private final MemberRepository memberRepository;
	
	public Member login(String loginId, String passwd) {
	
		return memberRepository.findByLoginId(loginId).filter(m->m.getPasswd().equals(passwd)).orElse(null);
	}
}
