package com.digirati.elucidate.web.controller.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digirati.elucidate.infrastructure.config.condition.IsAuthEnabled;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetails;

@RestController(SecurityUserController.CONTROLLER_NAME)
@RequestMapping("/user")
@Conditional(IsAuthEnabled.class)
public class SecurityUserController {

    public static final String CONTROLLER_NAME = "securityUserController";

    private static final String CURRENT_USER_REQUEST_PATH = "/current";

    @NotNull
    @RequestMapping(CURRENT_USER_REQUEST_PATH)
    public ResponseEntity<UserSecurityDetails> getCurrentUser(@NotNull Authentication authentication) {
        return ResponseEntity.status(HttpStatus.OK).body((UserSecurityDetails) authentication.getPrincipal());
    }
}
