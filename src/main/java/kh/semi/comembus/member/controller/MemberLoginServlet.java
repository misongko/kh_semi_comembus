package kh.semi.comembus.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kh.semi.comembus.member.model.dto.Member;
import kh.semi.comembus.member.model.service.MemberService;

@WebServlet("/membus/login")
public class MemberLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private MemberService memberService = new MemberService();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String location = request.getHeader("Referer");
		request.setAttribute("location", location);
		
		request.getRequestDispatcher("/WEB-INF/views/member/memberLogin.jsp")
			.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setCharacterEncoding("utf-8");
			
			String memberId = request.getParameter("memberId");
			String password = request.getParameter("password");
			String location = request.getParameter("location");
			System.out.println("memberId = " + memberId);
			System.out.println("password = " + password);
			System.out.println("location = " + location);
			
			Member member = memberService.findById(memberId);
			System.out.println("member@MemberLoginServlet = " + member); 
			
			HttpSession session = request.getSession(true); // session이 존재하지 않으면, 새로 생성해서 반환. true 생략 가능
			System.out.println(session.getId());
			
			if(member != null && password.equals(member.getPassword())) {
				session.setAttribute("loginMember", member);
			}
			else {
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			}
			
			response.sendRedirect(location);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}

}
