package uk.co.pol.service;

import java.io.IOException;

import uk.co.pol.model.TokenResponse;
import uk.co.pol.model.User;

public interface UserAuthService {

    String getAccessToken(String authCode) throws IOException;

    TokenResponse getTokens(String authCode) throws IOException;

    User getUserDetails(String accessToken);

}
