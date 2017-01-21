package com.josephmasini.swagger.codegen.jaxrs.spec.ext.core;

import com.google.common.base.CaseFormat;
import io.swagger.codegen.*;
import io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Swagger;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

public class JavaJAXRSSpecServerExtendedCodegen extends JavaJAXRSSpecServerCodegen {

    public JavaJAXRSSpecServerExtendedCodegen() {

        super();
        CodegenModelFactory.setTypeMapping(CodegenModelType.OPERATION, ExtendedCodegenOperation.class);
        CodegenModelFactory.setTypeMapping(CodegenModelType.RESPONSE, ExtendedCodegenResponse.class);
    }

    @Override
    public CodegenOperation fromOperation(final String path,
                                          final String httpMethod,
                                          final Operation operation,
                                          final Map<String, Model> definitions,
                                          final Swagger swagger) {

        final ExtendedCodegenOperation op = (ExtendedCodegenOperation) super.fromOperation(path,
                                                                                           httpMethod,
                                                                                           operation,
                                                                                           definitions,
                                                                                           swagger);

        op.hasReturnType = op.returnType != null;
        op.hasNoReturnType = op.returnType == null;

        op.operationIdUpperCase = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
                                                                 httpMethod.concat(path.replace("/", "_")
                                                                                       .replace("{", "by_")
                                                                                       .replace("}", "")).toLowerCase());

        if (op.hasProduces) {
            for (final Map<String, String> produce : op.produces) {
                produce.put("friendlyName",
                            CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL,
                                                           produce.get("mediaType").replace("/", "_")));
            }
        }

        return op;
    }

    public String getName() {

        return "jaxrs-spec-ext";
    }

    @Override
    public Map<String, Object> postProcessOperations(final Map<String, Object> objs) {

        super.postProcessOperations(objs);

        final Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
        if (operations != null) {
            for (ExtendedCodegenOperation operation : (List<ExtendedCodegenOperation>) operations.get("operation")) {

                if (operation.returnType.equals("void")) {
                    operation.returnType = null;
                }

                final List<? extends CodegenResponse> responses = operation.responses;
                for (final ExtendedCodegenResponse response : (List<ExtendedCodegenResponse>) responses) {
                    final Response.Status status = Response.Status.fromStatusCode(Integer.parseInt(response.code));
                    response.hasCodeFriendlyName = status != null;
                    response.hasNoCodeFriendlyName = status == null;
                    response.codeFriendlyName = status == null ? null : status.getReasonPhrase().replace(" ", "");
                    response.isArray = response.containerType != null && response.containerType.equals("array");
                }
            }
        }

        return objs;
    }

    @Override
    public String getHelp() {

        return "Generates a Java JAXRS Server according to JAXRS 2.0 specification.";
    }

    @Override
    public void processOpts() {

        super.processOpts();

        supportingFiles.clear(); // Don't need extra files provided by AbstractJAX-RS & Java Codegen

        writeOptional(outputFolder,
                      new SupportingFile("ResponseWrapper.mustache",
                                         (sourceFolder + '/' + apiPackage).replace(".", "/"), "ResponseWrapper.java"));
        writeOptional(outputFolder,
                      new SupportingFile("RestApplication.mustache",
                                         (sourceFolder + '/' + invokerPackage).replace(".", "/"), "RestApplication.java"));
    }
}
