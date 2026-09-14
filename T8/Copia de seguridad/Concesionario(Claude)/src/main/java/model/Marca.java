package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Entidad que representa la marca de un vehículo.
 * Tiene una relación OneToMany con {@link Coche}.
 */
@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_marca")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "pais_origen", length = 100)
    private String paisOrigen;

    /**
     * Relación bidireccional: una marca puede tener muchos coches.
     * mappedBy indica que Coche es el dueño de la relación.
     * CascadeType.ALL propaga las operaciones a los coches.
     */
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Coche> coches = new ArrayList<>();

    // ===================== CONSTRUCTORES =====================

    public Marca() {}

    public Marca(String nombre) {
        this.nombre = nombre;
    }

    public Marca(String nombre, String paisOrigen) {
        this.nombre = nombre;
        this.paisOrigen = paisOrigen;
    }

    // ===================== GETTERS Y SETTERS =====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPaisOrigen() { return paisOrigen; }
    public void setPaisOrigen(String paisOrigen) { this.paisOrigen = paisOrigen; }

    public List<Coche> getCoches() { return coches; }
    public void setCoches(List<Coche> coches) { this.coches = coches; }

    // ===================== MÉTODOS DE UTILIDAD =====================

    /**
     * Agrega un coche a la lista y establece la referencia bidireccional.
     */
    public void addCoche(Coche coche) {
        coches.add(coche);
        coche.setMarca(this);
    }

    @Override
    public String toString() {
        return String.format("Marca{id=%d, nombre='%s', paisOrigen='%s'}",
                id, nombre, paisOrigen != null ? paisOrigen : "N/A");
    }
}
