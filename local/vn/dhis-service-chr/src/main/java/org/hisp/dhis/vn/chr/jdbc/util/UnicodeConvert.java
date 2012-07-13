package org.hisp.dhis.vn.chr.jdbc.util;

/**
 * @author Dang Duy Hieu
 * @author Chau Thu Tran
 * 
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class UnicodeConvert
{

    
    private static final String[][] tVNI = {
            {"¸", "µ", "¶", "·", "¹", "©", "Ê", "Ç", "È", "É", "Ë", "¨", "¾", "»", "¼", "½", "Æ", "®", "Ð", "Ì", "Î", "Ï", "Ñ", "ª", "Õ", "Ò", "Ó", "Ô", "Ö", "Ý", "×", "Ø", "Ü", "Þ", "ã", "ß", "á", "â", "ä", "«", "è", "å", "æ", "ç", "é", "¬", "í", "ê", "ë", "ì", "î", "ó", "ï", "ñ", "ò", "ô", "­", "ø", "õ", "ö", "÷", "ù", "ý", "ú", "û", "ü", "þ"},
            {"¸", "µ", "¶", "•", "¹", "¢", "Ê", "Ç", "È", "É", "Ë", "¡", "¾", "»", "¼", "½", "Æ", "§", "Ð", "Ì", "Î", "Ï", "Ñ", "ª", "Õ", "Ò", "Ó", "Ô", "Ö", "Ý", "×", "Ø", "Ü", "Þ", "Ã", "ß", "Á", "Â", "Ä", "¤", "è", "Å", "Æ", "Ç", "É", "¬", "Í", "Ê", "Ë", "Ì", "Î", "Ó", "Ï", "Ñ", "Ò", "Ô", "­", "Ø", "Õ", "Ö", "÷", "Ù", "Ý", "Ú", "Û", "Ü", "Þ"}
    };
    
    private static final String[][] tUnicode = {
            {"á", "à", "ả", "ã", "ạ", "â", "ấ", "ầ", "ẩ", "ẫ", "ậ", "ă", "ắ", "ằ", "ẳ", "ẵ", "ặ", "đ", "é", "è", "ẻ", "ẽ", "ẹ", "ê", "ế", "ề", "ể", "ễ", "ệ", "í", "ì", "ỉ", "ĩ", "ị", "ó", "ò", "ỏ", "õ", "ọ", "ô", "ố", "ồ", "ổ", "ỗ", "ộ", "ơ", "ớ", "ờ", "ở", "ỡ", "ợ", "ú", "ù", "ủ", "ũ", "ụ", "ư", "ứ", "ừ", "ử", "ữ", "ự", "ý", "ỳ", "ỷ", "ỹ", "ỵ", "đ"},
            {"Á", "À", "Ả", "Ã", "Ạ", "Â", "Ấ", "Ầ", "Ẩ", "Ẫ", "Ậ", "Ă", "Ắ", "Ằ", "Ẳ", "Ẵ", "Ặ", "Đ", "É", "È", "Ẻ", "Ẽ", "Ẹ", "Ê", "Ế", "Ề", "Ể", "Ễ", "Ệ", "Í", "Ì", "Ỉ", "Ĩ", "Ị", "Ó", "Ò", "Ỏ", "Õ", "Ọ", "Ô", "Ố", "Ồ", "Ổ", "Ỗ", "Ộ", "Ơ", "Ớ", "Ờ", "Ở", "Ỡ", "Ợ", "Ú", "Ù", "Ủ", "Ũ", "Ụ", "Ư", "Ứ", "Ừ", "Ử", "Ữ", "Ự", "Ý", "Ỳ", "Ỷ", "Ỹ", "Ỵ", "Đ"}
    };
    

    private static final String[][] tUTF8Literal = {
            {"Ã¡", "Ã ", "áº£", "Ã£", "áº¡", "Ã¢", "áº¥", "áº§", "áº©", "áº«", "áº­", "Äƒ", "áº¯", "áº±", "áº³", "áºµ", "áº·", "Ä‘", "Ã©", "Ã¨", "áº»", "áº½", "áº¹", "Ãª", "áº¿", "á»", "á»ƒ", "á»…", "á»‡", "Ã­", "Ã¬", "á»‰", "Ä©", "á»‹", "Ã³", "Ã²", "á»", "Ãµ", "á»", "Ã´", "á»‘", "á»“", "á»•", "á»—", "á»™", "Æ¡", "á»›", "á»", "á»Ÿ", "á»¡", "á»£", "Ãº", "Ã¹", "á»§", "Å©", "á»¥", "Æ°", "á»©", "á»«", "á»­", "á»¯", "á»±", "Ã½", "á»³", "á»·", "á»¹", "á»µ", "Ä‘"},
            {"Ã", "Ã€", "áº¢", "Ãƒ", "áº ", "Ã‚", "áº¤", "áº¦", "áº¨", "áºª", "áº¬", "Ä‚", "áº®", "áº°", "áº²", "áº´", "áº¶", "Ä", "Ã‰", "Ãˆ", "áºº", "áº¼", "áº¸", "ÃŠ", "áº¾", "á»€", "á»‚", "á»„", "á»†", "Ã", "ÃŒ", "á»ˆ", "Ä¨", "á»Š", "Ã“", "Ã’", "á»Ž", "Ã•", "á»Œ", "Ã”", "á»", "á»’", "á»”", "á»–", "á»˜", "Æ ", "á»š", "á»œ", "á»ž", "á» ", "á»¢", "Ãš", "Ã™", "á»¦", "Å¨", "á»¤", "Æ¯", "á»¨", "á»ª", "á»¬", "á»®", "á»°", "Ã", "á»²", "á»¶", "á»¸", "á»´", "Ä"}
    };
    

    private static final String[][] tUnsigned = {
            {"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "d", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "e", "i", "i", "i", "i", "i", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "u", "y", "y", "y", "y", "y"},
            {"A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "A", "D", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "E", "I", "I", "I", "I", "I", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "O", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "U", "Y", "Y", "Y", "Y", "Y"}
    };
    
    private static final String[][] tNormalChar = {
            {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"},
            {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"}
    };
    
    private static final String[] tMainGroup = {"Ã", "á", "Å", "Ä", "Æ"};
    
    /*
     * --------------------------------------------------------------------------
     * -----------------------------------------------------------
     */
    private String comparedCharsForString( String s )
    {
        StringBuffer sNormal = new StringBuffer();
        StringBuffer sSpecial = new StringBuffer();

        for ( int i = 0; i < tMainGroup.length; i++ )
        {
            if ( s.contains( tMainGroup[i] ) )
            {
                sSpecial.append( this.getSpecialString( s ) );

                if ( sSpecial.length() >= 4 )
                {
                    if ( sSpecial.charAt( 0 ) == tMainGroup[1].charAt( 0 ) )
                    {
                        sNormal.append( this.mappingUTF8LiteraltoUnicode( sSpecial.substring( 0, 3 ).toString() ) );
                        sNormal.append( this.mappingUTF8LiteraltoUnicode( sSpecial.substring( 3 ).toString() ) );
                    }
                    else
                    {
                        sNormal.append( this.mappingUTF8LiteraltoUnicode( sSpecial.substring( 0, 2 ).toString() ) );
                        sNormal.append( this.mappingUTF8LiteraltoUnicode( sSpecial.substring( 2 ).toString() ) );
                    }
                }
                else
                {
                    sNormal.append( this.mappingUTF8LiteraltoUnicode( sSpecial.toString() ) );
                }
                
                s = s.replaceAll( sSpecial.toString(), sNormal.toString() );
                return s;
            }
        }

        return s;
    }

    private String getSpecialString( String sSpecial )
    {

        for ( int i = 0; i < tNormalChar.length; i++ )
            for ( int j = 0; j < tNormalChar[i].length; j++ )
                sSpecial = sSpecial.replaceAll( tNormalChar[i][j], "" );

        return sSpecial;
    }

    private String mappingUTF8LiteraltoUnicode( String sSpecial )
    {

        for ( int i = 0; i < tUTF8Literal.length; i++ )
            for ( int j = 0; j < tUTF8Literal[i].length; j++ )
                if ( sSpecial.equals( tUTF8Literal[i][j] ) )
                {

                    sSpecial = tUnicode[i][j];
                    return sSpecial;
                }
        return sSpecial;
    }

    /*
     * --------------------------------------------------------------------------
     * --------------------------------------------------------------------------
     */
    private String comparedChars( String sPrototype, boolean isAllowedSpaceChar, char c )
    {
        for ( int i = 0; i < tVNI.length; i++ )
        {

            for ( int j = 0; j < tVNI[i].length; j++ )
            {

                if ( (c == ' ') && isAllowedSpaceChar )
                {
                    return String.valueOf( c );
                }
                if ( tVNI[i][j].charAt( 0 ) == (c) )
                {

                    if ( "UNICODE".equals( sPrototype ) )
                    {

                        return tUnicode[i][j];
                    }
                    else if ( "UNSIGNED".equals( sPrototype ) )
                    {

                        return tUnsigned[i][j];
                    }
                }
            }
        }
        return String.valueOf( c );
    }

    /*
     * --------------------------------------------------------------------------
     * -----------------------------------------------------------
     */
    public String convertfromTCVN3toUnicode( String sTCVN3 )
    {
        StringBuffer result = new StringBuffer();

        for ( int i = 0; i < sTCVN3.length(); i++ )
        {
            result.append( this.comparedChars( "UNICODE", true, sTCVN3.trim().charAt( i ) ) );
        }

        return result.toString();
    }

    /*
     * --------------------------------------------------------------------------
     * -----------------------------------------------------------
     */
    public String convertfromTCVN3toUnsigned( String sTCVN3 )
    {

        StringBuffer result = new StringBuffer();

        for ( int i = 0; i < sTCVN3.length(); i++ )
        {
            result.append( this.comparedChars( "UNSIGNED", true, sTCVN3.trim().charAt( i ) ) );
        }

        return result.toString();
    }

    /*
     * --------------------------------------------------------------------------
     * -----------------------------------------------------------
     */
    public String convertfromUTF8LiteraltoUnicode( String sUTF8Literal )
    {

        StringBuffer result = new StringBuffer();

        String[] aTemp = sUTF8Literal.split( " " );

        for ( int i = 0; i < aTemp.length; i++ )
        {

            result.append( this.comparedCharsForString( aTemp[i] ) + " " );

        }

        return result.toString();
    }

    /*
     * --------------------------------------------------------------------------
     * --------------------------------------------------------------------------
     */
    public static void main( String[] args )
    {
        String line = "";
        try
        {

            UnicodeConvert converter = new UnicodeConvert();
            // Read
            FileInputStream fis = new FileInputStream(
                "C:\\Documents and Settings\\Administrator\\Desktop\\element.sql" );
            BufferedReader in = new BufferedReader( new InputStreamReader( fis, "UTF8" ) );

            // Write
            FileOutputStream fos = new FileOutputStream(
                "C:\\Documents and Settings\\Administrator\\Desktop\\element_convert.sql" );
            BufferedWriter out = new BufferedWriter( new OutputStreamWriter( fos, "UTF8" ) );

            while ( (line = in.readLine()) != null )
            {

                line = line.replace( "'0000-00-00'", "null" ).replace( "-00", "-01" ).replace( "'", " ' " ).replace(
                    ")", " ) " ).replace( "(", " ( " ).replace( "]", " ] " ).replace( "[", " ] " ).replace( "`", "" )
                    .replace( "\\'", "" ).replace( "\\", " " ).replace( "+", " ---- " ).replace( "*", " --- " )
                    .replace( "-", " - " );

                line = line.replace( "Admin", "15" );
                line = line.replace( "normal", "5402" );
                line = line.replace( "center", "5403" );
                line = line.replace( "skte_quan1", "5404" );
                line = line.replace( "skte_quan10", "5405" );
                line = line.replace( "skte_quan3", "5407" );
                line = line.replace( "skte_quan2", "5406" );
                line = line.replace( "skte_quan4", "5408" );
                line = line.replace( "skte_quan5", "5409" );
                line = line.replace( "skte_quan6", "5410" );
                line = line.replace( "skte_quan7", "5411" );
                line = line.replace( "skte_quan8", "5412" );
                line = line.replace( "skte_quan9", "5413" );
                line = line.replace( "skte_quan11", "5414" );
                line = line.replace( "skte_quan12", "5415" );
                line = line.replace( "skte_binhchanh", "5416" );
                line = line.replace( "skte_cangio", "5417" );
                line = line.replace( "skte_cuchi", "5418" );
                line = line.replace( "skte_hocmon", "5419" );
                line = line.replace( "skte_nhabe", "5420" );
                line = line.replace( "skte_binhtan", "5421" );
                line = line.replace( "skte_binhthanh", "5422" );
                line = line.replace( "skte_govap", "5423" );
                line = line.replace( "skte_phunhuan", "5424" );
                line = line.replace( "skte_tanbinh", "5499" );
                line = line.replace( "skte_tanphu", "5498" );
                line = line.replace( "skte_thuduc", "5500" );
                line = line.replace( "f16q4", "5425" );
                line = line.replace( "f15q4", "5426" );
                line = line.replace( "f14q4", "5427" );
                line = line.replace( "f1q4", "5428" );
                line = line.replace( "f2q4", "5429" );
                line = line.replace( "f3q4", "5430" );
                line = line.replace( "f4q4", "5431" );
                line = line.replace( "f5q4", "5432" );
                line = line.replace( "f6q4", "5433" );
                line = line.replace( "f7q4", "5434" );
                line = line.replace( "f8q4", "5435" );
                line = line.replace( "f9q4", "5436" );
                line = line.replace( "f10q4", "5437" );
                line = line.replace( "f11q4", "5438" );
                line = line.replace( "f12q4", "5439" );
                line = line.replace( "f13q4", "5440" );
                line = line.replace( "f17q4", "5441" );
                line = line.replace( "f18q4", "5442" );
                line = line.replace( "f1bt", "5443" );
                line = line.replace( "f2bt", "5444" );
                line = line.replace( "f3bt", "5445" );
                line = line.replace( "f5bt", "5446" );
                line = line.replace( "f6bt", "5501" );
                line = line.replace( "f7bt", "5447" );
                line = line.replace( "f11bt", "5448" );
                line = line.replace( "f12bt", "5449" );
                line = line.replace( "f13bt", "5450" );
                line = line.replace( "f14bt", "5451" );
                line = line.replace( "f15bt", "5452" );
                line = line.replace( "f17bt", "5453" );
                line = line.replace( "f19bt", "5454" );
                line = line.replace( "f21bt", "5455" );
                line = line.replace( "f22bt", "5456" );
                line = line.replace( "f24bt", "5457" );
                line = line.replace( "f25bt", "5458" );
                line = line.replace( "f26bt", "5459" );
                line = line.replace( "f27bt", "5460" );
                line = line.replace( "f28bt", "5461" );
                line = line.replace( "giang", "5462" );
                line = line.replace( "f1q5", "5463" );
                line = line.replace( "f2q5", "5464" );
                line = line.replace( "f3q5", "5465" );
                line = line.replace( "f4q5", "5466" );
                line = line.replace( "f5q5", "5467" );
                line = line.replace( "f6q5", "5468" );
                line = line.replace( "f7q5", "5469" );
                line = line.replace( "f8q5", "5470" );
                line = line.replace( "f9q5", "5471" );
                line = line.replace( "f10q5", "5472" );
                line = line.replace( "f11q5", "5473" );
                line = line.replace( "f12q5", "5474" );
                line = line.replace( "f13q5", "5475" );
                line = line.replace( "f14q5", "5476" );
                line = line.replace( "f15q5", "5477" );
                
                String stringBuffer = converter.convertfromUTF8LiteraltoUnicode( line ) + "\n";
                
                out.write( stringBuffer.replace( " ' ", "'" ).replace( " ) ", ")" ).replace( " ] ", "]" ).replace(
                    " ] ", "[" ).replace( " --- ", "*" ).replace( " ---- ", "+" ) );
               
            }

            System.out.print( "\n\n\n finish " );

            in.close();
            out.close(); fos.close();

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            System.out.print( "\n\n\n line : " + line );
        }
    }
}
