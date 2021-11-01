package com.digirati.elucidate.service.security.impl;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.infrastructure.security.Permission;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetailsContext;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.annotation.AnnotationReference;
import com.digirati.elucidate.model.annotation.AnnotationReferenceCollection;
import com.digirati.elucidate.model.security.SecurityGroup;
import com.digirati.elucidate.model.security.SecurityUser;
import com.digirati.elucidate.model.security.SecurityUserReference;
import com.digirati.elucidate.repository.security.GroupMembershipRepository;
import com.digirati.elucidate.repository.security.UserRepository;
import com.digirati.elucidate.service.query.W3CAnnotationService;
import com.digirati.elucidate.service.security.SecurityGroupMembershipService;
import com.digirati.elucidate.service.security.SecurityGroupService;
import com.digirati.elucidate.service.security.SecurityUserReferenceCollection;

@Service(SecurityGroupMembershipServiceImpl.SERVICE_NAME)
public class SecurityGroupMembershipServiceImpl implements SecurityGroupMembershipService {

    public static final String SERVICE_NAME = "securityGroupMembershipServiceImpl";

    private final UserSecurityDetailsContext securityContext;
    private final SecurityGroupService securityGroupService;
    private final W3CAnnotationService w3cAnnotationService;
    private final GroupMembershipRepository membershipRepository;
    private final UserRepository userRepository;

    public SecurityGroupMembershipServiceImpl(
            UserSecurityDetailsContext securityContext,
            SecurityGroupService securityGroupService,
            W3CAnnotationService w3cAnnotationService,
            GroupMembershipRepository membershipRepository,
            UserRepository userRepository,
            IRIBuilderService iriBuilder) {

        this.securityContext = securityContext;
        this.securityGroupService = securityGroupService;
        this.w3cAnnotationService = w3cAnnotationService;
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
    }

    @NotNull
    @Override
    public ServiceResponse<Void> addAnnotationToGroup(String collectionId, String annotationId, String groupId) {
        return handleAnnotationAndGroup(
                collectionId,
                annotationId,
                groupId,
                membershipRepository::createAnnotationGroupMembership
        );
    }

    @NotNull
    @Override
    public ServiceResponse<Void> removeAnnotationFromGroup(String collectionId, String annotationId, String groupId) {
        return handleAnnotationAndGroup(
                collectionId,
                annotationId,
                groupId,
                membershipRepository::removeAnnotationGroupMembership
        );
    }

    @NotNull
    @Override
    public ServiceResponse<AnnotationReferenceCollection> getGroupAnnotations(String groupId) {
        ServiceResponse<SecurityGroup> groupRes = securityGroupService.getGroup(groupId);

        if (groupRes.getStatus() != Status.OK) {
            return new ServiceResponse<>(groupRes.getStatus());
        }

        SecurityGroup group = groupRes.getObj();

        if (!securityContext.isAuthorized(Permission.MANAGE, group)) {
            return new ServiceResponse<>(Status.UNAUTHORIZED);
        }

        List<AnnotationReference> annotationRefs = membershipRepository.getAnnotationGroupMemberships(group.getPk());
        AnnotationReferenceCollection collection = new AnnotationReferenceCollection(annotationRefs);

        return new ServiceResponse<>(Status.OK, collection);
    }

    @NotNull
    @Override
    public ServiceResponse<Void> addUserToGroup(String userId, String groupId) {
        return handleUserAndGroup(userId, groupId, membershipRepository::createUserGroupMembership);
    }

    @NotNull
    @Override
    public ServiceResponse<Void> removeUserFromGroup(String userId, String groupId) {
        return handleUserAndGroup(userId, groupId, membershipRepository::removeUserGroupMembership);
    }

    @NotNull
    @Override
    public ServiceResponse<SecurityUserReferenceCollection> getGroupUsers(String groupId) {
        ServiceResponse<SecurityGroup> groupRes = securityGroupService.getGroup(groupId);

        if (groupRes.getStatus() != Status.OK) {
            return new ServiceResponse<>(groupRes.getStatus());
        }

        SecurityGroup group = groupRes.getObj();

        if (!securityContext.isAuthorized(Permission.MANAGE, group)) {
            return new ServiceResponse<>(Status.UNAUTHORIZED);
        }

        List<SecurityUserReference> users = membershipRepository.getUserGroupMemberships(group.getPk());
        SecurityUserReferenceCollection collection = new SecurityUserReferenceCollection(users);

        return new ServiceResponse<>(Status.OK, collection);
    }

    @Nullable
    private ServiceResponse<Void> handleAnnotationAndGroup(String collectionId, String annotationId, String groupId, @NotNull BiConsumer<Integer, Integer> consumer) {
        ServiceResponse<SecurityGroup> groupRes = securityGroupService.getGroup(groupId);
        if (groupRes.getStatus() != Status.OK) {
            return new ServiceResponse<>(groupRes.getStatus(), null);
        }

        ServiceResponse<W3CAnnotation> annotationRes = w3cAnnotationService.getAnnotation(collectionId, annotationId);
        if (annotationRes.getStatus() != Status.OK) {
            return new ServiceResponse<>(annotationRes.getStatus(), null);
        }

        W3CAnnotation annotation = annotationRes.getObj();
        SecurityGroup group = groupRes.getObj();

        if (!securityContext.isAuthorized(Permission.MANAGE, annotation)
                || !securityContext.isAuthorized(Permission.MANAGE, group)) {
            return new ServiceResponse<>(Status.UNAUTHORIZED, null);
        }

        consumer.accept(annotation.getPk(), group.getPk());
        return new ServiceResponse<>(Status.OK, null);
    }

    @Nullable
    private ServiceResponse<Void> handleUserAndGroup(String userId, String groupId, @NotNull BiConsumer<Integer, Integer> consumer) {
        ServiceResponse<SecurityGroup> groupRes = securityGroupService.getGroup(groupId);

        if (groupRes.getStatus() != Status.OK) {
            return new ServiceResponse<>(groupRes.getStatus(), null);
        }

        Optional<SecurityUser> details = userRepository.getUserById(userId);
        if (!details.isPresent()) {
            return new ServiceResponse<>(Status.NOT_FOUND, null);
        }

        SecurityUser user = details.get();
        SecurityGroup group = groupRes.getObj();

        if (!securityContext.isAuthorized(Permission.MANAGE, group)) {
            return new ServiceResponse<>(Status.UNAUTHORIZED, null);
        }

        consumer.accept(user.getPk(), group.getPk());
        return new ServiceResponse<>(Status.OK, null);
    }
}
