package com.sensys.event.service;

import com.sensys.event.dto.EventDto;
import com.sensys.event.entity.Event;
import com.sensys.event.enums.EventType;
import com.sensys.event.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Map.entry;

@Service
public class EventService {

    Logger log = LoggerFactory.getLogger(EventService.class);


    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ViolationService violationService;

    private Map<EventType, String> violationMap = Map.ofEntries(
            entry(EventType.RED_LIGHT, "100"),
            entry(EventType.SPEED, "50")
    );

    @CachePut(cacheNames = "Events", key = "#id")
    public  Event saveEvent(String id, EventDto dto) {
        Event event = Event.builder()
                .id(id)
                .eventDate(dto.getEventDate())
                .eventType(dto.getEventType())
                .licensePlate(dto.getLicensePlate())
                .limit(dto.getLimit())
                .speed(dto.getSpeed())
                .processed(false)
                .unity(dto.getUnity()).build();
        event = eventRepository.save(event);

        if (violationMap.containsKey(event.getEventType())) {
            violationService.createViolation(event.getId(),
                    violationMap.get(event.getEventType()));
        }

        event.setProcessed(true);
        eventRepository.save(event);
        return event;
    }

}
