package org.lab.core.utils.mapper;

import org.lab.domain.interfaces.DomainObject;
import org.lab.domain.interfaces.PresentationObject;


public class ObjectMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper mapper =
            new com.fasterxml.jackson.databind.ObjectMapper();

    public <T extends DomainObject> T mapToDomain(
            PresentationObject presentationObject,
            Class<T> domainClass
    ) {
        return mapper.convertValue(presentationObject, domainClass);
    }

    public <T extends PresentationObject> T mapToPresentation(
            DomainObject  domainObject,
            Class<T> presentationObjectClass
    ) {
        return mapper.convertValue(domainObject, presentationObjectClass);
    }

    public <T extends DomainObject> T mapFromRaw(
            Object rawObject,
            Class<T> domainClass
    ) {
        return mapper.convertValue(rawObject, domainClass);
    }
}
