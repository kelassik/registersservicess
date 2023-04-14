package com.digital.solution.generalservice.web;

import com.digital.solution.generalservice.domain.dto.GenerateToClobRequest;
import com.digital.solution.generalservice.service.GeneralService;
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
@RequestMapping(value = "/v1/general")
public class GeneralController {

    @Autowired
    private GeneralService generalService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to generate to clob")
    @PostMapping(value = "/generatetoclob", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String generateToClob(@Valid @RequestBody GenerateToClobRequest request) {
        return generalService.generateToClob(request);
    }
}
