package com.aluracursos.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AutorData(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaDeNacimiento,
        @JsonAlias("death_year") Integer fechaDeMuerte
) {
}