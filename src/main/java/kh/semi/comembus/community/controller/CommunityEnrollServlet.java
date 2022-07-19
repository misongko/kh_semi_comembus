package kh.semi.comembus.community.controller;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import kh.semi.comembus.community.model.dto.Community;
import kh.semi.comembus.community.model.service.CommunityService;

/**
 * Servlet implementation class CommunityEnrollServlet
 */
/**
 * Servlet implementation class BoardEnrollServlet
 */
@WebServlet("/community/communityEnroll")
public class CommunityEnrollServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CommunityService communityService = new CommunityService();	
	/**
	 * GET 게시글 등록폼 요청
	 * form 페이지에 연결
	 * 
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = (String) request.getParameter("co_type");
		Community qenroll = null;
		Community fenroll = null;
		Community senroll = null;
		
			if("Q".equals(type)) {
				request.setAttribute("qenroll", qenroll);
				request.getRequestDispatcher("/WEB-INF/views/community/qnaEnroll.jsp")
				.forward(request, response);
				
			}else if("F".equals(type)) {
				request.setAttribute("fenroll", fenroll);
				request.getRequestDispatcher("/WEB-INF/views/community/freeEnroll.jsp")
				.forward(request, response);
				
			}else {
				request.setAttribute("senroll", senroll);
				request.getRequestDispatcher("/WEB-INF/views/community/shareEnroll.jsp")
				.forward(request, response);
			}
		}
	

	/**
	 * POST db에 insert 요청(게시글 등록하기 버튼 눌렀을시 작동할)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = (String) request.getParameter("co_type");
		try {
			// 1.사용자입력값처리
			String title = request.getParameter("coTitle");
			String writer = request.getParameter("coWriter");
			String content = request.getParameter("coContent");
			String ctype = request.getParameter("coType");
			Community commu = new Community(0, title, writer, content, 0, null, 0, ctype);
			
			// 2.업무로직
//			int result = communityService.insertCommunity(community);
			
			// 3.redirect(오류나지 않는이상 무조건 성공)
			//Session에 등록해야 리다이렉트 후에 내용이 나온다.
			if("Q".equals(type)) {
				int qenroll = communityService.enrollQna(commu);
				request.getSession().setAttribute("msg", "게시글을 성공적으로 등록했습니다");
				response.sendRedirect(request.getContextPath() + "/community/qnaList");
				
			}else if("F".equals(type)) {
//				Community fenroll = communityService.enrollFree(commu);
				request.getSession().setAttribute("msg", "게시글을 성공적으로 등록했습니다");
				response.sendRedirect(request.getContextPath() + "/community/freeList");
				
			}else {
//				Community senroll = communityService.enrollShare(commu);
				request.getSession().setAttribute("msg", "게시글을 성공적으로 등록했습니다");
				response.sendRedirect(request.getContextPath() + "/community/shareList");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	
	}

}
