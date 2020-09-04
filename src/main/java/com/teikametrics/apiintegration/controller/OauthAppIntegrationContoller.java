package com.teikametrics.apiintegration.controller;

import com.teikametrics.apiintegration.config.OAuthGitHubAppProperties;
import com.teikametrics.apiintegration.constants.Constants;
import com.teikametrics.apiintegration.exception.AuthorizationAbortedException;
import com.teikametrics.apiintegration.service.impl.FetchOAuthTokenImpl;
import com.teikametrics.apiintegration.util.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OauthAppIntegrationContoller {

    @Autowired
    private FetchOAuthTokenImpl fetchOAuthTokenImpl;

    @Autowired
    private OAuthGitHubAppProperties oAuthGitHubAppProperties;


    // start the process of authorization by clicking Sign In on the returned "auth" page

    @GetMapping("/authorize")
    public String authorize(Model model) {
        model.addAttribute(Constants.API_AUTHORIZE, oAuthGitHubAppProperties.getProvider().getApiAuthorize());
        model.addAttribute(Constants.CLIENT_ID_VAR, oAuthGitHubAppProperties.getRegistration().getClientId());

        model.addAttribute(Constants.SCOPE_VAR, Utility.toCommaSeparatedString(oAuthGitHubAppProperties.getRegistration().getScope()));

        return "auth";
    }

    // after clicking Sign In in the previous step, extract the code from the url returned from the API provider and call the
    // fetchAccessToken() to get the access_token

    @GetMapping("/")
    @ResponseBody
    public void extractAuthCode(@RequestParam(value = "code") String code, @RequestParam(value = "state", required = false) String state) {

        fetchOAuthTokenImpl.fetchAccessToken(code, state);
    }

    // for handling error for specific paths

    @PostMapping("/error")
    @ResponseBody
    public ResponseEntity<Object> displayError() throws AuthorizationAbortedException {
        throw new AuthorizationAbortedException(HttpStatus.BAD_REQUEST, Constants.ABORT_EXCEPTION_REASON);
    }
}
