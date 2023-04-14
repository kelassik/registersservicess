package com.digital.solution.generalservice.web;

import com.digital.solution.generalservice.domain.dto.generatequery.CreateQueryRequest;
import com.digital.solution.generalservice.domain.dto.generatequery.UpdateQueryRequest;
import com.digital.solution.generalservice.service.generatequery.CreateTableService;
import com.digital.solution.generalservice.service.generatequery.UpdateQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;

@Api
@Controller
@SuppressWarnings("all")
@RequestMapping(value = "/v1/createquery")
public class CreateQueryController {

    @Autowired
    private CreateTableService createTableService;

    @Autowired
    private UpdateQueryService updateQueryService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to create table script query")
    @PostMapping(value = "/createtablescriptquery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String createTableScriptQuery(@Valid @RequestBody CreateQueryRequest request) {
        return createTableService.generateQueryCreateTable(request);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to update script query")
    @PostMapping(value = "/updatetablescriptquery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String updateTableScriptQuery(@Valid @RequestBody UpdateQueryRequest request) {
        return updateQueryService.generateQueryUpdate(request);
    }
}
