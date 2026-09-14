package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa a un autor.
 * Relación: Un autor puede tener MUCHOS libros (OneToMany).
 */
@Entity
@Table(name = "autores")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 100)
    private String apellido;

    /**
     * DNI único por autor. La restricción UNIQUE en BD impide duplicados.
     * Si se intenta insertar un DNI repetido, Hibernate lanzará una excepción
     * que capturaremos en el Controller.
     */
    @Column(name = "dni", unique = true, nullable = false, length = 20)
    private String dni;

    /**
     * Lista de libros del autor.
     * mappedBy = "autor" indica que la FK está en la tabla 'libros' (campo autor_id).
     * cascade = ALL significa que si borramos un autor, se borran sus libros.
     * fetch = LAZY  -> los libros se cargan solo cuando se accede a la lista (eficiente).
     */
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    public Autor(String nombre, String apellido, String dni) {
    }

    // Constructores, Getters y Setters generados por Lombok

    @Override
    public String toString() {
        return String.format("Autor[id=%d, nombre='%s %s', dni='%s']",
                id, nombre, apellido, dni);
    }
}
