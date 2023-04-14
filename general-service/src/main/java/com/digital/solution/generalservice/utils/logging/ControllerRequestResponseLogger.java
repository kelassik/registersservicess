package com.digital.solution.generalservice.utils.logging;

import com.digital.solution.generalservice.utils.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Iterator;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.LINE_SEPARATOR_PROPERTY;

public class ControllerRequestResponseLogger extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ControllerRequestResponseLogger.class);
    private static final String LINE_SEPARATOR = System.getProperty(LINE_SEPARATOR_PROPERTY);

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper httpServletRequest, ContentCachingResponseWrapper httpServletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            afterRequest(httpServletRequest, httpServletResponse);
            httpServletResponse.copyBodyToResponse();
        }
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled()) {
            logRequest(request, request.getContentAsByteArray());
            logResponse(response);
        }
    }

    @SuppressWarnings("all")
    private void logRequest(ContentCachingRequestWrapper request, byte[] body) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR);
        sb.append("==========================INCOMING Request Begin============================").append(LINE_SEPARATOR);
        sb.append("URI          : {").append(request.getRequestURI()).append("}").append(LINE_SEPARATOR);
        sb.append("Method       : {").append(request.getMethod()).append("}").append(LINE_SEPARATOR);
        sb.append("Headers      : {[");
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            sb.append(headerName);
            sb.append(":\"");
            sb.append(request.getHeader(headerName));
            sb.append("\"");
            if (headers.hasMoreElements()) {
                sb.append(", ");
            }
        }
        sb.append("]}").append(LINE_SEPARATOR);

        sb.append("Request body : {");
        StringUtils.trimBase64String(sb, new String(body, StandardCharsets.UTF_8));
        sb.append("}").append(LINE_SEPARATOR);
        sb.append("==========================INCOMING Request End==============================");
        log.info(sb.toString());
    }

    @SuppressWarnings("all")
    private void logResponse(ContentCachingResponseWrapper response) {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE_SEPARATOR);
        sb.append("==========================INCOMING Response Begin===========================").append(LINE_SEPARATOR);
        sb.append("Status code  : {").append(response.getStatusCode()).append("}").append(LINE_SEPARATOR);

        sb.append("Headers      : {[");
        Iterator<String> headers = response.getHeaderNames().iterator();
        while (headers.hasNext()) {
            String headerName = headers.next();
            sb.append(headerName);
            sb.append(":\"");
            sb.append(response.getHeader(headerName));
            sb.append("\"");
            if (headers.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]}").append(LINE_SEPARATOR);

        if (response.getContentSize() > 0) {
            String contentType = response.getContentType();
            if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)
                    || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType)
                    || MediaType.APPLICATION_XML_VALUE.equals(contentType)) {
                sb.append("Response body: {");
                StringUtils.trimBase64String(sb, new String(response.getContentAsByteArray(), StandardCharsets.UTF_8));
                sb.append("}").append(LINE_SEPARATOR);
            }
        }
        sb.append("==========================INCOMING Response End=============================");
        log.info(sb.toString());
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }
}
