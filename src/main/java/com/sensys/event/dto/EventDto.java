package com.sensys.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sensys.event.enums.EventType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Builder
public class EventDto {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "GMT+2")
    private Date eventDate;

    @NotNull
    private EventType eventType;

    @NotNull
    private String licensePlate;

    private String speed;

    private String limit;

    private String unity;

}
