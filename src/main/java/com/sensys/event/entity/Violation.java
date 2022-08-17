package com.sensys.event.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.geode.pdx.PdxReader;
import org.apache.geode.pdx.PdxSerializable;
import org.apache.geode.pdx.PdxWriter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Data
@Builder
@Region("Violations")
@NoArgsConstructor
public class Violation  implements PdxSerializable {

    @PersistenceConstructor
    public Violation(String id, String eventId, String fine, boolean paid) {
        this.id = id;
        this.eventId = eventId;
        this.fine = fine;
        this.paid = paid;
    }

    @Id
    private String id;

    private String eventId;

    private String fine;

    private boolean paid;

    @Override
    public void toData(PdxWriter pdxWriter) {
        pdxWriter.writeString("id", getId()).markIdentityField("id")
                .writeString("eventId", getEventId())
                .writeString("fine", getFine())
                .writeBoolean("paid", isPaid());
    }

    @Override
    public void fromData(PdxReader pdxReader) {
        this.setId( pdxReader.readString("id"));
        this.setEventId(pdxReader.readString("eventId"));
        this.setFine(pdxReader.readString("fine"));
        this.setPaid(pdxReader.readBoolean("paid"));
    }
}
