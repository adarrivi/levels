package com.levels.http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.concurrent.ThreadSafe;

import com.levels.exception.InvalidParameterException;
import com.levels.http.controller.HttpStringResponseController;
import com.sun.net.httpserver.HttpExchange;

/**
 * Class that retrieves from HttpExchange the url parameters and the post body
 * 
 * @author adarrivi
 * 
 */
@ThreadSafe
class HttpExchangeParameterHelper {

    public static final String URL_PARAMETERS_TAG = "parameters";
    public static final String POST_BODY_TAG = "postBody";

    private static final String PARAM_VALUE_SEPARATOR_REGEX = "[=]";
    private static final String URL_PARAMETER_SEPARATOR_REGEX = "[?]";
    private static final String FILE_ENCODING = System.getProperty("file.encoding");

    private ParameterVerifier parameterVerifier;

    HttpExchangeParameterHelper() {
        // Limiting scope, so it can be used only within server package
    }

    // Limiting scope, so it can be used only within server package
    void setParameterVerifier(ParameterVerifier parameterVerifier) {
        this.parameterVerifier = parameterVerifier;
    }

    public RequestParameter retrieveParameters(HttpExchange exchange) {
        try {
            RequestParameter parameter = new RequestParameter();
            parameter.setUrlParameters(retrieveUrlParametersMap(exchange));
            if (HttpStringResponseController.POST.equalsIgnoreCase(exchange.getRequestMethod())) {
                parameter.setPostBody(retrievePostBodyInteger(exchange));
            }
            parameter.setIntegerFromUrl(getIntegerFromURI(exchange.getRequestURI()));
            return parameter;
        } catch (IOException ex) {
            throw new InvalidParameterException("Error trying to retrieve the parameters from the request", ex);
        }

    }

    private Map<String, String> retrieveUrlParametersMap(HttpExchange exchange) throws UnsupportedEncodingException {
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        return parseQueryRetrievingUrlParams(query);
    }

    private Integer retrievePostBodyInteger(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        String postBodyAsString = br.readLine();
        Integer postBody = null;
        if (postBodyAsString != null) {
            postBody = parameterVerifier.getValueAsUnsignedInt(postBodyAsString);
        }
        return postBody;
    }

    private Map<String, String> parseQueryRetrievingUrlParams(String query) throws UnsupportedEncodingException {
        Map<String, String> parameters = new HashMap<>();
        if (query != null) {
            String pairs[] = query.split(URL_PARAMETER_SEPARATOR_REGEX);
            for (String pair : pairs) {
                String param[] = pair.split(PARAM_VALUE_SEPARATOR_REGEX);
                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], FILE_ENCODING);
                }
                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], FILE_ENCODING);
                }
                parameters.put(key, value);
            }
        }
        return parameters;
    }

    private int getIntegerFromURI(URI uri) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri.toString());
        matcher.find();
        return parameterVerifier.getValueAsUnsignedInt(matcher.group());
    }
}
