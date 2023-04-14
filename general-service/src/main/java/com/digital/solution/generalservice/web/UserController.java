package com.digital.solution.generalservice.web;

import com.digital.solution.generalservice.domain.dto.user.CreateAuthenticationRequest;
import com.digital.solution.generalservice.domain.dto.user.EmailValidationRequest;
import com.digital.solution.generalservice.domain.dto.user.LoginRequest;
import com.digital.solution.generalservice.domain.dto.user.LoginResponse;
import com.digital.solution.generalservice.domain.dto.user.RegistrationRequest;
import com.digital.solution.generalservice.domain.dto.user.RegistrationResponse;
import com.digital.solution.generalservice.service.user.RegistrationService;
import com.digital.solution.generalservice.service.user.UserService;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Api
@Controller
@SuppressWarnings("all")
@RequestMapping(value = "/v1/user")
public class UserController extends BaseController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to registration user")
    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public RegistrationResponse registration(@Valid @RequestBody RegistrationRequest request, HttpServletRequest httpServletRequest) {
        return registrationService.userRegistration(request, generateMetadata(httpServletRequest));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to email validation")
    @PostMapping(value = "/emailvalidation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void emailValidation(@Valid @RequestBody EmailValidationRequest request, HttpServletRequest httpServletRequest) {
        registrationService.emailValidation(request, generateMetadata(httpServletRequest));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to create authentication")
    @PostMapping(value = "/createauthentication", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void createAuthentication(@Valid @RequestBody CreateAuthenticationRequest request, HttpServletRequest httpServletRequest) {
        registrationService.createAuthentication(request, generateMetadata(httpServletRequest));
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Perform to login")
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpServletRequest) {
        return userService.login(request, generateMetadata(httpServletRequest));
    }
}
