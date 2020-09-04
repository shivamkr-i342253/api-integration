package com.teikametrics.apiintegration.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teikametrics.apiintegration.config.OAuthGitHubAppProperties;
import com.teikametrics.apiintegration.constants.Constants;
import com.teikametrics.apiintegration.model.Event;
import com.teikametrics.apiintegration.service.ApiAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ApiAccessImpl implements ApiAccess {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CommitsProcessorImpl commitsProcessorImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OAuthGitHubAppProperties oAuthGitHubAppProperties;

    private static final Logger LOG = LoggerFactory.getLogger(ApiAccessImpl.class);

    // the method is used to make first API call to the provider using the access_token

    @Override
    public Optional<String> checkApiCall(String bearerAuthToken) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(bearerAuthToken);

        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map> response = restTemplate.exchange(oAuthGitHubAppProperties.getProvider().getApiUser(), HttpMethod.GET, httpEntity, Map.class);

        if (response.hasBody()) {

            Map responseBody = response.getBody();

            if (responseBody != null) {

                return Optional.of(responseBody.getOrDefault(Constants.LOGIN, null).toString());
            }

        }

        return Optional.empty();
    }

    // the method fetches the events for user and after success call the method for extracting commits

    @Override
    public void fetchUserEvents(String bearerAuthToken, String username) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(bearerAuthToken);
        httpHeaders.set(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_GITHUB_EVENT_VALUE);

        HttpEntity httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<JsonNode> response = restTemplate.exchange(oAuthGitHubAppProperties.getProvider().getApiUserEvents(), HttpMethod.GET, httpEntity, JsonNode.class, username);

        if (response.hasBody()) {

            List<Event> events = objectMapper.convertValue(response.getBody(), new TypeReference<List<Event>>() {});

            if (events != null) {

                // call to the findMostRecentCommits() finds the most recent commits performed by the user itself

                commitsProcessorImpl.findMostRecentCommits(events);
            }
            else {

                LOG.info("No response received");
            }

        }
        else {

            LOG.error("Error receiving response");
        }

    }

}
