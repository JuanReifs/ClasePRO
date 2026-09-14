package controller;

import dao.CocheDAO;
import dao.MarcaDAO;
import model.Coche;
import model.Combustible;
import model.Estado;
import model.Marca;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Controlador principal del concesionario.
 *
 * <p>Contiene toda la lógica de negocio: gestión de coches, búsquedas,
 * modificaciones, ventas y delegación de operaciones CSV al
 * {@link FileController}.</p>
 *
 * <p>Actúa como intermediario entre la capa de presentación ({@code Main})
 * y la capa de acceso a datos ({@code DAO}).</p>
 */
public class ConcesionarioController {

    private final CocheDAO       cocheDAO;
    private final MarcaDAO       marcaDAO;
    private final FileController fileController;
    private final Scanner        scanner;

    public ConcesionarioController(Scanner scanner) {
        this.cocheDAO       = new CocheDAO();
        this.marcaDAO       = new MarcaDAO();
        this.fileController = new FileController();
        this.scanner        = scanner;
    }

    // ===================================================================
    //  1. CARGAR COCHES DESDE CSV
    // ===================================================================

    /**
     * Solicita al usuario la ruta del CSV y delega en {@link FileController}.
     */
    public void cargarDesdeCSV() {
        System.out.print("\n  Ruta del archivo CSV (Enter para 'coches.csv'): ");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) ruta = "coches.csv";

