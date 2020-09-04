package com.teikametrics.apiintegration.service;

import java.util.Optional;

public interface ApiAccess {

    Optional<String> checkApiCall(String bearerToken);

    void fetchUserEvents(String bearerAuthToken, String username);
}
