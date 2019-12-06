package io.swagger.client.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

@javax.annotation.Generated(value = "com.github.krr.swagger.codegen.generators.JavaClientSDKGenerator", date = "2019-11-21T16:59:52.094-08:00[America/Los_Angeles]")public class OAuth implements Authentication {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public void applyToParams(MultiValueMap<String, String> queryParams, HttpHeaders headerParams) {
        if (accessToken != null) {
            headerParams.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }
    }
}
