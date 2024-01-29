package com.anik.app.common.response.error;

import com.anik.app.common.response.BaseResponse;
import com.anik.app.common.response.contract.ApiErrorResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import java.util.List;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
final public class ErrorResponse extends BaseResponse implements ApiErrorResponse {
    private String stackTrace;
    private List<ValidationError> fieldErrors;
}
