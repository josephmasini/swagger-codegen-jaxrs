package com.josephmasini.swagger.codegen.jaxrs.spec.ext.core;

import io.swagger.codegen.CodegenResponse;

public class ExtendedCodegenResponse extends CodegenResponse {

    public boolean hasCodeFriendlyName;
    public boolean hasNoCodeFriendlyName;
    public String codeFriendlyName;
    public boolean isArray;

    public ExtendedCodegenResponse() {

        super();
    }
}
