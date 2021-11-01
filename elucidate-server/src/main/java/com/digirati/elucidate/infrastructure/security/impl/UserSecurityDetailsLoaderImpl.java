package com.digirati.elucidate.infrastructure.security.impl;

import java.util.Collections;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.infrastructure.generator.IDGenerator;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetails;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetailsLoader;
import com.digirati.elucidate.model.security.SecurityUser;
import com.digirati.elucidate.repository.security.GroupRepository;
import com.digirati.elucidate.repository.security.UserRepository;

public class UserSecurityDetailsLoaderImpl implements UserSecurityDetailsLoader {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final IDGenerator idGenerator;

    public UserSecurityDetailsLoaderImpl(IDGenerator idGenerator, UserRepository userRepository, GroupRepository groupRepository) {
        this.idGenerator = idGenerator;
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
    }

    @NotNull
    @Override
    public UserSecurityDetails createUser(String username) {
        SecurityUser user = userRepository.createUser(idGenerator.generateId(), username);

        return new UserSecurityDetails(user, Collections.emptyList());
    }

    @Override
    public Optional<UserSecurityDetails> getUser(String username) {
        return userRepository.getUser(username).map(user -> new UserSecurityDetails(user, groupRepository.getGroupsByUserId(user.getPk())));
    }
}
