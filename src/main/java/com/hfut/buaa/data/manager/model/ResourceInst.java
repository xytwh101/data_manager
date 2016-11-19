package com.hfut.buaa.data.manager.model;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by tanweihan on 16/11/19.
 */
public class ResourceInst {
    private Resource resource;

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Properties getProperties() {
        Properties properties = new Properties();
        try {
            properties.load(resource.getInputStream());
            Object a = properties.get("hadoop-url");
            System.out.println(a);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
