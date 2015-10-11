package com.hathway.androidinterviewquestion;

import android.app.Application;

import com.getinch.retrogram.Instagram;
import com.hathway.androidinterviewquestion.support.InMemoryConnectionRepositoryImpl;

public class MainApplication extends Application {
    private ConnectionRepository connectionRepository;
    private Instagram instagramService;

    @Override
    public void onCreate() {
        super.onCreate();
        connectionRepository = new InMemoryConnectionRepositoryImpl();
    }

    public ConnectionRepository getConnectionRepository() {
        return connectionRepository;
    }

    public void setInstagramService(Instagram instagramService) {
        this.instagramService = instagramService;
    }

    public Instagram getInstagramService() {
        return instagramService;
    }
}
