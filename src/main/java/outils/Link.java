package outils;

import java.net.URI;


public class Link {

    private String rel;
    private String httpMethod;
    private URI uri;

    public Link(String rel, String httpMethod, URI uri){
        this.rel = rel;
        this.httpMethod = httpMethod;
        this.uri = uri;
    }
}
