<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    isELIgnored = "false"
%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<c:set var = "contextPath" value = "${pageContext.request.contextPath }"></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>고객정보 조회</title>
<script src = "http://code.jquery.com/jquery-latest.min.js"></script>
<script type = "text/javascript">
	function process(){
		var cust_id = $("#cust_id").val();
		console.log("cust_id : " + cust_id);
		if(cust_id == ''){
			alert("고객번호를 입력하세요.");
			 return;
		}
		$.ajax({
			type : "post",
			async : false,
			url : "${contextPath }/cust2",
			dataType : "text",
			data : {cust_id : cust_id},
			success : function(data, textStatus){
				var jsonInfo = JSON.parse(data);
				
				$("#id").val(jsonInfo.customer.cust_id);
				$("#name").val(jsonInfo.customer.cust_name);
				$("#state").val(jsonInfo.customer.cust_state);
				$("#country").val(jsonInfo.customer.cust_country);
				$("#email").val(jsonInfo.customer.cust_email);
	
		
			},
			error : function(data, textStatus){
				alert("에러가 발생했습니다.");
			}
		});
	
	}

</script>
</head>
<body>
고객번호 <input type = "text" name = "cust_id"> 
<input type = "button" value = "조회" onClick = "process()">

<br><br>

<table>
	<tr>
	<td>고객번호</td>
	<td><input type = "text" id = "id"></td>
	</tr>
	<tr>
	<td>고객이름</td>
	<td><input type = "text" id = "name"></td>
	</tr>
	<tr>
	<td>고객주</td>
	<td><input type = "text" id = "state"></td>
	</tr>
	<tr>
	<td>고객국가</td>
	<td><input type = "text" id = "country"></td>
	</tr>
	<tr>
	<td>고객이메일</td>
	<td><input type = "text" id = "email"></td>
	</tr>

</table>
	
</body>
</html>