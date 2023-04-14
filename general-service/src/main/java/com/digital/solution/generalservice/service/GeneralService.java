package com.digital.solution.generalservice.service;

import com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant;
import com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant;
import com.digital.solution.generalservice.domain.dto.GenerateToClobRequest;
import com.digital.solution.generalservice.domain.dto.error.ErrorCodeResponse;
import com.digital.solution.generalservice.domain.entity.MasterErrorCode;
import com.digital.solution.generalservice.repository.MasterErrorCodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.digital.solution.generalservice.domain.constant.GeneralConstant.ERROR_MESSAGE_DEFAULT_ENG;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.ERROR_MESSAGE_DEFAULT_IDN;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.ERROR_TITLE_DEFAULT_ENG;
import static com.digital.solution.generalservice.domain.constant.GeneralConstant.ERROR_TITLE_DEFAULT_IDN;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
@Service
public class GeneralService extends BaseService {

    @Autowired
    @SuppressWarnings("all")
    private MasterErrorCodeRepository masterErrorCodeRepository;

    public String generateToClob(GenerateToClobRequest request) {
        log.info("start generateToClob");
        log.info("request data : {}", request);
        var resultReadFile = readAndWriteFileUtils.readFile(request.getFileName());
        if (StringUtils.isEmpty(resultReadFile)) {
            return resultReadFile;
        }

        StringBuilder result = new StringBuilder("TO_CLOB('");
        int intervalRequest = request.getIntervalToClob();
        int interval = intervalRequest;
        int index = 0;
        for (char data : resultReadFile.toCharArray()) {
            result.append(data);
            if (index == interval) {
                result.append("') || TO_CLOB('");
                interval += intervalRequest;
            }
            index++;
        }
        result.append("')");

        return result.toString();
    }

    public ErrorCodeResponse getErrorCode(@NotNull SourceSystemConstant sourceSystem, @NotNull ErrorCodeConstant errorCode) {
        String handlerInfo = "the error code is not yet in the database";
        var masterErrorCode = MasterErrorCode.builder()
                .sourceSystem(SOURCE_SYSTEM_CLICKS.getSourceSystem())
                .errorCode(errorCode.getErrorCode())
                .engTitle(ERROR_TITLE_DEFAULT_ENG)
                .indTitle(ERROR_TITLE_DEFAULT_IDN)
                .engMessage(ERROR_MESSAGE_DEFAULT_ENG)
                .indMessage(ERROR_MESSAGE_DEFAULT_IDN)
                .description(ERROR_MESSAGE_DEFAULT_ENG)
                .build();
        var masterErrorCodeOptional = masterErrorCodeRepository.findBySourceSystemAndErrorCode(sourceSystem.getSourceSystem(), errorCode.getErrorCode());
        if (masterErrorCodeOptional.isPresent()) {
            masterErrorCode = masterErrorCodeOptional.get();
            handlerInfo = null;
        }
        return ErrorCodeResponse.builder()
                .sourceSystem(masterErrorCode.getSourceSystem())
                .errorCode(masterErrorCode.getErrorCode())
                .engTittle(masterErrorCode.getEngTitle())
                .idnTittle(masterErrorCode.getIndTitle())
                .engMessage(masterErrorCode.getEngMessage())
                .idnMessage(masterErrorCode.getIndMessage())
                .description(masterErrorCode.getSourceSystem())
                .handlerInfo(handlerInfo)
                .build();
    }

    public static void main(String[] args) throws JsonProcessingException {
        String additionalData = "{\"accountName\":\"XTRA SAVERS IDR\",\"accountNumber\":\"703058330600\",\"accountType\":\"SDA\",\"srcAccountOwnerName\":\"CN000679 RICHIE DUA\",\"payeeCode\":\"806\",\"productNameEn\":\"Lion Air\",\"productNameId\":\"Lion Air\",\"amount\":200000.00,\"totalAmount\":200000.00,\"fee\":0,\"sourceOfFund\":\"703058330600\",\"transactionType\":\"ALL_BILLPAYMENT\",\"transactionId\":\"RB0330000251338\",\"esbCategoryName\":\"Airline Ticket Payment\",\"balance\":849700342530.27,\"cifNo\":\"11180000031815\",\"userId\":1084249,\"currency\":\"IDR\",\"keyValue\":\"77989797879\",\"customerNumber\":\"77989797879\",\"customerName\":\"WhatIsShungiteNoNotSugeKnighte\",\"pan\":\"95950001084249\",\"flightDetails\":[{\"cimbNumberOfCust\":1,\"cimbCarrier\":\"02\",\"cimbClassOfFlight\":\"1\",\"cimbFrom\":\"003\",\"cimbTo\":\"003\",\"cimbFlightNumber\":\"1111\",\"cimbDepatureDt\":\"12-12\",\"cimbDepatureTime\":\"07:07\"},{\"cimbNumberOfCust\":1,\"cimbCarrier\":\"02\",\"cimbClassOfFlight\":\"1\",\"cimbFrom\":\"003\",\"cimbTo\":\"003\",\"cimbFlightNumber\":\"1111\",\"cimbDepatureDt\":\"12-12\",\"cimbDepatureTime\":\"07:07\"},{\"cimbNumberOfCust\":1,\"cimbCarrier\":\"02\",\"cimbClassOfFlight\":\"1\",\"cimbFrom\":\"003\",\"cimbTo\":\"003\",\"cimbFlightNumber\":\"1111\",\"cimbDepatureDt\":\"12-12\",\"cimbDepatureTime\":\"07:07\"}],\"airlineCode\":9,\"airlineCode2\":1,\"flightNumber\":3,\"pnrCode\":\"123456789012345\",\"numberOfCust\":[1,1,1],\"billerCategoryId\":\"13\",\"paymentMethod\":{\"value\":\"0\",\"recurringDay\":null,\"recurringDayOfWeek\":null,\"recurringDayOfMonth\":null,\"recurringEnd\":null,\"amountLimit\":null},\"accountIndex\":0,\"fdsTransactionKey\":\"20230330162336000RB0330000251338\",\"accountAmount\":100000,\"schedule\":false,\"challenge\":false}";
        Map<String, Object> parameters = new ObjectMapper().readValue(additionalData, new TypeReference<>() {});
        System.out.println(parameters.get("flightDetails"));
    }
}
