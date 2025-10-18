# Informe de Implementación — Problema del riego óptimo

## 0. Descripción del problema

El objetivo es encontrar una **permutación óptima** $\pi$ de los tablones que **minimice** el costo total de riego de la finca dada.

### Definición formal

Dado un conjunto de $n$ tablones, cada uno con tres atributos:
- **Tiempo de supervivencia** ($ts_i$): tiempo de supervivencia del tablón
- **Tiempo de riego** ($tr_i$): tiempo que toma regar el tablón
- **Prioridad** ($p_i$): prioridad del tablón
Se busca obtener la mejor programación de riego para una finca, donde el costo de riego de un tablon $i$ está definido como:

$
\text{CRF}(i, t) = p_i \cdot \max(0, t + tr_i - ts_i)
$

donde $t$ es el tiempo de inicio del riego del tablón $i$.

### Ejemplo ilustrativo

**Tablones:**
1. $ts_1=5, tr_1=2, p_1=3$
2. $ts_2=8, tr_2=3, p_2=2$
3. $ts_3=6, tr_3=1, p_3=4$

**Objetivo:** Minimizar el costo total de riego comenzando en $t=0$.

**Análisis de permutaciones:**

- **Permutación [1,2,3]:**
  - Tablón 1 en $t=0$: $\text{CRF}(1,0) = 3 \cdot \max(0, 0+2-5) = 3 \cdot 0 = 0$
  - Tablón 2 en $t=2$: $\text{CRF}(2,2) = 2 \cdot \max(0, 2+3-8) = 2 \cdot 0 = 0$
  - Tablón 3 en $t=5$: $\text{CRF}(3,5) = 4 \cdot \max(0, 5+1-6) = 4 \cdot 0 = 0$
  - **Costo total: 0**

- **Permutación [2,3,1]:**
  - Tablón 2 en $t=0$: $\text{CRF}(2,0) = 2 \cdot \max(0, 0+3-8) = 2 \cdot 0 = 0$
  - Tablón 3 en $t=3$: $\text{CRF}(3,3) = 4 \cdot \max(0, 3+1-6) = 4 \cdot 0 = 0$
  - Tablón 1 en $t=4$: $\text{CRF}(1,4) = 3 \cdot \max(0, 4+2-5) = 3 \cdot 1 = 3$
  - **Costo total: 3**

- **Permutación [3,2,1]:**
  - Tablón 3 en $t=0$: $\text{CRF}(3,0) = 4 \cdot \max(0, 0+1-6) = 4 \cdot 0 = 0$
  - Tablón 2 en $t=1$: $\text{CRF}(2,1) = 2 \cdot \max(0, 1+3-8) = 2 \cdot 0 = 0$
  - Tablón 1 en $t=4$: $\text{CRF}(1,4) = 3 \cdot \max(0, 4+2-5) = 3 \cdot 1 = 3$
  - **Costo total: 3**

**Solución óptima:** Permutación [1,2,3] con costo total **0**.

**Observación:** Todos los tablones se riegan antes de su tiempo de supervivencia, evitando penalizaciones.

---

## 1. Lenguaje y herramientas usadas

- **Lenguaje:** Java 24
- **Bibliotecas estándar:** `java.util`.

- **Estructuras usadas:** Arrays, HashSet, HashMap y representación con Objects.
- **Motivación de elección:** Java es robusto y de tipado Estático, permitiendo asi la detección temprana de errores, sus estructuras optimizadas facilitan la representación en las implementaciones.
---

## 2. Estructura del proyecto

El proyecto se organizó en archivos principales:

```

knapsack_project/
│
├── knapsack_report.py # Implementaciones ingenua, dinámica y voraz
├── benchmark.py # Script para medición de tiempos (no usado en este informe)
├── plot_results.py # Script para graficar resultados (no usado en este informe)
├── requirements.txt # Dependencias (matplotlib opcional)
└── .github/
└── workflows/
└── ci.yml # Pipeline de compilación/ejecución

```

---

## 3. Ejecución del proyecto

La ejecución se hace desde consola.

### Ejemplo de ejecución básica:

```bash
python knapsack_report.py
```

### Parámetros técnicos

- El programa puede generar instancias aleatorias con:
  - `n`: número de objetos.
  - `max_weight`: peso máximo de un objeto.
  - `max_value`: valor máximo de un objeto.
  - `seed`: semilla para reproducibilidad.

