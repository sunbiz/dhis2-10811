package org.hisp.dhis.smscommand;

import java.util.Set;

import org.hisp.dhis.dataset.DataSet;

public class SMSCommand {

    private int id; // id for this element
    private String name;
    
    private String parser; // message type
    private String seperator;

    private DataSet dataset; 
    private Set<SMSCode> codes;
    private String codeSeperator; 
    
    public SMSCommand(String name, String parser, String seperator, DataSet dataset, Set<SMSCode> codes,
            String codeSeperator) {
        this.name = name;
        this.parser = parser;
        this.seperator = seperator;
        this.dataset = dataset;
        this.codes = codes;
        this.setCodeSeperator(codeSeperator);
    }

    public SMSCommand(String name, String parser, String seperator, DataSet dataset, Set<SMSCode> codes) {
        this.name = name;
        this.parser = parser;
        this.seperator = seperator;
        this.dataset = dataset;
        this.codes = codes;
    }



    public SMSCommand(String parser, String name, DataSet dataset, Set<SMSCode> codes) {
        this.parser = parser;
        this.name = name;
        this.dataset = dataset;
        this.codes = codes;
    }

    public SMSCommand(String parser, String name, DataSet dataset) {
        this.parser = parser;
        this.name = name;
        this.dataset = dataset;
    }

    public SMSCommand(String name, String parser) {
        this.name = name;
        this.parser = parser;
    }

    public SMSCommand() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParser() {
        return parser;
    }

    public void setParser(String parser) {
        this.parser = parser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DataSet getDataset() {
        return dataset;
    }

    public void setDataset(DataSet dataset) {
        this.dataset = dataset;
    }

    public Set<SMSCode> getCodes() {
        return codes;
    }

    public void setCodes(Set<SMSCode> codes) {
        this.codes = codes;
    }

    public String getSeperator() {
        return seperator;
    }

    public void setSeperator(String seperator) {
        this.seperator = seperator;
    }

    public String getCodeSeperator() {
        return codeSeperator;
    }

    public void setCodeSeperator(String codeSeperator) {
        this.codeSeperator = codeSeperator;
    }


}
