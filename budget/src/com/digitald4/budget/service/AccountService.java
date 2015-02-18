package com.digitald4.budget.service;

import static com.digitald4.common.util.Calculate.parseInt;
import static com.digitald4.common.util.Calculate.parseDouble;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
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
	
	public JSONObject getPortfolios(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		Portfolio activePortfolio = (Portfolio)session.getAttribute("portfolio");;
		for (UserPortfolio userPortfolio : UserPortfolio.getCollection(new String[]{"USER_ID"}, user.getId())) {
			json.put(userPortfolio.getPortfolio().toJSON());
			if (activePortfolio == null) {
				activePortfolio = userPortfolio.getPortfolio();
			}
		}
		return new JSONObject().put("activePortfolio", activePortfolio.toJSON())
				.put("portfolios", json);
	}
	
	public JSONObject addPortfolio(HttpServletRequest request) throws JSONException, Exception {
		HttpSession session = request.getSession();
		Portfolio portfolio = (Portfolio) new Portfolio().setName(request.getParameter("name")).save();
		portfolio.addUserPortfolio(new UserPortfolio().setUser((User)session.getAttribute("user")).setRole(GenData.UserPortfolioRole_OWNER.get()));
		session.setAttribute("portfolio", portfolio);
		return getPortfolios(request);
	}
	
	public JSONObject updatePortfolio(HttpServletRequest request) throws JSONException, Exception {
		return Portfolio.getInstance(parseInt(request.getParameter("id")))
				.setPropertyValue(request.getParameter("property"), request.getParameter("value"))
				.save().toJSON();
	}
	
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
		int categoryId = parseInt(request.getParameter("category_id"));
		Portfolio portfolio = getActivePortfolio(request);
		portfolio.addAccount(new Account().setName(name).setCategoryId(categoryId));
		return getAccounts(request);
	}
	
	public static Portfolio getActivePortfolio(HttpServletRequest request) throws Exception {
		return Portfolio.getInstance(parseInt(request.getParameter("portfolioId")));
	}

	public JSONObject updateAccount(HttpServletRequest request) throws JSONException, Exception {
		return Account.getInstance(parseInt(request.getParameter("id")))
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
		String sdStr = request.getParameter("startDate");
		String edStr = request.getParameter("endDate");
		Date startDate = sdStr != null ? DateTime.parse(sdStr).toDate() : null;
		Date endDate = edStr != null ? DateTime.parse(edStr).toDate() : null;
		for (Transaction transaction : getActivePortfolio(request).getTransactions(startDate, endDate)) {
			json.put(transaction.toJSON());
		}
		return json;
	}

	public JSONArray addTransaction(HttpServletRequest request) throws JSONException, Exception {
		String date = request.getParameter("date");
		String name = request.getParameter("name");
		String billId = request.getParameter("billId");
		int debitAccountId = parseInt(request.getParameter("debitAccountId"));
		int creditAccountId = parseInt(request.getParameter("creditAccountId"));
		double amount = Double.parseDouble(request.getParameter("amount"));
		Transaction trans = new Transaction().setNameD(name).setDateD(FormatText.parseDate(date))
				.setDebitAccountId(debitAccountId).setCreditAccountId(creditAccountId).setAmount(amount);
		if (billId != null) {
			trans.setBillId(parseInt(billId));
		}
		trans.save();
		return getTransactions(request);
	}

	public JSONArray updateTransaction(HttpServletRequest request) throws JSONException, Exception {
		Transaction.getInstance(parseInt(request.getParameter("id")))
				.setPropertyValue(request.getParameter("property"), request.getParameter("value")).save();
		return getTransactions(request);
	}
	
	public JSONArray getBills(HttpServletRequest request) throws JSONException, Exception {
		JSONArray json = new JSONArray();
		String sdStr = request.getParameter("startDate");
		String edStr = request.getParameter("endDate");
		Date startDate = sdStr != null ? DateTime.parse(sdStr).toDate() : null;
		Date endDate = edStr != null ? DateTime.parse(edStr).toDate() : null;
		for (Bill bill : getActivePortfolio(request).getBills(startDate, endDate)) {
			JSONObject jo = bill.toJSON();
			if (bill.isNewInstance()) {
				jo.put("transId", bill.getTransactions().get(0).getId());
			}
			json.put(jo);
		}
		return json;
	}

	public JSONArray addBill(HttpServletRequest request) throws JSONException, Exception {
		Bill bill = new Bill().setDueDate(FormatText.parseDate(request.getParameter("dueDate")))
				.setAmountDue(parseDouble(request.getParameter("amountDue")))
				.setAccountId(parseInt(request.getParameter("accountId")))
				.setNameD(request.getParameter("name"));
		for (int a = 0; a < 10; a++) {
			double amount = parseDouble(request.getParameter("accounts[" + a + "][amount]"));
			if (amount != 0) {
				bill.addTransaction(new Transaction().setAmount(amount)
						.setDebitAccountId(parseInt(request.getParameter("accounts[" + a + "][id]"))));
			}
		}
		bill.save();
		return getBills(request);
	}

	public JSONArray updateBill(HttpServletRequest request) throws JSONException, Exception {
		Bill bill = Bill.getInstance(parseInt(request.getParameter("id")));
		if (bill == null) {
			Transaction transaction = Transaction.getInstance(parseInt(request.getParameter("transId")));
			bill = new Bill().addTransaction(transaction)
					.setAccount(transaction.getCreditAccount())
					.setAmountDue(transaction.getAmount())
					.setDueDate(transaction.getDate());
		}
		bill.setPropertyValue(request.getParameter("property"), request.getParameter("value")).save();
		return getBills(request);
	}

	public JSONArray updateBillTrans(HttpServletRequest request) throws JSONException, Exception {
		String id = request.getParameter("id");
		Transaction trans = id != null ? Transaction.getInstance(parseInt(id)) : 
				new Transaction().setBillId(parseInt(request.getParameter("billId")))
						.setDebitAccountId(parseInt(request.getParameter("accountId")));
		trans.setPropertyValue(request.getParameter("property"), request.getParameter("value")).save();
		return getBills(request);
	}
}
