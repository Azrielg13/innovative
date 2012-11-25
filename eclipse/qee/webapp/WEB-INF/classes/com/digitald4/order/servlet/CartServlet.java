package com.digitald4.order.servlet;
import com.digitald4.order.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class CartServlet extends ParentServlet{
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		try{
			HttpSession session = request.getSession(true);
			Cart cart = (Cart)session.getAttribute("cart");
			if(cart == null){
				cart = new Cart();
				session.setAttribute("cart", cart);
			}
			String action = request.getParameter("action");
			String itemNo = request.getParameter("item");
			String cartIndex = request.getParameter("cartindex");
			if(itemNo != null && (action == null || action.equals("add"))){
				Class.forName("org.gjt.mm.mysql.Driver");
				Connection con = DriverManager.getConnection(getServletContext().getInitParameter("dburl"),getServletContext().getInitParameter("dbuser"),getServletContext().getInitParameter("dbpass"));
				ResultSet rs = con.createStatement().executeQuery("SELECT * from tbl_item WHERE Item_No='"+itemNo+"'");
				if(rs.next())
					cart.add(new Item(itemNo,rs.getString("item"),rs.getString("picture"),rs.getDouble("cost")*(1+rs.getDouble("profit_margin")),rs.getBoolean("shipping"),rs.getBoolean("tax")));
				response.sendRedirect("cart");
			}
			else if(cartIndex != null){
				Item item = cart.getItemByIndex(Integer.parseInt(cartIndex));
				if(item != null && action.equals("update")){
					String qty = request.getParameter("qty");
					if(qty == null || qty.length() == 0 || qty.equals("0"))
						cart.remove(Integer.parseInt(cartIndex));
					else
						item.setQty(Integer.parseInt(qty));
					String itemSize = request.getParameter("ItemSize");
					if(itemSize != null && itemSize.length() > 0)
						itemSize = itemSize.trim();
					item.setSize(itemSize);	
					String itemColor = request.getParameter("ItemColor");
					if(itemColor != null && itemColor.length() > 0)
						itemColor = itemColor.trim();
					item.setColor(itemColor);	
				}
				else if(item != null && action.equals("rm") || action.equals("remove")){
					cart.remove(Integer.parseInt(cartIndex));
				}
				//Send redirect so that if the user refreshes the page the contents of the cart will not be effected
				response.sendRedirect("cart");
			}else{
				if(company == null)
					System.out.println("*************************************************Company is null************************************************");
      				request.setAttribute("company", company);
	      			request.setAttribute("body", "/WEB-INF/jsp/cart.jsp");
      				layoutPage.forward(request, response);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		doGet(request,response);
	}
}