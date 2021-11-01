package com.digirati.elucidate.service.extractor.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.github.jsonldjava.utils.JsonUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.infrastructure.extractor.agent.AnnotationCreatorExtractor;
import com.digirati.elucidate.infrastructure.extractor.agent.AnnotationGeneratorExtractor;
import com.digirati.elucidate.infrastructure.extractor.body.AnnotationBodyExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationCSSSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationDataPositionSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationFragmentSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationInlineFragmentSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationSVGSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationTextPositionSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationTextQuoteSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.selector.AnnotationXPathSelectorExtractor;
import com.digirati.elucidate.infrastructure.extractor.targets.AnnotationTargetExtractor;
import com.digirati.elucidate.infrastructure.extractor.temportal.AnnotationTemporalExtractor;
import com.digirati.elucidate.model.annotation.agent.AnnotationAgent;
import com.digirati.elucidate.model.annotation.body.AnnotationBody;
import com.digirati.elucidate.model.annotation.selector.css.AnnotationCSSSelector;
import com.digirati.elucidate.model.annotation.selector.dataposition.AnnotationDataPositionSelector;
import com.digirati.elucidate.model.annotation.selector.fragment.AnnotationFragmentSelector;
import com.digirati.elucidate.model.annotation.selector.svg.AnnotationSVGSelector;
import com.digirati.elucidate.model.annotation.selector.textposition.AnnotationTextPositionSelector;
import com.digirati.elucidate.model.annotation.selector.textquote.AnnotationTextQuoteSelector;
import com.digirati.elucidate.model.annotation.selector.xpath.AnnotationXPathSelector;
import com.digirati.elucidate.model.annotation.targets.AnnotationTarget;
import com.digirati.elucidate.model.annotation.temporal.AnnotationTemporal;
import com.digirati.elucidate.repository.AnnotationAgentStoreRepository;
import com.digirati.elucidate.repository.AnnotationBodyStoreRepository;
import com.digirati.elucidate.repository.AnnotationSelectorStoreRepository;
import com.digirati.elucidate.repository.AnnotationTargetStoreRepository;
import com.digirati.elucidate.repository.AnnotationTemporalStoreRepository;
import com.digirati.elucidate.service.extractor.AnnotationExtractorService;

@Service(AnnotationExtractorServiceImpl.SERVICE_NAME)
public class AnnotationExtractorServiceImpl implements AnnotationExtractorService {

    private static final Logger LOGGER = Logger.getLogger(AnnotationExtractorServiceImpl.class);

    public static final String SERVICE_NAME = "annotationExtractorServiceImpl";

    private final AnnotationBodyStoreRepository annotationBodyStoreRepository;
    private final AnnotationTargetStoreRepository annotationTargetStoreRepository;
    private final AnnotationSelectorStoreRepository annotationSelectorStoreRepository;
    private final AnnotationAgentStoreRepository annotationAgentStoreRepository;
    private final AnnotationTemporalStoreRepository annotationTemporalStoreRepository;

    @Autowired
    public AnnotationExtractorServiceImpl(AnnotationBodyStoreRepository annotationBodyStoreRepository, AnnotationTargetStoreRepository annotationTargetStoreRepository, AnnotationSelectorStoreRepository annotationSelectorStoreRepository, AnnotationAgentStoreRepository annotationAgentStoreRepository, AnnotationTemporalStoreRepository annotationTemporalStoreRepository) {
        this.annotationBodyStoreRepository = annotationBodyStoreRepository;
        this.annotationTargetStoreRepository = annotationTargetStoreRepository;
        this.annotationSelectorStoreRepository = annotationSelectorStoreRepository;
        this.annotationAgentStoreRepository = annotationAgentStoreRepository;
        this.annotationTemporalStoreRepository = annotationTemporalStoreRepository;
    }

