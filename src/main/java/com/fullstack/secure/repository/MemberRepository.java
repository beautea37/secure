package com.fullstack.secure.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.fullstack.secure.entity.Member;

import lombok.extern.log4j.Log4j2;

/*
 * 테스트용 Member 객체를 생성, WAS 로딩시 해당 객체를 메모리에 로드합니다.
 * 이때 사용하는 어노테이션은 @PostConstruct라고해서 init() 메서드와 일반적으로 같이 사용됩니다.
 * 이걸 이용하면 WAS실행시에 해당 메서드가 제일 먼저 실행이 되고, 메모리에 값을 올립니다.
 * 사용자 정보에 관련된 내용은 한사람 이상이라고 가정하여, Collection으로 관리하고, 동기화를 적용해서 동기화에 안정적인 
 * 컬렉션을 사용합니다.   
 */
@Log4j2
@Repository
public class MemberRepository {

	//멤버객체를 관리할 컬렉션 초기화.
	private static Map<Long, Member> storage = Collections.synchronizedMap(new HashMap<Long, Member>());
	
	private static long seq = 0;//Member 객체의 시퀀스
	
	//테스트용 데이터 추가 init() 정의 반드시 @PostConstruct선언해야합니다
	@PostConstruct
	public void init() {
		Member member = Member.builder().loginId("asdf").passwd("123").name("이름").build();
		
		//save는 정의 후에 호출예정
		save(member);
	}
	
	//save()정의 member에 시퀀스 설정하고, 생성된 멤버의 toString(),
	//회원들 저장소(Map)에 추가 작업 후 저장될 Member 객체를 init에 리턴해줘야 합니다.
	private Member save(Member member) {
		member.setId(++seq);
		
		log.info("저장될 멤버 정보" + member);
		
		//Map에 put
		storage.put(member.getId(), member);
		
		return member;
	}
	//이번엔 id를 기준으로 사용자를 찾아서 리턴하는 메서드를 정의합니다.
	//지금까지 했던 기능들을 나열할테니 잘 이해해보세요
	
	//사용자 저장소(storage)에서 전체 사용자를 ArrayList를 변환합니다.
	private List<Member> findAll(){
		
		return new ArrayList<>(storage.values());
	}
	
	//위 정의된 findAll 에서 생성된 회원 정보를 이용, 특정 id에 해당하는 
	//Member가 있는지 조회 후 findFirst()(Stream)을 이용 객체를 리턴시키는 메서드 정의
	public Optional<Member> findByLoginId(String loginId){
		
		return this.findAll().stream().filter(m->m.getLoginId().equals(loginId)).findFirst();
	}
	
	public Member findById(String loginId) {
		
		return findByLoginId(loginId).get();
	}
	
	
	
	
	
	
}
