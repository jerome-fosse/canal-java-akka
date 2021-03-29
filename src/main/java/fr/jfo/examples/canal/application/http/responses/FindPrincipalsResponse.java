package fr.jfo.examples.canal.application.http.responses;

import fr.jfo.examples.canal.application.http.model.Principal;
import lombok.Value;

import java.util.List;

@Value
public class FindPrincipalsResponse {
    private final String titleName;
    private final List<Principal> principals;
}
