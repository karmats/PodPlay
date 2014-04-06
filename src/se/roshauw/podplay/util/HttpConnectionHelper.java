package se.roshauw.podplay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class to help with http connections.
 * 
 * @author mats
 * 
 */
public class HttpConnectionHelper {

    // Connection timeout 5 seconds
    private static final int CONNECTION_TIMEOUT = 5000;

    /**
     * Connect to a url, connection timeouts etc will be set here
     * 
     * @param url
     *            The url as string to connect to
     * @return
     */
    public HttpURLConnection connectToUrl(String url) {
        PodPlayUtil.logInfo("Connecting to " + url);
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            // Give it a chance to read response for two seconds
            connection.setReadTimeout(CONNECTION_TIMEOUT + 2000);
            return connection;
        } catch (Exception e) {
            PodPlayUtil.logError(e.getMessage());
        }
        return null;
    }

    /**
     * Reads a response from a url connection and return its string
     * 
     * @param connection
     *            The connection to read the response from
     * @return
     */
    public String readStreamFromConnection(HttpURLConnection connection) {
        BufferedReader reader = null;
        StringBuffer data = new StringBuffer("");
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            PodPlayUtil.logError(e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    PodPlayUtil.logError(e.getMessage());
                }
            }
        }
        return data.toString();
    }
}
