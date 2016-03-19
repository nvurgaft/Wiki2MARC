package com.protowiki.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.protowiki.beans.Author;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  This model is responsible for the interaction with the Wikipedia article 
 *  content API.
 * 
 *  We use this class to get article content such as the article's abstracts, 
 *  as long as an article exists.
 * 
 * @author Nick
 */
public class WikipediaRemoteAPIModel {

    public static Logger logger = LoggerFactory.getLogger(WikipediaRemoteAPIModel.class);

    /**
     *
     * @param language
     * @param articleName
     * @return
     */
    public String getAbstractByArticleName(String articleName, String language) {
        if (articleName == null || articleName.isEmpty()) {
            logger.warn("No article name was provided");
            return null;
        }
        
        if (language == null || language.isEmpty()) {
            logger.warn("No language tag was provided");
            return null;
        }

        // TODO: should find a better solution for url encoding but this should suffice for now
        articleName = articleName.replace(" ", "%20");

        StringBuilder sb = new StringBuilder();
        sb.append("https://")
                .append(language)
                .append(".wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&redirects=1&titles=")
                .append(articleName);
        String response = null;
        try {
            URL url = new URL(sb.toString());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");

            int status = request.getResponseCode();
            if (status >= 200 && status < 400) {
                StringBuilder respBuff = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        respBuff.append(line).append("\n");
                    }
                }
                JsonObject inter = this.parseToJsonObject(respBuff.toString());
                response = this.extractAbstractFromJson(inter);
            }
        } catch (MalformedURLException murlex) {
            logger.error("MalformedURLException occured while parsing URL string onto a URL object", murlex);
        } catch (IOException ioex) {
            logger.error("IOException occured while opening HTTP connection from the URL", ioex);
        }
        // TODO: finish
        return response;
    }

    /**
     *
     * @param authors
     * @param language
     * @return
     */
    public Map<String, String> getMultipleAbstractsByAuthors(List<Author> authors, String language) {

        if (language==null || language.isEmpty()) {
            logger.warn("No language token was provided");
            return null;
        }
        
        if (authors == null || authors.isEmpty()) {
            logger.warn("No article names were provided");
            return null;
        }
        
        List<String> articleNames = authors.stream().map(n -> {
            return n.getNames().get(language);
        }).collect(Collectors.toList());
        
        Map<String, String> resultMap = new HashMap<>();
        
        authors.stream().forEach(a -> {
            String abs = this.getAbstractByArticleName(language, a.getNames().get(language));
            resultMap.put(a.getViafId(), abs);
        });

        return resultMap;
    }
    
    /**
     * 
     * @param string
     * @return 
     */
    private JsonObject parseToJsonObject(String string) {
        return new JsonParser().parse(string).getAsJsonObject();
    }

    /**
     *  This method extracts the article content abstract from the response JSON
     * @param jsonObject
     * @return String object holding the abstract
     */
    private String extractAbstractFromJson(JsonObject jsonObject) {
        String result = null;
        try {
            JsonObject jo = jsonObject.getAsJsonObject("query").getAsJsonObject("pages");
            JsonObject v = null;
            for (Map.Entry<String, JsonElement> elem : jo.entrySet()) {
                v = (JsonObject) elem.getValue();
            }
            if (v != null) {
                result = v.getAsJsonPrimitive("extract").toString();
            }
        } catch (Exception ex) {
            logger.error("Exception occured while attempting to extract article abstract from result json", ex);
            return null;
        }
        return result;
    }
}
