package com.umc.test.user;

import static com.umc.test.config.BaseResponseStatus.POST_USERS_EMPTY_EMAIL;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.umc.test.config.BaseException;
import com.umc.test.config.BaseResponse;
import com.umc.test.user.model.DeleteUserReq;
import com.umc.test.user.model.GetUserRes;
import com.umc.test.user.model.PatchUserReq;
import com.umc.test.user.model.PostLoginReq;
import com.umc.test.user.model.PostLoginRes;
import com.umc.test.user.model.PostUserReq;
import com.umc.test.user.model.PostUserRes;
import com.umc.test.user.model.User;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/app/users") // 공통된 경로
public class UserController{

	final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass()); // Log 남기기

	@Autowired // 객체 생성을 스프링에서 자동으로 생성해주는 역할, 주입하려는 객체의 타입이 일치하는 객체를 자동으로 주입
	private final UserProvider userProvider;
	@Autowired 
	private final UserService userService;
	@Autowired
	//private final JwtService jwtService;

	public UserController(UserProvider userProvider, UserService userService) {
		this.userProvider = userProvider; // provider는 select할 때 사용
		this.userService = userService; // service는 insert, update, delete할 때 사용
	}

	@ResponseBody
	@PostMapping("/sign-up")
	public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq){
		// @RequestBody란 클라이언트가 전송하는 HTTP Request Body를 자바 객체로 매핑시켜주는 어노테이션 (body값이 PostUserReq 타입이다~)
		// email에 값이 존재하는지 빈값으로 요청하지 않았는지 검사한다. 빈값으로 요청했다면 에러메시지를 보낸다.
		if(postUserReq.getEmail() == null) {
			return new BaseResponse<>(POST_USERS_EMPTY_EMAIL);
		}

		// 이메일 정규표현 : 입력받은 이메일이 email@domain.xxx와 같은 형식인지 검사한다. 형식이 올바르지 않으면 에러메시지를 보낸다.
		/*
		if(!isRegexEmail(postUserReq.getEmail())) {
			return new BaseResponse<>(POST_USER_INVALID_EMAIL);	
		}
		 */

		try {
			PostUserRes postUserRes = userService.createUser(postUserReq);
			return new BaseResponse<>(postUserRes);
		} catch(Exception e) {
			return new BaseResponse<>((((BaseException) e).getStatus()));
		}
	}

	@ResponseBody
	@PostMapping("/log-in")
	public BaseResponse<PostLoginRes> logIn(@RequestBody PostLoginReq postLoginReq){
		try {
			PostLoginRes postLoginRes = userProvider.logIn(postLoginReq);
			return new BaseResponse<>(postLoginRes);
		} catch(Exception e) {
			return new BaseResponse<>((((BaseException) e).getStatus()));
		}
	}

	@ResponseBody
	@GetMapping("")
	public BaseResponse<List<GetUserRes>> getUsers(@RequestParam(required = false) String nickname){
		// path-variable null값이나 공백값이 들어가는 경우엔 적용하지 말 것
		try {
			if(nickname == null) {
				List<GetUserRes> getUserRes = userProvider.getUsers();
				return new BaseResponse<>(getUserRes);
			}
			List<GetUserRes> getUserRes = userProvider.getUsersByNickName(nickname);
			return new BaseResponse<>(getUserRes);
		} catch(BaseException e) {
			return new BaseResponse<>((e.getStatus()));
		}
	}

	@ResponseBody
	@GetMapping("/{userIdx}")
	public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx){
		// path-variable null값이나 공백값이 들어가는 경우엔 적용하지 말 것
		try {
			GetUserRes getUserRes = userProvider.getUser(userIdx);
			return new BaseResponse<>(getUserRes);
		} catch(Exception e) {
			return new BaseResponse<>((((BaseException) e).getStatus()));
		}
	}

	@ResponseBody
	@PatchMapping("/{userIdx}")
	public BaseResponse<String> modifyUserName(@PathVariable("userIdx") int userIdx, @RequestBody User user){
		try {
			PatchUserReq patchUserReq = new PatchUserReq(userIdx, user.getNickname());
			userService.modifyUserName(patchUserReq);

			String result = "회원정보가 수정되었습니다.";
			return new BaseResponse<>(result);
		} catch(BaseException e) {
			return new BaseResponse<>((((BaseException) e).getStatus()));
		}
	}
	
	@ResponseBody
	@DeleteMapping("/delete/{userIdx}")
	public BaseResponse<String> deleteUser(@PathVariable("userIdx") int userIdx){
		try {
			DeleteUserReq deleteUserReq = new DeleteUserReq(userIdx);
			userService.deleteUser(deleteUserReq);
			
			String result = "회원정보가 삭제되었습니다.";
			return new BaseResponse<>(result);
		} catch(BaseException e) {
			return new BaseResponse<>(e.getStatus());
		}
	}
}