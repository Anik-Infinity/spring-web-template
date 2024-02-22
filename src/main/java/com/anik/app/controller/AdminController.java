package com.anik.app.controller;

import com.anik.app.response.contract.ApiResponse;
import com.anik.app.response.success.ResponseUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "admin-demo-controller", description = "Only accessible for ADMIN. restricted for MANAGER and USER")
public class AdminController {

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse> get() {
        return ResponseUtils.buildResponse("GET:: admin controller");
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
//    @Hidden
    public ResponseEntity<ApiResponse> post() {
        return ResponseUtils.buildResponse("POST:: admin controller");
    }
    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
//    @Hidden
    public ResponseEntity<ApiResponse> put() {
        return ResponseUtils.buildResponse("PUT:: admin controller");
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
//    @Hidden
    public ResponseEntity<ApiResponse> delete() {
        return ResponseUtils.buildResponse("DELETE:: admin controller");
    }
}
