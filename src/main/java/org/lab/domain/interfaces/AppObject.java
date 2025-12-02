package org.lab.domain.interfaces;

sealed interface AppObject
        permits DomainObject,
        PresentationObject {
}
