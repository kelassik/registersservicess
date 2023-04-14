package com.digital.solution.generalservice.utils;

import com.digital.solution.generalservice.exception.GenericException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.web.util.HtmlUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static com.digital.solution.generalservice.domain.constant.PatternDateConstant.PATTERN_DATE_NO_SEPARATOR;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_READ_FILE;
import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_WRITE_FILE;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
public class ReadAndWriteFileUtils {

    private static final String BASE_URL_TRANSACTION_FILE = "transaction.txt";

    public String readFile(String fileName) {
        log.info("start readFileTransaction");
        String request;
        try (var inputStream = new FileInputStream(fileName)){
            request = new String(inputStream.readAllBytes());
        } catch (IOException e) {
            log.error("Error IOException : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_READ_FILE);
        }

        if (isHtml(request)) {
            var doc = Jsoup.parse(request);
            request = doc.toString();
        }

        return request;
    }

    @SuppressWarnings("all")
    public String getTransactionId() {
        log.info("start getTransactionId");

        return getTransactionId(true);
    }

    public String getTransactionId(boolean increment) {
        String fileTransaction = readFile(BASE_URL_TRANSACTION_FILE);
        if (Boolean.FALSE.equals(increment)) {
            return fileTransaction;
        }

        LocalDate dateNow = LocalDate.now();
        AtomicInteger counterReference = new AtomicInteger(0);
        var dateNowString = dateNow.format(DateTimeFormatter.ofPattern(PATTERN_DATE_NO_SEPARATOR.getPattern()));
        var transactionDateString = fileTransaction.substring(2, 10);
        LocalDate transactionDate = LocalDate.parse(transactionDateString, DateTimeFormatter.ofPattern(PATTERN_DATE_NO_SEPARATOR.getPattern()));
        log.info("transaction date : {}", transactionDate);
        if (dateNow.equals(transactionDate)) {
            var transactionCounter = StringUtils.right(fileTransaction, 5);
            counterReference.set(Integer.parseInt(transactionCounter));
        }

        String counterString = StringUtils.leftPad(String.valueOf(counterReference.incrementAndGet()), 5, "0");
        var transactionId = StringUtils.join("RB", dateNowString, counterString);
        log.info("transactionId : {}", transactionId);
        writeFile(transactionId);

        return transactionId;
    }

    private void writeFile(String transactionId) {
        try (FileOutputStream outputStream = new FileOutputStream(BASE_URL_TRANSACTION_FILE)) {
            byte[] strToBytes = transactionId.getBytes();
            outputStream.write(strToBytes);
        } catch (IOException e) {
            log.error("Error IOException : {}", e.getMessage(), e);
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_WRITE_FILE);
        }
    }

    private boolean isHtml(String input) {
        return input != null && !input.equals(HtmlUtils.htmlEscape(input));
    }
}
