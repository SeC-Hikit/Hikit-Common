package org.hikit.common.datasource;

import org.bson.Document;
import org.hikit.common.data.mapper.EntityMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Component
public class DocumentListMapperHelper<T> {
    public List<T> toEntries(Iterable<Document> documents, EntityMapper<T> entityMapper) {
        return StreamSupport.stream(documents.spliterator(), false).map(entityMapper::mapToObject).collect(toList());
    }
}
