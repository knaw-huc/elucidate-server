package com.digirati.elucidate.service.security;

import java.util.List;

import com.digirati.elucidate.model.security.SecurityUserReference;

public final class SecurityUserReferenceCollection {

    private final List<SecurityUserReference> users;

    public SecurityUserReferenceCollection(List<SecurityUserReference> users) {
        this.users = users;
    }

    public List<SecurityUserReference> getUsers() {
        return users;
    }
}
