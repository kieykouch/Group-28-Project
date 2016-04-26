package com.group28.cs160.noms4two;

import com.github.scribejava.core.builder.api.DefaultApi10a;
import com.github.scribejava.core.model.OAuth1RequestToken;

/**
 * Created by Haojun on 4/20/16.
 */
public class YelpApi extends DefaultApi10a {
//    private static final String AUTHORIZE_URL = "http://jimbo.com/oauth/authorize?token=%s";

    protected YelpApi() {
    }

    private static class InstanceHolder {
        private static final YelpApi INSTANCE = new YelpApi();
    }

    public static YelpApi instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint(){
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(OAuth1RequestToken requestToken) {
        return null;
    }
}