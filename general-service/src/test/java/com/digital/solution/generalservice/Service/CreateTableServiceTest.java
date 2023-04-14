package com.digital.solution.generalservice.Service;

import com.digital.solution.generalservice.domain.dto.generatequery.CreateQueryRequest;
import com.digital.solution.generalservice.exception.GenericException;
import com.digital.solution.generalservice.service.generatequery.CreateTableService;
import com.digital.solution.generalservice.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@Slf4j
@RunWith(value = SpringRunner.class)
@ContextConfiguration(classes = {CreateTableService.class, ObjectMapper.class})
public class CreateTableServiceTest {

    @Autowired
    @SuppressWarnings("all")
    private CreateTableService createTableService;

    @MockBean
    @SuppressWarnings("all")
    private ObjectMapperUtils objectMapperUtils;

    @Autowired
    @SuppressWarnings("all")
    private ObjectMapper objectMapper;

    private static final String BASE_URL_CREATE_SCRIPT_QUERY = "createscriptquery/";
    private static final String CREATE_SCRIPT_QUERY_REQUEST = BASE_URL_CREATE_SCRIPT_QUERY.concat("RequestSuccess.json");
    private static final String CREATE_SCRIPT_QUERY_RESPONSE = BASE_URL_CREATE_SCRIPT_QUERY.concat("Response.txt");

    private CreateQueryRequest getCreateQueryRequest() {
        var request = CreateQueryRequest.builder().build();
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(CREATE_SCRIPT_QUERY_REQUEST)){
            if (inputStream != null) {
                request = objectMapper.readValue(inputStream.readAllBytes(), CreateQueryRequest.class);
            }
        } catch (IOException e) {
            log.info("Error IOException : {}", e.getMessage(), e);
        }

        return request;
    }

    @SuppressWarnings("all")
    private String getCreateQueryResponse() {
        String response = null;
        try (var inputStream = getClass().getClassLoader().getResourceAsStream(CREATE_SCRIPT_QUERY_RESPONSE)){
            if (inputStream != null) {
                response = new String(inputStream.readAllBytes());
            }
        } catch (IOException e) {
            log.info("Error IOException : {}", e.getMessage(), e);
        }

        return response;
    }

    @Test
    public void makeCreateQueryTestWithOnePrimaryKey_ExpectSuccess() {
        var request = getCreateQueryRequest();
        var result = createTableService.generateQueryCreateTable(request);
        assertNotNull(result);
    }

    @Test(expected = GenericException.class)
    public void makeCreateQueryTestWithTwoPrimaryKey_ExpectErrorGeneral() {
        var request = getCreateQueryRequest();
        request.getFieldTables().get(1).setPrimaryKey(true);
        createTableService.generateQueryCreateTable(request);
    }

    @Test(expected = GenericException.class)
    public void makeCreateQueryWithFieldTableListEmpty_ExpectErrorGeneral() {
        var request = getCreateQueryRequest();
        request.setFieldTables(new ArrayList<>());
        createTableService.generateQueryCreateTable(request);
    }

    @Test
    public void makeCreateQueryTestWithPrimaryKeyEmpty_ExpectSuccess() {
        var request = getCreateQueryRequest();
        request.getFieldTables().get(0).setPrimaryKey(false);
        var result = createTableService.generateQueryCreateTable(request);
        assertNotNull(result);
    }

    @Test
    public void makeCreateQueryTestWithAllCommentFiledEmpty_ExpectSuccess() {
        var request = getCreateQueryRequest();
        request.getFieldTables().forEach(fieldTable -> fieldTable.setComment(null));
        var result = createTableService.generateQueryCreateTable(request);
        assertNotNull(result);
    }

    @Test
    public void makeCreateQueryTestWithAllNullableTrue_ExpectSuccess() {
        var request = getCreateQueryRequest();
        request.getFieldTables().forEach(fieldTable -> fieldTable.setNullable(true));
        var result = createTableService.generateQueryCreateTable(request);
        assertNotNull(result);
    }

    @Test
    public void makeCreateQueryTestWithSchemaTableEmpty_ExpectSuccess() {
        var request = getCreateQueryRequest();
        request.setSchemaName(null);
        var result = createTableService.generateQueryCreateTable(request);
        assertNotNull(result);
    }
}
