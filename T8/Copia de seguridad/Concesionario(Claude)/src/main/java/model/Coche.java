package model;

import jakarta.persistence.*;

/**
 * Entidad principal que representa un vehículo del concesionario.
 * Tiene una relación ManyToOne con {@link Marca}.
 */
@Entity
@Table(name = "coches")
public class Coche {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coche")
    private Long id;

    @Column(name = "matricula", nullable = false, unique = true, length = 10)
    private String matricula;

    /**
     * Relación ManyToOne: muchos coches pertenecen a una marca.
     * Es el lado "dueño" de la relación (tiene la foreign key en BD).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;

    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @Column(name = "color", length = 50)
    private String color;

    /**
     * Almacenado como String en BD usando el nombre del enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "combustible", nullable = false, length = 20)
    private Combustible combustible;

    @Column(name = "anio", nullable = false)
    private int anio;

    @Column(name = "kilometros", nullable = false)
    private int kilometros;

    @Column(name = "precio", nullable = false)
    private double precio;

    /**
     * Estado de venta almacenado como String en BD.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private Estado estado;

    // ===================== CONSTRUCTORES =====================

    public Coche() {}

    public Coche(String matricula, Marca marca, String modelo, String color,
                 Combustible combustible, int anio, int kilometros,
                 double precio, Estado estado) {
        this.matricula   = matricula;
        this.marca       = marca;
        this.modelo      = modelo;
        this.color       = color;
        this.combustible = combustible;
        this.anio        = anio;
        this.kilometros  = kilometros;
        this.precio      = precio;
        this.estado      = estado;
    }

    // ===================== GETTERS Y SETTERS =====================

    public Long getId()                    { return id; }
    public void setId(Long id)             { this.id = id; }

    public String getMatricula()           { return matricula; }
    public void setMatricula(String m)     { this.matricula = m; }

    public Marca getMarca()                { return marca; }
    public void setMarca(Marca marca)      { this.marca = marca; }

    public String getModelo()              { return modelo; }
    public void setModelo(String modelo)   { this.modelo = modelo; }

    public String getColor()               { return color; }
    public void setColor(String color)     { this.color = color; }

    public Combustible getCombustible()              { return combustible; }
    public void setCombustible(Combustible c)        { this.combustible = c; }

    public int getAnio()                   { return anio; }
    public void setAnio(int anio)          { this.anio = anio; }

    public int getKilometros()             { return kilometros; }
    public void setKilometros(int km)      { this.kilometros = km; }

    public double getPrecio()              { return precio; }
    public void setPrecio(double precio)   { this.precio = precio; }

    public Estado getEstado()              { return estado; }
    public void setEstado(Estado estado)   { this.estado = estado; }

    // ===================== MÉTODOS DE UTILIDAD =====================

    /**
     * Convierte el coche a una línea CSV.
     * Formato: matricula,marca,modelo,color,combustible,anio,kilometros,precio,vendido
     */
    public String toCSV() {
        return String.join(",",
                matricula,
                marca.getNombre(),
                modelo,
                color,
                combustible.name(),
                String.valueOf(anio),
                String.valueOf(kilometros),
                String.valueOf(precio),
                estado.toCSV()
        );
    }

    /**
     * Devuelve una representación legible del coche para mostrar en consola.
     */
    @Override
    public String toString() {
        return String.format(
                "┌─────────────────────────────────────────────┐%n" +
                        "│ ID: %-5d  Matrícula: %-10s           │%n" +
                        "│ Marca: %-12s  Modelo: %-14s │%n" +
                        "│ Color: %-10s  Año: %-6d  Km: %-8d │%n" +
                        "│ Combustible: %-10s  Precio: %8.2f € │%n" +
                        "│ Estado: %-35s │%n" +
                        "└─────────────────────────────────────────────┘",
                id, matricula,
                marca.getNombre(), modelo,
                color, anio, kilometros,
                combustible.name(), precio,
                estado.name()
        );
    }
}
