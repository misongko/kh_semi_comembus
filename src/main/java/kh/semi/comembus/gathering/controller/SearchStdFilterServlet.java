package kh.semi.comembus.gathering.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import kh.semi.comembus.gathering.model.dto.Gathering;
import kh.semi.comembus.gathering.model.service.GatheringService;

/**
 * Servlet implementation class SearchStdFilterServlet
 */
@WebServlet("/gathering/searchStdFilter")
public class SearchStdFilterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GatheringService gatheringService = new GatheringService();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			// 페이징 설정
			int cPage = 1;
			int numPerPage = 12;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch(NumberFormatException e) {}
			
			String searchLocal = request.getParameter("searchLocal");
			String searchTopic = request.getParameter("searchTopic");
			String selectLocalKeyword = request.getParameter("selectLocalKeyword");
			String selectTopicKeyword = request.getParameter("selectTopicKeyword");
			String statusYN = request.getParameter("statusYN");
			// 체크 시 N=모집중, 체크해제 시 All
			String memberId = request.getParameter("memberId");
			// 로그인을 했다면 memberId가, 안했다면 "" 공백문자가
			
			System.out.println("확인용 searchLocal = " + searchLocal);
			System.out.println(">>23일 확인용 searchTopic = " + searchTopic);
			System.out.println("확인용 selectLocalKeyword = " + selectLocalKeyword);
			System.out.println(">> 23일 확인용 selectTopicKeyword = " + selectTopicKeyword);
			System.out.println("확인용 statusYN = " + statusYN);
			System.out.println("확인용 memberId = " + memberId);
			
			Map<String, Object> param = new HashMap<>();
			param.put("searchLocal", searchLocal);
			param.put("searchTopic", searchTopic);
			param.put("selectLocalKeyword", selectLocalKeyword);
			param.put("selectTopicKeyword", selectTopicKeyword);
			param.put("statusYN", statusYN);
			param.put("start", (cPage - 1) * numPerPage + 1);
			param.put("end", cPage * numPerPage);
			System.out.println("확인용 param = " + param);
			
			// 2. 업무로직
			// content 영역
			List<Gathering> studyList = gatheringService.findStudyLike(param);
			// bookmark 영역
			Map<String, Object> bmParam = new HashMap<>();
			if(memberId != null) {
				bmParam.put("loginMemberId", memberId);
				System.out.println(">>> memberId " + memberId);
				System.out.println(">>> bmParam = " + bmParam);
			}
			List<Gathering> bookmarkList = gatheringService.findAllStdBookmarked(bmParam);
			
			System.out.println(">>> 필터링 확인 loginMemberId " + memberId);
			System.out.println(">>> 필터링 확인 studyList " + studyList);
			System.out.println(">>> 필터링 확인 bookmarkList " + bookmarkList);
			
			// pagebar 영역
			int totalContent = gatheringService.getStdTotalContentLike(param);
			System.out.println("필터링 totalContent = " + totalContent);
			System.out.println("cPage = " + cPage);
			
			response.setContentType("application/json; charset=utf-8");
			Map<String, Object> searchList = new HashMap<>();
			searchList.put("studyList", studyList);
			searchList.put("totalContent", totalContent);
			searchList.put("cPage", cPage);
			searchList.put("bookmarkList", bookmarkList);
			String jsonStr = new Gson().toJson(searchList);
			response.getWriter().print(jsonStr);
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}


}
