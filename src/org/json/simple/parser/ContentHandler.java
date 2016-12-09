package org.json.simple.parser;

import java.io.IOException;

/**
 * A simplified and stoppable SAX-like content handler for stream processing of JSON text. <p>
 * @see org.xml.sax.ContentHandler
 * @see org.json.simple.parser.JSONParser#parse(java.io.Reader, ContentHandler, boolean) <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public interface ContentHandler {
    
    /**
     * Receive notification of the beginning of JSON processing. <p> The parser will invoke this method only once. <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO
     */
    void startJSON() throws ParseException, IOException;
    
    /**
     * Receive notification of the end of JSON processing. <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO
     */
    void endJSON() throws ParseException, IOException;
    
    /**
     * Receive notification of the beginning of a JSON object. <p>
     * @return false if the handler wants to stop parsing after return. <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO <p>
     * @see #endJSON
     */
    boolean startObject() throws ParseException, IOException;
    
    /**
     * Receive notification of the end of a JSON object.
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException
     * @see #startObject
     */
    boolean endObject() throws ParseException, IOException;
    
    /**
     * Receive notification of the beginning of a JSON object entry. <p>
     * @param key - Key of a JSON object entry. <p>
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO <p>
     * @see #endObjectEntry
     */
    boolean startObjectEntry(String key) throws ParseException, IOException;
    
    /**
     * Receive notification of the end of the value of previous object entry. <p>
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO <p>
     * @see #startObjectEntry
     */
    boolean endObjectEntry() throws ParseException, IOException;
    
    /**
     * Receive notification of the beginning of a JSON array. <p>
     * @return false if the handler wants to stop parsing after return.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException TODO <p>
     * @see #endArray
     */
    boolean startArray() throws ParseException, IOException;
    
    /**
     * Receive notification of the end of a JSON array. <p>
     * @return false if the handler wants to stop parsing after return. <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException <p>
     * @see #startArray
     */
    boolean endArray() throws ParseException, IOException;
    
    /**
     * Receive notification of the JSON primitive values: java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null <p>
     * @param value - Instance of the following: java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null <p>
     * @return false if the handler wants to stop parsing after return. <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     * @throws IOException
     */
    boolean primitive(Object value) throws ParseException, IOException;
}
