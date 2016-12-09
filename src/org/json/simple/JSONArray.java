/*
 * $Id: JSONArray.java,v 1.1 2006/04/15 14:10:48 platform Exp $ Created on 2006-4-10
 */
package org.json.simple;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A JSON array. JSONObject supports java.util.List interface. <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public class JSONArray extends ArrayList<Object> implements JSONAware, JSONStreamAware {
    
    private static final long serialVersionUID = 3957988303675231981L;
    /**
     * Constante con texto "null".
     */
    public static final String NULL = "null";
    
    /**
     * Encode a list into JSON text and write it to out. <p> If this list is also a JSONStreamAware or a JSONAware, JSONStreamAware and JSONAware specific behaviors will be ignored at this top level. <p>
     * @see org.json.simple.JSONValue#writeJSONString(Object, Writer) <p>
     * @param list type List containing data to be encoded in JSON Format.
     * @param out type Writer containing object who will write encoded string to somewhere. <p>
     * @throws IOException in case of file errors related.
     */
    public static void writeJSONString(List<Object> list, Writer out) throws IOException {
        if (list == null) {
            out.write(NULL);
            return;
        }
        boolean first = true;
        Iterator<?> iter = list.iterator();
        out.write('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                out.write(',');
            Object value = iter.next();
            if (value == null) {
                out.write(NULL);
                continue;
            }
            JSONValue.writeJSONString(value, out);
        }
        out.write(']');
    }
    
    @Override
    public void writeJSONString(Writer out) throws IOException {
        writeJSONString(this, out);
    }
    
    /**
     * Convert a list to JSON text. The result is a JSON array. <p> If this list is also a JSONAware, JSONAware specific behaviors will be omitted at this top level. <p>
     * @see org.json.simple.JSONValue#toJSONString(Object) <p>
     * @param list type List containing data where to get JSON string. <p>
     * @return JSON text, or NULL if list is null.
     */
    public static String toJSONString(List<Object> list) {
        if (list == null) return NULL;
        boolean first = true;
        StringBuffer sb = new StringBuffer();
        Iterator<?> iter = list.iterator();
        sb.append('[');
        while (iter.hasNext()) {
            if (first)
                first = false;
            else
                sb.append(',');
            Object value = iter.next();
            if (value == null) {
                sb.append(NULL);
                continue;
            }
            sb.append(JSONValue.toJSONString(value));
        }
        sb.append(']');
        return sb.toString();
    }
    
    @Override
    public String toJSONString() {
        return toJSONString(this);
    }
    
    @Override
    public String toString() {
        return toJSONString();
    }
}
