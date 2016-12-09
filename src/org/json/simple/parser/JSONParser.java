/*
 * $Id: JSONParser.java,v 1.1 2006/04/15 14:10:48 platform Exp $ Created on 2006-4-15
 */
package org.json.simple.parser;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.Yytoken.TokenType;

/**
 * Parser for JSON text. Please note that JSONParser is NOT thread-safe. <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public class JSONParser {
    
    /**
     * java.lang.Enum type with all different tokens used in JSON format.
     */
    public static enum ElementType {
        /**
         * Start token.
         */
        INIT,
        /**
         * Final value. Will contain some of the following types: string, number, boolean, null, object, array
         */
        FINISHED_VALUE,
        /**
         * Object token.
         */
        IN_OBJECT,
        /**
         * Array token.
         */
        IN_ARRAY,
        /**
         * Atributo usado para TODO.
         */
        PASSED_PAIR_KEY,
        /**
         * Atributo usado para TODO.
         */
        PAIR_VALUE,
        /**
         * End token.
         */
        END,
        /**
         * Error token.
         */
        IN_ERROR
    }
    
    /**
     * Status stack.
     */
    private LinkedList<ElementType> handlerStatusStack;
    /**
     * Like scanner type. Used to read data from ..
     */
    private Yylex lexer = new Yylex((Reader) null);
    /**
     * Contains the current token.
     */
    private Yytoken token = null;
    /**
     * Has the current status value.
     */
    private ElementType status = ElementType.INIT;
    
    /**
     * Return current status from stack. <p>
     * @param statusStack type LinkedList<ElementType> with all status data as stack. <p>
     * @return integer with the current status.
     */
    private ElementType peekStatus(LinkedList<ElementType> statusStack) {
        if (statusStack.size() == 0) return ElementType.IN_ERROR;
        ElementType status = statusStack.getFirst();
        return status;
    }
    
    /**
     * Reset the parser to the initial state without resetting the underlying reader.
     */
    public void reset() {
        token = null;
        status = ElementType.INIT;
        handlerStatusStack = null;
    }
    
    /**
     * Reset the parser to the initial state with a new character reader. <p>
     * @param in - type Reader object instance where data is stored..
     */
    public void reset(Reader in) {
        lexer.yyreset(in);
        reset();
    }
    
    /**
     * Getter.
     * @return The position of the beginning of the current token.
     */
    public int getPosition() {
        return lexer.getPosition();
    }
    
    /**
     * Método usado para TODO . <p>
     * @param s - String where data is allocated (must be in JSON format) <p>
     * @return Instance of the following classes: org.json.simple.JSONObject, org.json.simple.JSONArray, java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null <p>
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public Object parse(String s) throws ParseException {
        return parse(s, (ContainerFactory) null);
    }
    
    /**
     * Método usado para TODO . <p>
     * @param s - String where data is allocated (must be in JSON format)
     * @param containerFactory <p>
     * @return Instance of the following classes: org.json.simple.JSONObject, org.json.simple.JSONArray, java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public Object parse(String s, ContainerFactory containerFactory) throws ParseException {
        StringReader in = new StringReader(s);
        try {
            return parse(in, containerFactory);
        } catch (IOException ie) {
            /*
             * Actually it will never happen.
             */
            throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
        }
    }
    
    /**
     * Método usado para TODO . <p>
     * @param in - type Reader object instance where data is stored. <p>
     * @return Instance of the following classes: org.json.simple.JSONObject, org.json.simple.JSONArray, java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null <p>
     * @throws IOException - In case of file read exception or similar
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public Object parse(Reader in) throws IOException, ParseException {
        return parse(in, (ContainerFactory) null);
    }
    
    /**
     * Parse JSON text into java object from the input source. <p>
     * @param in - type Reader object instance where data is stored.
     * @param containerFactory - Use this factory to create your own JSON object and JSON array containers. <p>
     * @return Instance of the following classes: org.json.simple.JSONObject, org.json.simple.JSONArray, java.lang.String, java.lang.Number, java.lang.Boolean, java.lang.Null <p>
     * @throws IOException - In case of file read exception or similar.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public Object parse(Reader in, ContainerFactory containerFactory) throws IOException, ParseException {
        this.reset(in);
        LinkedList<ElementType> statusStack = new LinkedList<>();
        LinkedList<Object> valueStack = new LinkedList<>();
        try {
            do {
                nextToken();
                switch (status) {
                    case INIT:
                        switch (token.type) {
                            case VALUE:
                                status = ElementType.FINISHED_VALUE;
                                statusStack.addFirst(status);
                                valueStack.addFirst(token.value);
                                break;
                            case LEFT_BRACE:
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                valueStack.addFirst(createObjectContainer(containerFactory));
                                break;
                            case LEFT_SQUARE:
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                valueStack.addFirst(createArrayContainer(containerFactory));
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                        }// inner switch
                        break;
                    case FINISHED_VALUE:
                        if (token.type == TokenType.EOF)
                            return valueStack.removeFirst();
                        else
                            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                    case IN_OBJECT:
                        switch (token.type) {
                            case COMMA:
                                break;
                            case VALUE:
                                if (token.value instanceof String) {
                                    String key = (String) token.value;
                                    valueStack.addFirst(key);
                                    status = ElementType.PASSED_PAIR_KEY;
                                    statusStack.addFirst(status);
                                } else {
                                    status = ElementType.IN_ERROR;
                                }
                                break;
                            case RIGHT_BRACE:
                                if (valueStack.size() > 1) {
                                    statusStack.removeFirst();
                                    valueStack.removeFirst();
                                    status = peekStatus(statusStack);
                                } else {
                                    status = ElementType.FINISHED_VALUE;
                                }
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                                break;
                        }// inner switch
                        break;
                    case PASSED_PAIR_KEY:
                        switch (token.type) {
                            case COLON:
                                break;
                            case VALUE:
                                statusStack.removeFirst();
                                String key = (String) valueStack.removeFirst();
                                JSONObject parent = (JSONObject) valueStack.getFirst();
                                parent.put(key, token.value);
                                status = peekStatus(statusStack);
                                break;
                            case LEFT_SQUARE:
                                statusStack.removeFirst();
                                key = (String) valueStack.removeFirst();
                                parent = (JSONObject) valueStack.getFirst();
                                JSONArray newArray = createArrayContainer(containerFactory);
                                parent.put(key, newArray);
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                valueStack.addFirst(newArray);
                                break;
                            case LEFT_BRACE:
                                statusStack.removeFirst();
                                key = (String) valueStack.removeFirst();
                                parent = (JSONObject) valueStack.getFirst();
                                JSONObject newObject = createObjectContainer(containerFactory);
                                parent.put(key, newObject);
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                valueStack.addFirst(newObject);
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                        }
                        break;
                    case IN_ARRAY:
                        switch (token.type) {
                            case COMMA:
                                break;
                            case VALUE:
                                JSONArray val = (JSONArray) valueStack.getFirst();
                                val.add(token.value);
                                break;
                            case RIGHT_SQUARE:
                                if (valueStack.size() > 1) {
                                    statusStack.removeFirst();
                                    valueStack.removeFirst();
                                    status = peekStatus(statusStack);
                                } else {
                                    status = ElementType.FINISHED_VALUE;
                                }
                                break;
                            case LEFT_BRACE:
                                val = (JSONArray) valueStack.getFirst();
                                JSONObject newObject = createObjectContainer(containerFactory);
                                val.add(newObject);
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                valueStack.addFirst(newObject);
                                break;
                            case LEFT_SQUARE:
                                val = (JSONArray) valueStack.getFirst();
                                JSONArray newArray = createArrayContainer(containerFactory);
                                val.add(newArray);
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                valueStack.addFirst(newArray);
                                break;
                            default:
                                this.status = ElementType.IN_ERROR;
                        }// inner switch
                        break;
                    case IN_ERROR:
                        throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, this.token);
                    default:
                        break;
                }// switch
                if (this.status == ElementType.IN_ERROR) {
                    throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, this.token);
                }
            } while (this.token.type != TokenType.EOF);
        } catch (IOException ie) {
            throw ie;
        }
        throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
    }
    
    private void nextToken() throws ParseException, IOException {
        this.token = lexer.yylex();
        if (this.token == null) this.token = new Yytoken(TokenType.EOF, null);
    }
    
    private JSONObject createObjectContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) return new JSONObject();
        JSONObject m = containerFactory.createObjectContainer();
        if (m == null) return new JSONObject();
        return m;
    }
    
    private JSONArray createArrayContainer(ContainerFactory containerFactory) {
        if (containerFactory == null) return new JSONArray();
        JSONArray l = containerFactory.creatArrayContainer();
        if (l == null) return new JSONArray();
        return l;
    }
    
    /**
     * Get an JSON Object from string data (JSON formatted obviously). <p>
     * @param s - String where data is allocated (must be in JSON format)
     * @param contentHandler TODO <p>
     * @throws ParseException in case or any error at string s parameter JSON format.
     */
    public void parse(String s, ContentHandler contentHandler) throws ParseException {
        parse(s, contentHandler, false);
    }
    
    /**
     * Método usado para TODO <p>
     * @param s - String where data is allocated (must be in JSON format)
     * @param contentHandler TODO
     * @param isResume TODO
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public void parse(String s, ContentHandler contentHandler, boolean isResume) throws ParseException {
        StringReader in = new StringReader(s);
        try {
            parse(in, contentHandler, isResume);
        } catch (IOException ie) {
            /*
             * Actually it will never happen.
             */
            throw new ParseException(-1, ParseException.ERROR_UNEXPECTED_EXCEPTION, ie);
        }
    }
    
    /**
     * Método usado para TODO . <p>
     * @param in - type Reader object instance where data is stored.
     * @param contentHandler TODO
     * @throws IOException - In case of file read exception or similar.
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public void parse(Reader in, ContentHandler contentHandler) throws IOException, ParseException {
        parse(in, contentHandler, false);
    }
    
    /**
     * Stream processing of JSON text. <p>
     * @see ContentHandler <p>
     * @param in - type Reader object instance where data is stored.
     * @param contentHandler TODO
     * @param isResume - Indicates if it continues previous parsing operation. If set to true, resume parsing the old stream, and parameter 'in' will be ignored. If this method is called for the first time in this instance, isResume will be
     *            ignored. <p>
     * @throws IOException - In case of file read exception or similar
     * @throws ParseException - JSONParser will stop and throw the same exception to the caller when receiving this exception.
     */
    public void parse(Reader in, ContentHandler contentHandler, boolean isResume) throws IOException, ParseException {
        if (!isResume) {
            reset(in);
            handlerStatusStack = new LinkedList<ElementType>();
        } else {
            if (handlerStatusStack == null) {
                isResume = false;
                reset(in);
                handlerStatusStack = new LinkedList<ElementType>();
            }
        }
        LinkedList<ElementType> statusStack = handlerStatusStack;
        try {
            do {
                switch (status) {
                    case INIT:
                        contentHandler.startJSON();
                        nextToken();
                        switch (token.type) {
                            case VALUE:
                                status = ElementType.FINISHED_VALUE;
                                statusStack.addFirst(status);
                                if (!contentHandler.primitive(token.value)) return;
                                break;
                            case LEFT_BRACE:
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                if (!contentHandler.startObject()) return;
                                break;
                            case LEFT_SQUARE:
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                if (!contentHandler.startArray()) return;
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                        }// inner switch
                        break;
                    case FINISHED_VALUE:
                        nextToken();
                        if (token.type == TokenType.EOF) {
                            contentHandler.endJSON();
                            status = ElementType.END;
                            return;
                        } else {
                            status = ElementType.IN_ERROR;
                            throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                        }
                    case IN_OBJECT:
                        nextToken();
                        switch (token.type) {
                            case COMMA:
                                break;
                            case VALUE:
                                if (token.value instanceof String) {
                                    String key = (String) token.value;
                                    status = ElementType.PASSED_PAIR_KEY;
                                    statusStack.addFirst(status);
                                    if (!contentHandler.startObjectEntry(key)) return;
                                } else {
                                    status = ElementType.IN_ERROR;
                                }
                                break;
                            case RIGHT_BRACE:
                                if (statusStack.size() > 1) {
                                    statusStack.removeFirst();
                                    status = peekStatus(statusStack);
                                } else {
                                    status = ElementType.FINISHED_VALUE;
                                }
                                if (!contentHandler.endObject()) return;
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                                break;
                        }// inner switch
                        break;
                    case PASSED_PAIR_KEY:
                        nextToken();
                        switch (token.type) {
                            case COLON:
                                break;
                            case VALUE:
                                statusStack.removeFirst();
                                status = peekStatus(statusStack);
                                if (!contentHandler.primitive(token.value)) return;
                                if (!contentHandler.endObjectEntry()) return;
                                break;
                            case LEFT_SQUARE:
                                statusStack.removeFirst();
                                statusStack.addFirst(ElementType.PAIR_VALUE);
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                if (!contentHandler.startArray()) return;
                                break;
                            case LEFT_BRACE:
                                statusStack.removeFirst();
                                statusStack.addFirst(ElementType.PAIR_VALUE);
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                if (!contentHandler.startObject()) return;
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                        }
                        break;
                    case PAIR_VALUE:
                        /*
                         * IN_PAIR_VALUE is just a marker to indicate the end of an object entry, it doesn't process any token, therefore delay consuming token until next round.
                         */
                        statusStack.removeFirst();
                        status = peekStatus(statusStack);
                        if (!contentHandler.endObjectEntry()) return;
                        break;
                    case IN_ARRAY:
                        nextToken();
                        switch (token.type) {
                            case COMMA:
                                break;
                            case VALUE:
                                if (!contentHandler.primitive(token.value)) return;
                                break;
                            case RIGHT_SQUARE:
                                if (statusStack.size() > 1) {
                                    statusStack.removeFirst();
                                    status = peekStatus(statusStack);
                                } else {
                                    status = ElementType.FINISHED_VALUE;
                                }
                                if (!contentHandler.endArray()) return;
                                break;
                            case LEFT_BRACE:
                                status = ElementType.IN_OBJECT;
                                statusStack.addFirst(status);
                                if (!contentHandler.startObject()) return;
                                break;
                            case LEFT_SQUARE:
                                status = ElementType.IN_ARRAY;
                                statusStack.addFirst(status);
                                if (!contentHandler.startArray()) return;
                                break;
                            default:
                                status = ElementType.IN_ERROR;
                        }// inner switch
                        break;
                    case END:
                        return;
                    case IN_ERROR:
                        throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }// switch
                if (status == ElementType.IN_ERROR) {
                    throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
                }
            } while (token.type != TokenType.EOF);
        } catch (IOException ie) {
            status = ElementType.IN_ERROR;
            throw ie;
        } catch (ParseException pe) {
            status = ElementType.IN_ERROR;
            throw pe;
        } catch (RuntimeException re) {
            status = ElementType.IN_ERROR;
            throw re;
        } catch (Error e) {
            status = ElementType.IN_ERROR;
            throw e;
        }
        status = ElementType.IN_ERROR;
        throw new ParseException(getPosition(), ParseException.ERROR_UNEXPECTED_TOKEN, token);
    }
}
