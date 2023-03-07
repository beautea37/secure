package com.fullstack.secure.club.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember {
    @Id
    private String email;
    private String name;
    private String password;
    // OAuth2를 적용할 필드. 소셜로그인 여부 필드
    private boolean fromSocial;
    /*
    테이블 생성시 참조 테이블인 권한 테이블을 생성하는데 이 때 사용할 타입이 enum이다.
    그래서 이 타입을 사용하는 선언으로 @ElementCollection을 사용한다.
    기본적으로 fetch타입은 Lazy
     */

    @ElementCollection(fetch = FetchType.LAZY)
    //위에서처럼
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>();
    public void addMemberRole(ClubMemberRole clubMemberRole) {
        roleSet.add(clubMemberRole);
    }
}
