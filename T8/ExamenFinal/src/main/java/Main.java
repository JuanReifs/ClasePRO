import model.Libro;
import model.Biblioteca;
import model.Autor;
import controller.BibliotecaController;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        Libro libros = new Libro();
        Autor autores = new Autor();
        Biblioteca biblioteca = new Biblioteca();
        BibliotecaController bibliotecaController = new BibliotecaController();

        int opcion;
        do {
            System.out.println("1: Añadir libro");
            System.out.println("2.");
            System.out.println("3.");
            System.out.println("4.");
            System.out.println("5.");
            System.out.println("6.");
            System.out.println("7.");
            System.out.println("8.");
            opcion = scanner.nextInt();
            switch (opcion) {
               case 1 ->{
                   bibliotecaController.guardarLibro(libros);
               }

            }
        } while (opcion != 0);


    }
}