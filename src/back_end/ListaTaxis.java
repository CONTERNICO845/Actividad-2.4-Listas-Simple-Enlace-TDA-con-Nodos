package back_end;

/**
 * TDA Lista Unidireccional (Simplemente Enlazada) de registros {@link Taxi}.
 *
 * <p>La estructura mantiene la referencia al primer nodo mediante el atributo
 * {@code primero}. Cada {@link Nodo} enlaza con su sucesor a través del
 * atributo {@code siguiente}. <strong>No se utiliza ninguna clase de
 * {@code java.util} ni arreglos estáticos.</strong></p>
 *
 * <p>Operaciones soportadas:</p>
 * <ul>
 *   <li>Insertar al principio, al final y por posición.</li>
 *   <li>Remover al principio, al final y por posición.</li>
 *   <li>Mostrar (consultar) al principio, al final y por posición.</li>
 *   <li>Buscar por ID (valor clave del record).</li>
 *   <li>Vaciar la lista y verificar si está vacía.</li>
 *   <li>Recorrer y obtener la representación completa de la lista.</li>
 * </ul>
 *
 * @author Manuel Ángel Espinoza López. Geovani Gael Carmona Barbosa. Victor Manuel Leyva Perez
 */
public class ListaTaxis {

    /** Referencia al primer nodo de la lista; {@code null} si está vacía. */
    private Nodo primero;

    /** Cantidad actual de nodos en la lista. */
    private int tamanio;

    // ================================================================
    // Constructor
    // ================================================================

    /**
     * Construye una lista vacía (primero = null, tamanio = 0).
     */
    public ListaTaxis() {
        primero  = null;
        tamanio  = 0;
    }

    // ================================================================
    // Auxiliares
    // ================================================================

    /**
     * Indica si la lista no contiene ningún nodo.
     *
     * @return {@code true} si {@code primero == null}.
     */
    public boolean estaVacia() {
        return primero == null;
    }

    /**
     * Retorna la cantidad de elementos presentes en la lista.
     *
     * @return número de nodos.
     */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Restablece {@code primero} a {@code null} y el tamaño a 0,
     * liberando todos los nodos para el recolector de basura de la JVM.
     */
    public void vaciar() {
        primero  = null;
        tamanio  = 0;
    }

    // ================================================================
    // Inserción
    // ================================================================

    /**
     * Inserta un nuevo record al <b>principio</b> de la lista.
     *
     * <p>El nuevo {@link Nodo} pasa a ser el nuevo {@code primero}; su
     * referencia {@code siguiente} apunta a la antigua cabeza.</p>
     *
     * @param taxi record {@link Taxi} a insertar.
     */
    public void insertarAlPrincipio(Taxi taxi) {
        Nodo nuevo = new Nodo(taxi);
        nuevo.setSiguiente(primero);   // enlaza con la antigua cabeza
        primero = nuevo;               // nuevo nodo es ahora la cabeza
        tamanio++;
    }

    /**
     * Inserta un nuevo record al <b>final</b> de la lista.
     *
     * <p>Se recorre la lista desde {@code primero} hasta el último nodo
     * (cuyo {@code siguiente} es {@code null}) y se enlaza el nuevo nodo.</p>
     *
     * @param taxi record {@link Taxi} a insertar.
     */
    public void insertarAlFinal(Taxi taxi) {
        Nodo nuevo = new Nodo(taxi);
        if (estaVacia()) {
            primero = nuevo;
        } else {
            Nodo actual = primero;
            while (actual.getSiguiente() != null) {
                actual = actual.getSiguiente();
            }
            actual.setSiguiente(nuevo); // el último apunta al nuevo nodo
        }
        tamanio++;
    }

    /**
     * Inserta un nuevo record en la <b>posición</b> indicada (base 0).
     *
     * <p>Posición 0 equivale a {@link #insertarAlPrincipio(Taxi)}.
     * Posición {@link #getTamanio()} equivale a {@link #insertarAlFinal(Taxi)}.</p>
     *
     * @param taxi     record {@link Taxi} a insertar.
     * @param posicion índice (0-based) donde se insertará el nodo.
     * @throws IndexOutOfBoundsException si la posición es negativa o mayor al tamaño actual.
     */
    public void insertarEnPosicion(Taxi taxi, int posicion) {
        if (posicion < 0 || posicion > tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango [0, " + tamanio + "].");
        }
        if (posicion == 0) {
            insertarAlPrincipio(taxi);
            return;
        }
        if (posicion == tamanio) {
            insertarAlFinal(taxi);
            return;
        }
        // Avanzar hasta el nodo predecesor (posición - 1)
        Nodo anterior = primero;
        for (int i = 0; i < posicion - 1; i++) {
            anterior = anterior.getSiguiente();
        }
        Nodo nuevo = new Nodo(taxi);
        nuevo.setSiguiente(anterior.getSiguiente()); // nuevo apunta al actual ocupante
        anterior.setSiguiente(nuevo);                // predecesor apunta al nuevo
        tamanio++;
    }

    // ================================================================
    // Remoción
    // ================================================================

