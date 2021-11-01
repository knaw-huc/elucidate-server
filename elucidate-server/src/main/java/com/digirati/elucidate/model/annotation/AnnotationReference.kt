package com.digirati.elucidate.model.annotation

import com.fasterxml.jackson.annotation.JsonProperty

class AnnotationReference(@get:JsonProperty val collectionId: String, @get:JsonProperty val id: String)