package model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una Biblioteca.
 * Relación: Una biblioteca puede tener MUCHOS libros (OneToMany).
 */
@Entity
@Table(name = "bibliotecas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Biblioteca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "calle", nullable = false, length = 200)
    private String calle;

    @Column(name = "localidad", nullable = false, length = 100)
    private String localidad;

    @Column(name = "provincia", nullable = false, length = 100)
    private String provincia;

    /**
     * Libros que están actualmente en esta biblioteca.
     * Un libro solo puede estar en UNA biblioteca (la FK está en la tabla libros).
     */
    @OneToMany(mappedBy = "biblioteca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Libro> libros = new ArrayList<>();

    public Biblioteca(String calle, String localidad, String provincia) {
    }

    // Constructores, Getters y Setters generados por Lombok

    @Override
    public String toString() {
        return String.format("Biblioteca[id=%d, calle='%s', localidad='%s', provincia='%s']",
                id, calle, localidad, provincia);
    }
}
