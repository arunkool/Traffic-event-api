package com.sensys.event.service;

import com.sensys.event.entity.FineSummary;
import com.sensys.event.entity.Violation;
import com.sensys.event.exception.ViolationNotFoundException;
import com.sensys.event.repository.ViolationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ViolationService {

    @Autowired
    private ViolationRepository violationRepository;

    @Async
    public Violation createViolation(String eventId, String fine) {
        Violation violation = violationRepository.save(Violation.builder()
                .id(UUID.randomUUID().toString())
                .eventId(eventId)
                .fine(fine)
                .paid(false)
                .build());

        return violation;
    }

    public List<Violation> getAllViolations() {
        return StreamSupport.stream(violationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public String payFine(String violationId) throws ViolationNotFoundException {
        Violation violation = violationRepository.findById(violationId).orElseThrow(() -> new ViolationNotFoundException("Violation not exist with id :" + violationId));
        violation.setPaid(true);
        violationRepository.save(violation);
        return "fine paid successfully !!";
    }

    public FineSummary fineSummary() {
        AtomicReference<Double> paid = new AtomicReference<>(0D);
        AtomicReference<Double> toBePaid = new AtomicReference<>(0D);
        violationRepository.findAll().forEach(v -> {
            if (v.isPaid()) {
                paid.updateAndGet(v1 -> v1 + Double.parseDouble(v.getFine()));
            } else {
                toBePaid.updateAndGet(v1 -> v1 + Double.parseDouble(v.getFine()));
            }
        });
        return FineSummary.builder()
                .paidFine(paid.get())
                .ToBePaidFine(toBePaid.get()).build();
    }

}
