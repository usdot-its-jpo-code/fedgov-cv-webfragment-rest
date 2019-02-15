package gov.dot.its.jpo.sdcsdw.restfragment.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class containing valid deposit options
 */
public class DepositOptions {

    private static final List<String> encodeTypeOptions = new ArrayList<String>();
    
    static {
        encodeTypeOptions.add("hex");
        encodeTypeOptions.add("base64");
        encodeTypeOptions.add("uper");
    }
    
    public static List<String> getEncodeTypeOptions() {
        List<String> encodeTypeOptions = new ArrayList<String>();
        for(String option : DepositOptions.encodeTypeOptions) {
            encodeTypeOptions.add(option);
        }
        return encodeTypeOptions;
    }
}
