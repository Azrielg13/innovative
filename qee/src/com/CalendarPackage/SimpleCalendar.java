package com.CalendarPackage;


public class SimpleCalendar{

	public static boolean isLeap( int year ){
		//# after 1752, any year divisible by 4 except those divisible by 100 but
		//# not by 400 are leap years.
		return (year % 4 == 0 && (year <= 1752 || year % 100 != 0 || year % 400 == 0));
	}//end isLeap()

	public static int daysInMonth( int month, int year ){
		switch(month){
			case 2: return isLeap(year)?29:28;
			case 4: 
			case 6:
			case 9: 
			case 11: return 30;
		}
		return 31;
	}//end daysInMonth()

	public static int yearStart( int y ){
		return monthStart( 1, y ) ;
	}//end yearStart()

	//I have no idea how this function works.  It returns an int between 0 and 6 representing the day
	//of the week on which a month starts (0=Sunday)
	public static int monthStart( int m, int y ){
		int d = 1;
		int[] dArray = {0,3,2,5,0,3,5,1,4,6,2,4} ;
		if( m < 3 ){
			y-- ;
		}
		return (int) ( y + Math.floor(y/4) - Math.floor(y/100) + Math.floor(y/400) + dArray[m-1] + d ) % 7 ;
	}
}