    /**
     * Remueve el nodo al <b>principio</b> de la lista.
     *
     * <p>{@code primero} se reubica hacia el nodo indicado por el
     * {@code siguiente} del nodo removido.</p>
     *
     * @return el record {@link Taxi} del nodo eliminado.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Taxi removerAlPrincipio() {
        verificarNoVacia();
        Taxi removido = primero.getDato();
        primero = primero.getSiguiente(); // cabeza avanza al siguiente
        tamanio--;
        return removido;
    }

    /**
     * Remueve el nodo al <b>final</b> de la lista.
     *
     * <p>Se recorre hasta el penúltimo nodo y se ajusta su
     * referencia {@code siguiente} a {@code null}.</p>
     *
     * @return el record {@link Taxi} del nodo eliminado.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Taxi removerAlFinal() {
        verificarNoVacia();
        if (primero.getSiguiente() == null) {
            // Único elemento
            Taxi removido = primero.getDato();
            primero = null;
            tamanio--;
            return removido;
        }
        Nodo penultimo = primero;
        while (penultimo.getSiguiente().getSiguiente() != null) {
            penultimo = penultimo.getSiguiente();
        }
        Taxi removido = penultimo.getSiguiente().getDato();
        penultimo.setSiguiente(null); // aísla el último nodo
        tamanio--;
        return removido;
    }

    /**
     * Remueve el nodo en la <b>posición</b> indicada (base 0).
     *
     * @param posicion índice (0-based) del nodo a remover.
     * @return el record {@link Taxi} del nodo eliminado.
     * @throws IllegalStateException     si la lista está vacía.
     * @throws IndexOutOfBoundsException si la posición es negativa o >= tamaño.
     */
    public Taxi removerEnPosicion(int posicion) {
        verificarNoVacia();
        verificarRango(posicion);
        if (posicion == 0) {
            return removerAlPrincipio();
        }
        // Localizar el predecesor
        Nodo anterior = primero;
        for (int i = 0; i < posicion - 1; i++) {
            anterior = anterior.getSiguiente();
        }
        Taxi removido = anterior.getSiguiente().getDato();
        anterior.setSiguiente(anterior.getSiguiente().getSiguiente()); // salta el nodo eliminado
        tamanio--;
        return removido;
    }

    // ================================================================
    // Visualización / Consulta
    // ================================================================

    /**
     * Retorna el record almacenado en el <b>primer</b> nodo sin modificar la lista.
     *
     * @return record {@link Taxi} del primer nodo.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Taxi mostrarAlPrincipio() {
        verificarNoVacia();
        return primero.getDato();
    }

    /**
     * Retorna el record almacenado en el <b>último</b> nodo sin modificar la lista.
     *
     * @return record {@link Taxi} del último nodo.
     * @throws IllegalStateException si la lista está vacía.
     */
    public Taxi mostrarAlFinal() {
        verificarNoVacia();
        Nodo actual = primero;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     * Retorna el record en la <b>posición</b> indicada (base 0) sin modificar la lista.
     *
     * @param posicion índice (0-based) del nodo a consultar.
     * @return record {@link Taxi} en esa posición.
     * @throws IllegalStateException     si la lista está vacía.
     * @throws IndexOutOfBoundsException si la posición es negativa o >= tamaño.
     */
    public Taxi mostrarEnPosicion(int posicion) {
        verificarNoVacia();
        verificarRango(posicion);
        Nodo actual = primero;
        for (int i = 0; i < posicion; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    // ================================================================
    // Búsqueda
    // ================================================================

    /**
     * Busca un {@link Taxi} cuyo <b>ID</b> coincida con el valor dado,
     * recorriendo la lista secuencialmente desde {@code primero}.
     *
     * @param id valor clave a buscar.
     * @return arreglo de dos elementos: {@code [Taxi encontrado, posición (Integer)]}
     *         o {@code null} si no se encontró.
     */
    public Object[] buscarPorId(int id) {
        Nodo actual = primero;
        int  pos    = 0;
        while (actual != null) {
            if (actual.getDato().id() == id) {
                return new Object[]{ actual.getDato(), pos };
            }
            actual = actual.getSiguiente();
            pos++;
        }
        return null; // no encontrado
    }

    // ================================================================
    // Recorrido completo
    // ================================================================

    /**
     * Construye una representación textual de toda la lista, recorriendo
     * los nodos desde {@code primero} mediante la referencia {@code siguiente}.
     *
     * @return cadena con todos los records separados por salto de línea,
     *         o mensaje indicando que la lista está vacía.
     */
    public String listarTodo() {
        if (estaVacia()) {
            return "(La lista está vacía)";
        }
        StringBuilder sb = new StringBuilder();
        Nodo actual = primero;
        int  pos    = 0;
        while (actual != null) {
            sb.append("Pos ").append(pos).append(": ").append(actual.getDato()).append("\n");
            actual = actual.getSiguiente();
            pos++;
        }
        return sb.toString().trim();
    }

    // ================================================================
    // Métodos de validación internos
    // ================================================================

    /**
     * Lanza {@link IllegalStateException} si la lista está vacía.
     */
    private void verificarNoVacia() {
        if (estaVacia()) {
            throw new IllegalStateException("La lista está vacía.");
        }
    }

    /**
     * Lanza {@link IndexOutOfBoundsException} si la posición es inválida.
     *
     * @param posicion índice a validar.
     */
    private void verificarRango(int posicion) {
        if (posicion < 0 || posicion >= tamanio) {
            throw new IndexOutOfBoundsException(
                    "Posición " + posicion + " fuera de rango [0, " + (tamanio - 1) + "].");
        }
    }
}