    @Override
    public void processAnnotationCreate(W3CAnnotation w3cAnnotation) {
        try {
            LOGGER.info(String.format("Processing CREATE for W3CAnnotation [%s]", w3cAnnotation));
            createAnnotationBodies(w3cAnnotation);
            createAnnotationTargets(w3cAnnotation);
            createAnnotationCreators(w3cAnnotation);
            createAnnotationGenerators(w3cAnnotation);
            createAnnotationTemporals(w3cAnnotation);
            LOGGER.info(String.format("Completed processing CREATE for W3CAnnotation [%s]", w3cAnnotation));
        } catch (Exception e) {
            LOGGER.error(String.format("An error occurred processing W3CAnnotation [%s]", w3cAnnotation), e);
        }
    }

    @Override
    public void processAnnotationUpdate(W3CAnnotation w3cAnnotation) {
        LOGGER.info(String.format("Processing UPDATE for W3CAnnotation [%s]", w3cAnnotation));
        processAnnotationDelete(w3cAnnotation);
        processAnnotationCreate(w3cAnnotation);
    }

    @Override
    public void processAnnotationDelete(W3CAnnotation w3cAnnotation) {
        LOGGER.info(String.format("Processing DELETE for W3CAnnotation [%s]", w3cAnnotation));

        deleteAnnotationCreators(w3cAnnotation);
        deleteAnnotationGenerators(w3cAnnotation);
        deleteAnnotationTemporals(w3cAnnotation);

        List<AnnotationBody> annotationBodies = deleteBodies(w3cAnnotation);
        for (AnnotationBody annotationBody : annotationBodies) {
            int bodyPk = annotationBody.getPk();
            deleteAnnotationSelectors(bodyPk, null);
            deleteAnnotationCreators(null, bodyPk, null);
            deleteAnnotationGenerators(null, bodyPk, null);
            deleteAnnotationTemporals(null, bodyPk, null);
        }

        List<AnnotationTarget> annotationTargets = deleteTargets(w3cAnnotation);
        for (AnnotationTarget annotationTarget : annotationTargets) {
            int targetPk = annotationTarget.getPk();
            deleteAnnotationSelectors(null, targetPk);
            deleteAnnotationCreators(null, null, targetPk);
            deleteAnnotationGenerators(null, null, targetPk);
            deleteAnnotationTemporals(null, null, targetPk);
        }
    }

    private void createAnnotationBodies(W3CAnnotation w3cAnnotation) throws IOException {
        int annotationPk = w3cAnnotation.getPk();
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();

        LOGGER.info(String.format("Processing `body` values for W3CAnnotation [%s]", w3cAnnotation));
        List<AnnotationBody> annotationBodies = new AnnotationBodyExtractor().extractBodies(jsonMap);
        LOGGER.info(String.format("Got [%s] `body` values for W3CAnnotation [%s]", annotationBodies.size(), w3cAnnotation));
        for (AnnotationBody annotationBody : annotationBodies) {

            String bodyIri = annotationBody.getBodyIri();
            String sourceIri = annotationBody.getSourceIri();
            String bodyJson = JsonUtils.toString(annotationBody.getJsonMap());
            LOGGER.info(String.format("Preparing to create `body` with ID [%s], source IRI [%s] and JSON [%s]", bodyIri, sourceIri, bodyJson));
            annotationBody = annotationBodyStoreRepository.createAnnotationBody(annotationPk, bodyIri, sourceIri, bodyJson);
            LOGGER.info(String.format("Created AnnotationBody [%s] in repository", annotationBody));

            int bodyPk = annotationBody.getPk();
            Map<String, Object> bodyJsonMap = annotationBody.getJsonMap();
            createAnnotationSelectors(bodyPk, null, bodyJsonMap);
            createAnnotationCreators(null, bodyPk, null, bodyJsonMap);
            createAnnotationGenerators(null, bodyPk, null, bodyJsonMap);
            createAnnotationTemporals(null, bodyPk, null, bodyJsonMap);
        }
    }

