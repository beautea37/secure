package com.fullstack.secure.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fullstack.secure.DTO.LoginFormDTO;
import com.fullstack.secure.entity.Member;
import com.fullstack.secure.role.SessionConstants;
import com.fullstack.secure.service.LoginService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/*
 * 사용자의 세션을 검증해서 여부에 따라 다른 화면을 보여주도록 분기합니다.
 * 로그인된 상태라면 사용자의 이름을 출력 할 것이고, 서블릿에서 사용한 session을 사용예정입니다.
 *
 * 스프링에서는 @SessionAttribute를 이용해서 간편하게 적용할 수 있습니다.
 *
 * @GetMapping에서는 로그인 폼을 @PostMapping 에서는 로그인 요청을 처리하도록 합니다.
 *
 * 요청 데이터를 LoginForm에 바인딩 하고 결과는 BindingResult를 사용합니다.
 *
 * 조건에 따라서 redirectURL을 사용, 리다렉션하고
 *
 * 만약 BindingResult에 타입이 안맞거나 회원이 존재하지 않는 경우 loginForm으로 리턴시킬예정
 */
@Controller
@Log4j2
@RequiredArgsConstructor
public class LoginController {

	//Service 추가
	private final LoginService loginService;

	//아래의 모든 매핑에선 여러분이 응용할 수 있도록 요청 패턴과 이에 대응하는
	//Viewer를 지금까지 했던 것과는 다르게 처리하였으니, 필요시 응용하세요.
	@GetMapping("/session/login")
	public String loginForm(@ModelAttribute LoginFormDTO loginFormDTO) {

		//Viewer매핑
		return "login/loginForm";

	}

	//아래에서는 못보던 @이 좀 나옵니다
	//일단 Validate(필드 검증), 서비스 수행 후 결과 바인딩되는 BindingResult(보통은 결과가 비정상적인 경우 오류 메세지 출력용으로 사용됨)등 입니다.
	@PostMapping("/session/login")
	public String login(@ModelAttribute @Validated LoginFormDTO formDTO , BindingResult bindingResult,
						@RequestParam(defaultValue = "/") String redirectURL, HttpServletRequest request) {

		//해당 사용자가 없거나 인증 오류등이 발생한 경우
		//bindingResult이 해당 오류에 대한 내용을 바인딩 함 따라서 이를 이용해 분기합니다
//      if(bindingResult.hasErrors()) {
//         return "login/loginForm";//로그인 폼 바인딩 함
//      }

		//로그인한 Member 객체 얻어내기
		Member loginMember = loginService.login(formDTO.getLoginId(), formDTO.getPassword());

		//멤버객체가 null일 경우, BindingResult에 에러 메세지 추가
		if(loginMember == null) {
			//bindingResult.reject("Failed Login", "ID 또는 비번이 틀립니다.");
			return "login/loginForm";
		}

		//이 영역은 테스트 후 등록된 사용자라면 세션을 적용할 예정입니다.
		//세션객체를 얻어냅니다. session 객체는 HttpServletRequest 객체를 통해 얻어냅니다.
		HttpSession session = request.getSession();
		session.setAttribute(SessionConstants.NORM_MEM , loginMember);

		return "redirect:" + redirectURL;
	}

	@PostMapping("/logout")
	public String logout(HttpServletRequest httpServletRequest) {

		//http세션을 아예 만들지 못하도록 getSeeeion(false)로 준다
		//만약 true로 주면 세션이 없을 경우 새로운 세션을 생성한다
		//만약 세션이 없을 경우엔 null을 리턴함
		HttpSession session = httpServletRequest.getSession(false);

		//세션에 담긴 모든 정보들을 무효화하는 메서드는 invalidate()임.
		//이것만 호출하면 모두 무효화 처리됩니다.
		if(session != null) {
			session.invalidate();
		}
		return "redirect:/session/login";
	}

}