/*
 * $Id: ItemList.java,v 1.1 2006/04/15 14:10:48 platform Exp $ Created on 2006-3-24
 */
package org.json.simple;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * |a:b:c| => |a|,|b|,|c| |:| => ||,|| |a:| => |a|,||
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public class ItemList {
    
    /**
     * Between elements separator used char.
     */
    private String sp = ",";
    /**
     * Collection List type object where all data is allocated.
     */
    List<String> items = new ArrayList<String>();
    
    /**
     * Constructor de clase.
     */
    public ItemList() {}
    
    /**
     * Class constructor. <p>
     * @param s string data containing elements to be used as elements in this ItemList.
     */
    public ItemList(String s) {
        this.split(s, sp, items);
    }
    
    /**
     * Class constructor. <p>
     * @param s string data containing elements to be used as elements in this ItemList.
     * @param sp string containing data separator character.
     */
    public ItemList(String s, String sp) {
        this.sp = s;
        this.split(s, sp, items);
    }
    
    /**
     * Class constructor. <p>
     * @param s string data containing elements to be used as elements in this ItemList.
     * @param sp string containing data separator character.
     * @param isMultiToken true if s parameter has more than 1 element separated by the separator char.
     */
    public ItemList(String s, String sp, boolean isMultiToken) {
        split(s, sp, items, isMultiToken);
    }
    
    /**
     * Getter. <p>
     * @return type List<String> with data contained in <pre>items</pre> attribute.
     */
    public List<String> getItems() {
        return this.items;
    }
    
    /**
     * Getter. <p>
     * @return an String array with data contained in <pre>items</pre> attribute.
     */
    public String[] getArray() {
        return (String[]) this.items.toArray();
    }
    
    /**
     * Método usado para TODO. <p>
     * @param s string data containing elements to be used as elements in this ItemList.
     * @param sp string containing data separator character.
     * @param append TODO
     * @param isMultiToken true if s parameter has more than 1 element separated by the separator char.
     */
    public void split(String s, String sp, List<String> append, boolean isMultiToken) {
        if (s == null || sp == null) return;
        if (isMultiToken) {
            StringTokenizer tokens = new StringTokenizer(s, sp);
            while (tokens.hasMoreTokens()) {
                append.add(tokens.nextToken().trim());
            }
        } else {
            this.split(s, sp, append);
        }
    }
    
    /**
     * Método usado para TODO. <p>
     * @param s string data containing elements to be used as elements in this ItemList.
     * @param sp string containing data separator character.
     * @param append TODO
     */
    public void split(String s, String sp, List<String> append) {
        if (s == null || sp == null) return;
        int pos = 0;
        int prevPos = 0;
        do {
            prevPos = pos;
            pos = s.indexOf(sp, pos);
            if (pos == -1) break;
            append.add(s.substring(prevPos, pos).trim());
            pos += sp.length();
        } while (pos != -1);
        append.add(s.substring(prevPos).trim());
    }
    
    /**
     * Setter. <p>
     * @param sp string containing data separator character.
     */
    public void setSP(String sp) {
        this.sp = sp;
    }
    
    /**
     * Adds a new element with data contained in <pre>item</pre> parameter at position specify in <pre>i</pre> parameter. <p>
     * @param i desired position where to insert data.
     * @param item contains data to insert.
     */
    public void add(int i, String item) {
        if (item == null) return;
        items.add(i, item.trim());
    }
    
    /**
     * Adds a new element with data contained in <pre>item</pre> parameter. <p>
     * @param item contains data to insert.
     */
    public void add(String item) {
        if (item == null) return;
        items.add(item.trim());
    }
    
    /**
     * Add all elements contained at list parameter. <p>
     * @param list type ItemList containing data to be added.
     */
    public void addAll(ItemList list) {
        items.addAll(list.items);
    }
    
    /**
     * Add all elements contained in s parameter witch are separated by sp attribute specified character. <p>
     * @param s element list separated by character specified at sp attribute.
     */
    public void addAll(String s) {
        this.split(s, sp, items);
    }
    
    /**
     * Add all elements contained in s parameter witch are separated by sp parameter specified character. <p>
     * @param s element list separated by character specified at sp parameter.
     * @param sp separator character used to get list of elements.
     */
    public void addAll(String s, String sp) {
        this.split(s, sp, items);
    }
    
    /**
     * Add all elements contained in s parameter witch are separated by sp parameter specified character. <p>
     * @param s element list separated by character specified at sp parameter.
     * @param sp separator character used to get list of elements.
     * @param isMultiToken true if s parameter has more than 1 element separated by the separator char.
     */
    public void addAll(String s, String sp, boolean isMultiToken) {
        this.split(s, sp, items, isMultiToken);
    }
    
    /**
     * Getter. <p>
     * @param i position where to get desired element in this ItemList. <p>
     * @return get the element at position specified by <pre>i</pre> parameter.
     */
    public String get(int i) {
        return items.get(i);
    }
    
    /**
     * Returns ItemList number of elements. <p>
     * @return integer type with the number of elements.
     */
    public int size() {
        return items.size();
    }
    
    @Override
    public String toString() {
        return toString(sp);
    }
    
    /**
     * Convert all data to String type. <p>
     * @param sp separator character to be used. <p>
     * @return String type with all data contained in this ItemList
     */
    public String toString(String sp) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < items.size(); i++) {
            if (i == 0)
                sb.append(items.get(i));
            else {
                sb.append(sp);
                sb.append(items.get(i));
            }
        }
        return sb.toString();
    }
    
    /**
     * Clears all data.
     */
    public void clear() {
        items.clear();
    }
    
    /**
     * Reset data and set default separator char <pre>","</pre>
     */
    public void reset() {
        sp = ",";
        items.clear();
    }
}
