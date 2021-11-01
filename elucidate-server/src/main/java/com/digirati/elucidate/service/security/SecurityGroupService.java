package com.digirati.elucidate.service.security;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.security.SecurityGroup;

public interface SecurityGroupService {
    @NotNull
    ServiceResponse<SecurityGroup> createGroup(String label);

    ServiceResponse<SecurityGroup> getGroup(String id);
}
