
package controller;
/*
import dao.AutorDAO;
import dao.BibliotecaDAO;
import dao.LibroDAO;
import model.Autor;
import model.Biblioteca;
import model.Libro;
import org.hibernate.exception.ConstraintViolationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BibliotecaController {

    private final AutorDAO      autorDAO;
    private final BibliotecaDAO bibliotecaDAO;
    private final LibroDAO      libroDAO;

    public BibliotecaController() {
        this.autorDAO      = new AutorDAO();
        this.bibliotecaDAO = new BibliotecaDAO();
        this.libroDAO      = new LibroDAO();
    }

    public Biblioteca crearBiblioteca(String calle, String localidad, String provincia) {
        Biblioteca bib = new Biblioteca(calle, localidad, provincia);
        try {
            bibliotecaDAO.guardarBiblioteca(bib);
            System.out.println("✔ Biblioteca creada: " + bib);
        } catch (Exception e) {
            System.err.println("✘ Error al crear biblioteca: " + e.getMessage());
        }
        return bib;
    }

    public void listarTodasBibliotecas() {
        List<Biblioteca> lista = bibliotecaDAO.obtenerTodas();
        System.out.println("\n===== BIBLIOTECAS REGISTRADAS (" + lista.size() + ") =====");
        lista.forEach(b -> System.out.printf(
                "  ID: %d | %s, %s (%s)%n",
                b.getId(), b.getCalle(), b.getLocalidad(), b.getProvincia()));
        System.out.println("=".repeat(45));
    }

    public Autor crearAutor(String nombre, String apellido, String dni) {
        Autor autor = new Autor(nombre, apellido, dni);
        try {
            autorDAO.guardarAutor(autor);
            System.out.println("✔ Autor creado: " + autor);
            return autor;
        } catch (Exception e) {
            if (esDuplicado(e)) {
                System.err.printf("✘ Error: Ya existe un autor con el DNI '%s'. " +
                        "No se puede insertar un DNI duplicado.%n", dni);
            } else {
                System.err.println("✘ Error inesperado al crear autor: " + e.getMessage());
            }
            return null;
        }
    }

    public void listarTodosAutores() {
        List<Autor> lista = autorDAO.obtenerTodos();
        System.out.println("\n===== AUTORES REGISTRADOS (" + lista.size() + ") =====");
        lista.forEach(a -> System.out.printf(
                "  ID: %d | %s %s | DNI: %s%n",
                a.getId(), a.getNombre(), a.getApellido(), a.getDni()));
        System.out.println("=".repeat(45));
    }

    public Autor buscarAutorPorDni(String dni) {
        Autor autor = autorDAO.buscarPorDni(dni);
        if (autor == null) {
            System.err.println("✘ No se encontró ningún autor con DNI: " + dni);
        }
        return autor;
    }

    public Libro insertarLibro(String isbn, String titulo, int paginas, String tipo, int anio, String dniAutor, Long bibliotecaId) {
        Autor autor = autorDAO.buscarPorDni(dniAutor);
        if (autor == null) {
            System.err.printf("✘ No se puede insertar el libro: " +
                    "no existe autor con DNI '%s'.%n", dniAutor);
            return null;
        }

        Biblioteca biblioteca = bibliotecaDAO.buscarPorId(bibliotecaId);
        if (biblioteca == null) {
            System.err.printf("✘ No se puede insertar el libro: " +
                    "no existe biblioteca con ID %d.%n", bibliotecaId);
            return null;
        }

        Libro libro = new Libro(isbn, titulo, paginas, tipo, anio, autor, biblioteca);
        try {
            libroDAO.guardarLibro(libro);
            System.out.printf("✔ Libro insertado: '%s' (ISBN: %s) en %s, %s%n",
                    titulo, isbn, biblioteca.getLocalidad(), biblioteca.getProvincia());
            return libro;
        } catch (Exception e) {
            if (esDuplicado(e)) {
                System.err.printf("✘ Error: Ya existe un libro con el ISBN '%s'. " +
                        "No se puede insertar un ISBN duplicado.%n", isbn);
            } else {
                System.err.println("✘ Error inesperado al insertar libro: " + e.getMessage());
            }
            return null;
        }
    }

    public void listarLibrosDeBiblioteca(Long bibliotecaId) {
        Biblioteca bib = bibliotecaDAO.buscarPorId(bibliotecaId);
        if (bib == null) {
            System.err.println("✘ No existe biblioteca con ID: " + bibliotecaId);
            return;
        }

        List<Libro> libros = libroDAO.obtenerLibrosPorBiblioteca(bibliotecaId);
        System.out.printf("%n===== LIBROS EN '%s, %s' (%d libros) =====%n",
                bib.getLocalidad(), bib.getProvincia(), libros.size());

        if (libros.isEmpty()) {
            System.out.println("  (Sin libros registrados)");
        } else {
            libros.forEach(l -> System.out.printf(
                    "  ISBN: %-15s | %-30s | Autor: %s %s | %d págs. | %s | Año: %d%n",
                    l.getIsbn(), l.getTitulo(),
                    l.getAutor().getNombre(), l.getAutor().getApellido(),
                    l.getNumeroPaginas(), l.getTipo(), l.getAnioPublicacion()));
        }
        System.out.println("=".repeat(45));
    }
}
*/
    // =========================================================================
    //  EXPORTACIÓN A CSV
    // =========================================================================

    /**
     * Exporta todos los libros de un autor concreto a un archivo CSV.
     *
     * Formato CSV generado:
     *   ISBN,Titulo,Paginas,Tipo,AnioPublicacion,AutorNombre,AutorApellido,BibliotecaLocalidad
     *
     * @param dniAutor    DNI del autor cuyos libros se exportarán
     * @param rutaArchivo Ruta del archivo CSV de salida (ej: "libros_autor.csv")
     */
    public void exportarLibrosAutorCsv(String dniAutor, String rutaArchivo) {

        // Buscar el autor
        Autor autor = autorDAO.buscarPorDni(dniAutor);
        if (autor == null) {
            System.err.printf("✘ No se puede exportar: no existe autor con DNI '%s'.%n", dniAutor);
            return;
        }

        List<Libro> libros = libroDAO.obtenerLibrosPorAutor(autor.getId());

        // Escribir el CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaArchivo))) {

            // Cabecera
            bw.write("ISBN,Titulo,NumeroPaginas,Tipo,AnioPublicacion," +
                    "AutorNombre,AutorApellido,AutorDNI," +
                    "BibliotecaCalle,BibliotecaLocalidad,BibliotecaProvincia");
            bw.newLine();

            // Filas
            for (Libro l : libros) {
                String linea = String.join(",",
                        escaparCsv(l.getIsbn()),
                        escaparCsv(l.getTitulo()),
                        String.valueOf(l.getNumeroPaginas()),
                        escaparCsv(l.getTipo()),
                        String.valueOf(l.getAnioPublicacion()),
                        escaparCsv(l.getAutor().getNombre()),
                        escaparCsv(l.getAutor().getApellido()),
                        escaparCsv(l.getAutor().getDni()),
                        escaparCsv(l.getBiblioteca().getCalle()),
                        escaparCsv(l.getBiblioteca().getLocalidad()),
                        escaparCsv(l.getBiblioteca().getProvincia())
                );
                bw.write(linea);
                bw.newLine();
            }

            System.out.printf("✔ Exportados %d libros del autor '%s %s' a '%s'%n",
                    libros.size(), autor.getNombre(), autor.getApellido(), rutaArchivo);

        } catch (IOException e) {
            System.err.println("✘ Error al escribir el CSV: " + e.getMessage());
        }
    }

    // =========================================================================
    //  MÉTODOS AUXILIARES PRIVADOS
    // =========================================================================

    /**
     * Comprueba si una excepción tiene como causa una violación de restricción única (UNIQUE).
     * Recorre la cadena de causas para detectar ConstraintViolationException.
     */
    private boolean esDuplicado(Exception e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof ConstraintViolationException) {
                return true;
            }
            // MySQL también lanza java.sql.SQLIntegrityConstraintViolationException
            if (causa instanceof java.sql.SQLIntegrityConstraintViolationException) {
                return true;
            }
            causa = causa.getCause();
        }
        return false;
    }

    /**
     * Escapa un valor para CSV: si contiene comas, comillas o saltos de línea,
     * lo envuelve entre comillas dobles y duplica las comillas internas.
     */
    private String escaparCsv(String valor) {
        if (valor == null) return "";
        if (valor.contains(",") || valor.contains("\"") || valor.contains("\n")) {
            return "\"" + valor.replace("\"", "\"\"") + "\"";
        }
        return valor;
    }
}