- Ejemplo:

```python
from knapsack_report import gen_instance, knapsack_dp
items, W = gen_instance(10, 20, 100, seed=1)
print(knapsack_dp(items, W))
```

---

## 4. Ideas de solución

### a) Solución ingenua (fuerza bruta)

Generar todos los subconjuntos ($2^n$) y elegir el de mayor valor que cumpla la restricción:

$$
\text{Óptimo} = \max_{S \subseteq \{1,\dots,n\},\; \sum_{i \in S} w_i \leq W} \sum_{i \in S} v_i
$$

**Ejemplo:** con $W=5$, objetos como el caso introductorio → se recorren todos los subconjuntos hasta encontrar $\{1,2\}$.

---

### b) Solución dinámica

#### Subestructura óptima:
El problema exhibe **subestructura óptima** porque:

> **Si tenemos una solución óptima para regar un conjunto de tablones $S$ comenzando en tiempo $t$, entonces:**
> - La decisión del **primer tablón** a regar (digamos $k$) es óptima
> - La solución para el **subconjunto restante** $S \setminus \{k\}$ comenzando en tiempo $t + tr_k$ también debe ser óptima

#### Relación de recurrencia:

##### Definicion del estado
Sea $DP[S, t]$ el **costo mínimo** para regar todos los tablones en el conjunto $S$, comenzando en el tiempo $t$.

##### Caso base
$$
DP[\emptyset, t] = 0 \quad \text{(no hay tablones por regar)}
$$

##### Relación recursiva:
Para un conjunto no vacío $S$ y tiempo $t$:

$
DP[S, t] = \min_{i \in S} \left\{ \text{CRF}(i, t) + DP[S \setminus \{i\}, t + tr_i] \right\}
$

Donde:
- $\text{CRF}(i, t) = p_i \cdot \max(0, t + tr_i - ts_i)$ es el costo de regar el tablón $i$ en el tiempo $t$
- $S \setminus \{i\}$ es el conjunto $S$ sin el tablón $i$
- $t + tr_i$ es el nuevo tiempo después de regar el tablón $i$

##### Solución al Problema Original

$$
\text{Costo Óptimo} = DP[S_{\text{inicial}}, 0]
$$

donde $S_{\text{inicial}} = \{0, 1, 2, \ldots, n-1\}$ (todos los tablones).

---

### c) Solución voraz

Ordenar los objetos por eficiencia:

$$
\text{ratio}_i = \frac{v_i}{w_i}
$$

y agregar al conjunto mientras la capacidad lo permita.

**Ejemplo:** con objetos

1. $v/w = 1.5$,
2. $v/w = 1.33$,
3. $v/w = 1.25$.
   Se eligen primero los más eficientes hasta llenar la capacidad.

---

## 5. Partes importantes del código

- **Generación de instancias (`gen_instance`)**
  Permite crear listas de objetos aleatorios y fijar una capacidad $W$. Relaciona pesos y valores de forma controlada.

- **Fuerza bruta (`knapsack_bruteforce`)**
  Recorre subconjuntos mediante bitmasks. Se incluye **poda temprana**: si la suma de pesos supera $W$, se descarta inmediatamente.

- **Programación dinámica (`knapsack_dp`)**
  Construye una tabla `V` y aplica la recurrencia matemática. Después, reconstruye la solución verificando si un objeto fue incluido comparando `V[i][w]` con `V[i-1][w]`.

- **Voraz (`knapsack_greedy_ratio`)**
  Ordena por $\frac{v_i}{w_i}$ y selecciona mientras quepa. Refleja fielmente la heurística.

Cada módulo se ajusta a la definición matemática explicada en la sección 4.

---

## 6. Pipeline de compilación/ejecución

Se definió un pipeline simple de integración continua en **GitHub Actions** para verificar que el proyecto se ejecute sin errores (no incluye pruebas de rendimiento ni validación).

Archivo: `.github/workflows/ci.yml`

```yaml
name: Knapsack CI

on:
  push:
    branches: ["main"]
  pull_request:
    branches: ["main"]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3

      - name: Set up Python
        uses: actions/setup-python@v4
        with:
          python-version: "3.10"

      - name: Install dependencies
        run: |
          python -m pip install --upgrade pip
          if [ -f requirements.txt ]; then pip install -r requirements.txt; fi

      - name: Run knapsack main script
        run: |
          python knapsack_report.py
```

```

```
