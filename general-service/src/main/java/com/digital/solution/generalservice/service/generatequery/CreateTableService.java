package com.digital.solution.generalservice.service.generatequery;

import com.digital.solution.generalservice.domain.dto.generatequery.CreateQueryRequest;
import com.digital.solution.generalservice.domain.dto.generatequery.CreateFieldTableList;
import com.digital.solution.generalservice.exception.GenericException;
import com.digital.solution.generalservice.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.digital.solution.generalservice.domain.constant.error.ErrorCodeConstant.ERROR_CODE_INVALID_REQUEST;
import static com.digital.solution.generalservice.domain.constant.error.SourceSystemConstant.SOURCE_SYSTEM_CLICKS;

@Slf4j
@Service
public class CreateTableService extends BaseService {

    public String generateQueryCreateTable(CreateQueryRequest request) {
        log.info("start makeCreateQuery");
        log.info("request data : {}", request);

        readAndWriteFileUtils.getTransactionId();
        var fieldTables = request.getFieldTables();
        validationRequestCreateQuery(fieldTables);

        StringBuilder query = new StringBuilder(StringUtils.join("CREATE TABLE", StringUtils.SPACE));
        generateScriptQuerySchema(query, request.getSchemaName());

        StringBuilder commentFiledName = new StringBuilder();
        StringBuilder fieldPrimaryKey = new StringBuilder();
        query.append(StringUtils.join(request.getTableName().toUpperCase(), "(\n"));
        fieldTables.forEach(fieldTable -> {
            getFieldPrimaryKey(fieldPrimaryKey, fieldTable);
            generateScriptQueryCommentField(commentFiledName, request, fieldTable);
            generateQueryCreateFiled(query, fieldTable);
        });

        generateScriptQueryPrimaryKey(query, fieldPrimaryKey, request);
        query.append(StringUtils.join(");\n\n", commentFiledName.toString()));
        var result = query.toString();
        log.info("result : {}", result);
        return result;
    }

    private void validationRequestCreateQuery(List<CreateFieldTableList> fieldTables) {
        if (CollectionUtils.isEmpty(fieldTables)) {
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_REQUEST);
        }

        var totalPrimary = fieldTables.stream().filter(CreateFieldTableList::isPrimaryKey).count();
        if (totalPrimary > 1) {
            throw new GenericException(SOURCE_SYSTEM_CLICKS, ERROR_CODE_INVALID_REQUEST);
        }
    }

    private void getFieldPrimaryKey(StringBuilder fieldPrimaryKey, CreateFieldTableList fieldTable) {
        if (Boolean.TRUE.equals(fieldTable.isPrimaryKey())) {
            fieldPrimaryKey.append(fieldTable.getFiledName());
        }
    }

    private void generateScriptQueryCommentField(StringBuilder commentFiledName, CreateQueryRequest request, CreateFieldTableList fieldTable) {
        if (StringUtils.isNotEmpty(fieldTable.getComment())) {
            commentFiledName.append(StringUtils.join("COMMENT ON COLUMN", StringUtils.SPACE));
            if (StringUtils.isNotEmpty(request.getSchemaName())) {
                commentFiledName.append(StringUtils.join(request.getSchemaName(), "."));
            }
            commentFiledName.append(StringUtils.join(request.getTableName(), ".", fieldTable.getFiledName(), StringUtils.SPACE, "IS", StringUtils.SPACE, "'", fieldTable.getComment(), "';\n"));
        }
    }

    private void generateScriptQueryPrimaryKey(StringBuilder query, StringBuilder fieldPrimaryKey, CreateQueryRequest request) {
        var fieldName = fieldPrimaryKey.toString();
        if (StringUtils.isNotEmpty(fieldName)) {
            query.append(StringUtils.join("CONSTRAINT", StringUtils.SPACE, request.getTableName(), "_PK PRIMARY KEY (\"", fieldName, "\")\n"));
        } else {
            query.deleteCharAt(query.length() - 2);
        }
    }

    private void generateQueryCreateFiled(StringBuilder query, CreateFieldTableList fieldTable) {
        query.append(StringUtils.join("\"", fieldTable.getFiledName(), "\"", StringUtils.SPACE, fieldTable.getTypeData(), "(", fieldTable.getLength()));
        if (StringUtils.isNotEmpty(fieldTable.getSecondLength())) {
            if (NumberUtils.isCreatable(fieldTable.getSecondLength())) {
                query.append(",");
            }
            query.append(StringUtils.join(StringUtils.SPACE, fieldTable.getSecondLength()));
        }
        query.append(")");

        generateScriptQueryNotNull(query, fieldTable);
        query.append(",\n");
    }

    private void generateScriptQueryNotNull(StringBuilder query, CreateFieldTableList fieldTable) {
        if (Boolean.FALSE.equals(fieldTable.isNullable())) {
            query.append(StringUtils.join(StringUtils.SPACE, "NOT NULL ENABLE"));
        }
    }
}
