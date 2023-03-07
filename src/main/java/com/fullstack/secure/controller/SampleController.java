package com.fullstack.secure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SampleController {
    @GetMapping("/sample/exAll")
    public void exAll() {

    }
    @GetMapping("/sample/exMember")
    public void exMember() {

    }
    @GetMapping("/sample/exAdmin")
    public void admin() {

    }
    /*
    AuthCationManager(인증작업처리 관리자)
    AuthnicationProvider(인증의 종류, 즉 인증 방식을 구현한 구현 객체 선택)
    선택된 인증방식을 통해 인증될 인증 정보객체인 UseDetailService객체 호출해 인증작업 시도
     */
}
