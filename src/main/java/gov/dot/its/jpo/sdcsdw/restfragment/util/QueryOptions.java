package gov.dot.its.jpo.sdcsdw.restfragment.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class containing valid query options and defaults.
 *
 */
public class QueryOptions {

	//Default values for applicable parameters
	private static final String defaultResultEncoding = "hex";
	private static final String defaultResultPackaging = "none";
	private static final String defaultOrderByField = "none";
	private static final String defaultOrderByOrder = "ascending";
	private static final int defaultSkip = 0;
	private static final int defaultLimit = 0;
	private static final String defaultStartDateOperator = "GTE";
	private static final String defaultEndDateOperator = "LTE";
	
	//Collection of possible dialog ID values as Strings (ability to choose from either number or string representation)
	private static final List<String> dialogIdOptions = new ArrayList<String>();
	
	//Collection of possible start and date date operators
	private static final List<String> startDateOperatorOptions = new ArrayList<String>();
	private static final List<String> endDateOperatorOptions = new ArrayList<String>();
	
	//Collection of possible result encodings
	private static final List<String> resultEncodingOptions = new ArrayList<String>();
	
	//Collection of possible result packaging options
	private static final List<String> resultPackagingOptions = new ArrayList<String>();
	
	//Collection of possible result ordering by field options
	private static final List<String> orderByFieldOptions = new ArrayList<String>();
	
	//Collection of possible result ordering options
	private static final List<String> orderByOrderOptions = new ArrayList<String>();
	
	//The possible date formats
	private static final String sdfNoMillis = "yyyy-MM-dd'T'HH:mm:ss";
	private static final String sdfMillis = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	
	static {
	    resultEncodingOptions.add("hex");
	    resultEncodingOptions.add("base64");
	    resultEncodingOptions.add("full");
	    
	    resultPackagingOptions.add("none");
	    resultPackagingOptions.add("bundle");
	    resultPackagingOptions.add("distribution");
	    
	    orderByFieldOptions.add("createdAt");
	    orderByFieldOptions.add("requestId");
	    orderByFieldOptions.add("none");
	    
	    orderByOrderOptions.add("1");
	    orderByOrderOptions.add("-1");
	    orderByOrderOptions.add("ascending");
	    orderByOrderOptions.add("descending");
	    
	    dialogIdOptions.add("154");
	    dialogIdOptions.add("vehSitDataMessage");
	    dialogIdOptions.add("155");
	    dialogIdOptions.add("dataSubscription");
	    dialogIdOptions.add("156");
	    dialogIdOptions.add("advSitDataDep");
	    dialogIdOptions.add("157");
	    dialogIdOptions.add("advSitDatDist");
	    dialogIdOptions.add("158");
	    dialogIdOptions.add("reserved1");
	    dialogIdOptions.add("159");
	    dialogIdOptions.add("reserved2");
	    dialogIdOptions.add("160");
	    dialogIdOptions.add("objReg");
	    dialogIdOptions.add("161");
	    dialogIdOptions.add("objDisc");
	    dialogIdOptions.add("162");
	    dialogIdOptions.add("intersectionSitData");
	    dialogIdOptions.add("163");
	    dialogIdOptions.add("intersectionSitDataQuery");
	    
	    startDateOperatorOptions.add("GT");
	    startDateOperatorOptions.add("GTE");
	    
	    endDateOperatorOptions.add("LT");
	    endDateOperatorOptions.add("LTE");
	}
	
	public static List<String> getDialogIdOptions() {
		List<String> dialogIdOptions = new ArrayList<String>();
		for(String option : QueryOptions.dialogIdOptions) {
			dialogIdOptions.add(option);
		}
		return dialogIdOptions;
	}
	
	public static List<String> getStartDateOperatorOptions() {
		List<String> startDateOperatorOptions = new ArrayList<String>();
		for(String option : QueryOptions.startDateOperatorOptions) {
			startDateOperatorOptions.add(option);
		}
		return startDateOperatorOptions;
	}
	
	public static List<String> getEndDateOperatorOptions() {
		List<String> endDateOperatorOptions = new ArrayList<String>();
		for(String option : QueryOptions.endDateOperatorOptions) {
			endDateOperatorOptions.add(option);
		}
		return endDateOperatorOptions;
	}
	
	public static List<String> getResultEncodingOptions() {
		List<String> resultEncodingOptions = new ArrayList<String>();
		for(String option : QueryOptions.resultEncodingOptions) {
			resultEncodingOptions.add(option);
		}
		return resultEncodingOptions;
	}
	
	public static List<String> getResultPackagingOptions() {
		List<String> resultPackagingOptions = new ArrayList<String>();
		for(String option : QueryOptions.resultPackagingOptions) {
			resultPackagingOptions.add(option);
		}
		return resultPackagingOptions;
	}
	
	public static List<String> getOrderByFieldOptions() {
		List<String> orderByFieldOptions = new ArrayList<String>();
		for(String option : QueryOptions.orderByFieldOptions) {
			orderByFieldOptions.add(option);
		}
		return orderByFieldOptions;
	}
	
	public static List<String> getOrderByOrderOptions() {
		List<String> orderByOrderOptions = new ArrayList<String>();
		for(String option : QueryOptions.orderByOrderOptions) {
			orderByOrderOptions.add(option);
		}
		return orderByOrderOptions;
	}
	
	public static DateFormat getSDFNoMillis() {
		return new SimpleDateFormat(sdfNoMillis);
	}
	
	public static DateFormat getSDFMillis() {
		return new SimpleDateFormat(sdfMillis);
	}
	
	public static String getDefaultResultEncoding() {
		return defaultResultEncoding;
	}
	
	public static String getDefaultResultPackaging() {
		return defaultResultPackaging;
	}
	
	public static String getDefaultOrderByField() {
		return defaultOrderByField;
	}
	
	public static String getDefaultOrderByOrder() {
		return defaultOrderByOrder;
	}
	
	public static int getDefaultSkip() {
		return defaultSkip;
	}
	
	public static int getDefaultLimit() {
		return defaultLimit;
	}
	
	public static String getDefaultStartDateOperator() {
		return defaultStartDateOperator;
	}
	
	public static String getDefaultEndDateOperator() {
		return defaultEndDateOperator;
	}
}
