package back_end;

/**
 * Record de dominio inmutable que representa un Taxi registrado en el sistema.
 *
 * Al ser un record, Java genera automáticamente: constructor canónico,
 * métodos de acceso (id(), nombre(), apellido(), edad(), curp(), placas(),
 * modelo()), equals(), hashCode() y toString().
 *
 * @param id      Identificador numérico único del taxi/conductor.
 * @param nombre  Nombre del conductor.
 * @param apellido Apellido del conductor.
 * @param edad    Edad del conductor.
 * @param curp    CURP del conductor (18 caracteres alfanuméricos).
 * @param placas  Placas del vehículo.
 * @param modelo  Modelo del vehículo.
 *
 * @author Manuel Ángel Espinoza López. Geovani Gael Carmona Barbosa. Victor Manuel Leyva Perez
 */
public record Taxi(int id, String nombre, String apellido, int edad,
                   String curp, String placas, String modelo) {

    /**
     * Constructor compacto con validaciones de dominio.
     */
    public Taxi {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un entero positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        }
        if (edad <= 0 || edad > 120) {
            throw new IllegalArgumentException("La edad debe estar entre 1 y 120.");
        }
        if (curp == null || curp.isBlank()) {
            throw new IllegalArgumentException("La CURP no puede estar vacía.");
        }
        if (placas == null || placas.isBlank()) {
            throw new IllegalArgumentException("Las placas no pueden estar vacías.");
        }
        if (modelo == null || modelo.isBlank()) {
            throw new IllegalArgumentException("El modelo no puede estar vacío.");
        }
        nombre   = nombre.trim();
        apellido = apellido.trim();
        curp     = curp.trim().toUpperCase();
        placas   = placas.trim().toUpperCase();
        modelo   = modelo.trim();
    }

    /**
     * Representación legible para mostrar en la GUI.
     */
    @Override
    public String toString() {
        return String.format("[%d] %s %s | Edad: %d | CURP: %s | Placas: %s | Modelo: %s",
                id, nombre, apellido, edad, curp, placas, modelo);
    }
}
