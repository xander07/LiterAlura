package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.models.*;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoApi;
import com.aluracursos.literalura.service.ConvertirDatos;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.awt.print.Book;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvertirDatos convertirDatos = new ConvertirDatos();
    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String URL_SEARCH = "?search=";
    private final LibroRepository repository;


    public Principal(LibroRepository repository) {
        this.repository = repository;
    }

    public void mostrarMenu(){
        var opcion = -1;

        while(opcion != 0) {

            String menu = """
                    1 - Buscar libro por titulo
                    2 - Mostrar libros registrados
                    3 - Mostrar Autores registrados
                    4 - Buscar Autores en un determinado año
                    5 - Mostrar libros por idioma
                    6 - Mostrar top 10 de los mejores libros
                    7 - Mostrar estadísticas generales
                    8 - Buscar autor por nombre
                    
                    0 - Salir
                    """;

            while (opcion != 0) {
                System.out.println(menu);
                opcion = scanner.nextInt();
                scanner.nextLine();

                try {
                    switch (opcion) {
                        case 1:
                            buscarLibroPorTitulo();
                            break;

                        case 2:
                            mostrarLibros();
                            break;

                        case 3:
                            mostrarAutores();
                            break;

                        case 4:
                            buscarAutoresPorAnno();
                            break;

                        case 5:
                            buscarLibrosPorIdioma();
                            break;

                        case 6:
                            mostrarTop10();
                            break;

                        case 7:
                            estadisticas();
                            break;

                        case 8:
                            buscarAutorPorNombre();
                            break;

                        case 0:
                            break;
                        default:
                            System.out.println("Opción Inválida");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Opción inválida");
                }
            }

        }
    }


    private void buscarLibroPorTitulo(){
        System.out.println("Ingrese el título del libro que quiere buscar: ");

        String titulo = scanner.nextLine().replace(" ", "%20");
        var json = consumoApi.obtenerDatos(URL_BASE +
                URL_SEARCH + titulo);

        var datos = convertirDatos.gettingData(json, Datos.class);

        Optional<LibroData> busquedaLibro = datos.libros()
                .stream()
                .findFirst();

        if (busquedaLibro.isPresent()) {
            System.out.println(
                    "********* LIBRO *********\n" +
                    "Titulo: " + busquedaLibro.get().titulo() +
                    "\n Autor: " +  busquedaLibro.get().autores().stream()
                            .map(autor -> autor.nombre())
                            .limit(1).collect(Collectors.joining()) +
                    "\n Idioma: " + busquedaLibro.get().idiomas().stream().collect(Collectors.joining()) +
                    "\n Número de descargas: " + busquedaLibro.get().numeroDeDescargas() +
                    "\n *************************"
            );

            try {
                List<Libro> libroEncontrado;

                libroEncontrado = busquedaLibro.stream()
                        .map(libro -> new Libro(libro))
                        .collect(Collectors.toList());

                Autor autorEncontrado = busquedaLibro.stream()
                        .flatMap(l -> l.autores().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList())
                        .stream().findFirst().get();

                Optional<Autor> autoresBd = repository.findAutorByNombreContaining(
                        busquedaLibro.get().autores()
                                .stream()
                                .map(a -> a.nombre())
                                .collect(Collectors.joining())
                );

                Optional<Libro> optionalLibro = repository.getLibroContainsEqualsIgnoreCaseTitulo(titulo);

                if (optionalLibro.isPresent()) {
                    System.out.println("El libro ya existe en la Base de Datos");
                } else {
                    Autor autor;
                    if (autoresBd.isPresent()) {
                        autor = autoresBd.get();
                        System.out.println("El autor ya existe en la Base de Datos");
                    } else {
                        autor = autorEncontrado;
                        repository.save(autor);
                    }

                    autor.setLibros(libroEncontrado);
                    repository.save(autor);
                }

            } catch (Exception e){
                System.out.println("Ha ocurrido un error: " + e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado");
        }
    }

    public void buscarAutorPorNombre() {
        System.out.println(" Ingrese el nombre del autor que desea buscar: ");
        String nombre = scanner.nextLine();

        Optional<Autor> autor = repository.findAutorByNombreContaining(nombre);

        if (autor.isPresent()) {
            System.out.println(
                    "********** AUTOR **********\n" +
                    "Autor: " + autor.get().getNombre() +
                    "\nFecha de nacimiento: " + autor.get().getFechaDeNacimiento() +
                    "\nFecha de muerte: " + autor.get().getFechaDeMuerte() +
                    "\nLibros: " + autor.get().getLibros().stream()
                            .map(l -> l.getTitulo())
                            .collect(Collectors.toList()) +
                    "\n***************************"
            );
        } else {
            System.out.println("El autor no está registrado en la Base de Datos");
        }
    }

    public void mostrarLibros() {
        List<Libro> libros = repository.findLibrosByAutor();

        libros.forEach(l -> System.out.println(
                "********** LIBRO **********\n" +
                "Titulo: " + l.getTitulo() +
                "\n Autor: " + l.getAutor().getNombre() +
                "\n Idioma: " + l.getIdioma().getIdioma() +
                "\n Número de descargas: " + l.getNumeroDeDescargas() +
                "\n ***************************"
        ));
    }

    public void mostrarAutores () {
        List<Autor> autores = repository.findAll();

        autores.forEach(a -> System.out.println(
                "********** Autor **********\n" +
                "Autor: " + a.getNombre() +
                "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                "\nFecha de muerte: " + a.getFechaDeMuerte() +
                "\n Libros: " + a.getLibros().stream()
                        .map(l -> l.getTitulo())
                        .collect(Collectors.toList()) +
                "\n***************************"
        ));
    }

    public void buscarAutoresPorAnno() {
        System.out.println("Ingrese el año para el que quieres ver los autores que están vivos: ");

        try {
            Integer anno = Integer.valueOf(scanner.nextLine());
            List<Autor> autores = repository.getAutorbyfechaDeMuerte(anno);

            if (!autores.isEmpty()) {
                autores.forEach(a -> System.out.println(
                        "********** Autor **********\n" +
                                "Autor: " + a.getNombre() +
                                "\nFecha de nacimiento: " + a.getFechaDeNacimiento() +
                                "\nFecha de muerte: " + a.getFechaDeMuerte() +
                                "\n Libros: " + a.getLibros().stream()
                                .map(l -> l.getTitulo())
                                .collect(Collectors.toList()) +
                                "\n***************************"
                ));
            } else {
                System.out.println("Lo sentimos, no hay autores vivos para el año: " + anno);
            }
        } catch (NumberFormatException e){
            System.out.println("Por favor ingrese un número válido");
        }
    }

    public void buscarLibrosPorIdioma() {
        String listaIdiomas = """
                Escoje el idioma para buscar libros
                
                en - Inglés
                es - Español
                fr - Frances
                it - Italiano
                pt - Portugués
                """;

        System.out.println(listaIdiomas);
        String idioma = scanner.nextLine().toLowerCase();

        if (idioma.equalsIgnoreCase("en")
            || idioma.equalsIgnoreCase("es")
            || idioma.equalsIgnoreCase("fr")
            || idioma.equalsIgnoreCase("it")
            || idioma.equalsIgnoreCase("pt")) {

            Lenguaje idiom = Lenguaje.fromString(idioma);
            List<Libro> libros = repository.findLibroByIdioma(idiom);

            if (libros.isEmpty()) {
                System.out.println("Lo sentimos, no tenemos libros registrados en ese idioma");
            } else {
                libros.forEach(l -> System.out.println(
                        "********** LIBRO **********\n" +
                                "Titulo: " + l.getTitulo() +
                                "\n Autor: " + l.getAutor().getNombre() +
                                "\n Idioma: " + l.getIdioma().getIdioma() +
                                "\n Número de descargas: " + l.getNumeroDeDescargas() +
                                "\n ***************************"
                ));
            }
        } else {
            System.out.println("Ingrese un Idioma Válido");
        }

    }

    private void mostrarTop10() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        var info = convertirDatos.gettingData(json, Datos.class);

        info.libros().stream()
                .sorted(Comparator.comparing(LibroData::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
    }

    public void estadisticas() {
        var json = consumoApi.obtenerDatos(URL_BASE);
        var info = convertirDatos.gettingData(json, Datos.class);

        IntSummaryStatistics est = info.libros().stream()
                .filter(e -> e.numeroDeDescargas() > 0)
                .collect(Collectors.summarizingInt(LibroData::numeroDeDescargas));

        Integer promedio = (int) est.getAverage();
        System.out.println(
                "********** ESTADISTICAS **********\n" +
                "\n Cantidad promedio de descargas: " + est.getAverage() +
                "\n Máximo número de descargas: " + est.getMax() +
                "\n Mínimo número de descargas: " + est.getMin() +
                "\n Total de libros evaluados: " + est.getCount() +
                "\n**********************************"
        );
    }




}
