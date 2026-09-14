package model;

/**
 * Enumerado que representa los tipos de combustible disponibles para un vehículo.
 */
public enum Combustible {
    GASOLINA,
    DIESEL,
    ELECTRICO,
    HIBRIDO;

    /**
     * Parsea un String (insensible a mayúsculas) al enum correspondiente.
     *
     * @param valor Texto con el tipo de combustible.
     * @return El valor del enum correspondiente.
     * @throws IllegalArgumentException Si el valor no coincide con ningún tipo.
     */
    public static Combustible fromString(String valor) {
        return switch (valor.trim().toUpperCase()) {
            case "GASOLINA"  -> GASOLINA;
            case "DIESEL"    -> DIESEL;
            case "ELECTRICO", "ELÉCTRICO" -> ELECTRICO;
            case "HIBRIDO", "HÍBRIDO"     -> HIBRIDO;
            default -> throw new IllegalArgumentException(
                    "Combustible desconocido: '" + valor + "'. " +
                            "Valores válidos: GASOLINA, DIESEL, ELECTRICO, HIBRIDO");
        };
    }
}
