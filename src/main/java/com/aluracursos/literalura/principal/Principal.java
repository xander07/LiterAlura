package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.models.Libro;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.ConsumoApi;
import com.aluracursos.literalura.service.ConvertirDatos;

import java.util.Scanner;

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
                            //
                            break;

                        case 2:
                            //
                            break;

                        case 3:
                            //
                            break;

                        case 4:
                            //
                            break;

                        case 5:
                            //
                            break;

                        case 6:
                            //
                            break;

                        case 7:
                            //
                            break;

                        case 8:
                            //
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


}
