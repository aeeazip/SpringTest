package com.umc.test.user;

import static com.umc.test.config.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.test.config.BaseResponseStatus.MODIFY_FAIL_USERNAME;
import static com.umc.test.config.BaseResponseStatus.PASSWORD_ENCRYPTION_EMAIL;
import static com.umc.test.config.BaseResponseStatus.POST_USERS_EXISTS_EMAIL;
import static com.umc.test.config.BaseResponseStatus.DELETE_FAIL_USER;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umc.test.config.BaseException;
import com.umc.test.user.model.DeleteUserReq;
import com.umc.test.user.model.PatchUserReq;
import com.umc.test.user.model.PostUserReq;
import com.umc.test.user.model.PostUserRes;
import com.umc.test.utils.SHA256;

import ch.qos.logback.classic.Logger;
@Service
public class UserService{
	

	final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private final UserDao userDao;
	private final UserProvider userProvider;
	// private final JwtService jwtService;
	
	@Autowired
	public UserService(UserDao userDao, UserProvider userProvider) {
		this.userDao = userDao;
		this.userProvider = userProvider; // provider는 select할 때 사용
		//this.jwtService = jwtService;
	}
	
	// 회원가입(POST)
	public PostUserRes createUser(PostUserReq postUserReq) throws BaseException{
		if(userProvider.checkEmail(postUserReq.getEmail()) == 1)
			throw new BaseException(POST_USERS_EXISTS_EMAIL);
		
		String pwd;
		try {
			// 암호화 : postUserReq에서 제공받은 비밀번호를 보안을 위해 암호화시켜 DB에 저장한다.
			// ex) password123 -> sfa;dhfha;oehofhwpehowjefoef
			pwd = new SHA256().encrypt(postUserReq.getPassword()); // 암호화
			postUserReq.setPassword(pwd);
		} catch(Exception e) { // 암호화 실패하면 에러 발생
			throw new BaseException(PASSWORD_ENCRYPTION_EMAIL);
		}
		
		try {
			int userIdx = userDao.createUser(postUserReq);
			return new PostUserRes(userIdx);
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public void modifyUserName(PatchUserReq patchUserReq) throws BaseException{
		try {
			int result = userDao.modifyUserName(patchUserReq);
			if(result == 0) {
				throw new BaseException(MODIFY_FAIL_USERNAME);
			}
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
	
	public void deleteUser(DeleteUserReq deleteUserReq) throws BaseException{
		try {
			int result = userDao.deleteUser(deleteUserReq);
			if(result == 0) {
				throw new BaseException(DELETE_FAIL_USER);
			}
		} catch(Exception e) {
			throw new BaseException(DATABASE_ERROR);
		}
	}
}