package com.fullstack.secure.DTO;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/*
 * 이 클래스는 요청 데이터를 바인딩하는 목적 다 알죠?>?
 * 여기에 validation 어노테이션을 추가해서 빈 값에 대한 처리를 합니다.
 */
@Data
public class LoginFormDTO {

	@NotBlank
	private String loginId;
	@NotBlank
	private String password;
}
