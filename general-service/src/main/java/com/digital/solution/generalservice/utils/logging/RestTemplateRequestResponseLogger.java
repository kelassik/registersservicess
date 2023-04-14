package com.digital.solution.generalservice.utils.logging;

import com.digital.solution.generalservice.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.LINE_SEPARATOR_PROPERTY;

public class RestTemplateRequestResponseLogger implements ClientHttpRequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RestTemplateRequestResponseLogger.class);
    private static final String LINE_SEPARATOR = System.getProperty(LINE_SEPARATOR_PROPERTY);

    @Override
    public @NotNull ClientHttpResponse intercept(@NotNull HttpRequest request, byte @NotNull [] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(LINE_SEPARATOR);
            sb.append("==========================OUTGOING Request Begin============================").append(LINE_SEPARATOR);
            sb.append("URI          : {").append(request.getURI()).append("}").append(LINE_SEPARATOR);
            sb.append("Method       : {").append(request.getMethod()).append("}").append(LINE_SEPARATOR);
            sb.append("Headers      : {").append(request.getHeaders()).append("}").append(LINE_SEPARATOR);

            sb.append("Request body : {");
            StringUtils.trimBase64String(sb, new String(body, StandardCharsets.UTF_8));
            sb.append("}").append(LINE_SEPARATOR);
            sb.append("==========================OUTGOING Request End==============================");
            log.debug(sb.toString());
        }
    }

    @SuppressWarnings("deprecation")
    private void logResponse(ClientHttpResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(LINE_SEPARATOR);
            sb.append("==========================OUTGOING Response Begin===========================").append(LINE_SEPARATOR);
            sb.append("Status code  : {").append(response.getStatusCode()).append("}").append(LINE_SEPARATOR);
            sb.append("Status text  : {").append(response.getStatusText()).append("}").append(LINE_SEPARATOR);
            sb.append("Headers      : {").append(response.getHeaders()).append("}").append(LINE_SEPARATOR);

            if (response.getHeaders().getContentLength() > 0) {
                MediaType contentType=response.getHeaders().getContentType();
                if (MediaType.APPLICATION_JSON.equals(contentType) || MediaType.APPLICATION_JSON_UTF8.equals(contentType) || MediaType.APPLICATION_XML.equals(contentType)) {
                    sb.append("Response body: {");
                    StringUtils.trimBase64String(sb, StreamUtils.copyToString(response.getBody(), Charset.defaultCharset()));
                    sb.append("}").append(LINE_SEPARATOR);
                }
            }
            sb.append("==========================OUTGOING Response End=============================");
            log.debug(sb.toString());
        }
    }
}
