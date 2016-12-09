/*
 * $Id: Yytoken.java,v 1.1 2006/04/15 14:10:48 platform Exp $ Created on 2006-4-15
 */
package org.json.simple.parser;

/**
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public class Yytoken {
    
    /**
     * Allowed token types.
     */
    public static enum TokenType {
        /**
         * JSON primitive type (string, number, boolean, null)
         */
        VALUE,
        /**
         * JSON left brace type (JSONObject begins)
         */
        LEFT_BRACE,
        /**
         * JSON right brace type (JSONObject ends)
         */
        RIGHT_BRACE,
        /**
         * JSON left square type (JSONArray begins)
         */
        LEFT_SQUARE,
        /**
         * JSON right square type (JSONArray ends)
         */
        RIGHT_SQUARE,
        /**
         * JSON comma (indicates either next element in array or next object attribute)
         */
        COMMA,
        /**
         * Indicate Key name end (or value entry commence).
         */
        COLON,
        /**
         * End of file.
         */
        EOF
    }
    
    /**
     * JSON primitive type.
     */
    // public static final int TYPE_VALUE=0;//JSON primitive value: string,number,boolean,null
    /**
     * JSON left brace type (JSONObject begins)
     */
    // public static final int TYPE_LEFT_BRACE=1;
    /**
     * JSON right brace type (JSONObject ends)
     */
    // public static final int TYPE_RIGHT_BRACE=2;
    /**
     * JSON left square type (JSONArray begins)
     */
    // public static final int TYPE_LEFT_SQUARE=3;
    /**
     * JSON right square type (JSONArray ends)
     */
    // public static final int TYPE_RIGHT_SQUARE=4;
    /**
     * JSON comma (indicates either next element in array or next object attribute)
     */
    // public static final int TYPE_COMMA=5;
    /**
     * Indicate Key name end (or value entry commence).
     */
    // public static final int TYPE_COLON=6;
    /**
     * Type end of file.
     */
    // public static final int TYPE_EOF=-1;//
    /**
     * Store object instance type.
     */
    public TokenType type;
    /**
     * Store entry value.
     */
    public Object value = null;
    
    /**
     * Constructor de clase. <p>
     * @param type contains a valid JSON type.
     * @param value data to be stored.
     */
    public Yytoken(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }
    
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case VALUE:
                sb.append("VALUE(").append(value).append(")");
                break;
            case LEFT_BRACE:
                sb.append("LEFT BRACE({)");
                break;
            case RIGHT_BRACE:
                sb.append("RIGHT BRACE(})");
                break;
            case LEFT_SQUARE:
                sb.append("LEFT SQUARE([)");
                break;
            case RIGHT_SQUARE:
                sb.append("RIGHT SQUARE(])");
                break;
            case COMMA:
                sb.append("COMMA(,)");
                break;
            case COLON:
                sb.append("COLON(:)");
                break;
            case EOF:
                sb.append("END OF FILE");
                break;
        }
        return sb.toString();
    }
}
