/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hisp.dhis.sms.input;

import java.util.Map;

/**
 *
 * @author Christian
 */
public interface IParser {
    static final String DATA_ENTRY_KEYWORD = "DHISDATAENTRYKEYWORD";
    
    public Map<String, String> parse(String sms);
}
