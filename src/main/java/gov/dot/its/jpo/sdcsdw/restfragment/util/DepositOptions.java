package gov.dot.its.jpo.sdcsdw.restfragment.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing valid deposit options
 */
public class DepositOptions {

    private static List<String> encodeTypeOptions = new ArrayList<String>();
    
    //The encode types as constants
    public static final String ENCODE_TYPE_HEX = "hex";
    public static final String ENCODE_TYPE_BASE64 = "base64";
    public static final String ENCODE_TYPE_UPER = "uper";
    
    static {
        encodeTypeOptions.add(ENCODE_TYPE_HEX);
        encodeTypeOptions.add(ENCODE_TYPE_BASE64);
        encodeTypeOptions.add(ENCODE_TYPE_UPER);
    }
    
    public static List<String> getEncodeTypeOptions() {
        List<String> encodeTypeOptions = new ArrayList<String>();
        for(String option : DepositOptions.encodeTypeOptions) {
            encodeTypeOptions.add(option);
        }
        return encodeTypeOptions;
    }
}
