package com.digirati.elucidate.model.annotation;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class AnnotationReference {

    private final String collectionId;
    private final String id;

    public AnnotationReference(String collectionId, String id) {
        this.collectionId = collectionId;
        this.id = id;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getCollectionId() {
        return collectionId;
    }
}
