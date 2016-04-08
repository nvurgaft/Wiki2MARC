package com.protowiki.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class JsonUtils {
    
    public static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    
    /**
     * 
     * @param string
     * @return 
     */
    public static JsonObject parseToJsonObject(String string) {
        return new JsonParser().parse(string).getAsJsonObject();
    }

    /**
     *  This method extracts the article content abstract from the response JSON
     * @param jsonObject
     * @return String object holding the abstract
     */
    public static String extractAbstractFromJson(JsonObject jsonObject) {
        String result = null;
        try {
            JsonObject jo = jsonObject.getAsJsonObject("query").getAsJsonObject("pages");
            JsonObject v = null;
            for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
                v = (JsonObject) elem.getValue();
            }
            if (v != null && v.getAsJsonPrimitive("extract")!=null) {
                result = v.getAsJsonPrimitive("extract").toString();
            }
        } catch (Exception ex) {
            logger.error("Exception occured while attempting to extract article abstract from result json", ex);
            return null;
        }
        return result;
    }
}
