import java.util.Scanner;

import controller.GestorProductos;

public class Main {


    Scanner scanner;
    static void main(String[] args) {

     GestorProductos gestorProductos = new GestorProductos();
        Scanner scanner = new Scanner(System.in);

        int opcion = 0;
        do {
            System.out.println("Elige una opcion");
            System.out.println("1. Crear producto y almacenarlo en la base de datos");
            System.out.println("2.Eliminar un producto");
            System.out.println("3. Buscar producto por ID");
            System.out.println("4.Actualizar producto buscado por id");
            System.out.println("5. Mostrar todos");
            System.out.println("6. Finalizar proceso");
            opcion = scanner.nextInt();

            switch (opcion){
                case 1->{
                    System.out.println("Nombre");
                    String nombre = scanner.next();
                    System.out.println("Precio");
                    Double precio = scanner.nextDouble();
                    System.out.println("Cantidad");
                    int cantidad = scanner.nextInt();
                    gestorProductos.crearPRoducto(nombre, precio, cantidad);

                }
                case 2->{
                    System.out.println("Introduce el ID");
                    Long id = scanner.nextLong();

                    gestorProductos.borrarProducto(id);

                }
                case 3->{
                    System.out.println("Introduce el ID");
                    Long id = scanner.nextLong();
                gestorProductos.buscarPorID(id);
                }
                case 4->{

                }
                case 5->{
                gestorProductos.mostrarTodos();
                }
            }
        }while(opcion !=6);
    }
}
