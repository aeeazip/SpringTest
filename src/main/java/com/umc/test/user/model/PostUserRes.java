package com.umc.test.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor // 해당 클래스의 모든 멤버 변수를 받는 생성자를 생성
@NoArgsConstructor(access = AccessLevel.PROTECTED)

/*
 * 회원가입의 결과를 보여주는 데이터의 형태
 * */
public class PostUserRes{
	private int userIdx;
// private String jwt;
}