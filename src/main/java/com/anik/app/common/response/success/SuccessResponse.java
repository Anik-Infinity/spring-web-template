package com.anik.app.common.response.success;

import com.anik.app.common.response.BaseResponse;
import com.anik.app.common.response.contract.ApiSuccessResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
final public class SuccessResponse<T> extends BaseResponse implements ApiSuccessResponse {
    private T data;
}
