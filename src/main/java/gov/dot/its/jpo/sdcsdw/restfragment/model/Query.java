package gov.dot.its.jpo.sdcsdw.restfragment.model;

public class Query {
	
	private String systemQueryName;
	private String resultEncoding;
	private String resultPackaging;
	private String orderByField;
	private String orderByOrder;
	private int skip;
	private int limit;
	private String dialogId;
	private double nwLat;
	private double nwLon;
	private double seLat;
	private double seLon;
	private String startDate;
	private String startDateOperator;
	private String endDate;
	private String endDateOperator;
	
	/**
	 * @return the systemQueryName
	 */
	public String getSystemQueryName() {
		return systemQueryName;
	}
	/**
	 * @param systemQueryName the systemQueryName to set
	 */
	public void setSystemQueryName(String systemQueryName) {
		this.systemQueryName = systemQueryName;
	}
	/**
	 * @return the resultEncoding
	 */
	public String getResultEncoding() {
		return resultEncoding;
	}
	/**
	 * @param resultEncoding the resultEncoding to set
	 */
	public void setResultEncoding(String resultEncoding) {
		this.resultEncoding = resultEncoding;
	}
	/**
	 * @return the resultPackaging
	 */
	public String getResultPackaging() {
		return resultPackaging;
	}
	/**
	 * @param resultPackaging the resultPackaging to set
	 */
	public void setResultPackaging(String resultPackaging) {
		this.resultPackaging = resultPackaging;
	}
	/**
	 * @return the orderByField
	 */
	public String getOrderByField() {
		return orderByField;
	}
	/**
	 * @param orderByField the orderByField to set
	 */
	public void setOrderByField(String orderByField) {
		this.orderByField = orderByField;
	}
	/**
	 * @return the orderByOrder
	 */
	public String getOrderByOrder() {
		return orderByOrder;
	}
	/**
	 * @param orderByOrder the orderByOrder to set
	 */
	public void setOrderByOrder(String orderByOrder) {
		this.orderByOrder = orderByOrder;
	}
	/**
	 * @return the skip
	 */
	public int getSkip() {
		return skip;
	}
	/**
	 * @param skip the skip to set
	 */
	public void setSkip(int skip) {
		this.skip = skip;
	}
	/**
	 * @return the limit
	 */
	public int getLimit() {
		return limit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
	/**
	 * @return the dialogId
	 */
	public String getDialogId() {
		return dialogId;
	}
	/**
	 * @param dialogId the dialogId to set
	 */
	public void setDialogId(String dialogId) {
		this.dialogId = dialogId;
	}
	/**
	 * @return the nwLat
	 */
	public double getNwLat() {
		return nwLat;
	}
	/**
	 * @param nwLat the nwLat to set
	 */
	public void setNwLat(double nwLat) {
		this.nwLat = nwLat;
	}
	/**
	 * @return the nwLon
	 */
	public double getNwLon() {
		return nwLon;
	}
	/**
	 * @param nwLon the nwLon to set
	 */
	public void setNwLon(double nwLon) {
		this.nwLon = nwLon;
	}
	/**
	 * @return the seLat
	 */
	public double getSeLat() {
		return seLat;
	}
	/**
	 * @param seLat the seLat to set
	 */
	public void setSeLat(double seLat) {
		this.seLat = seLat;
	}
	/**
	 * @return the seLon
	 */
	public double getSeLon() {
		return seLon;
	}
	/**
	 * @param seLon the seLon to set
	 */
	public void setSeLon(double seLon) {
		this.seLon = seLon;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the startDateOperator
	 */
	public String getStartDateOperator() {
		return startDateOperator;
	}
	/**
	 * @param startDateOperator the startDateOperator to set
	 */
	public void setStartDateOperator(String startDateOperator) {
		this.startDateOperator = startDateOperator;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the endDateOperator
	 */
	public String getEndDateOperator() {
		return endDateOperator;
	}
	/**
	 * @param endDateOperator the endDateOperator to set
	 */
	public void setEndDateOperator(String endDateOperator) {
		this.endDateOperator = endDateOperator;
	}
}
