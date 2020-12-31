package com.hywx.zuulservice.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: sat-cloud
 * @description:
 * @author: tangjing
 * @create: 2020-06-09 09:53
 **/
@Primary
@Component
public class SwaggerDocConfigure implements SwaggerResourcesProvider {

    @Value("${swagger.doc.resources}")
    private  String resourceList ;

    public String getResourceList() {
        return resourceList;
    }

    public void setResourceList(String resourceList) {
        this.resourceList = resourceList;
    }
    private final RouteLocator routeLocator;

    public SwaggerDocConfigure(RouteLocator routeLocator) {
        this.routeLocator = routeLocator;
    }

    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<Route> routes = routeLocator.getRoutes();
        List<String> swaggerResoureceList = Arrays.asList(StringUtils.splitByWholeSeparatorPreserveAllTokens(resourceList, ","));
        for (Route route : routes) {
            if(swaggerResoureceList.contains(route.getLocation())){
                resources.add(swaggerResource(route.getId(), route.getFullPath().replace("/**", "/v2/api-docs"), "2.0"));
            }
        }

//			routes.forEach(route -> resources.add(swaggerResource(route.getId(), route.getFullPath().replace("/**", "/v2/api-docs"),"2.0")));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }


}
