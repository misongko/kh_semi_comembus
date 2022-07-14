package kh.semi.comembus.member.model.dao;

import static kh.semi.comembus.common.JdbcTemplate.close;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


import kh.semi.comembus.member.model.dto.JobCode;
import kh.semi.comembus.member.model.dto.Member;
import kh.semi.comembus.member.model.dto.MemberRole;
import kh.semi.comembus.member.model.dto.QuitYN;
import kh.semi.comembus.member.model.exception.MemberException;

public class MemberDao {
	
	private Properties prop = new Properties();
	
	// 미송 코드 시작
	public MemberDao() {
		String filename = MemberDao.class.getResource("/sql/member/member-query.properties").getPath();
		System.out.println("filename@MemberDao = " + filename);
		try {
			prop.load(new FileReader(filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Member findById(Connection conn, String memberId) {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Member member = null;
		String sql = prop.getProperty("findById");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberId);
			
			rset = pstmt.executeQuery();
			
			while(rset.next()) {
				member = handleMemberResultSet(rset);
			}
			
		} catch (SQLException e) {
			throw new MemberException("회원 아이디 조회 오류", e);
		} finally {
			close(rset);
			close(pstmt);
		}
		
		return member;
	}
	
	private Member handleMemberResultSet(ResultSet rset) throws SQLException {
		String memberId = rset.getString("member_id"); 
		JobCode jobCode = JobCode.valueOf(rset.getString("job_code"));
		String nickName = rset.getString("member_nickname"); 
		String memberName = rset.getString("member_name"); 
		String password = rset.getString("password"); 
		String phone = rset.getString("phone");
		String introduction = rset.getString("introduction");
		MemberRole memberRole = MemberRole.valueOf(rset.getString("member_role"));
		Date enrollDate = rset.getDate("enroll_date");
		Date quitDate = rset.getDate("quit_date") != null ? rset.getDate("quit_date") : null;
		QuitYN quitYN = QuitYN.valueOf(rset.getString("quit_yn"));
		return new Member(memberId, jobCode, nickName, memberName, password, phone, introduction, memberRole, enrollDate, quitDate, quitYN);
		
	}
	// 미송 코드 끝
	
	
	
	// 수진 코드 시작
	
	// 수진 코드 끝

}