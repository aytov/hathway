package com.hathway.androidinterviewquestion.support;

import com.hathway.androidinterviewquestion.ConnectionRepository;

public class InMemoryConnectionRepositoryImpl implements ConnectionRepository {
    private static String connection;

    @Override
    public void setConnection(String connection) {
        this.connection = connection;
    }

    @Override
    public String getConnection() {
        return connection;
    }

    @Override
    public void removeConnection() {
        connection = null;
    }
}
