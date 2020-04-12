package com.accrualify.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chetan  dahule
 * @since Friday, 10 April 2020
 */
public class Constants {
	public static final String UNAUTHORIZED_MSG = "User is not authorize,please check user name and password";


	public static String getDateString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		return sdf.format(date);
	}
}
