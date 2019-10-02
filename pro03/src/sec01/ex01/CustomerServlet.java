package sec01.ex01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		response.setContentType("text/html;charset=utf-8");
		CustomerDAO dao = new CustomerDAO();
		PrintWriter pw = response.getWriter();
		
		String command = request.getParameter("command");
		if(command != null && command.equals("delete")) {
			System.out.println("delete 변경");
			String id = request.getParameter("id");
			dao.delCustomer(id);
		}
		
		String id2 = request.getParameter("id");	
		List list = dao.listCustomer(id2);
		System.out.println(id2);
		
		
		pw.print("<html><body>");
		pw.print("<table border=1><tr align = 'center' bgcolor = 'violet'>");
		pw.print("<td>고객번호</td><td>고객이름</td><td>고객주소</td><td>주문조회</td><td>삭제</td></tr>");
		
		for(int i = 0; i < list.size(); i++) {
			CustomerVO custVO = (CustomerVO) list.get(i);
			String cust_id = custVO.getId();
			String cust_name = custVO.getName(); 
			String cust_address = custVO.getAddress();                                                                            
			pw.print("<tr><td>" + cust_id + "</td><td>" + cust_name + "</td><td>" + cust_address + "</td><td>" + "<a href= ' /pro07/ordercheck?id=" + cust_id + "'>주문조회</a></td>" + "<td><a href= ' /pro07/customer?command=delete&id=" + cust_id
					+ "'> 삭제 </a></td></tr>");
			pw.print("</table></body></html>");
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}

