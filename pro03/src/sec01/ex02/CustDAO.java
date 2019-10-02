package sec01.ex02;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.json.simple.JSONArray;

import sec01.ex01.CustomerVO;

public class CustDAO {
	private Connection con;
	private PreparedStatement pstmt;
	private DataSource dataFactory;
	
	public CustDAO() {
		try {
			Context ctx = new InitialContext();
			Context envContext = (Context)ctx.lookup("java:/comp/env");
			dataFactory = (DataSource)envContext.lookup("jdbc/oracle");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public List listCustomer(CustVO customerVO) {
		List list = new ArrayList();
		
		try {
			con = dataFactory.getConnection();
			
			String query = "SELECT * FROM customers"
						 + " WHERE cust_id = '" + customerVO.getCust_id() + "'";
			System.out.println("query : " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String cust_id = rs.getString("cust_id");
				String cust_name = rs.getString("cust_name");
				String cust_address = rs.getString("cust_address");
				String cust_state = rs.getString("cust_state");
				String cust_zip = rs.getString("cust_zip");
				String cust_country = rs.getString("cust_country");
				String cust_contact = rs.getString("cust_contact");
				String cust_email = rs.getString("cust_email");
				
				CustVO vo = new CustVO();
				vo.setCust_id(cust_id);
				vo.setCust_name(cust_name);
				vo.setCust_address(cust_address);
				vo.setCust_state(cust_state);
				vo.setCust_zip(cust_zip);
				vo.setCust_country(cust_country);
				vo.setCust_contact(cust_contact);
				vo.setCust_email(cust_email);
				list.add(vo);
			}
			rs.close();
			pstmt.close();
			con.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean overlappedID(String cust_id) {
		boolean result = false;
		try {
			con = dataFactory.getConnection();
			String query = "SELECT DECODE(count(*), 1, 'true', 'false') AS result FROM customers"
						 + " WHERE cust_id = '" + cust_id + "'";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			result = Boolean.parseBoolean(rs.getString("result"));
			rs.close();
			pstmt.close();
			con.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public void stateList(JSONArray state) {
		try {
			con = dataFactory.getConnection();
			
			String query = "SELECT DISTINCT(cust_state) FROM customers WHERE cust_state IS NOT NULL";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				state.add(rs.getString("cust_state"));
			}
			rs.close();
			pstmt.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void countryList(JSONArray country) {
		try {
			con = dataFactory.getConnection();
			
			String query = "SELECT DISTINCT(cust_country) FROM customers WHERE cust_country IS NOT NULL";
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				country.add(rs.getString("cust_country"));
			}
			rs.close();
			pstmt.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addCustomer(CustVO vo) {
		try {
			con = dataFactory.getConnection();
			String query = "INSERT INTO customers(cust_id, cust_name, cust_address, cust_state, cust_zip, cust_country, cust_contact, cust_email) " 
						 + " VALUES('" + vo.getCust_id() + "', '" + vo.getCust_name() + "', '" + vo.getCust_address() + "', '" + vo.getCust_state() + "', '" + vo.getCust_zip() + "', '" + vo.getCust_country() + "', '" + vo.getCust_contact() + "', '" + vo.getCust_email() + "')";
			System.out.println("add query = " + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void modifyCustomer(CustVO vo, String cust_id) {
		try {
			con = dataFactory.getConnection();
			String query = "UPDATE customers"
						 + " SET cust_id = '" + vo.getCust_id() + "', cust_name = '" + vo.getCust_name() + "', cust_address = '" + vo.getCust_address() + "', cust_state = '" + vo.getCust_state() + "', cust_zip = '" + vo.getCust_zip() + "', cust_country = '" + vo.getCust_country() + "', "
						 + " cust_contact = '" + vo.getCust_contact() + "', cust_email = '" + vo.getCust_email() + "'"
						 + " WHERE cust_id = '" + cust_id + "'";
			System.out.println("modify query = " + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void delCustomer(CustVO vo, String cust_id) {
		try {
			con = dataFactory.getConnection();
			String query = "DELETE FROM customers"
						 + " WHERE cust_id = '" + cust_id + "'";
			System.out.println("delete query = " + query);
			pstmt = con.prepareStatement(query);
			pstmt.executeUpdate();
			pstmt.close();			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public String beforeCustomer( String b_id) {
		String cust_id = null;
		try {
			con = dataFactory.getConnection();
			String query = "SELECT before_value"  
						 + " FROM ( SELECT cust_id" 
						 + "            , LAG(cust_id, 1, 0) OVER (ORDER BY cust_id desc) as before_value" 
						 + "            , LEAD(cust_id, 1, 0) OVER (ORDER BY cust_id desc) as after_value"  
						 + "       FROM   customers" 
						 + "       ORDER BY cust_id desc"
						 + "     )"  
						 + " WHERE cust_id = '" + b_id + "'";
			System.out.println("before query = " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				cust_id = rs.getString("before_value");
			
			}
			rs.close();
			pstmt.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cust_id;
	}

	public String afterCustomer(String a_id) {
		String cust_id = null;
		try {
			con = dataFactory.getConnection();
			String query = "SELECT after_value"  
						 + " FROM ( SELECT cust_id" 
						 + "            , LAG(cust_id, 1, 0) OVER (ORDER BY cust_id desc) as before_value" 
						 + "            , LEAD(cust_id, 1, 0) OVER (ORDER BY cust_id desc) as after_value"  
						 + "       FROM   customers" 
						 + "       ORDER BY cust_id desc"
						 + "     )"  
						 + " WHERE cust_id = '" + a_id + "'";
			System.out.println("before query = " + query);
			pstmt = con.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				cust_id = rs.getString("after_value");
			}
			rs.close();
			pstmt.close();
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cust_id;
	}
}
