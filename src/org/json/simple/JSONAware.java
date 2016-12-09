package org.json.simple;

/**
 * Beans that support customized output of JSON text shall implement this interface. <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 * @author Daniel J. Umpiérrez
 */
public interface JSONAware {
    
    /**
     * @return JSON text
     */
    String toJSONString();
}
