package com.umc.test.user;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.umc.test.user.model.DeleteUserReq;
import com.umc.test.user.model.GetUserRes;
import com.umc.test.user.model.PatchUserReq;
import com.umc.test.user.model.PostLoginReq;
import com.umc.test.user.model.PostUserReq;
import com.umc.test.user.model.User;

@Repository
public class UserDao{
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	// 회원가입
	public int createUser(PostUserReq p) {
		String query = "insert into User (email, password, nickname) values (?,?,?)";
		Object[] param = new Object[] {p.getEmail(), p.getPassword(), p.getNickname()};
		this.jdbcTemplate.update(query, param);
		
		String lastInserIdQuery = "select last_insert_id()"; // 가장 마지막에 삽입된 id값을 가져온다. 
		return this.jdbcTemplate.queryForObject(lastInserIdQuery, int.class); // 마지막으로 삽입된 유저의 userIdx를 반환한다. 
	}
	
	// 이메일 확인
	public int checkEmail(String email) {
		String query = "select exists(select email from User where email=?)";
		String param = email;
		return this.jdbcTemplate.queryForObject(query, int.class, param); // 쿼리문의 결과 (존재하지 않음 = false[0] / 존재함 = true[1] 반환)
	}
	
	// 회원정보 수정
	public int modifyUserName(PatchUserReq p) {
		String query = "update User set nickname=? where userIdx=?";
		Object[] param = new Object[] {p.getNickname(), p.getUserIdx()};
		return this.jdbcTemplate.update(query, param);
	}
	
	// 로그인 : 해당 email에 해당되는 user의 암호화된 비밀번호 값을 가져온다.
	public User getPwd(PostLoginReq postLoginReq) {
		String query = "select userIdx, password, email, nickname from User where email=?";
		String param = postLoginReq.getEmail();
		return this.jdbcTemplate.queryForObject(query,
				(rs, rowNum) -> new User(
						rs.getInt("userIdx"),
						rs.getString("nickname"),
						rs.getString("email"),
						rs.getString("password")),
				param);
				
	}
	
	public List<GetUserRes> getUsers(){
		String query = "select * from User";
		return this.jdbcTemplate.query(query,
				(rs, rowNum) -> new GetUserRes(
						rs.getInt("userIdx"),
						rs.getString("nickname"),
						rs.getString("email"),
						rs.getString("password"))
				);
	}
	public List<GetUserRes> getUserByNickName(String nickname){
		String query = "select * from User where nickname=?";
		String param = nickname;
		return this.jdbcTemplate.query(query, 
				(rs, rowNum) -> new GetUserRes(
						rs.getInt("userIdx"),
						rs.getString("nickname"),
						rs.getString("email"),
						rs.getString("password")), 
					param);
				
	}
	
	public GetUserRes getUser(int userIdx) {
		String query = "select * from User where userIdx=?";
		int param = userIdx;
		return (GetUserRes) this.jdbcTemplate.query(query, 
				(rs, rowNum) -> new GetUserRes(
						rs.getInt("userIdx"),
						rs.getString("nickname"),
						rs.getString("email"),
						rs.getString("password")), 
					param);
	}
	
	public int deleteUser(DeleteUserReq deleteUserReq) {
		String query = "delete from User where userIdx=?";
		Object[] param = new Object[] {deleteUserReq.getUserIdx()};
		return this.jdbcTemplate.update(query, param);
	}
	
	
}