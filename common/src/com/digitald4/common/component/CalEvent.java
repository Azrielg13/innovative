package com.digitald4.common.component;

import java.util.Date;

import org.joda.time.DateTime;

public interface CalEvent {

	public DateTime getStartTime();
	
	public DateTime getEndTime();
	
	public int getDuration();
	
	public boolean isOnDay(Date date);

}
