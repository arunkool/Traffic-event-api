package com.sensys.event;

import com.sensys.event.dto.EventDto;
import com.sensys.event.entity.Event;
import com.sensys.event.entity.FineSummary;
import com.sensys.event.entity.Violation;
import com.sensys.event.enums.EventType;
import com.sensys.event.repository.ViolationRepository;
import com.sensys.event.service.EventService;
import com.sensys.event.service.ViolationService;
import org.apache.geode.cache.DataPolicy;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@ContextConfiguration
@SuppressWarnings("unused")
@SpringBootTest
public class ViolationsTests {

    @Autowired
    private EventService eventService;

    @Autowired
    private ViolationService violationService;


    @MockBean
    private ViolationRepository violationRepository;

    @Autowired
    private GemFireCache gemfireCache;

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

        assertThat(this.violations).isNotNull();
        assertThat(this.violations).isSameAs(this.gemfireCache.getRegion("/Violations"));
        assertThat(this.violations.getRegionService()).isEqualTo(this.gemfireCache);
        assertThat(this.violations.getName()).isEqualTo("Violations");
        assertThat(this.violations.getFullPath()).isEqualTo("/Violations");
        assertThat(this.violations.getAttributes()).isNotNull();
        assertThat(this.violations.getAttributes().getDataPolicy()).isEqualTo(DataPolicy.EMPTY);

    }


    @Test
    public void payFineTest() {
        Violation violationBeforePay = Violation.builder()
                .id(UUID.randomUUID().toString())
                .eventId(event.getId())
                .fine("100")
                .paid(false)
                .build();
        Optional<Violation> optViolation = Optional.of(violationBeforePay);
        when(violationRepository.findById(violationBeforePay.getId())).thenReturn(optViolation);
        Violation violationAfterPaid = optViolation.get();
        violationAfterPaid.setPaid(true);
        when(violationRepository.save(violationBeforePay)).thenReturn(violationAfterPaid);
        violationService.payFine(violationBeforePay.getId());
        when(violationRepository.findAll()).thenReturn(Arrays.asList(violationAfterPaid));
        List<Violation> violations = violationService.getAllViolations();
        assertEquals(true,violations.get(0).isPaid());
    }

    @Test
    public void getSummaryTest() {
        Violation violation1 = Violation.builder()
                .id(UUID.randomUUID().toString())
                .eventId((UUID.randomUUID().toString()))
                .fine("100")
                .paid(false)
                .build();
        Violation violation2 = Violation.builder()
                .id(UUID.randomUUID().toString())
                .eventId((UUID.randomUUID().toString()))
                .fine("200")
                .paid(false)
                .build();
        when(violationRepository.findAll()).thenReturn(Arrays.asList(violation1,violation2));
        FineSummary summary = violationService.fineSummary();
        assertTrue(summary.getToBePaidFine().equals(300D));
        assertTrue(summary.getPaidFine().equals(0D));

    }

    @Configuration
    @EnableGemFireMockObjects
    @ClientCacheApplication(name = "SpringApacheGeodeConfiguration")
    @EnableEntityDefinedRegions(basePackageClasses = Violation.class)
    @EnableGemfireRepositories(basePackageClasses = ViolationRepository.class)
    @EnablePdx
    @SuppressWarnings("unused")
    @ComponentScan(basePackageClasses = ViolationService.class, basePackages = {"com.sensys.event.service", "com.sensys.event.advice"})

    static class TestGeodeConfig {
    }

}