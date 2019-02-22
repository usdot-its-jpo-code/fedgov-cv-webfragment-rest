package gov.dot.its.jpo.sdcsdw.restfragment.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * POJO for the query request
 */
public class Query {

    private String systemQueryName;
    private String resultEncoding;
    private String resultPackaging;
    private String orderByField;
    private String orderByOrder;
    private Integer skip;
    private Integer limit;
    private String dialogId;
    private Double nwLat;
    private Double nwLon;
    private Double seLat;
    private Double seLon;
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
     * @param systemQueryName
     *            the systemQueryName to set
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
     * @param resultEncoding
     *            the resultEncoding to set
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
     * @param resultPackaging
     *            the resultPackaging to set
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
     * @param orderByField
     *            the orderByField to set
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
     * @param orderByOrder
     *            the orderByOrder to set
     */
    public void setOrderByOrder(String orderByOrder) {
        this.orderByOrder = orderByOrder;
    }

    /**
     * @return the skip
     */
    public Integer getSkip() {
        return skip;
    }

    /**
     * @param skip
     *            the skip to set
     */
    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    /**
     * @return the limit
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit
     *            the limit to set
     */
    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * @return the dialogId
     */
    public String getDialogId() {
        return dialogId;
    }

    /**
     * @param dialogId
     *            the dialogId to set
     */
    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    /**
     * @return the nwLat
     */
    public Double getNwLat() {
        return nwLat;
    }

    /**
     * @param nwLat
     *            the nwLat to set
     */
    public void setNwLat(Double nwLat) {
        this.nwLat = nwLat;
    }

    /**
     * @return the nwLon
     */
    public Double getNwLon() {
        return nwLon;
    }

    /**
     * @param nwLon
     *            the nwLon to set
     */
    public void setNwLon(Double nwLon) {
        this.nwLon = nwLon;
    }

    /**
     * @return the seLat
     */
    public Double getSeLat() {
        return seLat;
    }

    /**
     * @param seLat
     *            the seLat to set
     */
    public void setSeLat(Double seLat) {
        this.seLat = seLat;
    }

    /**
     * @return the seLon
     */
    public Double getSeLon() {
        return seLon;
    }

    /**
     * @param seLon
     *            the seLon to set
     */
    public void setSeLon(Double seLon) {
        this.seLon = seLon;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
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
     * @param startDateOperator
     *            the startDateOperator to set
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
     * @param endDate
     *            the endDate to set
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
     * @param endDateOperator
     *            the endDateOperator to set
     */
    public void setEndDateOperator(String endDateOperator) {
        this.endDateOperator = endDateOperator;
    }

    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = null;
        try {
            ret = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            System.out.println("Unable to parse Query into String");
        }
        return ret;

    }
}
