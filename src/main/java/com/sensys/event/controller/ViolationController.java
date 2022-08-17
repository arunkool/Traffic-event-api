package com.sensys.event.controller;

import com.sensys.event.entity.FineSummary;
import com.sensys.event.entity.Violation;
import com.sensys.event.service.ViolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/violations")
public class ViolationController {

    @Autowired
    ViolationService service;

    @GetMapping()
    public ResponseEntity<List<Violation>> getAllViolations(){
        return ResponseEntity.ok(service.getAllViolations());
    }
    @PatchMapping("/payFine/{violationId}")
    public ResponseEntity<String> payFine(@PathVariable String violationId){
        return ResponseEntity.ok(service.payFine(violationId));
    }

    @GetMapping("/summary")
    public ResponseEntity<FineSummary> getSummary(){
        return ResponseEntity.ok(service.fineSummary());
    }
}
