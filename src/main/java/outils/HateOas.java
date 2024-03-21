package outils;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HateOas {

    private List<Link> listeLinks;
    private String message;

    public void addLink(Link link){
        listeLinks.add(link);
    }

}
