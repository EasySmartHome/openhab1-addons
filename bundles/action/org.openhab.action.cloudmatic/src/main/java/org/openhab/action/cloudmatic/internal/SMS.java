package org.openhab.action.cloudmatic.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HeaderElement;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMS {
    private static final String CLOUDMATIC_ENDPOINT = "https://www.cloudmatic.de/openhabapi.php";
    private static final String CLOUDMATIC_KEY = "edf801a0-f5a9-41f2-bd79-14bf58e3855b";
    private static final int HTTP_TIMEOUT = 2000;
    private static final int HTTP_RETRIES = 3;

    private static final Logger logger = LoggerFactory.getLogger(SMS.class);

    public static boolean send(String id, String username, String password, String phoneNumber, String message) {
        HttpClient client = new HttpClient();

        PostMethod postMethod = new PostMethod(CLOUDMATIC_ENDPOINT);
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setSoTimeout(HTTP_TIMEOUT);
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(HTTP_RETRIES, false));

        NameValuePair[] data = { new NameValuePair("action", "sms"), new NameValuePair("cmId", id),
                new NameValuePair("cmUsername", username), new NameValuePair("cmPassword", password),
                new NameValuePair("phoneNumber", phoneNumber), new NameValuePair("body", message),
                new NameValuePair("key", CLOUDMATIC_KEY) };

        postMethod.setRequestBody(data);

        try {
            int statusCode = client.executeMethod(postMethod);

            if (statusCode != HttpStatus.SC_OK) {
                logger.warn("Method failed: {}", postMethod.getStatusLine());
                return false;
            }

            InputStream tmpResponseStream = postMethod.getResponseBodyAsStream();
            Header encodingHeader = postMethod.getResponseHeader("Content-Encoding");
            if (encodingHeader != null) {
                for (HeaderElement ehElem : encodingHeader.getElements()) {
                    if (ehElem.toString().matches(".*gzip.*")) {
                        tmpResponseStream = new GZIPInputStream(tmpResponseStream);
                        logger.debug("GZipped InputStream from {}", CLOUDMATIC_ENDPOINT);
                    } else if (ehElem.toString().matches(".*deflate.*")) {
                        tmpResponseStream = new InflaterInputStream(tmpResponseStream);
                        logger.debug("Deflated InputStream from {}", CLOUDMATIC_ENDPOINT);
                    }
                }
            }

            String responseBody = IOUtils.toString(tmpResponseStream);
            if (!responseBody.isEmpty()) {
                logger.debug("Response body: {}", responseBody);
            }
            return true;
        } catch (HttpException e) {
            logger.warn("HTTP protocol violation: {}", e);
            return false;
        } catch (IOException e) {
            logger.warn("Transport error: {}", e);
            return false;
        } finally {
            postMethod.releaseConnection();
        }
    }
}