        try {
            int n = fileController.importarDesdeCSV(ruta);
            System.out.println("  ✔ Se importaron " + n + " coches correctamente.");
        } catch (IOException e) {
            System.err.println("  ✘ Error al leer el archivo: " + e.getMessage());
        }
    }

    // ===================================================================
    //  2. MOSTRAR TODOS LOS COCHES
    // ===================================================================

    public void mostrarTodos() {
        List<Coche> coches = cocheDAO.listarTodos();
        if (coches.isEmpty()) {
            System.out.println("\n  No hay coches registrados.");
        } else {
            System.out.println("\n  ══ TODOS LOS COCHES (" + coches.size() + ") ══");
            coches.forEach(c -> System.out.println(c));
        }
    }

    // ===================================================================
    //  3. BUSCAR POR MATRÍCULA
    // ===================================================================

    public void buscarPorMatricula() {
        System.out.print("\n  Introduce la matrícula: ");
        String matricula = scanner.nextLine().trim();

        cocheDAO.buscarPorMatricula(matricula)
                .ifPresentOrElse(
                        c -> System.out.println("\n" + c),
                        () -> System.out.println("  No se encontró ningún coche con esa matrícula.")
                );
    }

    // ===================================================================
    //  4. BUSCAR POR MARCA
    // ===================================================================

    public void buscarPorMarca() {
        System.out.print("\n  Introduce la marca: ");
        String marca = scanner.nextLine().trim();

        List<Coche> coches = cocheDAO.buscarPorMarca(marca);
        mostrarListado("COCHES DE LA MARCA: " + marca.toUpperCase(), coches);
    }

    // ===================================================================
    //  5. BUSCAR POR MODELO
    // ===================================================================

    public void buscarPorModelo() {
        System.out.print("\n  Introduce el modelo: ");
        String modelo = scanner.nextLine().trim();

        List<Coche> coches = cocheDAO.buscarPorModelo(modelo);
        mostrarListado("COCHES DEL MODELO: " + modelo.toUpperCase(), coches);
    }

    // ===================================================================
    //  6. BUSCAR POR RANGO DE PRECIO
    // ===================================================================

    public void buscarPorRangoPrecio() {
        try {
            System.out.print("\n  Precio mínimo (€): ");
            double min = Double.parseDouble(scanner.nextLine().trim());

            System.out.print("  Precio máximo (€): ");
            double max = Double.parseDouble(scanner.nextLine().trim());

            if (min > max) {
                System.out.println("  El precio mínimo no puede ser mayor que el máximo.");
                return;
            }

            List<Coche> coches = cocheDAO.buscarPorRangoPrecio(min, max);
            mostrarListado(String.format("COCHES ENTRE %.2f€ Y %.2f€", min, max), coches);

        } catch (NumberFormatException e) {
            System.out.println("  Precio inválido. Introduce un número.");
        }
    }

    // ===================================================================
    //  7. BUSCAR POR ESTADO
    // ===================================================================

    public void buscarPorEstado() {
        System.out.println("\n  Estado: 1) DISPONIBLE   2) VENDIDO");
        System.out.print("  Elige opción: ");
        String opcion = scanner.nextLine().trim();

        Estado estado = switch (opcion) {
            case "1" -> Estado.DISPONIBLE;
            case "2" -> Estado.VENDIDO;
            default  -> null;
        };

        if (estado == null) {
            System.out.println("  Opción inválida.");
            return;
        }

        List<Coche> coches = cocheDAO.buscarPorEstado(estado);
        mostrarListado("COCHES " + estado.name(), coches);
    }

    // ===================================================================
    //  8. AGREGAR NUEVO COCHE
    // ===================================================================

    public void agregarCoche() {
        System.out.println("\n  ══ AGREGAR NUEVO COCHE ══");

        try {
            System.out.print("  Matrícula: ");
            String matricula = scanner.nextLine().trim().toUpperCase();

            if (cocheDAO.existeMatricula(matricula)) {
                System.out.println("  ✘ Ya existe un coche con esa matrícula.");
                return;
            }

            System.out.print("  Marca: ");
            String nombreMarca = scanner.nextLine().trim();
            Marca marca = marcaDAO.buscarOCrear(nombreMarca);

            System.out.print("  Modelo: ");
            String modelo = scanner.nextLine().trim();

            System.out.print("  Color: ");
            String color = scanner.nextLine().trim();

            Combustible combustible = pedirCombustible();
            if (combustible == null) return;

            System.out.print("  Año de fabricación: ");
            int anio = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Kilómetros: ");
            int km = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("  Precio (€): ");
            double precio = Double.parseDouble(scanner.nextLine().trim());

            Coche nuevo = new Coche(matricula, marca, modelo, color,
                    combustible, anio, km, precio, Estado.DISPONIBLE);

            cocheDAO.guardar(nuevo);
            System.out.println("\n  ✔ Coche agregado correctamente con ID: " + nuevo.getId());

        } catch (NumberFormatException e) {
            System.out.println("  ✘ Valor numérico inválido: " + e.getMessage());
        }
    }

    // ===================================================================
    //  9. MODIFICAR DATOS DE UN COCHE
    // ===================================================================

    public void modificarCoche() {
        System.out.print("\n  Introduce la matrícula del coche a modificar: ");
        String matricula = scanner.nextLine().trim();

        Optional<Coche> opt = cocheDAO.buscarPorMatricula(matricula);
        if (opt.isEmpty()) {
            System.out.println("  No se encontró ningún coche con esa matrícula.");
            return;
        }

        Coche coche = opt.get();
        System.out.println("\n  Coche actual:");
        System.out.println(coche);
        System.out.println("\n  (Pulsa Enter para mantener el valor actual)\n");

        try {
            System.out.printf("  Modelo [%s]: ", coche.getModelo());
            String modelo = scanner.nextLine().trim();
            if (!modelo.isEmpty()) coche.setModelo(modelo);

            System.out.printf("  Color [%s]: ", coche.getColor());
            String color = scanner.nextLine().trim();
            if (!color.isEmpty()) coche.setColor(color);

            System.out.printf("  Año [%d]: ", coche.getAnio());
            String anioStr = scanner.nextLine().trim();
            if (!anioStr.isEmpty()) coche.setAnio(Integer.parseInt(anioStr));

            System.out.printf("  Kilómetros [%d]: ", coche.getKilometros());
            String kmStr = scanner.nextLine().trim();
            if (!kmStr.isEmpty()) coche.setKilometros(Integer.parseInt(kmStr));

            System.out.printf("  Precio [%.2f]: ", coche.getPrecio());
            String precioStr = scanner.nextLine().trim();
            if (!precioStr.isEmpty()) coche.setPrecio(Double.parseDouble(precioStr));

            System.out.println("  ¿Cambiar combustible? (s/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                Combustible c = pedirCombustible();
                if (c != null) coche.setCombustible(c);
            }

            cocheDAO.actualizar(coche);
            System.out.println("\n  ✔ Coche actualizado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("  ✘ Valor numérico inválido. No se guardaron los cambios.");
        }
    }

    // ===================================================================
    //  10. VENDER UN COCHE
    // ===================================================================

    public void venderCoche() {
        System.out.print("\n  Introduce la matrícula del coche a vender: ");
        String matricula = scanner.nextLine().trim();

        Optional<Coche> opt = cocheDAO.buscarPorMatricula(matricula);
        if (opt.isEmpty()) {
            System.out.println("  No se encontró ningún coche con esa matrícula.");
            return;
        }

        Coche coche = opt.get();

        if (coche.getEstado() == Estado.VENDIDO) {
            System.out.println("  Este coche ya está marcado como VENDIDO.");
            return;
        }

        coche.setEstado(Estado.VENDIDO);
        cocheDAO.actualizar(coche);
        System.out.printf("%n  ✔ Coche %s (%s %s) marcado como VENDIDO.%n",
                coche.getMatricula(), coche.getMarca().getNombre(), coche.getModelo());
    }

    // ===================================================================
    //  11. ELIMINAR UN COCHE
    // ===================================================================

    public void eliminarCoche() {
        System.out.print("\n  Introduce la matrícula del coche a eliminar: ");
        String matricula = scanner.nextLine().trim();

        Optional<Coche> opt = cocheDAO.buscarPorMatricula(matricula);
        if (opt.isEmpty()) {
            System.out.println("  No se encontró ningún coche con esa matrícula.");
            return;
        }

        Coche coche = opt.get();
        System.out.printf("%n  ¿Eliminar %s %s (%s)? (s/n): ",
                coche.getMarca().getNombre(), coche.getModelo(), coche.getMatricula());

        if (!scanner.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("  Operación cancelada.");
            return;
        }

        boolean eliminado = cocheDAO.eliminar(coche.getId());
        System.out.println(eliminado
                ? "  ✔ Coche eliminado correctamente."
                : "  ✘ No se pudo eliminar el coche.");
    }

    // ===================================================================
    //  12. BUSCAR POR COMBUSTIBLE
    // ===================================================================

    public void buscarPorCombustible() {
        Combustible combustible = pedirCombustible();
        if (combustible == null) return;

        List<Coche> coches = cocheDAO.buscarPorCombustible(combustible);
        mostrarListado("COCHES CON COMBUSTIBLE: " + combustible.name(), coches);
    }

    // ===================================================================
    //  13. MOSTRAR TODOS DE UNA MARCA
    // ===================================================================

    public void mostrarPorMarca() {
        System.out.println("\n  Marcas disponibles:");
        marcaDAO.listarTodas().forEach(m -> System.out.println("    - " + m.getNombre()));

        System.out.print("\n  Introduce la marca: ");
        String marca = scanner.nextLine().trim();

        List<Coche> coches = cocheDAO.buscarPorMarca(marca);
        mostrarListado("TODOS LOS COCHES DE: " + marca.toUpperCase(), coches);
    }

    // ===================================================================
    //  14. SOLO DISPONIBLES
    // ===================================================================

    public void mostrarDisponibles() {
        List<Coche> coches = cocheDAO.buscarPorEstado(Estado.DISPONIBLE);
        mostrarListado("COCHES DISPONIBLES", coches);
    }

    // ===================================================================
    //  15. SOLO VENDIDOS
    // ===================================================================

    public void mostrarVendidos() {
        List<Coche> coches = cocheDAO.buscarPorEstado(Estado.VENDIDO);
        mostrarListado("COCHES VENDIDOS", coches);
    }

    // ===================================================================
    //  16. GUARDAR EN CSV
    // ===================================================================

    public void guardarEnCSV() {
        System.out.print("\n  Ruta de destino (Enter para 'coches_export.csv'): ");
        String ruta = scanner.nextLine().trim();
        if (ruta.isEmpty()) ruta = "coches_export.csv";

        try {
            List<Coche> todos = cocheDAO.listarTodos();
            fileController.exportarACSV(ruta, todos);
            System.out.println("  ✔ Datos guardados correctamente.");
        } catch (IOException e) {
            System.err.println("  ✘ Error al guardar: " + e.getMessage());
        }
    }

    // ===================================================================
    //  UTILIDAD: GENERAR CSV DE EJEMPLO
    // ===================================================================



    // ===================================================================
    //  MÉTODOS PRIVADOS DE APOYO
    // ===================================================================

    /**
     * Muestra un listado de coches con título y gestiona el caso vacío.
     */
    private void mostrarListado(String titulo, List<Coche> coches) {
        System.out.println("\n  ══ " + titulo + " (" + coches.size() + ") ══");
        if (coches.isEmpty()) {
            System.out.println("  No se encontraron coches.");
        } else {
            coches.forEach(c -> System.out.println(c));
        }
    }

    /**
     * Solicita al usuario un tipo de combustible y lo devuelve.
     *
     * @return El combustible elegido, o null si la opción no es válida.
     */
    private Combustible pedirCombustible() {
        System.out.println("  Tipo de combustible:");
        System.out.println("    1) GASOLINA   2) DIESEL   3) ELECTRICO   4) HIBRIDO");
        System.out.print("  Elige opción: ");
        String op = scanner.nextLine().trim();

        return switch (op) {
            case "1" -> Combustible.GASOLINA;
            case "2" -> Combustible.DIESEL;
            case "3" -> Combustible.ELECTRICO;
            case "4" -> Combustible.HIBRIDO;
            default  -> {
                System.out.println("  Opción inválida.");
                yield null;
            }
        };
    }
}
