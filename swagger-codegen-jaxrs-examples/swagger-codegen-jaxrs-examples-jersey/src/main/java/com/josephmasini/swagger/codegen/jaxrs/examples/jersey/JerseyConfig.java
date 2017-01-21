package com.josephmasini.swagger.codegen.jaxrs.examples.jersey;

import org.glassfish.jersey.server.ResourceConfig;

/**
 * Created for Scentia by Joe on 20/01/2017.
 */
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(MessagesApiImpl.class);
    }

}
