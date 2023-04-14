package com.digital.solution.generalservice.service.generatequery;

import com.digital.solution.generalservice.domain.dto.generatequery.UpdateFieldTableList;
import com.digital.solution.generalservice.domain.dto.generatequery.UpdateQueryRequest;
import com.digital.solution.generalservice.exception.GenericException;
import com.digital.solution.generalservice.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_REQUEST;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
@Service
public class UpdateQueryService extends BaseService {

    public String generateQueryUpdate(UpdateQueryRequest request) {
        log.info("start generateQueryUpdate");
        log.info("request data : {}", request);

        readAndWriteFileUtils.getTransactionId();
        var fieldTables = request.getFieldTables();
        if (CollectionUtils.isEmpty(fieldTables)) {
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_REQUEST);
        }

        StringBuilder query = new StringBuilder(StringUtils.join("UPDATE", StringUtils.SPACE));
        generateScriptQuerySchema(query, request.getSchemaName());
        query.append(StringUtils.join(request.getTableName(), StringUtils.SPACE, "SET", StringUtils.SPACE));
        generateSetQuery(query, fieldTables);
        generateWhereQuery(query, request);
        query.append(";");

        return query.toString();
    }

    private void generateSetQuery(StringBuilder query, List<UpdateFieldTableList> fieldTables) {
        var fieldTableLength = fieldTables.size();
        AtomicInteger index = new AtomicInteger(1);
        fieldTables.forEach(fieldTable -> {
            if (StringUtils.isNotEmpty(fieldTable.getFieldName()) && StringUtils.isNotEmpty(fieldTable.getValue())) {
                query.append(StringUtils.join(fieldTable.getFieldName(), StringUtils.SPACE, "='", fieldTable.getValue(), "'"));
            }

            if (index.get() < fieldTableLength) {
                query.append(",");
            }
            query.append(StringUtils.SPACE);
            index.set(index.incrementAndGet());
        });
    }

    private void generateWhereQuery(StringBuilder query, UpdateQueryRequest request) {
        var whereList = request.getWhereList();
        if (CollectionUtils.isNotEmpty(whereList)) {
            query.append(StringUtils.join("WHERE", StringUtils.SPACE));
            var whereTableLength = whereList.size();
            AtomicInteger index = new AtomicInteger(1);
            whereList.forEach(where -> {
                if (StringUtils.isNotEmpty(where.getFieldName()) && StringUtils.isNotEmpty(where.getValue())) {
                    query.append(StringUtils.join(where.getFieldName(), StringUtils.SPACE, "='", where.getValue(), "'"));
                }

                if (index.get() < whereTableLength) {
                    query.append(StringUtils.join(StringUtils.SPACE, "AND", StringUtils.SPACE));
                }
                index.set(index.incrementAndGet());
            });
        }
    }
}
