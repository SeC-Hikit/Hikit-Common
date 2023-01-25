package org.hikit.common.data.mapper;

import org.bson.Document;

public interface EntityMapper<T> {
    T mapToObject(Document document);
    Document mapToDocument(T object);
}