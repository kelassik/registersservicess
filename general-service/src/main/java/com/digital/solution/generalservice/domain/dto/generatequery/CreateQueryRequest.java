package com.digital.solution.generalservice.domain.dto.generatequery;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateQueryRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = -8985485077943438783L;

    @NotNull
    private List<CreateFieldTableList> fieldTables;
}
