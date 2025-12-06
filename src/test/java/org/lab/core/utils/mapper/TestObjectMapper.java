package org.lab.core.utils.mapper;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lab.domain.interfaces.DomainObject;
import org.lab.domain.interfaces.PresentationObject;

public class TestObjectMapper {
    private ObjectMapper mapper;

    interface TestDomain extends DomainObject {}
    interface TestPresentation extends PresentationObject {}

    static class DomainImpl implements TestDomain {
        public int id;
        public String name;

        public DomainImpl() {}
        public DomainImpl(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    static class PresentationImpl implements TestPresentation {
        public int id;
        public String name;

        public PresentationImpl() {}
        public PresentationImpl(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    void mapToDomain_shouldConvertPresentationToDomain() {
        PresentationImpl presentation = new PresentationImpl(1, "Alice");

        DomainImpl domain = mapper.mapToDomain(presentation, DomainImpl.class);

        assertEquals(1, domain.id);
        assertEquals("Alice", domain.name);
    }

    @Test
    void mapToPresentation_shouldConvertDomainToPresentation() {
        DomainImpl domain = new DomainImpl(2, "Bob");

        PresentationImpl presentation = mapper.mapToPresentation(domain, PresentationImpl.class);

        assertEquals(2, presentation.id);
        assertEquals("Bob", presentation.name);
    }

    @Test
    void mapFromRaw_shouldConvertMapToDomain() {
        Map<String, Object> raw = Map.of(
                "id", 3,
                "name", "Charlie"
        );

        DomainImpl domain = mapper.mapFromRaw(raw, DomainImpl.class);

        assertEquals(3, domain.id);
        assertEquals("Charlie", domain.name);
    }
}