    private void createAnnotationTargets(W3CAnnotation w3cAnnotation) throws IOException {
        int annotationPk = w3cAnnotation.getPk();
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();

        LOGGER.info(String.format("Processing `target` values for W3CAnnotation [%s]", w3cAnnotation));
        List<AnnotationTarget> annotationTargets = new AnnotationTargetExtractor().extractTargets(jsonMap);
        LOGGER.info(String.format("Got [%s] `target` values for W3CAnnotation [%s]", annotationTargets.size(), w3cAnnotation));
        for (AnnotationTarget annotationTarget : annotationTargets) {

            String targetIri = annotationTarget.getTargetIri();
            String sourceIri = annotationTarget.getSourceIri();
            String targetJson = JsonUtils.toString(annotationTarget.getJsonMap());
            LOGGER.info(String.format("Preparing to create `target` with ID [%s], source IRI [%s] and JSON [%s]", targetIri, sourceIri, targetJson));
            annotationTarget = annotationTargetStoreRepository.createAnnotationTarget(annotationPk, targetIri, sourceIri, targetJson);
            LOGGER.info(String.format("Created AnnotationTarget [%s] in repository", annotationTarget));

            int targetPk = annotationTarget.getPk();
            Map<String, Object> targetJsonMap = annotationTarget.getJsonMap();
            createAnnotationSelectors(null, targetPk, targetJsonMap);
            createAnnotationCreators(null, null, targetPk, targetJsonMap);
            createAnnotationGenerators(null, null, targetPk, targetJsonMap);
            createAnnotationTemporals(null, null, targetPk, targetJsonMap);
        }
    }

