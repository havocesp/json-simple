package org.json.simple.parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Container factory for creating containers for JSON object and JSON array. <p>
 * @see JSONParser#parse(java.io.Reader, ContainerFactory) <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public interface ContainerFactory {
    
    /**
     * @return A Map instance to store JSON object, or null if you want to use org.json.simple.JSONObject.
     */
    JSONObject createObjectContainer();
    
    /**
     * @return A List instance to store JSON array, or null if you want to use org.json.simple.JSONArray.
     */
    JSONArray creatArrayContainer();
}
