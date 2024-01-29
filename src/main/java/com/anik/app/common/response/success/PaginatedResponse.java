package com.anik.app.common.response.success;

import com.anik.app.common.response.BaseResponse;
import com.anik.app.common.response.contract.ApiSuccessResponse;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Getter
@SuperBuilder
final public class PaginatedResponse<T> extends BaseResponse implements ApiSuccessResponse {
    private Integer size;
    private Integer page;
    private Integer totalPages;
    private Long totalRecords;
    private Collection<T> data;
}
