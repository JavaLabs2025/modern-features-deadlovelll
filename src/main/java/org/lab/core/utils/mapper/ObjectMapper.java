package org.lab.core.utils.mapper;

import org.lab.domain.interfaces.DomainObject;
import org.lab.domain.interfaces.PresentationObject;


public class ObjectMapper {

    public <T extends DomainObject> T mapToDomain(
            PresentationObject presentationObject,
            Class<T> domainClass
    ) {
        return null;
    }

    public <T extends PresentationObject> T mapToPresentation(
            DomainObject  domainObject,
            Class<T> presentationObjectClass
    ) {
        return null;
    }
}
