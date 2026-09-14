package model;

/**
 * Enumerado que representa el estado de venta de un vehículo.
 */
public enum Estado {
    DISPONIBLE,
    VENDIDO;

    /**
     * Parsea un String al enum Estado correspondiente.
     * Acepta "true"/"false" (formato CSV) además de los nombres del enum.
     *
     * @param valor Texto con el estado.
     * @return El valor del enum correspondiente.
     * @throws IllegalArgumentException Si el valor no es reconocido.
     */
    public static Estado fromString(String valor) {
        return switch (valor.trim().toUpperCase()) {
            case "VENDIDO", "TRUE"       -> VENDIDO;
            case "DISPONIBLE", "FALSE"   -> DISPONIBLE;
            default -> throw new IllegalArgumentException(
                    "Estado desconocido: '" + valor + "'. " +
                            "Valores válidos: DISPONIBLE, VENDIDO (o true/false)");
        };
    }

    /**
     * Convierte el estado a formato booleano para el CSV.
     *
     * @return "true" si está vendido, "false" si está disponible.
     */
    public String toCSV() {
        return this == VENDIDO ? "true" : "false";
    }
}
