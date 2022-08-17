package com.sensys.event;

import com.sensys.event.dto.EventDto;
import com.sensys.event.entity.Event;
import com.sensys.event.entity.Violation;
import com.sensys.event.enums.EventType;
import com.sensys.event.repository.EventRepository;
import com.sensys.event.service.EventService;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.mapping.MappingPdxSerializer;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;
import org.springframework.data.gemfire.tests.mock.annotation.EnableGemFireMockObjects;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
public class EventsTests {

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GemFireCache gemfireCache;

    @Resource(name = "Events")
    private Region<Long, Event> events;

    @Resource(name = "Violations")
    private Region<Long, Violation> violations;

    private final String id = UUID.randomUUID().toString();
    private final EventDto testEventDto = EventDto.builder()
            .eventDate(Calendar.getInstance().getTime())
            .eventType(EventType.SPEED)
            .licensePlate("ADD-2123")
            .limit("70")
            .speed("120")
            .unity("km/h").build();

    private final Event event = Event.builder()
            .id(id)
            .eventDate(Calendar.getInstance().getTime())
            .eventType(EventType.SPEED)
            .licensePlate("ADD-2123")
            .limit("70")
            .speed("120")
            .processed(false)
            .unity("km/h").build();

    @Test
    public void gemfireCacheIsAClientCache() {
        assertThat(this.gemfireCache).isInstanceOf(ClientCache.class);
    }

    @Test
    public void gemfireMemberNameIsCorrect() {
        assertThat(this.gemfireCache.getDistributedSystem().getProperties().get("name"))
                .isEqualTo("SpringApacheGeodeConfiguration");
    }

    @Test
    public void cachePdxSerializationConfigurationIsCorrect() {
        assertThat(this.gemfireCache.getPdxSerializer()).isInstanceOf(MappingPdxSerializer.class);
    }

    @Test
    public void EventsRegionExistsAndConfigurationIsCorrect() {

        assertThat(this.events).isNotNull();
        assertThat(this.events).isSameAs(this.gemfireCache.getRegion("/Events"));
        assertThat(this.events.getRegionService()).isEqualTo(this.gemfireCache);
        assertThat(this.events.getName()).isEqualTo("Events");
        assertThat(this.events.getFullPath()).isEqualTo("/Events");
        assertThat(this.events.getAttributes()).isNotNull();
        assertThat(this.events.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.EMPTY);

    }

    @Test
    public void saveEventServiceTest() {

        when(eventRepository.save(event)).thenReturn(event);
        assertEquals(id, eventService.saveEvent(id, testEventDto).getId());
    }

    @Test
    public void saveEventRepositoryTest() {
        Event savedEvent = this.eventRepository.save(event);
        assertThat(savedEvent).isNotNull();
        assertThat(savedEvent).isEqualTo(event);
    }


    @Configuration
    @EnableGemFireMockObjects
    @ClientCacheApplication(name = "SpringApacheGeodeConfiguration")
    @EnableEntityDefinedRegions(basePackageClasses = Event.class)
    @EnableGemfireRepositories(basePackageClasses = EventRepository.class)
    @EnablePdx
    @SuppressWarnings("unused")
    @ComponentScan(basePackages = {"com.sensys.event.service", "com.sensys.event.advice"})

    static class TestGeodeConfig {
    }

}