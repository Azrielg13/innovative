package com.digitald4.budget.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.digitald4.budget.service.AccountService;
import com.digitald4.common.servlet.ParentServlet;


/**
 * The Budget Service Servlet.
 * 
 * @author Eddie Mayfield (eddiemay@gmail.com)
 */
@WebServlet(name = "Service Servlet", urlPatterns = {"/bs"})
public class ServiceServlet extends ParentServlet {
	public enum ACTIONS {getAccounts, addAccount, updateAccount, getBankAccounts, getAccountCats,
			getTransactions, addTransaction, updateTransaction, getBills, addBill, updateBill, updateBillTrans};
	private static AccountService accountService = new AccountService();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			JSONObject json = new JSONObject();
			try {
				if (!checkLoginAutoRedirect(request, response)) return;
				String action = request.getParameter("action");
				switch (ACTIONS.valueOf(action)) {
					case getAccounts: json.put("data", accountService.getAccounts(request)); break;
					case addAccount: json.put("data", accountService.addAccount(request)); break;
					case updateAccount: json.put("data", accountService.updateAccount(request)); break;
					case getBankAccounts: json.put("data", accountService.getBankAccounts(request)); break;
					case getAccountCats: json.put("data", accountService.getAccountCategories()); break;
					case getTransactions: json.put("data", accountService.getTransactions(request)); break;
					case addTransaction: json.put("data", accountService.addTransaction(request)); break;
					case updateTransaction: json.put("data", accountService.updateTransaction(request)); break;
					case getBills: json.put("data", accountService.getBills(request)); break;
					case addBill: json.put("data", accountService.addBill(request)); break;
					case updateBill: json.put("data", accountService.updateBill(request)); break;
					case updateBillTrans: json.put("data", accountService.updateBillTrans(request)); break;
				}
				json.put("valid", true);
			} catch (Exception e) {
				json.put("valid", false).put("error", e.getMessage()).put("stackTrace", formatStackTrace(e));
				e.printStackTrace();
			} finally {
				response.setContentType("application/json");
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				response.getWriter().println(json);
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		doGet(request,response);
	}
	
	public static String formatStackTrace(Exception e) {
		String out = "";
		for (StackTraceElement elem : e.getStackTrace()) {
			out += elem + "\n";
		}
		return out;
	}
}
