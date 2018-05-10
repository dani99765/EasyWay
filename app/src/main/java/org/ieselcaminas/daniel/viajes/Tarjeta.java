package org.ieselcaminas.daniel.viajes;

public class Tarjeta {
    private String textInicial;
    private String textFinal;
    private String textLocation;


    public Tarjeta(String textInicial, String textFinal, String textLocation) {
        this.textInicial = textInicial;
        this.textFinal = textFinal;
        this.textLocation = textLocation;
    }

    public String getTextInicial() {
        return textInicial;
    }

    public void setTextInicial(String textInicial) {
        this.textInicial = textInicial;
    }

    public String getTextFinal() {
        return textFinal;
    }

    public void setTextFinal(String textFinal) {
        this.textFinal = textFinal;
    }

    public String getTextLocation() {
        return textLocation;
    }

    public void setTextLocation(String textLocation) {
        this.textLocation = textLocation;
    }
}