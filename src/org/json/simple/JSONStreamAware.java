package org.json.simple;

import java.io.IOException;
import java.io.Writer;

/**
 * Beans that support customized output of JSON text to a writer shall implement this interface. <p>
 * @author FangYidong<fangyidong@yahoo.com.cn>
 */
public interface JSONStreamAware {
    
    /**
     * write JSON string to out.
     * @param out TODO
     * @throws IOException TODO
     */
    void writeJSONString(Writer out) throws IOException;
}
