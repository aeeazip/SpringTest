package com.umc.test.user;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umc.test.config.BaseException;
import com.umc.test.config.Secret;
import com.umc.test.user.model.GetUserRes;
import com.umc.test.user.model.PostLoginReq;
import com.umc.test.user.model.PostLoginRes;
import com.umc.test.user.model.*;

import ch.qos.logback.classic.Logger;
import static com.umc.test.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.test.config.BaseResponseStatus.PASSWORD_DECRYPTION_EMAIL;
import static com.umc.test.config.BaseResponseStatus.FAILED_TO_LOGIN; 

import com.umc.test.utils.*;
@Service
public class UserProvider{
	private final UserDao userDao;
	//private final JwtService jwtService;
	
	final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public UserProvider(UserDao userDao) {
		this.userDao = userDao;
		//this.jwtService = jwtService;
	}
	
	public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
		User user = userDao.getPwd(postLoginReq);
		String pwd;
		
		
		try {
			// 암호화 : postUserReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장한다.
			// ex) password123 -> sfa;dhfha;oehofhwpehowjefoef
			pwd = user.getPassword();// 암호화
		} catch(Exception e) { // 암호화 실패하면 에러 발생
			throw new BaseException(PASSWORD_DECRYPTION_EMAIL);
		}
		
		
		if(postLoginReq.getPassword().equals(pwd)) {
			int userIdx = userDao.getPwd(postLoginReq).getUserIdx();
			return new PostLoginRes(userIdx);
		} else {
			throw new BaseException(FAILED_TO_LOGIN);
		}
	}
	
	// 해당 이메일이 이미 User Table에 존재하는지 확인
	public int checkEmail(String email) throws BaseException{
		try {
			return userDao.checkEmail(email);
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// Users들의 정보를 조회
	public List<GetUserRes> getUsers() throws BaseException{
		try {
			List<GetUserRes> getUserRes = userDao.getUsers();
			return getUserRes;
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// 해당 nickname을 갖는 User들의 정보 조회
	public List<GetUserRes> getUsersByNickName(String nickname) throws BaseException{
		try {
			List<GetUserRes> getUserRes = userDao.getUserByNickName(nickname);
			return getUserRes;
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	// 해당 userIdx를 갖는 user의 정보 조회
	public GetUserRes getUser(int userIdx) throws BaseException{
		try {
			GetUserRes getUserRes = userDao.getUser(userIdx);
			return getUserRes;
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
}