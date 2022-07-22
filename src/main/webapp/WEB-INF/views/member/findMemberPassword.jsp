<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/member/memberLogin.css" />

<section id=find-container>
	<h2>비밀번호 찾기</h2>
	<div class="find-content">
		<p>비밀번호는 암호화 저장되므로 분실 시 찾아드릴 수 없는 정보입니다.</p>
		<p>본인 확인을 통해 비밀번호를 재설정 하실 수 있습니다.</p>
	</div>
	
	<form id="findPasswordFrm" name="findPasswordFrm" action="<%= request.getContextPath() %>/membus/resetMemberPassword">
	<table id="findPasswordTable" class="find-table">
		<tbody>
			<tr> 
				<th><label for="name" class="find-label">이름</label></th>
				<td><input type="text" id="name" name="name" class="find-input"/></td>
			</tr>
			<tr>
				<th><label for="phone" class="find-label">핸드폰 번호</label></th>
           		<td><input type="text" id="phone" name="phone" class="find-input"/></td>
			</tr>
			<tr> 
				<th><label for="id" class="find-label">아이디</label></th>
				<td><input type="text" id="id" name="id" class="find-input"/></td>
			</tr>
        </tbody>
     </table>
     	<button id="resetPasswordBtn" class="find-btn">본인 확인</button>
     </form>
     
     <script>
	 // 본인 확인 폼 제출 전 유효성 검사
     document.findPasswordFrm.onsubmit = (e) => {
    	const nameVal = document.querySelector("#name").value;
    	const phoneVal= document.querySelector("#phone").value;
    	const idVal= document.querySelector("#id").value;
    	if(nameVal === "" || phoneVal === "" || idVal === ""){
    		alert("3가지 항목을 모두 정확히 입력하신 후 아이디 찾기가 가능합니다.");
    		return false;
    	}
     };
     </script>

</section>

<%@ include file="/WEB-INF/views/common/footer.jsp" %>