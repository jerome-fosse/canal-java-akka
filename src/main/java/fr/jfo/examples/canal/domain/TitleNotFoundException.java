package fr.jfo.examples.canal.domain;

public class TitleNotFoundException extends RuntimeException {

    public TitleNotFoundException() {
    }

    public TitleNotFoundException(String message) {
        super(message);
    }
}
