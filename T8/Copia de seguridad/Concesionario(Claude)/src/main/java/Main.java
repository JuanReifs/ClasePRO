import controller.ConcesionarioController;
import database.HibernateUtil;

import java.util.Scanner;

/**
 * Punto de entrada de la aplicación Concesionario ORM.
 *
 * <p>Gestiona el menú interactivo y delega cada opción al
 * {@link ConcesionarioController}.</p>
 *
 * <p>Se encarga también de inicializar y cerrar correctamente
 * la conexión con Hibernate al inicio y al final.</p>
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inicializar Hibernate (crea el SessionFactory y las tablas si no existen)
        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.err.println("╔══════════════════════════════════════════════════════╗");
            System.err.println("║  ERROR CRÍTICO: No se pudo conectar a la base de datos ║");
            System.err.println("║  Verifica hibernate.cfg.xml y que MySQL esté activo.   ║");
            System.err.println("╚══════════════════════════════════════════════════════╝");
            System.err.println("Detalle: " + e.getMessage());
            return;
        }

        ConcesionarioController controller = new ConcesionarioController(scanner);
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenu();

            System.out.print("  Elige una opción: ");
            String entrada = scanner.nextLine().trim();

            System.out.println();

            switch (entrada) {
                case "1"  -> controller.cargarDesdeCSV();
                case "2"  -> controller.mostrarTodos();
                case "3"  -> controller.buscarPorMatricula();
                case "4"  -> controller.buscarPorMarca();
                case "5"  -> controller.buscarPorModelo();
                case "6"  -> controller.buscarPorRangoPrecio();
                case "7"  -> controller.buscarPorEstado();
                case "8"  -> controller.agregarCoche();
                case "9"  -> controller.modificarCoche();
                case "10" -> controller.venderCoche();
                case "11" -> controller.eliminarCoche();
                case "12" -> controller.buscarPorCombustible();
                case "13" -> controller.mostrarPorMarca();
                case "14" -> controller.mostrarDisponibles();
                case "15" -> controller.mostrarVendidos();
                case "16" -> controller.guardarEnCSV();
                case "17" -> {
                    ejecutando = false;
                    System.out.println("  Hasta pronto. ¡Cerrando aplicación!");
                }

                default   -> System.out.println("  Opción no válida. Elige un número del 1 al 17.");
            }

            if (ejecutando) {
                System.out.println("\n  Pulsa Enter para continuar...");
                scanner.nextLine();
            }
        }

        // Cerrar recursos al salir
        scanner.close();
        HibernateUtil.shutdown();
    }

    /**
     * Imprime el menú principal en consola.
     */
    private static void mostrarMenu() {
        System.out.println("""

        ╔══════════════════════════════════════════════════╗
        ║          🚗  CONCESIONARIO ORM  🚗               ║
        ╠══════════════════════════════════════════════════╣
        ║  DATOS                                           ║
        ║   1. Cargar coches desde CSV                     ║
        ║   2. Mostrar todos los coches                    ║
        ║  16. Guardar cambios en CSV                      ║
        ╠══════════════════════════════════════════════════╣
        ║  BÚSQUEDAS                                       ║
        ║   3. Buscar por matrícula                        ║
        ║   4. Buscar por marca                            ║
        ║   5. Buscar por modelo                           ║
        ║   6. Buscar por rango de precio                  ║
        ║   7. Buscar por estado (disponible/vendido)      ║
        ║  12. Buscar por tipo de combustible              ║
        ╠══════════════════════════════════════════════════╣
        ║  LISTADOS RÁPIDOS                                ║
        ║  13. Ver todos los coches de una marca           ║
        ║  14. Ver solo coches disponibles                 ║
        ║  15. Ver solo coches vendidos                    ║
        ╠══════════════════════════════════════════════════╣
        ║  GESTIÓN                                         ║
        ║   8. Agregar nuevo coche                         ║
        ║   9. Modificar datos de un coche                 ║
        ║  10. Vender un coche                             ║
        ║  11. Eliminar un coche                           ║
        ╠══════════════════════════════════════════════════╣
        ║   0. Generar CSV de ejemplo (coches.csv)         ║
        ║  17. Salir                                       ║
        ╚══════════════════════════════════════════════════╝""");
    }
}
