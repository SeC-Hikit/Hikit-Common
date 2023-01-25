package org.hikit.common.datasource;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

public interface Datasource {
    MongoClient getClient();
    MongoDatabase getDB();
    String getDBName();
}
