package com.protowiki.model;

import com.google.gson.JsonObject;
import com.protowiki.beans.Author;
import com.protowiki.utils.JsonUtils;
import com.protowiki.utils.StringLib;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This model is responsible for the interaction with the Wikipedia article
 * content API.
 *
 * We use this class to get article content such as the article's abstracts, as
 * long as an article exists.
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
                logger.debug("respBuff => {}", respBuff.toString());

                JsonObject inter = JsonUtils.parseToJsonObject(respBuff.toString());
                logger.debug("inter => {}", inter);
                if (inter != null) {
                    // clean the response string
                    String rawAbstract = JsonUtils.extractAbstractFromJson(inter);
                    if (!rawAbstract.isEmpty()) {
                        response = rawAbstract
                                .replace("\\n", "")
                                .replace("\n", " ")
                                .replace('\r', ' ')
                                .replace("\\", "")
                                .trim();
                    } else {
                        response = "N/A";
                    }
                    response = StringLib.trimQuotationMarks(response);
                } else {
                    response = "N/A";
                }

//                InputStream inputStream= new FileInputStream(file);
//            Reader reader = new InputStreamReader(inputStream, "UTF-8");
//
//            InputSource is = new InputSource(reader);
//            is.setEncoding("UTF-8");
                response = new String(response.getBytes("UTF-8")).trim();

                logger.debug("response => {}", response);
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

        if (language == null || language.isEmpty()) {
            logger.warn("No language token was provided");
            return null;
        }

        if (authors == null || authors.isEmpty()) {
            logger.warn("No article names were provided");
            return null;
        }

        return authors.stream()
                .map(author -> {
                    logger.debug("{} => {}", language, author.getNames().get(language));
                    String abs = this.getAbstractByArticleName(author.getNames().get(language), language);
                    return new ImmutablePair<>(author.getViafId(), abs);

                })
                .collect(Collectors.toMap(p -> p.getLeft(), p -> p.getRight()));
    }

}
