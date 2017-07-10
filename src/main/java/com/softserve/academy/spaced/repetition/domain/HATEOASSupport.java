package com.softserve.academy.spaced.repetition.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

public class HATEOASSupport {
    private final List<Link> links;
    //Link link;

    public HATEOASSupport() {
        links = new LinkedList<>();
    }

    public void add(Link link) {
        this.links.add(link);
       // this.link = link;
    }

//    public Link getLink(){
//        return this.link;
//    }

    @JsonProperty("_links")
    public List<Link> getLinks() {
        return links;
    }
}
