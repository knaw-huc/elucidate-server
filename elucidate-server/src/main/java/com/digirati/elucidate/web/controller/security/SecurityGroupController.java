package com.digirati.elucidate.web.controller.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digirati.elucidate.infrastructure.config.condition.IsAuthEnabled;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.security.SecurityGroup;
import com.digirati.elucidate.service.security.SecurityGroupService;

@RestController(SecurityGroupController.CONTROLLER_NAME)
@RequestMapping("/group")
@Conditional(IsAuthEnabled.class)
public class SecurityGroupController {

    public static final String CONTROLLER_NAME = "securityGroupController";

    private static final String VARIABLE_GROUP_ID = "groupId";
    private static final String GROUP_REQUEST_PATH = "/{" + VARIABLE_GROUP_ID + "}";

    private final SecurityGroupService securityGroupService;

    @Autowired
    public SecurityGroupController(SecurityGroupService securityGroupService) {
        this.securityGroupService = securityGroupService;
    }

    @NotNull
    @PostMapping
    public ResponseEntity<SecurityGroup> createGroup(@NotNull @RequestBody SecurityGroup group) {
        ServiceResponse<SecurityGroup> response = securityGroupService.createGroup(group.getLabel());
        return ResponseEntity.status(HttpStatus.CREATED).body(response.getObj());
    }

    @NotNull
    @GetMapping(GROUP_REQUEST_PATH)
    public ResponseEntity<SecurityGroup> getGroup(@PathVariable(VARIABLE_GROUP_ID) String groupId) {
        ServiceResponse<SecurityGroup> response = securityGroupService.getGroup(groupId);
        Status status = response.getStatus();

        if (status == Status.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (status == Status.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(response.getObj());
    }
}
