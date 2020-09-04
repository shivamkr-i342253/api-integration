package com.teikametrics.apiintegration.config;

import com.teikametrics.apiintegration.constants.Constants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = Constants.GITHUB_APP)
public class OAuthGitHubAppProperties {

    private String userEmail;
    private Registration registration;
    private Provider provider;

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public static class Registration {
        private String clientId;
        private String clientSecret;
        private List<String> scope;

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public List<String> getScope() {
            return scope;
        }

        public void setScope(List<String> scope) {
            this.scope = scope;
        }
    }

    public static class Provider {
        private String domain;
        private String apiAuthorize;
        private String apiAccessToken;
        private String apiUser;
        private String apiUserEvents;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getApiAuthorize() {
            return apiAuthorize;
        }

        public void setApiAuthorize(String apiAuthorize) {
            this.apiAuthorize = apiAuthorize;
        }

        public String getApiAccessToken() {
            return apiAccessToken;
        }

        public void setApiAccessToken(String apiAccessToken) {
            this.apiAccessToken = apiAccessToken;
        }

        public String getApiUser() {
            return apiUser;
        }

        public void setApiUser(String apiUser) {
            this.apiUser = apiUser;
        }

        public String getApiUserEvents() {
            return apiUserEvents;
        }

        public void setApiUserEvents(String apiUserEvents) {
            this.apiUserEvents = apiUserEvents;
        }
    }

}
