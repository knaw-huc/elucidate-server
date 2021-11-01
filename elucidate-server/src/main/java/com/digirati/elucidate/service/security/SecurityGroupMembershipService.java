package com.digirati.elucidate.service.security;

import org.jetbrains.annotations.NotNull;

import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.annotation.AnnotationReferenceCollection;

public interface SecurityGroupMembershipService {
    @NotNull
    ServiceResponse<Void> addAnnotationToGroup(String collectionId, String annotationId, String groupId);

    @NotNull
    ServiceResponse<Void> removeAnnotationFromGroup(String collectionId, String annotationId, String groupId);

    @NotNull
    ServiceResponse<AnnotationReferenceCollection> getGroupAnnotations(String groupId);

    @NotNull
    ServiceResponse<Void> addUserToGroup(String userId, String groupId);

    @NotNull
    ServiceResponse<Void> removeUserFromGroup(String userId, String groupId);

    @NotNull
    ServiceResponse<SecurityUserReferenceCollection> getGroupUsers(String groupId);
}
