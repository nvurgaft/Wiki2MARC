package com.protowiki.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.protowiki.dal.WikipediaRemoteAPIModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nick
 */
public class JerseyClientTest {

    private static final String TRAGET_URL = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=Potato";

    public static Logger logger = LoggerFactory.getLogger(JerseyClientTest.class);

    /**
     * Test HttpUrlConnection
     */
    @Test
    public void testHtppUrlConnection() {

        try {

            URL con = new URL(TRAGET_URL);

            HttpURLConnection request = (HttpURLConnection) con.openConnection();
            request.setRequestMethod("GET");

            int status = request.getResponseCode();
            System.out.println("Status: " + status);

            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                System.out.println("Response: " + sb.toString());
            }

            JsonObject jsonObject = jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();

            JsonObject jo = jsonObject.getAsJsonObject("query").getAsJsonObject("pages");

            JsonObject v = null;
            for (Entry<String, JsonElement> elem : jo.entrySet()) {
                v = (JsonObject) elem.getValue();
            }
            String extract = v.getAsJsonPrimitive("extract").toString();

            System.out.println("Extract: " + extract);

        } catch (MalformedURLException ex) {
            logger.error("MalformedURLException", ex);
        } catch (IOException ex) {
            logger.error("IOException", ex);
        }
    }

    @Test
    public void testMultipleAbstractRequests() {

        List<String> subjects = Arrays.asList("Stack Overflow", "Google", "Yahoo", "LinkedIn");

        WikipediaRemoteAPIModel model = new WikipediaRemoteAPIModel();

        List<String> s1 = model.getAbstractsByArticleNames("en", subjects);
        s1.forEach(e -> {
            logger.info("s: " + e);
        });
        
        List<String> heSubjects = Arrays.asList("נעמי שמר", "מנחם בגין", "בנימין זאב הרצל", "גולדה מאיר");
        List<String> s2 = model.getAbstractsByArticleNames("he", heSubjects);
        s2.forEach(e -> {
            logger.info("s: " + e);
        });
    }

    /**
     * Test jersey client
     */
    @Test
    public void testClientGET() {

        Configuration clientConfiguration = new ClientConfig().register(LoggingFilter.class);

        Client client = ClientBuilder.newClient(clientConfiguration);

        WebTarget target = client.target(TRAGET_URL);

        Invocation.Builder invocationBuilder = target.request(MediaType.TEXT_HTML);
        Response response = invocationBuilder.get();

        System.out.println("Status: " + response.getStatus());
    }
}
