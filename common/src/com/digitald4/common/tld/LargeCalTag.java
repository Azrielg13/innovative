package com.digitald4.common.tld;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;

import com.digitald4.common.component.CalEvent;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;

public class LargeCalTag extends DD4Tag {
	private final static int MAX_EVENT_LINES = 4;
	private final static String START = "<div id=\"cal_supp\"></div>"
			+"<div class=\"block-border\">"
			+"<div class=\"block-content\">"
			+"<h1>Large calendar</h1>"
			+"<div class=\"block-controls\">"
			+"<ul class=\"controls-buttons\">"
			+"<li><img src=\"images/icons/fugue/navigation-180.png\" width=\"16\" height=\"16\" onclick=\"setMonth(%prev_year, %prev_month)\"/></li>"
			+"<li class=\"sep\"></li>"
			+"<li class=\"controls-block\"><strong>%month_year</strong></li>"
			+"<li class=\"sep\"></li>"
			+"<li><img src=\"images/icons/fugue/navigation.png\" width=\"16\" height=\"16\" onclick=\"setMonth(%next_year, %next_month)\"/></li>"
			+"</ul>"
			+"</div>"
			+"<div class=\"no-margin\">"
			+"<table cellspacing=\"0\" class=\"calendar\">"
			+"<thead>"
			+"<tr><th scope=\"col\" class=\"black-cell\"><span class=\"success\"></span></th>"
			+"<th scope=\"col\" class=\"week-end\">Sunday</th><th scope=\"col\">Monday</th><th scope=\"col\">Tuesday</th><th scope=\"col\">Wednesday</th>"
			+"<th scope=\"col\">Thursday</th><th scope=\"col\">Friday</th><th scope=\"col\" class=\"week-end\">Saturday</th>"
			+"</tr></thead><tbody>";
	private final static String WEEK_START = "<tr><th scope=\"row\">%weeknum</th>";
	private final static String WEEK_END = "</tr>";
	private final static String END = "</tbody></table></div><ul class=\"message no-margin\"><li>%event_count events found</li></ul></div></div>";
	private String title;
	private int month;
	private int year;
	private Collection<? extends CalEvent> events;

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setMonth(int month) {
		this.month = month;
	}
	
	public int getMonth() {
		return month;
	}
	
	public void setYear(int year) {
		this.year = year;
	}
	
	public int getYear() {
		return year;
	}
	
	public String getCssClass(Calendar cal) {
		if (cal.get(Calendar.MONTH) + 1 != month) {
			return "other-month";
		}
		Calendar today = Calendar.getInstance();
		if (cal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR) && cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
			return "today";
		}
		return "";
	}
	
	public void setEvents(Collection<? extends CalEvent> events) {
		this.events = events;
	}
	
	public Collection<? extends CalEvent> getEvents() {
		return events;
	}
	
	public int getEventCount() {
		return getEvents() != null ? getEvents().size() : 0;
	}
	
	public List<CalEvent> getEvents(Calendar cal) {
		List<CalEvent> events = new ArrayList<CalEvent>();
		if (getEvents() != null) {
			for (CalEvent event : getEvents()) {
				if (event.isActiveOnDay(cal.getTime()))
					events.add(event);
			}
		}
		return events;
	}
	
	public String getEventStr(Calendar cal) {
		List<CalEvent> events = getEvents(cal);
		if (events.size() == 0) {
			return "";
		}
		String out = "<ul class=\"events\">";
		int c = 0;
		for (CalEvent event : events) {
			DateTime st = event.getStart();
			if (++c == MAX_EVENT_LINES && events.size() > MAX_EVENT_LINES) {
				out += "</ul><div class=\"more-events\">" + (events.size() - c + 1) + " more events<ul>";
			}
			out += "<li><a onclick=\"editEvent(" + event.getId() + ")\"><b>"+FormatText.HOUR_MIN.format(st.toDate())+"</b>" + event.getTitle() + "</a></li>";
		}
		out += "</ul>";
		if (events.size() > MAX_EVENT_LINES) {
			out += "</div>";
		}
		return out;
	}
	
	@Override
	public String getOutput() {
		Calendar cal = Calculate.getCal(getYear(), getMonth(), 1);
		String out = START.replace("%title", getTitle()).replaceAll("%month_year", FormatText.USER_MONTH.format(cal.getTime()))
			.replaceAll("%prev_year", ""+(getMonth() > 1 ? getYear() : getYear() - 1)).replaceAll("%prev_month", ""+(getMonth() > 1 ? getMonth() - 1 : 12))
			.replaceAll("%next_year", ""+(getMonth() < 12 ? getYear() : getYear() + 1)).replaceAll("%next_month", ""+(getMonth() < 12 ? getMonth() + 1 : 1));
		cal.add(Calendar.DATE, Calendar.SUNDAY - cal.get(Calendar.DAY_OF_WEEK));
		Calendar nextMonth = Calculate.getCal(getYear(), getMonth(), 1);
		nextMonth.add(Calendar.MONTH, 1);
		while (cal.getTimeInMillis() < nextMonth.getTimeInMillis()) {
			out += WEEK_START.replaceAll("%weeknum", "" + cal.get(Calendar.WEEK_OF_YEAR));
			for (int d = 0; d < 7; d++) {
				int day = cal.get(Calendar.DAY_OF_MONTH);
				String date = FormatText.formatDate(cal, FormatText.USER_DATE);
				if (d == 0 || d == 6) {
					out += "<td class=\"weekend" + ((cal.get(Calendar.MONTH) == getMonth() - 1) ? "" : " other-month") + "\">";
				} else {
					out += "<td" + ((cal.get(Calendar.MONTH) == getMonth() - 1) ? "" : " class=\"other-month\"") + ">";
				}
				out += "<a href=\"#\" class=\"day\">" + day + "</a> <div class=\"add-event\" onclick=\"addEvent('" + date + "')\">Add</div>";
				out += getEventStr(cal);
				out += "</td>";
				cal.add(Calendar.DATE, 1);
			}
			out += WEEK_END;
		}
		out += END.replaceAll("%event_count", "" + getEventCount());
		return out;
	}
}
