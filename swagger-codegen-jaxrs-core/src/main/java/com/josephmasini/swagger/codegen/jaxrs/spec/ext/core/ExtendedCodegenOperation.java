package com.josephmasini.swagger.codegen.jaxrs.spec.ext.core;

import io.swagger.codegen.CodegenOperation;

public class ExtendedCodegenOperation extends CodegenOperation {

    public String operationIdUpperCase;
    public boolean hasReturnType;
    public boolean hasNoReturnType;

    public ExtendedCodegenOperation() {

        super();
    }
}