    private void createAnnotationSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        createAnnotationCssSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationDataPositionSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationInlineFragmentSelector(bodyPk, targetPk, jsonMap);
        createAnnotationFragmentSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationSvgSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationTextPositionSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationTextQuoteSelectors(bodyPk, targetPk, jsonMap);
        createAnnotationXPathSelectors(bodyPk, targetPk, jsonMap);
    }

    private void createAnnotationCssSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationCSSSelector> annotationCssSelectors = new AnnotationCSSSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationCSSSelector annotationCssSelector : annotationCssSelectors) {

            String selectorIri = annotationCssSelector.getSelectorIri();
            String value = annotationCssSelector.getValue();
            String selectorJson = JsonUtils.toString(annotationCssSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationCssSelector(bodyPk, targetPk, selectorIri, value, selectorJson);
        }
    }

    private void createAnnotationDataPositionSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationDataPositionSelector> annotationDataPositionSelectors = new AnnotationDataPositionSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationDataPositionSelector annotationDataPositionSelector : annotationDataPositionSelectors) {

            String selectorIri = annotationDataPositionSelector.getSelectorIri();
            Integer start = annotationDataPositionSelector.getStart();
            Integer end = annotationDataPositionSelector.getEnd();
            String selectorJson = JsonUtils.toString(annotationDataPositionSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationDataPositionSelector(bodyPk, targetPk, selectorIri, start, end, selectorJson);
        }
    }

    private void createAnnotationInlineFragmentSelector(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        AnnotationFragmentSelector annotationFragmentSelector = new AnnotationInlineFragmentSelectorExtractor().extractAnnotationInlineFragmentSelector(jsonMap);
        if (annotationFragmentSelector != null) {
            createAnnotationFragmentSelector(bodyPk, targetPk, annotationFragmentSelector);
        }
    }

    private void createAnnotationFragmentSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationFragmentSelector> annotationFragmentSelectors = new AnnotationFragmentSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationFragmentSelector annotationFragmentSelector : annotationFragmentSelectors) {
            createAnnotationFragmentSelector(bodyPk, targetPk, annotationFragmentSelector);
        }
    }

    private void createAnnotationFragmentSelector(Integer bodyPk, Integer targetPk, AnnotationFragmentSelector annotationFragmentSelector) throws IOException {

        String selectorIri = annotationFragmentSelector.getSelectorIri();
        String conformsTo = annotationFragmentSelector.getConformsTo();
        String value = annotationFragmentSelector.getValue();
        Integer x = annotationFragmentSelector.getX();
        Integer y = annotationFragmentSelector.getY();
        Integer w = annotationFragmentSelector.getW();
        Integer h = annotationFragmentSelector.getH();
        Integer start = annotationFragmentSelector.getStart();
        Integer end = annotationFragmentSelector.getEnd();
        String selectorJson = JsonUtils.toString(annotationFragmentSelector.getJsonMap());
        annotationSelectorStoreRepository.createAnnotationFragmentSelector(bodyPk, targetPk, selectorIri, conformsTo, value, x, y, w, h, start, end, selectorJson);
    }

    private void createAnnotationSvgSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationSVGSelector> annotationSvgSelectors = new AnnotationSVGSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationSVGSelector annotationSvgSelector : annotationSvgSelectors) {

            String selectorIri = annotationSvgSelector.getSelectorIri();
            String value = annotationSvgSelector.getValue();
            String selectorJson = JsonUtils.toString(annotationSvgSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationSvgSelector(bodyPk, targetPk, selectorIri, value, selectorJson);
        }
    }

    private void createAnnotationTextPositionSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationTextPositionSelector> annotationTextPositionSelectors = new AnnotationTextPositionSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationTextPositionSelector annotationTextPositionSelector : annotationTextPositionSelectors) {

            String selectorIri = annotationTextPositionSelector.getSelectorIri();
            Integer start = annotationTextPositionSelector.getStart();
            Integer end = annotationTextPositionSelector.getEnd();
            String selectorJson = JsonUtils.toString(annotationTextPositionSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationTextPositionSelector(bodyPk, targetPk, selectorIri, start, end, selectorJson);
        }
    }

    private void createAnnotationTextQuoteSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationTextQuoteSelector> annotationTextQuoteSelectors = new AnnotationTextQuoteSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationTextQuoteSelector annotationTextQuoteSelector : annotationTextQuoteSelectors) {

            String selectorIri = annotationTextQuoteSelector.getSelectorIri();
            String exact = annotationTextQuoteSelector.getExact();
            String prefix = annotationTextQuoteSelector.getPrefix();
            String suffix = annotationTextQuoteSelector.getSuffix();
            String selectorJson = JsonUtils.toString(annotationTextQuoteSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationTextQuoteSelector(bodyPk, targetPk, selectorIri, exact, prefix, suffix, selectorJson);
        }
    }

    private void createAnnotationXPathSelectors(Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationXPathSelector> annotationXPathSelectors = new AnnotationXPathSelectorExtractor().extractSelectors(jsonMap);
        for (AnnotationXPathSelector annotationXPathSelector : annotationXPathSelectors) {

            String selectorIri = annotationXPathSelector.getSelectorIri();
            String value = annotationXPathSelector.getValue();
            String selectorJson = JsonUtils.toString(annotationXPathSelector.getJsonMap());
            annotationSelectorStoreRepository.createAnnotationXPathSelector(bodyPk, targetPk, selectorIri, value, selectorJson);
        }
    }

    private void createAnnotationCreators(W3CAnnotation w3cAnnotation) throws IOException {
        int annotationPk = w3cAnnotation.getPk();
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();
        createAnnotationCreators(annotationPk, null, null, jsonMap);
    }

    private void createAnnotationCreators(Integer annotationPk, Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationAgent> annotationCreators = new AnnotationCreatorExtractor().extractCreators(jsonMap);
        for (AnnotationAgent annotationCreator : annotationCreators) {

            String creatorIri = annotationCreator.getAgentIri();
            String creatorJson = JsonUtils.toString(annotationCreator.getJsonMap());

            String[] types = annotationCreator.getTypes() != null ? annotationCreator.getTypes().toArray(new String[0]) : new String[]{};
            String[] typesJson = new String[types.length];
            if (types.length > 0) {
                for (int i = 0; i < typesJson.length; i++) {
                    typesJson[i] = JsonUtils.toString(annotationCreator.getTypesJsonList().get(i));
                }
            }

            String[] names = annotationCreator.getNames() != null ? annotationCreator.getNames().toArray(new String[0]) : new String[]{};
            String[] namesJson = new String[names.length];
            if (names.length > 0) {
                for (int i = 0; i < namesJson.length; i++) {
                    namesJson[i] = JsonUtils.toString(annotationCreator.getNameJsonMaps().get(i));
                }
            }

            String nickname = annotationCreator.getNickname();

            String[] emails = annotationCreator.getEmails() != null ? annotationCreator.getEmails().toArray(new String[0]) : new String[]{};
            String[] emailsJson = new String[emails.length];
            if (emails.length > 0) {
                for (int i = 0; i < emailsJson.length; i++) {
                    emailsJson[i] = JsonUtils.toString(annotationCreator.getEmailJsonMaps().get(i));
                }
            }

            String[] emailSha1s = annotationCreator.getEmailSha1s() != null ? annotationCreator.getEmailSha1s().toArray(new String[0]) : new String[]{};
            String[] emailSha1sJson = new String[emailSha1s.length];
            if (emailSha1s.length > 0) {
                for (int i = 0; i < emailSha1sJson.length; i++) {
                    emailSha1sJson[i] = JsonUtils.toString(annotationCreator.getEmailSha1JsonMaps().get(i));
                }
            }

            String[] homepages = annotationCreator.getHomepages() != null ? annotationCreator.getHomepages().toArray(new String[0]) : new String[]{};
            String[] homepagesJson = new String[homepages.length];
            if (homepages.length > 0) {
                for (int i = 0; i < homepagesJson.length; i++) {
                    homepagesJson[i] = JsonUtils.toString(annotationCreator.getHomepageJsonMaps().get(i));
                }
            }

            annotationAgentStoreRepository.createAnnotationCreator(annotationPk, bodyPk, targetPk, creatorIri, creatorJson, types, typesJson, names, namesJson, nickname, emails, emailsJson, emailSha1s, emailSha1sJson, homepages, homepagesJson);
        }
    }

    private void createAnnotationGenerators(W3CAnnotation w3cAnnotation) throws IOException {
        int annotationPk = w3cAnnotation.getPk();
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();
        createAnnotationGenerators(annotationPk, null, null, jsonMap);
    }

    private void createAnnotationGenerators(Integer annotationPk, Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationAgent> annotationGenerators = new AnnotationGeneratorExtractor().extractGenerators(jsonMap);
        for (AnnotationAgent annotationGenerator : annotationGenerators) {

            String generatorIri = annotationGenerator.getAgentIri();
            String generatorJson = JsonUtils.toString(annotationGenerator.getJsonMap());

            String[] types = annotationGenerator.getTypes() != null ? annotationGenerator.getTypes().toArray(new String[0]) : new String[]{};
            String[] typesJson = new String[types.length];
            if (types.length > 0) {
                for (int i = 0; i < typesJson.length; i++) {
                    typesJson[i] = JsonUtils.toString(annotationGenerator.getTypesJsonList());
                }
            }

            String[] names = annotationGenerator.getNames() != null ? annotationGenerator.getNames().toArray(new String[0]) : new String[]{};
            String[] namesJson = new String[names.length];
            if (names.length > 0) {
                for (int i = 0; i < namesJson.length; i++) {
                    namesJson[i] = JsonUtils.toString(annotationGenerator.getNameJsonMaps().get(i));
                }
            }

            String nickname = annotationGenerator.getNickname();

            String[] emails = annotationGenerator.getEmails() != null ? annotationGenerator.getEmails().toArray(new String[0]) : new String[]{};
            String[] emailsJson = new String[emails.length];
            if (emails.length > 0) {
                for (int i = 0; i < emailsJson.length; i++) {
                    emailsJson[i] = JsonUtils.toString(annotationGenerator.getEmailJsonMaps().get(i));
                }
            }

            String[] emailSha1s = annotationGenerator.getEmailSha1s() != null ? annotationGenerator.getEmailSha1s().toArray(new String[0]) : new String[]{};
            String[] emailSha1sJson = new String[emailSha1s.length];
            if (emailSha1s.length > 0) {
                for (int i = 0; i < emailSha1sJson.length; i++) {
                    emailSha1sJson[i] = JsonUtils.toString(annotationGenerator.getEmailSha1JsonMaps().get(i));
                }
            }

            String[] homepages = annotationGenerator.getHomepages() != null ? annotationGenerator.getHomepages().toArray(new String[0]) : new String[]{};
            String[] homepagesJson = new String[homepages.length];
            if (homepages.length > 0) {
                for (int i = 0; i < homepagesJson.length; i++) {
                    homepagesJson[i] = JsonUtils.toString(annotationGenerator.getHomepageJsonMaps().get(i));
                }
            }

            annotationAgentStoreRepository.createAnnotationGenerator(annotationPk, bodyPk, targetPk, generatorIri, generatorJson, types, typesJson, names, namesJson, nickname, emails, emailsJson, emailSha1s, emailSha1sJson, homepages, homepagesJson);
        }
    }

    private void createAnnotationTemporals(W3CAnnotation w3cAnnotation) throws IOException {
        int annotationPk = w3cAnnotation.getPk();
        Map<String, Object> jsonMap = w3cAnnotation.getJsonMap();
        createAnnotationTemporals(annotationPk, null, null, jsonMap);
    }

    private void createAnnotationTemporals(Integer annotationPk, Integer bodyPk, Integer targetPk, Map<String, Object> jsonMap) throws IOException {
        List<AnnotationTemporal> annotationTemporals = new AnnotationTemporalExtractor().extractTemporals(jsonMap);
        for (AnnotationTemporal annotationTemporal : annotationTemporals) {
            String type = annotationTemporal.getType();
            Date value = annotationTemporal.getValue();
            String temporalJson = JsonUtils.toString(annotationTemporal.getJsonMap());
            annotationTemporalStoreRepository.createAnnotationTemporal(annotationPk, bodyPk, targetPk, type, value, temporalJson);
        }
    }

    private List<AnnotationBody> deleteBodies(W3CAnnotation w3cAnnotation) {
        int annotationPk = w3cAnnotation.getPk();
        return annotationBodyStoreRepository.deletedAnnotationBodies(annotationPk);
    }

    private List<AnnotationTarget> deleteTargets(W3CAnnotation w3cAnnotation) {
        int annotationPk = w3cAnnotation.getPk();
        return annotationTargetStoreRepository.deleteAnnotationTargets(annotationPk);
    }

    private void deleteAnnotationSelectors(Integer bodyPk, Integer targetPk) {
        deleteAnnotationCssSelectors(bodyPk, targetPk);
        deleteAnnotationDataPositionSelectors(bodyPk, targetPk);
        deleteAnnotationFragmentSelectors(bodyPk, targetPk);
        deleteAnnotationSvgSelectors(bodyPk, targetPk);
        deleteAnnotationTextPositionSelectors(bodyPk, targetPk);
        deleteAnnotationTextQuoteSelectors(bodyPk, targetPk);
        deleteAnnotationXPathSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationCssSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationCssSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationDataPositionSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationDataPositionSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationFragmentSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationFragmentSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationSvgSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationSvgSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationTextPositionSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationTextPositionSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationTextQuoteSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationTextQuoteSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationXPathSelectors(Integer bodyPk, Integer targetPk) {
        annotationSelectorStoreRepository.deleteAnnotationXPathSelectors(bodyPk, targetPk);
    }

    private void deleteAnnotationCreators(W3CAnnotation w3cAnnotation) {
        int annotationPk = w3cAnnotation.getPk();
        deleteAnnotationCreators(annotationPk, null, null);
    }

    private void deleteAnnotationCreators(Integer annotationPk, Integer bodyPk, Integer targetPk) {
        annotationAgentStoreRepository.deleteAnnotationCreators(annotationPk, bodyPk, targetPk);
    }

    private void deleteAnnotationGenerators(W3CAnnotation w3cAnnotation) {
        int annotationPk = w3cAnnotation.getPk();
        deleteAnnotationGenerators(annotationPk, null, null);
    }

    private void deleteAnnotationGenerators(Integer annotationPk, Integer bodyPk, Integer targetPk) {
        annotationAgentStoreRepository.deleteAnnotationGenerators(annotationPk, bodyPk, targetPk);
    }

    private void deleteAnnotationTemporals(W3CAnnotation w3cAnnotation) {
        int annotationPk = w3cAnnotation.getPk();
        deleteAnnotationTemporals(annotationPk, null, null);
    }

    private void deleteAnnotationTemporals(Integer annotationPk, Integer bodyPk, Integer targetPk) {
        annotationTemporalStoreRepository.deleteAnnotationTemporals(annotationPk, bodyPk, targetPk);
    }
}
