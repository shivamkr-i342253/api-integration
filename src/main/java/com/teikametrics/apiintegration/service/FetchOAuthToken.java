package com.teikametrics.apiintegration.service;

public interface FetchOAuthToken {

    void fetchAccessToken(String code, String state);
}
