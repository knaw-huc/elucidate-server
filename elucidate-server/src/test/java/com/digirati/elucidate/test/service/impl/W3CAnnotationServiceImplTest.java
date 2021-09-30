package com.digirati.elucidate.test.service.impl;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation;
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection;
import com.digirati.elucidate.common.service.IRIBuilderService;
import com.digirati.elucidate.infrastructure.generator.IDGenerator;
import com.digirati.elucidate.infrastructure.security.impl.DefaultUserSecurityDetailsContext;
import com.digirati.elucidate.repository.AnnotationStoreRepository;
import com.digirati.elucidate.service.query.AbstractAnnotationService;
import com.digirati.elucidate.service.query.impl.W3CAnnotationServiceImpl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"javax.script.*", "javax.management.*"})
public class W3CAnnotationServiceImplTest extends AbstractAnnotationServiceImplTest<W3CAnnotation, W3CAnnotationCollection> {

    @Override
    protected AbstractAnnotationService<W3CAnnotation> createAnnotationService(IRIBuilderService iriBuilderService, AnnotationStoreRepository annotationStoreRepository) {
        return new W3CAnnotationServiceImpl(new DefaultUserSecurityDetailsContext(), annotationStoreRepository, iriBuilderService, new StaticIDGenerator());
    }

    @Override
    protected void validateConversionToAnnotation(W3CAnnotation w3cAnnotation, W3CAnnotation targetAnnotation) {
        assertThat(w3cAnnotation, is(equalTo(targetAnnotation)));
    }

    @Override
    protected W3CAnnotation generateAnnotationWithJsonMapOnly() {
        W3CAnnotation w3cAnnotation = new W3CAnnotation();
        w3cAnnotation.setJsonMap(generateRandomJsonMap());
        return w3cAnnotation;
    }

    private static class StaticIDGenerator implements IDGenerator {

        @Override
        public String generateId() {
            return "test-annotation-id";
        }
    }
}
