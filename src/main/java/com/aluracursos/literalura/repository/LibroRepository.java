package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.models.Autor;
import com.aluracursos.literalura.models.Lenguaje;
import com.aluracursos.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findAutorByNombreContaining(String nombre);

    @Query("SELECT l FROM libros l JOIN l.autor a WHERE l.titulo LIKE %:bTitulo%")
    Optional<Libro> getLibroContainsEqualsIgnoreCaseTitulo(String bTitulo);

    @Query("SELECT l From autores a join a.libros l")
    List<Libro> findLibrosByAutor();

    @Query("SELECT a FROM autores a WHERE a.fechaDeMuerte > :date ")
    List<Autor> getAutorbyfechaDeMuerte(Integer date);


    @Query("SELECT l FROM autores a join a.libros l WHERE l.idioma = :idioma")
    List<Libro> findLibroByIdioma(Lenguaje idioma);
}
