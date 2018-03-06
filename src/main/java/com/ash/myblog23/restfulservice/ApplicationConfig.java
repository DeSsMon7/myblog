package com.ash.myblog23.restfulservice;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Tmmy
 */

@javax.ws.rs.ApplicationPath("restfulservice")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        //add all resources classes
        resources.add(com.ash.myblog23.restfulservice.RecoveryService.class);
    }
}