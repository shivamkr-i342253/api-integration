package com.teikametrics.apiintegration.service.impl;

import com.teikametrics.apiintegration.config.OAuthGitHubAppProperties;
import com.teikametrics.apiintegration.constants.Constants;
import com.teikametrics.apiintegration.service.FetchOAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
public class FetchOAuthTokenImpl implements FetchOAuthToken {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApiAccessImpl apiAccessImpl;

    @Autowired
    private OAuthGitHubAppProperties oAuthGitHubAppProperties;

    private static final Logger LOG = LoggerFactory.getLogger(FetchOAuthTokenImpl.class);


    // the method calls the API requesting the access_token using the code returned

    @Override
    public void fetchAccessToken(String code, String state) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(Constants.HEADER_ACCEPT, Constants.HEADER_ACCEPT_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> bodyMap= new LinkedMultiValueMap<>();
        bodyMap.add(Constants.CLIENT_ID, oAuthGitHubAppProperties.getRegistration().getClientId());
        bodyMap.add(Constants.CLIENT_SECRET, oAuthGitHubAppProperties.getRegistration().getClientSecret());
        bodyMap.add(Constants.AUTHORIZATION_CODE, code);
        if (state != null) bodyMap.add(Constants.AUTH_STATE, state);

        HttpEntity<MultiValueMap<String, String> > httpRequest = new HttpEntity<>(bodyMap, httpHeaders);

        ResponseEntity<Map> response =  restTemplate.postForEntity(oAuthGitHubAppProperties.getProvider().getApiAccessToken(), httpRequest, Map.class);

        if (response.hasBody()) {

            Map responseBody = response.getBody();

            if (responseBody != null) {

                String bearerAuthToken = responseBody.getOrDefault(Constants.ACCESS_TOKEN, null).toString();

                if (bearerAuthToken != null) {

                    // based on the response received we use access_token and check calling the API for user fetching the username

                    Optional<String> userApiResponse = apiAccessImpl.checkApiCall(bearerAuthToken);

                    boolean verifyUserApiResponse = userApiResponse.isPresent();

                    if (verifyUserApiResponse) {

                        // when the above call is successful we move forward to fetch the user events required for the problem

                        apiAccessImpl.fetchUserEvents(bearerAuthToken, userApiResponse.get());
                    }
                    else {

                        LOG.error("Error fetching login user");
                    }
                }
                else {

                    LOG.info("Invalid access_token");
                }

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
