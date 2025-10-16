package br.edu.ifpr.tempconv.restful.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("webapi")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(br.edu.ifpr.tempconv.restful.TemperatureResource.class);
        return resources;
    }
}

