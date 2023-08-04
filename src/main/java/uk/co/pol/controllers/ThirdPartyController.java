package uk.co.pol.controllers;

import java.io.IOException;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.co.pol.model.User;
import uk.co.pol.service.UserAuthService;
import uk.co.pol.model.Query;
import uk.co.pol.model.TokenResponse;

@Controller
public class ThirdPartyController {
    private static final String SCOPE = "scope";
    private final UserAuthService userAuthService;

    @Value("${client-id}")
    private String clientId;
    @Value("${redirect-uri}")
    private String redirectUri;
    @Value("${authorise-uri}")
    private String authoriseUri;

    private static final String USER_SCOPE = "openid profile";

    @Autowired
    public ThirdPartyController(UserAuthService userAuthService) {
        this.userAuthService = userAuthService;
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/attemptLogin")
    public String attemptLogin(RedirectAttributes redirectAttributes,
                               @RequestParam Map<String,String> allParams) {
        StringBuilder scopes = new StringBuilder(USER_SCOPE);
        for (String param : allParams.keySet()) {
                scopes.append(allParams.get(param));
                scopes.append(" ");
        }


        redirectAttributes.addAttribute("response_type", "code");
        redirectAttributes.addAttribute("client_id", clientId);
        redirectAttributes.addAttribute("redirect_uri", redirectUri);
        redirectAttributes.addAttribute("prompt", "login");
        redirectAttributes.addAttribute(SCOPE, scopes.toString().trim());

        System.out.println("Authorize URL:" + authoriseUri + ' ' + redirectAttributes);
        return "redirect:" + authoriseUri;
    }

    @GetMapping(value = "/redirect")
    public String handleRedirect(@RequestParam("code") String code, Model model)
            throws IOException {
        TokenResponse tokens = userAuthService.getTokens(code);
        if(tokens.getIdToken() == null || tokens.getAccessToken() == null){
            throw new IOException("Could not get a token: " + tokens.getAdditionalProperties());
        }
        System.out.println("TOKENS: " + tokens.toString());
        User user = userAuthService.getUserDetails(tokens.getAccessToken());
        model.addAttribute("user", user);
        model.addAttribute("accessToken", tokens.getAccessToken());
        model.addAttribute("idToken", tokens.getIdToken());
        model.addAttribute("tokenIssuer", tokens.getTokenIssuer());
        model.addAttribute("query", new Query());
        return "loginResult";
    }

    @GetMapping(value = "/protected")
    public String protectedAccess(@RequestParam("userAccessToken") String userAccessToken, @RequestParam("userIdToken") String userIdToken, Model model)
            throws IOException {
        User user = userAuthService.getUserDetails(userAccessToken);
        model.addAttribute("user", user);
        model.addAttribute("accessToken", userAccessToken);
        model.addAttribute("idToken", userIdToken);
        return "protected";
    }

}