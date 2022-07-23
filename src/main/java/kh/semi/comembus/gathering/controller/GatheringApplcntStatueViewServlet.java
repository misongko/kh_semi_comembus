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

import kh.semi.comembus.alert.model.dto.Alert;
import kh.semi.comembus.alert.model.dto.IsRead;
import kh.semi.comembus.alert.model.dto.MessageType;
import kh.semi.comembus.alert.model.service.AlertService;
import kh.semi.comembus.common.ComembusUtils;
import kh.semi.comembus.gathering.model.dto.Gathering;
import kh.semi.comembus.gathering.model.dto.GatheringExt;
import kh.semi.comembus.gathering.model.dto.GatheringType;
import kh.semi.comembus.gathering.model.service.GatheringService;
import kh.semi.comembus.member.model.dto.JobCode;
import kh.semi.comembus.member.model.dto.MemberExt;
import kh.semi.comembus.member.model.service.MemberService;

/**
 * Servlet implementation class GatheringApplcntStatueViewServlet
 */
@WebServlet("/gathering/showApplicants")
public class GatheringApplcntStatueViewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GatheringService gatheringService = new GatheringService();
	private MemberService memberService = new MemberService();
	private AlertService alertService = new AlertService();
	/**
	 * 모임 상세보기 페이지에서 지원현황 탭 클릭시 페이지 이동.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		try {
			// 사용자 입력 페이지 정보
			int cPage = 1;
			int numPerPage = 20;
			try {
				cPage = Integer.parseInt(request.getParameter("cPage"));
			} catch (NumberFormatException e) {}
			
			int start = (cPage - 1) * numPerPage + 1;
			int end = cPage * numPerPage;
			Map<String, Object> param = new HashMap<>();
			param.put("start", start);
			param.put("end", end);
			//게시물번호
			int psNo = Integer.parseInt(request.getParameter("psNo"));
			param.put("psNo", psNo);
			List<MemberExt> memberList = memberService.getApldMemberList(param);
			int totalApldMemberNum = memberService.getApldMemberNum(psNo);
			String url = request.getRequestURI();
			String pagebar = ComembusUtils.getPagebar(cPage, numPerPage, totalApldMemberNum, url);
			
			//게시물 정보
			GatheringExt gathering = (GatheringExt) gatheringService.findByNo(psNo);
			
			
			request.setAttribute("gathering", gathering);
			request.setAttribute("memberList", memberList);
			request.setAttribute("pagebar", pagebar);
			request.getRequestDispatcher("/WEB-INF/views/gathering/projectApplicantStatue.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * 모임게시글상세>>지원자현황 페이지 신청결과 전송 후 페이지 리다이렉트
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//모임게시글번호, 지원자 아이디, 지원자 직무코드, 지원결과, 게시물타입을 받아옴
		int psNo = Integer.parseInt(request.getParameter("psNo"));
		String apldMemberId = request.getParameter("apldMemberId");
		String jobCode = request.getParameter("jobCode");
		String apldResult = request.getParameter("apldResult");
		GatheringType psType = GatheringType.valueOf(request.getParameter("psType"));

		//jsp ajax에서 결과 확인용 
		boolean result = false;
		
		//프로젝트 게시물인경우 직무별 모집정원, 모집된 인원 비교 
		Map<String, Integer> capacitiesByDept = new HashMap<>();
		if(psType == GatheringType.P && "O".equals(apldResult)) {
			capacitiesByDept = gatheringService.getCapacitiesByDept(psNo);
			GatheringExt project = (GatheringExt) gatheringService.findByNo(psNo);
			int capa = capacitiesByDept.get(jobCode) != null? capacitiesByDept.get(jobCode) : 0;
			int memCnt = 0;
			switch(JobCode.valueOf(jobCode)) {
			case PL: memCnt = project.getPlanning_cnt(); break;
			case DG: memCnt = project.getDesign_cnt(); break;
			case BE: memCnt = project.getBackend_cnt(); break;
			case FE: memCnt = project.getFrontend_cnt(); break;
			}
			
			if(capa == memCnt || capa == 0) {
				//응답 result = false
				response.setContentType("application/json; charset=utf-8");
				System.out.println("[@지원자현황서블릿] 직무별정원초과시result>>"+result);
				String jsonStr  = new Gson().toJson(result);
				response.getWriter().print(jsonStr);
				return;
			}
			else {
				//모임글 직무별 모집인원현황 테이블에 모집된 인원수 update
				Map<String, Object> param = new HashMap<>();
				param.put("psNo",psNo);
				param.put("jobCode", jobCode);
				int _result = gatheringService.addPSMemNumByDept(param);
				System.out.println("[@지원자현황서블릿] 1명추가result>>"+_result);
			
			}
			System.out.println("[@지원자현황서블릿] 직무별테이블변경result>>"+result);
			
		}
		
		//모임지원현황 테이블에 update
		Map<String, Object> param = new HashMap<>();
		param.put("psNo",psNo);
		param.put("apldMemberId", apldMemberId);
		param.put("apldResult", apldResult);
		int _result = gatheringService.updateApldResult(param);
		
			
		//알림테이블 insert
		String nickName = request.getParameter("nickName");
		Gathering gather = gatheringService.findByNo(psNo);
		//알림내용 글자 수 줄이기
		String title = gather.getTitle();
		String substrTitle = title.length() > 8? title.substring(0, 7)+"...": title;
		String alertContent = "["+substrTitle +"]의 모임장이 회원님의 지원을 "+ ("O".equals(apldResult) ? "수락" : "거절"+"하셨습니다.");
		
		Alert alert = new Alert(0, apldMemberId, psNo, 0, MessageType.APPLY_RESULT, alertContent, IsRead.N);
		_result = alertService.notifyAplcntResult(alert);
		result = _result > 0;
		System.out.println("[@지원자현황서블릿] 마지막result>>"+result);
		//응답result:boolean-> string으로 변환
		response.setContentType("application/json; charset=utf-8");
		String jsonStr  = new Gson().toJson(String.valueOf(result));
		response.getWriter().print(jsonStr);
	}

}
