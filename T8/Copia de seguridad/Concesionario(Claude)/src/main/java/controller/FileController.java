package controller;

import dao.CocheDAO;
import dao.MarcaDAO;
import model.Coche;
import model.Combustible;
import model.Estado;
import model.Marca;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;


public class FileController {

    /** Cabecera estándar del archivo CSV. */
    public static final String CABECERA_CSV =
            "matricula,marca,modelo,color,combustible,anio,kilometros,precio,vendido";

    private final CocheDAO cocheDAO;
    private final MarcaDAO marcaDAO;

    public FileController() {
        this.cocheDAO = new CocheDAO();
        this.marcaDAO = new MarcaDAO();
    }

    // ===================== IMPORTACIÓN =====================

    public int importarDesdeCSV(String rutaArchivo) throws IOException {
        Path path = Path.of(rutaArchivo);

        if (!Files.exists(path)) {
            throw new FileNotFoundException("Archivo no encontrado: " + rutaArchivo);
        }

        List<String> lineas = Files.readAllLines(path, StandardCharsets.UTF_8);

        if (lineas.isEmpty()) {
            System.out.println("  [CSV] El archivo está vacío.");
            return 0;
        }

        int importados = 0;
        int omitidos   = 0;
        int errores    = 0;

        // Detectar si la primera línea es la cabecera
        int inicio = lineas.get(0).toLowerCase().startsWith("matricula") ? 1 : 0;

        for (int i = inicio; i < lineas.size(); i++) {
            String linea = lineas.get(i).trim();
            if (linea.isEmpty()) continue;

            try {
                Coche coche = parsearLinea(linea);

                // Comprobar duplicado por matrícula
                if (cocheDAO.existeMatricula(coche.getMatricula())) {
                    System.out.printf("  [OMITIDO] Línea %d: matrícula '%s' ya existe en BD.%n",
                            i + 1, coche.getMatricula());
                    omitidos++;
                    continue;
                }

                cocheDAO.guardar(coche);
                importados++;

            } catch (Exception e) {
                System.err.printf("  [ERROR] Línea %d: %s → %s%n", i + 1, linea, e.getMessage());
                errores++;
            }
        }

        System.out.printf("%n  Importación completada: %d importados, %d omitidos, %d errores.%n",
                importados, omitidos, errores);
        return importados;
    }


    private Coche parsearLinea(String linea) {
        String[] campos = linea.split(",", -1);

        if (campos.length < 9) {
            throw new IllegalArgumentException(
                    "Formato inválido. Se esperan 9 campos, se encontraron " + campos.length);
        }

        String matricula   = campos[0].trim().toUpperCase();
        String nombreMarca = campos[1].trim();
        String modelo      = campos[2].trim();
        String color       = campos[3].trim();
        Combustible comb   = Combustible.fromString(campos[4].trim());
        int anio           = Integer.parseInt(campos[5].trim());
        int kilometros     = Integer.parseInt(campos[6].trim());
        double precio      = Double.parseDouble(campos[7].trim());
        Estado estado      = Estado.fromString(campos[8].trim());

        // Obtener o crear la marca
        Marca marca = marcaDAO.buscarOCrear(nombreMarca);

        return new Coche(matricula, marca, modelo, color, comb, anio, kilometros, precio, estado);
    }

    // ===================== EXPORTACIÓN =====================

    public void exportarACSV(String rutaArchivo, List<Coche> listaCoches) throws IOException {
        Path path = Path.of(rutaArchivo);

        // Crear directorios intermedios si no existen
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }

        List<String> lineas = new ArrayList<>();
        lineas.add(CABECERA_CSV);

        for (Coche coche : listaCoches) {
            lineas.add(coche.toCSV());
        }

        Files.write(path, lineas, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        System.out.printf("  [CSV] %d coches exportados a: %s%n",
                listaCoches.size(), path.toAbsolutePath());
    }



}
