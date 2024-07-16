package com.aluracursos.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.JoinColumn;

import java.util.List;

public record LibroData(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<AutorData> autores,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Integer numeroDeDescargas
        ) {
}
