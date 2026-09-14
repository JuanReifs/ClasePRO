package model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
/**
 * Entidad principal del sistema: representa un coche en el concesionario.
 * Mantiene una relación ManyToOne con la entidad Marca.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "marcas")
public class Marca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "marca", nullable = false, unique = true, length = 100)
    private String marca;

    @Column(name = "pais_origen", length = 100)
    private String paisOrigen;

    // Relación inversa: una marca tiene muchos coches
    @OneToMany(mappedBy = "marca", fetch = FetchType.EAGER)
    private List<Coche> coches = new ArrayList<>();


    public Marca(String marca, String paisOrigen) {
        this.marca = marca;
        this.paisOrigen = paisOrigen;
    }
}
