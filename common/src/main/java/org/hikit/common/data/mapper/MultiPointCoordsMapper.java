package org.hikit.common.data.mapper;

import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MultiPointCoordsMapper implements EntityMapper<MultiPointCoords2D> {
    public static final int LONG_INDEX = 0;
    public static final int LAT_INDEX = 1;

    public static final String MULTIPOINT_GEO_JSON = "MultiPoint";

    @Override
    public MultiPointCoords2D mapToObject(Document document) {
        final List<List> listOfCoords = document.getList(MultiPointCoords2D.COORDINATES, List.class);
        final List<List<Double>> constructedPairs = new ArrayList<>();
        listOfCoords.forEach(pair-> constructedPairs.add(Arrays.asList(
                (Double) pair.get(LONG_INDEX),
                (Double) pair.get(LAT_INDEX))));
        return new MultiPointCoords2D(constructedPairs);
    }

    @Override
    public Document mapToDocument(MultiPointCoords2D object) {
        return new Document(MultiPointCoords2D.TYPE, MULTIPOINT_GEO_JSON)
                .append(MultiPointCoords2D.COORDINATES, object.getCoordinates2D());
    }
}
