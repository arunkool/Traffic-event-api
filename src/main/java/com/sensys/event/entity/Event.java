package com.sensys.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sensys.event.enums.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

import java.util.Date;

@Data
@Builder
@Region("Events")
@NoArgsConstructor
public class Event implements PdxSerializable {

    @PersistenceConstructor
    public Event(String id, Date eventDate, EventType eventType, String licensePlate, String speed, String limit, String unity, boolean processed) {
        this.id = id;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.licensePlate = licensePlate;
        this.speed = speed;
        this.limit = limit;
        this.unity = unity;
        this.processed = processed;
    }

    @Id
    private String id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "GMT+2")
    private Date eventDate;

    private EventType eventType;

    private String licensePlate;

    private String speed;

    private String limit;

    private String unity;

    private boolean processed;


    @Override
    public void toData(PdxWriter pdxWriter) {
        pdxWriter.writeString("id", getId()).markIdentityField("id")
                .writeDate("eventDate", getEventDate())
                .writeString("eventType", getEventType().getValue())
                .writeString("speed", getSpeed())
                .writeString("limit", getLimit())
                .writeString("licensePlate", getLicensePlate())
                .writeBoolean("processed", isProcessed())
                .writeString("unity", getUnity());
    }

    @Override
    public void fromData(PdxReader pdxReader) {
        this.setId( pdxReader.readString("id"));
        this.setEventDate(pdxReader.readDate("eventDate"));
        this.setEventType(EventType.fromValue(pdxReader.readString("eventType")));
        this.setSpeed(pdxReader.readString("speed"));
        this.setLimit(pdxReader.readString("limit"));
        this.setLicensePlate(pdxReader.readString("licensePlate"));
        this.setProcessed(pdxReader.readBoolean("processed"));
        this.setUnity(pdxReader.readString("unity"));
    }
}
