package com.protowiki.dal;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
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
    public String getAbstractByArticleName(String language, String articleName) {
        if (articleName == null || articleName.isEmpty()) {
            return null;
        }

        // TODO: should find a better solution for url encoding but this should suffice for now
        articleName = articleName.replace(" ", "%20");

        StringBuilder sb = new StringBuilder();
        sb.append("https://")
                .append(language)
                .append(".wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=")
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
     * @param language
     * @param articleNames
     * @return
     */
    public List<String> getAbstractsByArticleNames(String language, List<? extends String> articleNames) {

        if (language==null || language.isEmpty()) {
            logger.warn("No language token was provided");
            return null;
        }
        
        if (articleNames == null || articleNames.isEmpty()) {
            logger.warn("No article names were provided");
            return null;
        }

        List<String> abstracts = articleNames.stream().map(s -> {
                return this.getAbstractByArticleName(language, s);
        }).collect(Collectors.toList());

        return abstracts;
    }

    private JsonObject parseToJsonObject(String string) {
        return new JsonParser().parse(string).getAsJsonObject();
    }

    /**
     *
     * @param jsonObject
     * @return
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
            } else {
                return null;
            }
        } catch (Exception ex) {
            logger.error("Exception occured while attempting to extract article abstract from result json", ex);
            return null;
        }
        return result;
    }
}
