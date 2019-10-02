package sec01.ex01;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ordercheck")
public class Ordercheck extends HttpServlet {
	private static final long serialVersionUID = 1L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		CustomerDAO dao = new CustomerDAO();
		PrintWriter pw = response.getWriter();
		String command = request.getParameter("command");
		
		if(command != null && command.equals("delOrd")) {
			String order_num = request.getParameter("order_num");
			String prod_id = request.getParameter("prod_id");
			dao.delOrd(order_num, prod_id);
		}
		
		String _id = request.getParameter("id");
		List list = dao.listOrder(_id);
		System.out.println(_id);
		
		pw.print("<html><body>");
		pw.print("<table border=1><tr align = 'center' bgcolor = 'violet'>");
		pw.print("<td>주문번호</td><td>제품명</td><td>항목수량</td><td>항목가격</td><td>삭제</td></tr>");
		
		for(int i =0; i < list.size(); i++) {
			CustomerVO custVO = (CustomerVO)list.get(i);
			String order_num = custVO.getOrder_num();
			String prod_name = custVO.getProd_name();
			String quantity = custVO.getQuantity();
			String item_price = custVO.getItem_price();
			String prod_id = custVO.getProd_id();
			pw.print("<tr><td>" + order_num + "</td><td>" + prod_name + "</td><td>" + quantity + "</td><td>" + item_price + "</td><td>" + "<a href= ' /pro07/ordercheck?command=delOrd&order_num=" + order_num + "&prod_id=" + prod_id +  "&id=" + _id + "'>삭제 </a></td></tr>");
		}
	}


}
