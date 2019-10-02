package sec01.ex01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class CustomerDAO {

	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;
	
	public CustomerDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public List listCustomer(String id) {
		List list = new ArrayList();

		try {
			con = dataFactory.getConnection();

			String query = "SELECT cust_id, cust_name, cust_address " 
						+ " FROM customers " 
						+ " WHERE cust_id LIKE '" + id + "'";

			System.out.println("prepareStatement: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String cust_id = rs.getString("cust_id");
				String cust_name = rs.getString("cust_name");
				String cust_address = rs.getString("cust_address");

				CustomerVO vo = new CustomerVO();
				vo.setId(cust_id);
				vo.setName(cust_name);
				vo.setAddress(cust_address);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List listOrder(String id) {
		List list = new ArrayList();

		try {
			
			con = dataFactory.getConnection();

			String query = "SELECT o.order_num, p.prod_name, quantity, item_price " 
						+ " FROM orders o, orderitems oi, customers c, products p " 
						+ " WHERE c.cust_id = o.cust_id "
						+ " AND o.order_num = oi.order_num "
						+ " AND p.prod_id = oi.prod_id "
						+ " AND o.cust_id LIKE '" + id + "'";

			System.out.println("prepareStatement: " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String order_num = rs.getString("order_num");
				String prod_name = rs.getString("prod_name");
				String quantity = rs.getString("quantity");
				String item_price = rs.getString("item_price");

				CustomerVO vo = new CustomerVO();
				vo.setOrder_num(order_num);
				vo.setProd_name(prod_name);
				vo.setQuantity(quantity);
				vo.setItem_price(item_price);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void delCustomer(String id) {
		try {
			Connection con = dataFactory.getConnection();
			
			String query = "delete from customers" 
						+ " where cust_id='" + id + "'";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void delOrd(String order_num, String prod_id) {
		try {
			Connection con = dataFactory.getConnection();
			
			
			String query = "delete from orderitems" 
						+ " where order_num='" + order_num + "'"
						+ " AND prod_id='" + prod_id + "'";
			System.out.println("prepareStatememt: " + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
