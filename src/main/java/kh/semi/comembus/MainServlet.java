package kh.semi.comembus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kh.semi.comembus.community.model.dto.Community;
import kh.semi.comembus.community.model.service.CommunityService;
import kh.semi.comembus.gathering.model.dto.Gathering;
import kh.semi.comembus.gathering.model.service.GatheringService;
import kh.semi.comembus.member.model.dto.Member;
import kh.semi.comembus.member.model.service.MemberService;

/**
 * Servlet implementation class MainServlet
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GatheringService gatheringService = new GatheringService();
	private MemberService memberService = new MemberService();
	private CommunityService communityService = new CommunityService();	
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {			
			
			Map<String, Object> param = new HashMap<>();
			param.put("start", 1);
			param.put("end", 4);
			
			// 프로젝트 미리보기(최신 4개)
			List<Gathering> projectList = gatheringService.findGatheringAll(param); 
			// 모집인원
			// List<GatheringExt> capacityJobList = gatheringService.findCapacityAll(param);

			// 멤버스 미리보기(최신 4개)			
			List<Member> memberList = memberService.findAll(param);

			// QnA 미리보기(최신 4개)
			List<Community> qlist = communityService.findQna(param);
				
			// 정보공유 미리보기
			List<Community> slist = communityService.findShare(param);
			
			// 자유주제 미리보기(최신 4개)
			List<Community> flist = communityService.findFree(param);
				
			
			// view단처리
			request.setAttribute("projectList", projectList);
			request.setAttribute("memberList", memberList);
			request.setAttribute("qlist", qlist);
			request.setAttribute("flist", flist);
			request.setAttribute("slist", slist);

			request.getRequestDispatcher("/WEB-INF/views/main.jsp").forward(request, response);
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		
	}



}