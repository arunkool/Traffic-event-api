package com.sensys.event.controller;

import com.sensys.event.dto.EventDto;
import com.sensys.event.entity.Event;
import com.sensys.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/save")
    public Event saveAndProcessEvent(@RequestBody @Valid EventDto eventDto) {

        return eventService.saveEvent((UUID.randomUUID().toString()),eventDto);
    }

}
