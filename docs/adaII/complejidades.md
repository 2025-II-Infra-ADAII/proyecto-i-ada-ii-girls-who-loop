# Informe de Complejidad — El problema del riego óptimo

En este informe se presentan los análisis de **complejidad temporal** y **espacial** de las soluciones implementadas al problema del riego óptimo: fuerza bruta, programación dinámica y algoritmo voraz. Se utilizará notación matemática en LaTeX para expresar el costo en función del número de tablones \(n\).

---

## 1. Fuerza bruta (solución ingenua)

### Complejidad temporal

La fuerza bruta consiste en explorar absolutamente todas las combinaciones posibles de los objetos.  
Cada objeto puede **incluirse o no incluirse** en la mochila, lo que genera un total de:
\[
2^n
\]
subconjuntos.

Para cada subconjunto, se debe calcular el peso y el valor total, lo cual implica recorrer hasta \(n\) elementos. En el peor caso, se hace este recorrido en todas las combinaciones, resultando en:
\[
T(n) = O(n \cdot 2^n)
\]

Esto significa que el algoritmo escala **exponencialmente**: un aumento modesto en el número de objetos hace que el tiempo de ejecución crezca de manera incontrolable. Por ejemplo, con \(n=30\), ya se tendrían más de mil millones de combinaciones a evaluar. Por eso, esta solución solo es viable para instancias muy pequeñas del problema.

### Complejidad espacial

El algoritmo mantiene:

- La lista de objetos: \(O(n)\).
- Una lista temporal para almacenar subconjuntos (se libera al final de cada iteración).

Por tanto:
\[
S(n) = O(n)
\]

---

## 2. Programación dinámica

### Complejidad temporal

#### Número de Estados Únicos

El número total de estados posibles está dado por:

$$
\text{Estados} = \sum_{k=0}^{n} \binom{n}{k} \cdot T_{\max}
$$

donde:
- $\binom{n}{k}$ representa todas las posibles **combinaciones** de $k$ tablones de $n$ totales
- $T_{\max}$ es el tiempo máximo posible

**Simplificación:**

Si asumimos que el tiempo está **discretizado** o **acotado**, el número de subconjuntos domina:

$\text{Estados} = 2^n \cdot T_{\max}$

Sin embargo, en la práctica, **no todos los tiempos son alcanzables**, por lo que el número real de estados es:

$\text{Estados}_{\text{reales}} \approx 2^n \cdot n$

Esto porque el tiempo máximo está acotado por $\sum_{i=0}^{n-1} tr_i$.

#### Trabajo por Estado

Por cada estado, el algoritmo:
1. Itera sobre todos los tablones disponibles en el conjunto $S$
2. Para cada tablón, hace:
   - Cálculo del costo: $O(1)$
   - Creación de nuevo conjunto: $O(|S|)$ en Java (HashSet)
   - Llamada recursiva: $O(1)$ (por memoización)

**Costo por estado:**
$T_{\text{estado}} = O(|S|^2) = O(n^2)$

#### Complejidad temporal total

**Desglose:**
$\boxed{T(n) = O(n^2 \cdot 2^n)}$

- $2^n$: número de subconjuntos posibles de tablones
- $n^2$: trabajo por cada estado (iteración + operaciones de conjunto)


### Complejidad espacial

$\boxed{S(n) = O(n \cdot 2^n)}$

**Desglose:**
- $2^n$: entradas en el HashMap de memoización
- $n$: espacio por cada estado (HashSet con máximo $n$ elementos)
- Pila de recursión: $O(n)$ (profundidad máxima)


---

## 3. Algoritmo voraz (greedy por razón valor/peso)

### Complejidad temporal

El algoritmo voraz comienza calculando la razón valor/peso de cada objeto, lo que requiere:
\[
O(n)
\]

Luego, se ordenan los objetos según esta razón, lo cual domina el costo total, ya que el ordenamiento eficiente requiere:
\[
O(n \log n)
\]

Finalmente, se recorre la lista ordenada para llenar la mochila, con un costo lineal:
\[
O(n)
\]

Por tanto, la complejidad total es:
\[
T(n) = O(n \log n)
\]

Esto convierte al algoritmo voraz en el más eficiente en tiempo entre las tres aproximaciones. Sin embargo, a diferencia de la programación dinámica, no garantiza la solución óptima, ya que tomar siempre el objeto con mejor razón local no asegura la mejor combinación global.

### Complejidad espacial

- Lista de objetos con razón valor/peso: \(O(n)\).
- No se requiere matriz adicional.

Así:
\[
S(n) = O(n)
\]

---

## 4. Resumen comparativo

| Estrategia            | Complejidad temporal | Complejidad espacial                   |
| --------------------- | -------------------- | -------------------------------------- |
| Fuerza bruta          | \(O(n \cdot 2^n)\)   | \(O(n)\)                               |
| Programación dinámica | \(O(n \cdot W)\)     | \(O(n \cdot W)\) ó \(O(W)\) optimizado |
| Voraz                 | \(O(n \log n)\)      | \(O(n)\)                               |

---

### 4.1 Analisis mediante gráficas

Comparacion teorica de complejidades en tiempo y espacio para las tres estrategias implementadas.

! [Comparativa de tiempo](../imagenes/g1.png)

Analisis de DP con W con n fijo

! [Comparativa DP](../imagenes/g2.png)

Recordar incluir la comparacion de tiempos con respecto a lo implementado. No se incluye en este ejemplo.

## 5. Conclusiones

- La **fuerza bruta** es inviable para \(n\) moderados debido a su crecimiento exponencial.
- La **programación dinámica** es óptima en exactitud, pero depende de \(W\), lo que puede ser prohibitivo si \(W\) es muy grande (pseudopolinomial).
- El **algoritmo voraz** es eficiente en tiempo y espacio, pero no garantiza la solución óptima.
