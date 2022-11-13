package com.umc.test.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 해당 클래스의 디폴트 생성자를 생성, 접근제한자 PROTECTED
public class PostUserReq{
	private String email;
	private String password;
	private String nickname;
}