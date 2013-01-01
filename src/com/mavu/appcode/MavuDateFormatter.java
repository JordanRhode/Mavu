package com.mavu.appcode;

public class MavuDateFormatter {
	
	public static String format(String date)
	{
		String[] dateVals = date.split("-");
		String monthStr = getMonthString(Integer.parseInt(dateVals[1]));
		String dateStr = monthStr + " " + dateVals[2] + ", " + dateVals[0];
		
		return dateStr;
	}

	private static String getMonthString(int i)
	{
		String month = "";
		
		switch (i)
		{
			case 1: month = "January"; break;
			case 2: month = "February"; break;
			case 3: month = "March"; break;
			case 4: month = "April"; break;
			case 5: month = "May"; break;
			case 6: month = "June"; break;
			case 7: month = "July"; break;
			case 8: month = "August"; break;
			case 9: month = "September"; break;
			case 10: month = "October"; break;
			case 11: month = "November"; break;
			case 12: month = "December"; break;					
		}
		return month;
	}
	
	
}
