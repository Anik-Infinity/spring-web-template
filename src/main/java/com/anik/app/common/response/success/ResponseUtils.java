package com.anik.app.common.response.success;

import com.anik.app.common.dto.BaseDTO;
import com.anik.app.common.response.contract.ApiResponse;
import com.anik.app.common.response.contract.ApiSuccessResponse;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

final public class ResponseUtils implements ApiSuccessResponse {
    private ResponseUtils() {
    }

    public static ResponseEntity<ApiResponse> buildResponse(String message) {
        return buildResponseEntity(HttpStatus.OK, null, message);
    }

    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatus,
                                                            String message) {
        return buildResponseEntity(httpStatus, null, message);
    }

    public static <T extends BaseDTO> ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatus,
                                                                                T data,
                                                                                String message) {
        return buildResponseEntity(httpStatus, data, message);
    }

    public static <T extends BaseDTO> ResponseEntity<ApiResponse> buildPaginatedResponse(HttpStatus httpStatus,
                                                                                         Page<T> page,
                                                                                         String message) {
        return buildPaginatedResponseEntity(httpStatus, page, message);
    }

    private static <T extends BaseDTO> ResponseEntity<ApiResponse> buildResponseEntity(HttpStatus httpStatus,
                                                                                       @Nullable T data,
                                                                                       String message) {
        return ResponseEntity.status(httpStatus.value())
              .body(SuccessResponse.<T>builder()
                    .status(httpStatus.value())
                    .title(httpStatus.getReasonPhrase())
                    .message(message)
                    .data(data)
                    .build());

    }

    private static <T extends BaseDTO> ResponseEntity<ApiResponse> buildPaginatedResponseEntity(HttpStatus httpStatus,
                                                                                                Page<T> page,
                                                                                                String message) {
        return ResponseEntity.status(httpStatus.value())
              .body(PaginatedResponse.<T>builder()
                    .status(httpStatus.value())
                    .title(httpStatus.getReasonPhrase())
                    .message(message)
                    .page(page.getPageable().getPageNumber())
                    .size(page.getSize())
                    .totalPages(page.getTotalPages())
                    .totalRecords(page.getTotalElements())
                    .data(page.getContent())
                    .build());

    }
}
