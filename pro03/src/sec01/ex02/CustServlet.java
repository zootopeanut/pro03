package sec01.ex02;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/cust2")
public class CustServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doHandle(request, response);
	}
	
	protected void doHandle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter writer = response.getWriter();
		
		System.out.println("cust_id : " + request.getParameter("cust_id") );
		String command = request.getParameter("command");
		System.out.println("command : " + command);
		
		CustDAO dao = new CustDAO();
		
		JSONObject totalObject = new JSONObject();
		JSONObject customerObject = new JSONObject();
		
		JSONArray errorArray = new JSONArray();
		JSONObject error = new JSONObject();
		
		JSONArray state = new JSONArray();
		JSONArray country = new JSONArray();

		dao.stateList(state);
		dao.countryList(country);
		
		totalObject.put("state", state);
		totalObject.put("country", country);
		
		String cust_id = request.getParameter("cust_id");
		//이전 아이디 찾기
		if(command != null && command.equals("before")){

			String b_id = dao.beforeCustomer(request.getParameter("cust_id"));
			System.out.println("이전 아이디 : " + b_id);
			cust_id = b_id;
			command = "search";
			
			
			
			//다음 아이디 찾기
		}else if(command != null && command.equals("after")){

			String a_id = dao.afterCustomer(request.getParameter("cust_id"));
			System.out.println("다음 아이디 : " + a_id);
			cust_id = a_id;
			command = "search";
		}
		//조회하는 기능
		if (command != null && command.equals("search")) { 
			System.out.println("cust_id = " + cust_id);
			CustDAO customerDAO = new CustDAO();
			CustVO customerVO = new CustVO();
			boolean overlappedID = customerDAO.overlappedID(cust_id); // ID 존재여부 확인위한 메소드
			customerVO.setCust_id(cust_id);

			if (overlappedID) {
				customerVO.setCust_id(cust_id);
				List list = customerDAO.listCustomer(customerVO);

				CustVO vo = (CustVO) list.get(0); // Json 데이터 타입으로 주고 받기 위한 과정
				customerObject.put("cust_id", vo.getCust_id());
				customerObject.put("cust_name", vo.getCust_name());
				customerObject.put("cust_address", vo.getCust_address());
				customerObject.put("cust_state", vo.getCust_state());
				customerObject.put("cust_zip", vo.getCust_zip());
				customerObject.put("cust_country", vo.getCust_country());
				customerObject.put("cust_contact", vo.getCust_contact());
				customerObject.put("cust_email", vo.getCust_email());
			}else {
				String message = "존재하지 않는 고객입니다.";
				error.put("errorMessage", message);
				errorArray.add(error);
			}

			totalObject.put("customer", customerObject); // 이 곳에 바로 list를 넣으려면 jsp에서 parse과정 거쳐야 함.
			totalObject.put("error", errorArray);
			
			
			//추가하는 기능
		}else if(command != null && command.equals("save") && request.getParameter("cust_id") == "") { 
			
			CustVO vo = new CustVO();
			
			String p_id = request.getParameter("id");
			String p_name = request.getParameter("name");
			String p_address = request.getParameter("address");
			String p_state = request.getParameter("state");
			String p_zip = request.getParameter("zip");
			String p_country = request.getParameter("country");
			String p_contact = request.getParameter("contact");
			String p_email = request.getParameter("email");
			
			vo.setCust_id(p_id);
			vo.setCust_name(p_name);
			vo.setCust_address(p_address);
			vo.setCust_state(p_state);
			vo.setCust_zip(p_zip);
			vo.setCust_country(p_country);
			vo.setCust_contact(p_contact);
			vo.setCust_email(p_email);
			
			dao.addCustomer(vo);
			
			//수정하는 기능
		}else if(command != null && command.equals("save") && request.getParameter("cust_id") != null) { 
			CustVO vo = new CustVO();
			
			String p_id = request.getParameter("id");
			String p_name = request.getParameter("name");
			String p_address = request.getParameter("address");
			String p_state = request.getParameter("state");
			String p_zip = request.getParameter("zip");
			String p_country = request.getParameter("country");
			String p_contact = request.getParameter("contact");
			String p_email = request.getParameter("email");
			
			vo.setCust_id(p_id);
			vo.setCust_name(p_name);
			vo.setCust_address(p_address);
			vo.setCust_state(p_state);
			vo.setCust_zip(p_zip);
			vo.setCust_country(p_country);
			vo.setCust_contact(p_contact);
			vo.setCust_email(p_email);
			
			dao.modifyCustomer(vo, request.getParameter("cust_id"));
			
			//삭제하는 기능
		}else if(command != null && command.equals("delete") && request.getParameter("cust_id") != null) { 
			CustVO vo = new CustVO();
			
			dao.delCustomer(vo, request.getParameter("cust_id"));
		}
		String jsonInfo = totalObject.toJSONString();
		writer.print(jsonInfo); //가장 마지막에 써줘야 함(서버에 전체적으로 뿌리기 위해)
	}
}
