package com.anik.app.core;

import com.anik.app.dto.UserResponseDTO;
import com.anik.app.exception.ResourceNotFoundException;
import com.anik.app.response.contract.ApiResponse;
import com.anik.app.response.success.ResponseUtils;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/demo")
@RequiredArgsConstructor
@Slf4j
public class DemoController {
    @GetMapping
    public ResponseEntity<ApiResponse> register() {
        Faker faker = new Faker();
        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
              .id(UUID.randomUUID())
              .version(1L)
              .createdAt(Instant.now())
              .updatedAt(Instant.now())
              .createdBy(UUID.randomUUID())
              .updatedBy(UUID.randomUUID())
              .firstName(faker.name().firstName())
              .lastName(faker.name().lastName())
              .phone(faker.phoneNumber().cellPhone())
              .email(faker.internet().emailAddress())
              .build();
        return ResponseUtils.buildResponse(HttpStatus.CREATED, userResponseDTO, "User Created");
    }

    @GetMapping("/test")
    public ResponseEntity<ApiResponse> login() {
        Faker faker = new Faker();
        List<UserResponseDTO> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                  .id(UUID.randomUUID())
                  .version(1L)
                  .createdAt(Instant.now())
                  .updatedAt(Instant.now())
                  .createdBy(UUID.randomUUID())
                  .updatedBy(UUID.randomUUID())
                  .firstName(faker.name().firstName())
                  .lastName(faker.name().lastName())
                  .phone(faker.phoneNumber().cellPhone())
                  .email(faker.internet().emailAddress())
                  .build();
            list.add(userResponseDTO);
        }

        Pageable pageable = PageRequest.of(0, 2);
        int start = pageable.getPageNumber() * pageable.getPageSize();
        Page<UserResponseDTO> page =
              PageableExecutionUtils.getPage(list.subList(start, pageable.getPageSize()), pageable, list::size);
        return ResponseUtils.buildPaginatedResponse(HttpStatus.OK, page, "Hello");
    }

    @GetMapping("/test1")
    public ResponseEntity<ApiResponse> test1() {
        return ResponseUtils.buildResponse("Only Message buildResponse");
    }

    @GetMapping("/test2")
    public ResponseEntity<ApiResponse> test2() {
        return ResponseUtils.buildResponse(HttpStatus.ACCEPTED,"with HttpStatus.ACCEPTED buildResponse");
    }

    @GetMapping("/test3")
    public ResponseEntity<ApiResponse> test3() {
        throw new ResourceNotFoundException();
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> errorTesting(@Valid @RequestBody UserResponseDTO userResponseDTO) {
        return ResponseUtils.buildResponse(HttpStatus.CREATED, userResponseDTO, "data created");
    }
}
