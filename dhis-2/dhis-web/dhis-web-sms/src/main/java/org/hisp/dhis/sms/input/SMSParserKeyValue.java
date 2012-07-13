/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.hisp.dhis.sms.input;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Christian
 */
public class SMSParserKeyValue implements IParser {

    private String separatorBetweenKeyValuePairs = " "; // The separator between two different keys, ie. 'ANC10 BCG15' separator being ' '
    private String separatorBetweenKeyAndValue = "";
    private boolean canUseNumbersInKey = false;
    private boolean firstWordIsKeyWord = true;
    private String separatorForDataEntryKeyword = " ";

    @Override
    public Map<String, String> parse(String sms) {
        System.out.println("SMS Input is: '"+sms+"'");
        sms = sms.replaceAll("\r\n"," ");
        sms = sms.replaceAll("\r", "");
        sms = sms.replaceAll("\n", " ");

        HashMap<String, String> output = new HashMap<String, String>();
        
        if (firstWordIsKeyWord) {
            int reportKeyWordLength = sms.indexOf(separatorForDataEntryKeyword);
            String dataEntryKeyword = sms.substring(0, reportKeyWordLength);
            System.out.println("Report keyword is: " + dataEntryKeyword);
            output.put(DATA_ENTRY_KEYWORD, dataEntryKeyword);
            sms = sms.substring(reportKeyWordLength); // Remove the keyword
            
            if( sms.startsWith(separatorForDataEntryKeyword) ) {
                sms = sms.replaceFirst(separatorForDataEntryKeyword, ""); // remove the keyword separator at the beginning
            }
            System.out.println("SMS is now: '"+sms+"'");
        }
        // Now move onto the message/data entry part
        sms = sms.trim();
        String[] smsArr = sms.split(separatorBetweenKeyValuePairs);

        for (int i = 0; i < smsArr.length; i++) {
            String s = smsArr[i];
            s = s.trim();
            
            if (!canUseNumbersInKey && separatorBetweenKeyAndValue.isEmpty()) { // Can't use numbers in key and no sep between key and value
                for (int j = 0; j < s.length(); j++) {
                    if (Character.isDigit(s.charAt(j))) { // If char is a digit and we can't use numbers in key
                        String key = s.substring(0, j);
                        String value = s.substring(j);
                        
                        output.put(key.trim(), value.trim());
                        System.out.println("Found Key: " + key + " Value: " + value);
                        System.out.println("");
                        break;
                    }
                }
            } else if( !separatorBetweenKeyAndValue.isEmpty()) { // When there is a separator between key and value you can implisitly use numbers in key.
                String[] split = s.split(separatorBetweenKeyAndValue);
                if(split.length != 2)
                    continue;
                String key = split[0];
                String value = split[1];
                output.put(key.trim(), value.trim());
            }

        }

        return output;
    }

    
    private boolean containsIllegalChars(String input) {
        if( input.indexOf(separatorBetweenKeyAndValue) != -1) {
            return true;
        } else if(input.indexOf(separatorBetweenKeyValuePairs) == -1) { // Perhaps just remove it? though we can't be sure if it's correct
            return true;
        } else if(input.indexOf(separatorForDataEntryKeyword) == -1) {
            return true;
        }
        
        return false;
    }
    
    public SMSParserKeyValue() {
    }

    public SMSParserKeyValue(String sepBetwKeyAndValue, String sepBetwKeyValuePairs, String sepForDataEntryKeyword,
            boolean firstWordIsKeyWord, boolean canUseNumbersInKey) {
        this.separatorBetweenKeyAndValue = sepBetwKeyAndValue;
        this.separatorBetweenKeyValuePairs = sepBetwKeyValuePairs;
        this.separatorForDataEntryKeyword = sepForDataEntryKeyword;
        
        this.canUseNumbersInKey=canUseNumbersInKey;
        this.firstWordIsKeyWord = firstWordIsKeyWord; 
        
        if(!sepBetwKeyAndValue.isEmpty())
            this.canUseNumbersInKey = true;
    }
        
    public void setCanUseNumbersInKey(boolean canUseNumbersInKey) {
        this.canUseNumbersInKey = canUseNumbersInKey;
    }

    public void setFirstWordIsKeyWord(boolean firstWordIsKeyWord) {
        this.firstWordIsKeyWord = firstWordIsKeyWord;
    }

    public void setSeparatorBetweenKeyAndValue(String separatorBetweenKeyAndValue) {
        this.separatorBetweenKeyAndValue = separatorBetweenKeyAndValue;
        if( separatorBetweenKeyAndValue != null && !separatorBetweenKeyAndValue.isEmpty()) {
            canUseNumbersInKey = true;
        }
        
    }

    public void setSeparatorBetweenKeyValuePairs(String separatorBetweenKeyValuePairs) {
        this.separatorBetweenKeyValuePairs = separatorBetweenKeyValuePairs;
    }

    public void setSeparatorForDataEntryKeyword(String separatorForDataEntryKeyword) {
        this.separatorForDataEntryKeyword = separatorForDataEntryKeyword;
    }
    
    
    
    
}
