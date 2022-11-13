package com.umc.test.user.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
/*
 * 로그인을 위해 서버에 전달할 데이터의 형태
 * */
public class GetUserRes{
	private int userIdx;
	private String nickname;
	private String email;
	private String password;
	//String createdAt;
	//String updateAt;
	//String status;
}