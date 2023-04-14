package com.digital.solution.generalservice.domain.dto.generatequery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateQueryRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = -3460129962374437746L;

    private List<UpdateFieldTableList> fieldTables;
    private List<UpdateFieldTableList> whereList;
}
