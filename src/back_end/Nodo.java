package back_end;

/**
 * Nodo dinámico que contiene una referencia al record {@link Taxi} (dato)
 * y una referencia al nodo {@code siguiente} en la secuencia.
 *
 * <p>La referencia {@code siguiente} es {@code null} cuando el nodo es el
 * último elemento de la lista (no existe sucesor).</p>
 *
 * @author Manuel Ángel Espinoza López. Geovani Gael Carmona Barbosa. Victor Manuel Leyva Perez
 */
public class Nodo {

    /** Dato almacenado en este nodo. */
    private Taxi dato;

    /** Referencia al nodo sucesor; {@code null} si es el último. */
    private Nodo siguiente;

    /**
     * Construye un nodo con el dato indicado y sin sucesor.
     *
     * @param dato record {@link Taxi} a almacenar.
     */
    public Nodo(Taxi dato) {
        this.dato      = dato;
        this.siguiente = null;
    }

    // ---------------------------------------------------------------
    // Accesores
    // ---------------------------------------------------------------

    /**
     * Retorna el record almacenado en este nodo.
     *
     * @return dato {@link Taxi}.
     */
    public Taxi getDato() {
        return dato;
    }

    /**
     * Actualiza el dato almacenado en este nodo.
     *
     * @param dato nuevo record {@link Taxi}.
     */
    public void setDato(Taxi dato) {
        this.dato = dato;
    }

    /**
     * Retorna la referencia al nodo sucesor.
     *
     * @return {@link Nodo} siguiente o {@code null}.
     */
    public Nodo getSiguiente() {
        return siguiente;
    }

    /**
     * Establece la referencia al nodo sucesor.
     *
     * @param siguiente {@link Nodo} sucesor o {@code null}.
     */
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
