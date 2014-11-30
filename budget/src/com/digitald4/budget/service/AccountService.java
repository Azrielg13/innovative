package com.digitald4.budget.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.budget.model.Account;
import com.digitald4.budget.model.Bill;
import com.digitald4.budget.model.GenData;
import com.digitald4.budget.model.Portfolio;
import com.digitald4.budget.model.Transaction;
import com.digitald4.budget.model.UserPortfolio;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.common.util.FormatText;

public class AccountService {
	
	public JSONArray getAccounts(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		for (Account account : getActivePortfolio(request).getAccounts()) {
			json.put(account.toJSON());
		}
		return json;
	}
	
	public JSONArray getBankAccounts(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		for (Account account : getActivePortfolio(request).getAccounts()) {
			if (account.getCategory() == GenData.AccountCategory_Bank_Account.get()) {
				json.put(account.toJSON());
			}
		}
		return json;
	}
	
	public JSONArray addAccount(HttpServletRequest request) throws JSONException, Exception {
		String name = request.getParameter("name");
		int categoryId = Integer.parseInt(request.getParameter("category_id"));
		Portfolio portfolio = getActivePortfolio(request);
		portfolio.addAccount(new Account().setName(name).setCategoryId(categoryId));
		return getAccounts(request);
	}
	
	public static Portfolio getActivePortfolio(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		Portfolio portfolio = (Portfolio)session.getAttribute("portfolio");
		if (portfolio == null) {
			User user = (User)session.getAttribute("user");
			List<UserPortfolio> userPortfolios = UserPortfolio.getCollection(new String[]{"USER_ID"}, user.getId());
			if (userPortfolios.isEmpty()) {
				portfolio = (Portfolio)new Portfolio().setName("My Portfolio")
						.addUserPortfolio(new UserPortfolio().setUser(user)
								.setRole(GenData.UserPortfolioRole_OWNER.get())).save();
			} else {
				portfolio = userPortfolios.get(0).getPortfolio();
			}
			session.setAttribute("portfolio", portfolio);
		}
		return portfolio;
	}

	public JSONObject updateAccount(HttpServletRequest request) throws JSONException, Exception {
		return Account.getInstance(Integer.parseInt(request.getParameter("id")))
				.setPropertyValue(request.getParameter("property"), request.getParameter("value"))
				.save().toJSON();
	}

	public JSONArray getAccountCategories() throws JSONException {
		for (GenData gd : GenData.values())
			gd.get();
		JSONArray cats = new JSONArray();
		for (GeneralData cat : GenData.AccountCategory.get().getGeneralDatas()) {
			cats.put(cat.toJSON());
		}
		return cats;
	}
	
	public JSONArray getTransactions(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		for (Transaction transaction : getActivePortfolio(request).getTransactions()) {
			json.put(transaction.toJSON());
		}
		return json;
	}

	public JSONArray addTransaction(HttpServletRequest request) throws JSONException, Exception {
		String date = request.getParameter("date");
		String name = request.getParameter("name");
		String billId = request.getParameter("billId");
		int debitAccountId = Integer.parseInt(request.getParameter("debitAccountId"));
		int creditAccountId = Integer.parseInt(request.getParameter("creditAccountId"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		Transaction trans = new Transaction().setName(name).setDate(FormatText.parseDate(date))
				.setDebitAccountId(debitAccountId).setCreditAccountId(creditAccountId).setAmount(amount);
		if (billId != null) {
			trans.setBillId(Integer.parseInt(billId));
		}
		getActivePortfolio(request).addTransaction(trans);
		return getTransactions(request);
	}

	public JSONArray updateTransaction(HttpServletRequest request) throws JSONException, Exception {
		Transaction.getInstance(Integer.parseInt(request.getParameter("id")))
				.setPropertyValue(request.getParameter("property"), request.getParameter("value")).save();
		return getTransactions(request);
	}
	
	public JSONArray getBills(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		for (Transaction transaction : getActivePortfolio(request).getTransactions()) {
			json.put(new Bill().addTransaction(transaction).setDueDate(transaction.getDate()).toJSON());
		}
		return json;
	}
}
