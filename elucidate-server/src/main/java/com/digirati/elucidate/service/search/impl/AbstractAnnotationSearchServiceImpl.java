package com.digirati.elucidate.service.search.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.digirati.elucidate.common.infrastructure.constants.JSONLDConstants;
import com.digirati.elucidate.common.infrastructure.constants.SearchConstants;
import com.digirati.elucidate.common.infrastructure.constants.URLConstants;
import com.digirati.elucidate.common.model.annotation.AbstractAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.infrastructure.security.Permission;
import com.digirati.elucidate.infrastructure.security.UserSecurityDetailsContext;
import com.digirati.elucidate.infrastructure.util.SelectorUtils;
import com.digirati.elucidate.model.ServiceResponse;
import com.digirati.elucidate.model.ServiceResponse.Status;
import com.digirati.elucidate.model.annotation.selector.fragment.TFragmentSelector;
import com.digirati.elucidate.model.annotation.selector.fragment.XYWHFragmentSelector;
import com.digirati.elucidate.repository.AnnotationSearchRepository;
import com.digirati.elucidate.service.search.AnnotationSearchService;

public abstract class AbstractAnnotationSearchServiceImpl<A extends AbstractAnnotation> implements AnnotationSearchService<A> {

    protected final Logger LOGGER = Logger.getLogger(getClass());

    private final UserSecurityDetailsContext securityContext;
    private final AnnotationSearchRepository annotationSearchRepository;

    protected AbstractAnnotationSearchServiceImpl(UserSecurityDetailsContext securityContext, AnnotationSearchRepository annotationSearchRepository) {
        this.securityContext = securityContext;
        this.annotationSearchRepository = annotationSearchRepository;
    }

    protected abstract A convertToAnnotation(W3CAnnotation w3cAnnotation);

    protected abstract String buildAnnotationIri(String collectionId, String annotationId);

