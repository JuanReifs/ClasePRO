package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "combustible")
@Getter
@Setter
@NoArgsConstructor

public class Combustible {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tipo_combustible")
    private String tipo;

    @Column(name = "descripción")
    private String descripcion;

    @OneToMany(mappedBy = "combustible",fetch = FetchType.EAGER)
    private List<Coche> coche = new ArrayList<>();

    public Combustible(String tipo, String descripcion) {
        this.tipo = tipo;
        this.descripcion = descripcion;
    }
}