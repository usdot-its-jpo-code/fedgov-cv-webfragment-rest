package gov.dot.its.jpo.sdcsdw.restfragment.model;

public class DepositRequest {

    private String systemDepositName;
    private String encodeType;
    private String encodeMsg;

    // Getters

    /**
     * Get the system deposit name.
     * 
     * @return system deposit name
     */
    public String getSystemDepositName() {
        return this.systemDepositName;
    }

    /**
     * Get the encode type.
     * 
     * @return encode type
     */
    public String getEncodeType() {
        return this.encodeType;
    }

    /**
     * Get the encode message.
     * 
     * @return encode message
     */
    public String getEncodeMsg() {
        return this.encodeMsg;
    }

    // Setters

    /**
     * Set the system deposit name.
     * 
     * @param systemDepositName
     */
    public void setSystemDepositName(String systemDepositName) {
        this.systemDepositName = systemDepositName;
    }

    /**
     * Set the encode type.
     * 
     * @param encodeType
     */
    public void setEncodeType(String encodeType) {
        this.encodeType = encodeType;
    }

    /**
     * Set the encode message.
     * 
     * @param encodeMsg
     */
    public void setEncodeMsg(String encodeMsg) {
        this.encodeMsg = encodeMsg;
    }
}
