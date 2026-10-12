# Actividad 2.4: Lista Simple Enlace — TDA con Nodos (Unidireccional)

Este repositorio contiene el código fuente de la **Práctica 2.4: Lista Unidireccional TDA con Nodos** de la materia de Estructura de Datos (`2026B_IH060_ED`). El proyecto implementa el Tipo de Dato Abstracto (TDA) **Lista Simplemente Enlazada** mediante una estructura dinámica de nodos enlazados bajo referencias `primero` y `siguiente`, integrada con una interfaz gráfica desarrollada en **Java Swing** para la administración de un registro de taxis.

---

## 👥 Integrantes del Equipo 10

- Leyva Pérez Victor Manuel
- Espinoza López Manuel Ángel
- Carmona Barbosa Geovani Gael

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura de **tres capas desacopladas**:

| Capa | Clase | Responsabilidad |
|------|-------|-----------------|
| **Dominio** | `Taxi.java` | Record inmutable con los atributos del conductor/vehículo. Validaciones en el constructor compacto. |
| **TDA** | `Nodo.java` + `ListaTaxis.java` | Estructura dinámica con punteros `primero` y `siguiente`. Lógica pura sin dependencias de la GUI. |
| **Vista** | `Gui_taxis.java` | Interfaz Swing que captura datos del formulario, invoca las operaciones del TDA y refleja el estado de la lista en tiempo real. |

```
src/
├── back_end/
│   ├── Taxi.java          ← record de dominio (inmutable)
│   ├── Nodo.java          ← nodo dinámico (dato + siguiente)
│   ├── ListaTaxis.java    ← TDA Lista Unidireccional (primero, siguiente)
│   └── Taxx_nodios.java   ← main de respaldo (NetBeans)
└── front_end/
    ├── Gui_taxis.java     ← interfaz Swing completa
    └── Gui_taxis.form     ← diseño visual NetBeans
```

---

## 🚀 Clonar el repositorio

```bash
git clone https://github.com/CONTERNICO845/Actividad-2.4-Listas-Simple-Enlace-TDA-con-Nodos.git
```

Abre el proyecto en **NetBeans** (es un proyecto Ant estándar) y ejecuta `Run Project` o compila manualmente:

```bash
javac -d build/classes -sourcepath src \
  src/back_end/Taxi.java \
  src/back_end/Nodo.java \
  src/back_end/ListaTaxis.java \
  src/front_end/Gui_taxis.java

java -cp build/classes front_end.Gui_taxis
```

> Requiere **Java 16+** (el tipo `record` fue introducido como característica estable en Java 16).

---

## ⚙️ Operaciones implementadas

### Inserción
| Botón | Operación | Descripción |
|-------|-----------|-------------|
| `Inset_inicio` | `insertarAlPrincipio` | El nuevo nodo pasa a ser `primero`; su `siguiente` apunta a la antigua cabeza. |
| `insert_final` | `insertarAlFinal` | Recorre desde `primero` hasta encontrar `siguiente == null` y enlaza el nuevo nodo. |
| `Insert_Posic` | `insertarEnPosicion` | Localiza el predecesor en la posición indicada y reenlaza `siguiente`. |

### Remoción
| Botón | Operación | Descripción |
|-------|-----------|-------------|
| `Rmove_inicio` | `removerAlPrincipio` | `primero` avanza al nodo señalado por `siguiente` del nodo removido. |
| `Rmove_final` | `removerAlFinal` | Llega al penúltimo nodo y ajusta su `siguiente` a `null`. |
| `Rmove_posic` | `removerEnPosicion` | El predecesor enlaza directamente con el sucesor del nodo eliminado. |

### Visualización (sin modificar la estructura)
| Botón | Operación |
|-------|-----------|
| `ver_inicio` | Retorna el record en `primero`. |
| `Ver_final` | Recorre hasta el último nodo y retorna su record. |
| `Buscar_valor` (panel izquierdo) | Retorna el record en la posición indicada. |

### Búsqueda
| Botón | Operación |
|-------|-----------|
| `Buscar_valor` / `Buscar(ID)` | Recorre desde `primero` comparando el campo `id` del record hasta encontrar coincidencia. |

### Auxiliares
| Botón | Operación |
|-------|-----------|
| `Vaciar_List` | Restablece `primero = null` previa confirmación con `JOptionPane`. |

---

## ✅ Validaciones implementadas

- **Lista vacía:** Intentar remover, mostrar o buscar cuando `primero == null` muestra un `JOptionPane.ERROR_MESSAGE` sin lanzar `NullPointerException`.
- **Posición fuera de rango:** La inserción, remoción o consulta con índice negativo o ≥ `tamanio` es rechazada con aviso amigable en pantalla.
- **Búsqueda sin resultados:** Se informa claramente al usuario cuando el ID buscado no coincide con ningún record de la lista.
- **Campos vacíos:** El formulario valida que todos los campos estén llenos antes de instanciar el record.
- **Formato incorrecto:** El campo `ID` y el campo `Edad` aceptan únicamente enteros positivos; cualquier otro valor devuelve el foco al campo infractor y muestra un `JOptionPane` de error.
- **Confirmación de vaciado:** La operación `Vaciar_List` solicita confirmación `YES/NO` antes de ejecutarse.

---

## 📋 Restricciones técnicas cumplidas

- ✅ Desarrollado exclusivamente en **Java**.
- ✅ Entidad de dominio modelada como **`record Taxi`** (inmutable).
- ✅ **Prohibido:** `java.util.List`, `java.util.LinkedList`, `ArrayList` o arreglos estáticos. La lista se construye manualmente mediante nodos con `primero` y `siguiente`.
- ✅ Interfaz gráfica con **`javax.swing.*`**; lógica del TDA totalmente separada de la vista.
- ✅ Datos validados antes de instanciar el record; excepciones controladas con `JOptionPane`.
