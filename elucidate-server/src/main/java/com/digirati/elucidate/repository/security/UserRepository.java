package com.digirati.elucidate.repository.security;

import java.util.Optional;

import com.digirati.elucidate.model.security.SecurityUser;

public interface UserRepository {
    Optional<SecurityUser> getUser(String uid);

    Optional<SecurityUser> getUserById(String id);

    SecurityUser createUser(String id, String uid);
}
