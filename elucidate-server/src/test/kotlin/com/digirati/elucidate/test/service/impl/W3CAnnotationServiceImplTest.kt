package com.digirati.elucidate.test.service.impl

import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotation
import com.digirati.elucidate.common.model.annotation.w3c.W3CAnnotationCollection
import com.digirati.elucidate.common.service.IRIBuilderService
import com.digirati.elucidate.infrastructure.generator.IDGenerator
import com.digirati.elucidate.infrastructure.security.impl.DefaultUserSecurityDetailsContext
import com.digirati.elucidate.repository.AnnotationStoreRepository
import com.digirati.elucidate.service.query.AbstractAnnotationService
import com.digirati.elucidate.service.query.impl.W3CAnnotationServiceImpl
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PowerMockIgnore("javax.script.*", "javax.management.*")
class W3CAnnotationServiceImplTest : AbstractAnnotationServiceImplTest<W3CAnnotation, W3CAnnotationCollection>() {
    override fun createAnnotationService(
            iriBuilderService: IRIBuilderService?,
            annotationStoreRepository: AnnotationStoreRepository?
    ): AbstractAnnotationService<W3CAnnotation> {
        return W3CAnnotationServiceImpl(
                DefaultUserSecurityDetailsContext(),
                annotationStoreRepository,
                iriBuilderService,
                StaticIDGenerator()
        )
    }

    override fun validateConversionToAnnotation(w3cAnnotation: W3CAnnotation, targetAnnotation: W3CAnnotation) {
        MatcherAssert.assertThat(w3cAnnotation, Matchers.`is`(Matchers.equalTo(targetAnnotation)))
    }

    override fun generateAnnotationWithJsonMapOnly(): W3CAnnotation {
        val w3cAnnotation = W3CAnnotation()
        w3cAnnotation.jsonMap = generateRandomJsonMap()
        return w3cAnnotation
    }

    private class StaticIDGenerator : IDGenerator {
        override fun generateId(): String {
            return "test-annotation-id"
        }
    }
}