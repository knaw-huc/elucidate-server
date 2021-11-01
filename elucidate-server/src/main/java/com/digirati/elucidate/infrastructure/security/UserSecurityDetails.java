package com.digirati.elucidate.infrastructure.security;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.model.security.SecurityGroup;
import com.digirati.elucidate.model.security.SecurityUser;

public class UserSecurityDetails {
    private final SecurityUser user;
    @NotNull
    private final List<SecurityGroup> groups;
    private final Set<Integer> groupSet;

    public UserSecurityDetails(SecurityUser user, @NotNull List<SecurityGroup> groups) {
        this.user = user;
        this.groups = groups;
        this.groupSet = groups.stream().map(SecurityGroup::getPk).collect(Collectors.toSet());
    }

    public SecurityUser getUser() {
        return user;
    }

    @NotNull
    public List<SecurityGroup> getGroups() {
        return groups;
    }

    public boolean hasAnyGroup(@NotNull Set<Integer> other) {
        return !Collections.disjoint(groupSet, other);
    }

    public boolean hasGroup(int other) {
        return groupSet.contains(other);
    }
}