    @Nullable
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByBody(@NotNull List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {

        boolean searchIds = fields.contains(SearchConstants.FIELD_ID);
        boolean searchSources = fields.contains(SearchConstants.FIELD_SOURCE);
        Integer[] xywhParameters = buildXywhParameters(xywh);
        Integer[] tParameters = buildTParameters(t);

        if (!searchIds && !searchSources) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - at least one of [%s] or [%s] must be provided", URLConstants.PARAM_FIELDS, fields, SearchConstants.FIELD_ID, SearchConstants.FIELD_SOURCE));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        LOGGER.info(String.format("Searching for Annotations by `body` using fields [%s] against value [%s] (strict matching: [%s]) with selectors [xywh = [%s], t = [%s]] and `creator` IRI [%s] and `generator` IRI [%s]", fields, value, strict, xywh, t, creatorIri, generatorIri));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByBody(searchIds, searchSources, value, strict, xywhParameters[0], xywhParameters[1], xywhParameters[2], xywhParameters[3], tParameters[0], tParameters[1], creatorIri, generatorIri);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `body` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @Nullable
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByTarget(@NotNull List<String> fields, String value, boolean strict, String xywh, String t, String creatorIri, String generatorIri) {

        boolean searchIds = fields.contains(SearchConstants.FIELD_ID);
        boolean searchSources = fields.contains(SearchConstants.FIELD_SOURCE);
        Integer[] xywhParameters = buildXywhParameters(xywh);
        Integer[] tParameters = buildTParameters(t);

        if (!searchIds && !searchSources) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - at least one of [%s] or [%s] must be provided", URLConstants.PARAM_FIELDS, fields, SearchConstants.FIELD_ID, SearchConstants.FIELD_SOURCE));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        LOGGER.info(String.format("Searching for Annotations by `target` using fields [%s] against value [%s] (strict matching: [%s]) with selectors [xywh = [%s], t = [%s]] and `creator` IRI [%s] and `generator` IRI [%s]", fields, value, strict, xywh, t, creatorIri, generatorIri));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByTarget(searchIds, searchSources, value, strict, xywhParameters[0], xywhParameters[1], xywhParameters[2], xywhParameters[3], tParameters[0], tParameters[1], creatorIri, generatorIri);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `target` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @Nullable
    private Integer[] buildXywhParameters(String xywh) {

        if (StringUtils.isNotBlank(xywh)) {

            xywh = String.format("%s=%s", URLConstants.PARAM_XYWH, xywh);

            XYWHFragmentSelector xywhFragmentSelector = SelectorUtils.extractXywhFragmentSelector(xywh);
            if (xywhFragmentSelector != null) {
                return new Integer[]{xywhFragmentSelector.getX(), xywhFragmentSelector.getY(), xywhFragmentSelector.getW(), xywhFragmentSelector.getH()};
            }
        }

        return new Integer[]{null, null, null, null};
    }

    @Nullable
    private Integer[] buildTParameters(String t) {

        if (StringUtils.isNotBlank(t)) {

            t = String.format("%s=%s", URLConstants.PARAM_T, t);

            TFragmentSelector tFragmentSelector = SelectorUtils.extractTFragmentSelector(t);
            if (tFragmentSelector != null) {
                return new Integer[]{tFragmentSelector.getStart(), tFragmentSelector.getEnd()};
            }
        }

        return new Integer[]{null, null};
    }

    @Nullable
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByCreator(@NotNull List<String> levels, @NotNull String type, String value, boolean strict) {

        boolean searchAnnotations = levels.contains(SearchConstants.LEVEL_ANNOTATION);
        boolean searchBodies = levels.contains(SearchConstants.LEVEL_BODY);
        boolean searchTargets = levels.contains(SearchConstants.LEVEL_TARGET);

        if (!searchAnnotations && !searchBodies && !searchTargets) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - at least one of [%s], [%s] or [%s] must be provided", URLConstants.PARAM_LEVELS, levels, SearchConstants.LEVEL_ANNOTATION, SearchConstants.LEVEL_BODY, SearchConstants.LEVEL_TARGET));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        if (!type.equals(SearchConstants.TYPE_ID) && !type.equals(SearchConstants.TYPE_NAME) && !type.equals(SearchConstants.TYPE_NICKNAME) && !type.equals(SearchConstants.TYPE_EMAIL) && !type.equals(SearchConstants.TYPE_EMAIL_SHA1)) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - one of [%s], [%s], [%s], [%s], [%s] must be provided", URLConstants.PARAM_TYPE, type, SearchConstants.TYPE_ID, SearchConstants.TYPE_NAME, SearchConstants.TYPE_NICKNAME, SearchConstants.TYPE_EMAIL, SearchConstants.TYPE_EMAIL_SHA1));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        LOGGER.info(String.format("Searching for Annotations by `creator` using levels [%s] with field [%s] against value [%s] using strict [%s]", levels, type, value, strict));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByCreator(searchAnnotations, searchBodies, searchTargets, type, value, strict);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `creator` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @Nullable
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByGenerator(@NotNull List<String> levels, @NotNull String type, String value, boolean strict) {

        boolean searchAnnotations = levels.contains(SearchConstants.LEVEL_ANNOTATION);
        boolean searchBodies = levels.contains(SearchConstants.LEVEL_BODY);
        boolean searchTargets = levels.contains(SearchConstants.LEVEL_TARGET);

        if (!searchAnnotations && !searchBodies && !searchTargets) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - at least one of [%s], [%s] or [%s] must be provided", URLConstants.PARAM_LEVELS, levels, SearchConstants.LEVEL_ANNOTATION, SearchConstants.LEVEL_BODY, SearchConstants.LEVEL_TARGET));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        if (!type.equals(SearchConstants.TYPE_ID) && !type.equals(SearchConstants.TYPE_NAME) && !type.equals(SearchConstants.TYPE_NICKNAME) && !type.equals(SearchConstants.TYPE_EMAIL) && !type.equals(SearchConstants.TYPE_EMAIL_SHA1)) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - one of [%s], [%s], [%s], [%s], [%s] must be provided", URLConstants.PARAM_TYPE, type, SearchConstants.TYPE_ID, SearchConstants.TYPE_NAME, SearchConstants.TYPE_NICKNAME, SearchConstants.TYPE_EMAIL, SearchConstants.TYPE_EMAIL_SHA1));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        LOGGER.info(String.format("Searching for Annotations by `generator` using levels [%s] with field [%s] against value [%s] using strict [%s]", levels, type, value, strict));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByGenerator(searchAnnotations, searchBodies, searchTargets, type, value, strict);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `generator` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @Nullable
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByTemporal(@NotNull List<String> levels, @NotNull List<String> types, Date since) {

        boolean searchAnnotations = levels.contains(SearchConstants.LEVEL_ANNOTATION);
        boolean searchBodies = levels.contains(SearchConstants.LEVEL_BODY);
        boolean searchTargets = levels.contains(SearchConstants.LEVEL_TARGET);

        if (!searchAnnotations && !searchBodies && !searchTargets) {
            LOGGER.warn(String.format("Provided search parameter [%s] with value [%s] is invalid - at least one of [%s], [%s] or [%s] must be provided", URLConstants.PARAM_LEVELS, levels, SearchConstants.LEVEL_ANNOTATION, SearchConstants.LEVEL_BODY, SearchConstants.LEVEL_TARGET));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        if (types.isEmpty() || (!types.contains(SearchConstants.TYPE_CREATED) && !types.contains(SearchConstants.TYPE_MODIFIED) && !types.contains(SearchConstants.TYPE_GENERATED))) {
            LOGGER.warn(String.format("Provided search parameter [%s] with values [%s] is invalid - one of [%s], [%s], [%s] must be provided", URLConstants.PARAM_TYPE, types, SearchConstants.TYPE_CREATED, SearchConstants.TYPE_MODIFIED, SearchConstants.TYPE_GENERATED));
            return new ServiceResponse<>(Status.NON_CONFORMANT, null);
        }

        LOGGER.info(String.format("Searching for Annotations by `temporal` using levels [%s] with types [%s] since [%s]", levels, types, since));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByTemporal(searchAnnotations, searchBodies, searchTargets, types.toArray(new String[0]), since);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `temporal` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @NotNull
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByRange(String targetId, float rangeStart, float rangeEnd) {

        LOGGER.info(String.format("Searching for Annotations by `range` using rangeStart [%s] and rangeEnd [%s]", rangeStart, rangeEnd));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByRange(targetId, rangeStart, rangeEnd);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `range` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @NotNull
    @Override
    public ServiceResponse<List<A>> searchAnnotationsByOverlap(String targetId, float rangeStart, float rangeEnd) {

        LOGGER.info(String.format("Searching for Annotations by `overlap` using rangeStart [%s] and rangeEnd [%s]", rangeStart, rangeEnd));
        List<W3CAnnotation> w3cAnnotations = annotationSearchRepository.getAnnotationsByOverlap(targetId, rangeStart, rangeEnd);

        List<A> annotations = convertAnnotations(w3cAnnotations);
        LOGGER.info(String.format("Searching for Annotations by `overlap` got [%s] hits", annotations.size()));
        return new ServiceResponse<>(Status.OK, annotations);
    }

    @NotNull
    private List<A> convertAnnotations(@NotNull List<W3CAnnotation> w3cAnnotations) {
        List<A> annotations = new ArrayList<>();
        for (W3CAnnotation w3cAnnotation : w3cAnnotations) {
            if (securityContext.isAuthorized(Permission.READ, w3cAnnotation)) {
                A annotation = convertToAnnotation(w3cAnnotation);
                annotation.getJsonMap().put(JSONLDConstants.ATTRIBUTE_ID, buildAnnotationIri(annotation.getCollectionId(), annotation.getAnnotationId()));
                annotations.add(annotation);
            }
        }
        return annotations;
    }
}
