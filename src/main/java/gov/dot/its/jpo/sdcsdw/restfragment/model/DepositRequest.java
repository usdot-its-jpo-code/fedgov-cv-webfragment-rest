package gov.dot.its.jpo.sdcsdw.restfragment.model;

public class DepositRequest {

    private String systemDepositName;
    private String encodeType;
    private String encodedMsg;

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
     * Get the encoded message.
     * 
     * @return encoded message
     */
    public String getEncodedMsg() {
        return this.encodedMsg;
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
     * Set the encoded message.
     * 
     * @param encodedMsg
     */
    public void setEncodedMsg(String encodedMsg) {
        this.encodedMsg = encodedMsg;
    }
}
