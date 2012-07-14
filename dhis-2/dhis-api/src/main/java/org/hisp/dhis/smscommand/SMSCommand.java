package org.hisp.dhis.smscommand;

import java.util.Set;

import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.sms.parse.ParserType;

public class SMSCommand
{

    private int id; // id for this element

    private String name;

    private String parser; // message type

    private ParserType parserType;

    private String separator;

    private DataSet dataset;

    private Set<SMSCode> codes;

    private String codeSeparator;

    private String defaultMessage;

    

    public SMSCommand( String name, String parser, ParserType parserType, String separator, DataSet dataset,
        Set<SMSCode> codes, String codeSeparator, String defaultMessage )
    {
        super();
        this.name = name;
        this.parser = parser;
        this.parserType = parserType;
        this.separator = separator;
        this.dataset = dataset;
        this.codes = codes;
        this.codeSeparator = codeSeparator;
        this.defaultMessage = defaultMessage;
    }

    public SMSCommand( String name, String parser, String separator, DataSet dataset, Set<SMSCode> codes,
        String codeSeparator )
    {
        this.name = name;
        this.parser = parser;
        this.separator = separator;
        this.dataset = dataset;
        this.codes = codes;
        this.setCodeSeparator( codeSeparator );
    }

    public SMSCommand( String name, String parser, String separator, DataSet dataset, Set<SMSCode> codes )
    {
        this.name = name;
        this.parser = parser;
        this.separator = separator;
        this.dataset = dataset;
        this.codes = codes;
    }

    public SMSCommand( String parser, String name, DataSet dataset, Set<SMSCode> codes )
    {
        this.parser = parser;
        this.name = name;
        this.dataset = dataset;
        this.codes = codes;
    }

    public SMSCommand( String parser, String name, DataSet dataset )
    {
        this.parser = parser;
        this.name = name;
        this.dataset = dataset;
    }

    public SMSCommand( String name, String parser )
    {
        this.name = name;
        this.parser = parser;
    }

    public SMSCommand()
    {

    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getParser()
    {
        return parser;
    }

    public void setParser( String parser )
    {
        this.parser = parser;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public DataSet getDataset()
    {
        return dataset;
    }

    public void setDataset( DataSet dataset )
    {
        this.dataset = dataset;
    }

    public Set<SMSCode> getCodes()
    {
        return codes;
    }

    public void setCodes( Set<SMSCode> codes )
    {
        this.codes = codes;
    }

    public String getSeparator()
    {
        return separator;
    }

    public void setSeparator( String separator )
    {
        this.separator = separator;
    }

    public String getCodeSeparator()
    {
        return codeSeparator;
    }

    public void setCodeSeparator( String codeSeparator )
    {
        this.codeSeparator = codeSeparator;
    }

    public String getDefaultMessage()
    {
        return defaultMessage;
    }

    public void setDefaultMessage( String defaultMessage )
    {
        this.defaultMessage = defaultMessage;
    }

    public ParserType getParserType()
    {
        if(parserType == null){
            return ParserType.KEY_VALUE_PARSER;
        }
        return parserType;
    }

    public void setParserType( ParserType parserType )
    {
        this.parserType = parserType;
    }

}
