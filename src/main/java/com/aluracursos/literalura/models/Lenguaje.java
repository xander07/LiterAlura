package com.aluracursos.literalura.models;

public enum Lenguaje {
    ES("es"),
    EN("en"),
    FR("fr"),
    IT("it"),
    PT("pt");

    private String idioma;

    Lenguaje(String idioma) {
        this.idioma = idioma;
    }

    public static Lenguaje fromString(String idioma) {
        for (Lenguaje l : Lenguaje.values()) {
            if(l.idioma.equals(idioma)){
                return l;
            }
        }
        throw new IllegalArgumentException("Idioma no soportado: " + idioma);
    }

    public String getIdioma() {
        return this.idioma;
    }
}
