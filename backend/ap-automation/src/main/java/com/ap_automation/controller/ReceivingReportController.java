package com.ap_automation.controller;

import com.ap_automation.dto.request.ReceivingReportRequest;
import com.ap_automation.dto.response.ReceivingReportResponse;
import com.ap_automation.service.ReceivingReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receiving-reports")
@RequiredArgsConstructor
public class ReceivingReportController {

    private final ReceivingReportService receivingReportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReceivingReportResponse create(
            @Valid @RequestBody ReceivingReportRequest request) {

        return receivingReportService.create(request);

    }

    @GetMapping("/{id}")
    public ReceivingReportResponse getById(
            @PathVariable Long id) {

        return receivingReportService.getById(id);

    }

    @GetMapping
    public List<ReceivingReportResponse> getAll() {

        return receivingReportService.getAll();

    }

    @PutMapping("/{id}")
    public ReceivingReportResponse update(
            @PathVariable Long id,
            @Valid @RequestBody ReceivingReportRequest request) {

        return receivingReportService.update(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id) {

        receivingReportService.delete(id);

    }

}