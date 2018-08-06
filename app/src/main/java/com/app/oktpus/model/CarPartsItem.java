package com.app.oktpus.model;

import org.codehaus.jackson.node.ObjectNode;

/**
 * Created by Gyandeep on 23/7/17.
 */

public class CarPartsItem {

    String name;
    //@SerializedName("external_url")
    String url;
    //@SerializedName("children")
    ObjectNode children;

    public CarPartsItem(String name, String url, ObjectNode children) {
        this.name = name;
        this.url = url;
        this.children = children;
    };

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ObjectNode getChildren() {
        return children;
    }
}