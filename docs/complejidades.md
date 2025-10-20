# Informe de Complejidad — El problema del riego óptimo

En este informe se presentan los análisis de **complejidad temporal** y **espacial** de las soluciones implementadas al problema del riego óptimo: fuerza bruta, programación dinámica y algoritmo voraz.

---

# 1. Fuerza Bruta

## Complejidad

El algoritmo por **fuerza bruta** genera todas las permutaciones posibles de los tablones (de 0 a *n−1*) y calcula el costo total de riego (**CRF**$_\Pi$) para cada una.  
En el código, esto ocurre en el método `backtrack`, que recorre recursivamente todas las combinaciones posibles y evalúa cada una con `evaluateCRF`.

---

### a) Generación de permutaciones

Para una finca con $n$ tablones, el número total de permutaciones posibles es:

$$n! = n \times (n - 1) \times (n - 2) \times \ldots \times 1$$

Cada llamada recursiva elige un tablón no usado y continúa hasta completar una permutación completa.  
Por tanto, la generación de todas las permutaciones tiene complejidad temporal:

$O(n!)$

---

### b) Evaluación del costo (`evaluateCRF`)

Por cada permutación generada, se recorre una vez la lista de tablones (longitud $n$) y se calcula el costo total de riego mediante la fórmula:

$$CRF_\Pi = \sum_{i=0}^{n-1} p_i \cdot \max(0, (t_{\Pi_i} + tr_i) - ts_i)$$

Este proceso tiene complejidad lineal $O(n)$ por permutación.

---

### c) Complejidad total

Combinando ambos factores:

$$T(n) = O(n!) \times O(n) = O(n \cdot n!)$$

Esto significa que el tiempo de ejecución crece **extremadamente rápido** a medida que aumenta el número de tablones.

| n  | Número de permutaciones | Operaciones aproximadas |
|----|--------------------------|--------------------------|
| 5  | 120                      | 600                      |
| 8  | 40,320                   | 322,560                  |
| 10 | 3,628,800                | 36,288,000               |

Por esta razón, el enfoque de fuerza bruta solo es útil para **tamaños pequeños de finca** (por ejemplo, hasta 10 tablones) y resulta **impracticable** para valores grandes de $n$.

---

### d) Complejidad espacial

Se almacenan las siguientes estructuras:

- Un arreglo `current` de tamaño $n$  
- Un arreglo `used` de tamaño $n$  
- Una estructura auxiliar `best` que guarda la mejor permutación encontrada

Por tanto, la complejidad espacial es:

$O(n)$

---

## Corrección

El algoritmo de **fuerza bruta** siempre encuentra la **solución óptima**, porque:

1. **Explora exhaustivamente todo el espacio de soluciones.**  
   El método `backtrack` genera todas las posibles permutaciones de los tablones sin omitir ninguna.

2. **Evalúa cada permutación usando la misma función de costo `evaluateCRF`.**  
   Así, todas las combinaciones posibles son medidas bajo el mismo criterio.

3. **Selecciona la de menor costo.**  
   Se compara el costo actual con el mejor encontrado y se actualiza si es menor.

Por tanto, en teoría y en la práctica (salvo errores de precisión o limitaciones de memoria para valores grandes de $n$), el algoritmo devuelve siempre **la respuesta correcta**, es decir, **la programación óptima de riego**.

### **Ejemplos de ejecución**
Para una finca con 10 tablones, cada uno con sus respectivos valores de ts (tiempo de supervivencia), tr (tiempo de riego) y p (prioridad), la estrategia de Fuerza Bruta genera todas las posibles permutaciones.

**Resultado obtenido:**

![Comparativa DP](/docs/imagenes/FuerzaBruta10.png)

**Interpretación**

- El algoritmo encontró que regar primero los tablones 3 y 6 minimiza los retrasos acumulados de los tablones con menor tiempo de supervivencia.

- Este orden equilibra el riego de tablones prioritarios y sensibles al tiempo, evitando penalizaciones altas.

Si se incrementara el número de tablones (por ejemplo, a 12 o más), el tiempo crecería de forma drástica, volviéndose computacionalmente inviable.

# 2. Programación dinámica

## Caracterización de la estructura de una solución óptima

El problema del **riego óptimo** presenta una **subestructura óptima**, ya que la decisión de regar un tablón en un momento determinado influye en el costo total, pero el subproblema restante (regar los tablones no seleccionados) mantiene la misma naturaleza.

Podemos definir un **subproblema** como:

$$DP(S, t) = \text{costo mínimo de regar los tablones del conjunto } S \text{ comenzando en el tiempo } t$$

donde:
- $S \subseteq \{0, 1, 2, \ldots, n-1\}$ es el conjunto de tablones restantes por regar  
- $t$ es el tiempo acumulado actual (momento en que se iniciará el riego del siguiente tablón)

La idea es **dividir y vencer**: resolver el costo mínimo para todos los subconjuntos de tablones posibles, aprovechando resultados previos mediante **memoización**.

---

## Definición recursiva del valor de una solución óptima

La recurrencia que define la programación dinámica es:

$$DP(S, t) = \min_{i \in S} [ p_i \cdot \max(0, (t + tr_i) - ts_i) + DP(S - \{i\}, t + tr_i) ]$$

con caso base:

$$DP(\emptyset, t) = 0$$

Esto significa que, para cada subconjunto $S$, se intenta regar cada tablón $i$ en primer lugar y se suma su costo de sufrimiento con el costo óptimo de regar los demás.

---

## Descripción del algoritmo

1. **Inicialización:** se crea una tabla o `HashMap` donde cada clave representa un subconjunto de tablones (por ejemplo, un entero binario) y su valor el costo mínimo asociado.  
2. **Cálculo recursivo:**  
   - Se exploran todas las opciones posibles de siguiente tablón a regar.  
   - Si el resultado de un subconjunto ya fue calculado, se reutiliza (memoización).  
3. **Construcción de la solución:**  
   - A partir de las decisiones óptimas almacenadas, se reconstruye la secuencia de riego que minimiza el CRF total.

El algoritmo se implementa mediante una función recursiva con memorización (`HashMap`) o una tabla de tamaño $2^n$.

---

## Complejidad temporal

#### Número de Estados Únicos

El número total de estados posibles está dado por:

$$\text{Estados} = \sum_{k=0}^{n} \binom{n}{k} \cdot T_{\max}$$

donde:
- $\binom{n}{k}$ representa todas las posibles **combinaciones** de $k$ tablones de $n$ totales  
- $T_{\max}$ es el tiempo máximo posible de regado acumulado

**Simplificación:**

Si asumimos que el tiempo está **discretizado** o **acotado**, el número de subconjuntos domina:

$\text{Estados} = 2^n \cdot T_{\max}$

Sin embargo, en la práctica, **no todos los tiempos son alcanzables**, por lo que el número real de estados es:

$\text{Estados}_{\text{reales}} \approx 2^n \cdot n$

Esto porque el tiempo máximo está acotado por $\sum_{i=0}^{n-1} tr_i$.

---

#### Trabajo por Estado

Por cada estado, el algoritmo:
1. Itera sobre todos los tablones disponibles en el conjunto $S$  
2. Para cada tablón, hace:
   - Cálculo del costo: $O(1)$  
   - Creación de nuevo conjunto: $O(|S|)$ en Java (HashSet)  
   - Llamada recursiva: $O(1)$ (por memoización)

**Costo por estado:**  
$T_{\text{estado}} = O(|S|^2) = O(n^2)$

---

## Complejidad temporal total

**Desglose:**  
$\boxed{T(n) = O(n^2 \cdot 2^n)}$

- $2^n$: número de subconjuntos posibles de tablones  
- $n^2$: trabajo por cada estado (iteración + operaciones de conjunto)

Aunque sigue siendo exponencial, **es mucho más eficiente que la fuerza bruta** $O(n \cdot n!)$, permitiendo resolver instancias de tamaño medio.

---

## Complejidad espacial

$\boxed{S(n) = O(n \cdot 2^n)}$

**Desglose:**  
- $2^n$: entradas en el HashMap de memoización  
- $n$: espacio por cada estado (HashSet con máximo $n$ elementos)  
- Pila de recursión: $O(n)$ (profundidad máxima)

---

## Corrección

El algoritmo de programación dinámica **garantiza la solución óptima**, porque:

1. Evalúa **todos los subconjuntos posibles** de tablones  
2. Usa **memoización**, por lo que cada subproblema se resuelve una sola vez  
3. Cumple los **principios de optimalidad de Bellman**, donde la solución óptima global se construye a partir de las soluciones óptimas de los subproblemas

Por lo tanto, este método **siempre produce la programación de riego con el menor costo total posible**, a diferencia del voraz, que puede fallar en algunos casos.

---

### **Ejemplos de ejecución**

Para la misma finca de 10 tablones que antes, se aplicó la estrategia de Programación Dinámica, la cual reduce drásticamente el número de combinaciones necesarias mediante memoización.

### Resultados obtenidos:

![Comparativa DP](/docs/imagenes/Dinamica10.png)

**Interpretación**

El resultado es idéntico al de la Fuerza Bruta, lo que confirma que la Programación Dinámica preserva la exactitud del algoritmo original.

Sin embargo, el tiempo de ejecución fue más de 6 veces menor, demostrando la eficiencia de la reutilización de subresultados mediante memoización.

**Con 12 tablones:**

![Comparativa DP](/docs/imagenes/Dinamica12.png)

---

## 3. Algoritmo voraz (Greedy por razón ts / (p * tr))

En un algoritmo voraz (Greedy), la idea central es tomar siempre la mejor decisión local posible en cada paso, aunque no garantice que sea la óptima (como sí lo hace fuerza bruta o la programación dinamica).

Se dice que es “basada por razón”, cuando cada elemento (tablón) se evalúa mediante una razón o proporción numérica que combina distintos factores del problema.

Luego, el algoritmo ordena o selecciona los elementos de acuerdo con esa razón que representa su eficiencia o urgencia.

---

### Complejidad temporal

El algoritmo voraz implementado en la clase `EstrategiaVoraz` utiliza la siguiente heurística:

$$
h(i) = \frac{ts_i}{p_i \cdot tr_i}
$$

donde:  
- $ts_i$: tiempo de supervivencia del tablón $i$ 
- $tr_i$: tiempo de riego del tablón $i$
- $p_i$: prioridad asignada al tablón $i$

El procedimiento general del algoritmo se compone de los siguientes pasos:

1. **Creación de la lista de índices**  
   Se genera una lista que representa cada tablón de la finca. $$[0, 1, 2, \ldots, n-1]$$ donde $n$ representa el número total de tablones. Esta proceso recorre todos los tablones una vez, por lo que su complejidad es: $$T_1(n) = O(n)$$


3. **Ordenamiento según la heurística**  

   Cada tablón se ordena con base en su valor heurístico \( h(i) \), empleando el método `sort()` utilizando un comparador (`Comparator.comparingDouble`). de Java. Este método usa internamente **Timsort**, cuyo costo promedio y peor caso es:

   $$T_2(n) = O(n \log n)$$

   En este paso **domina el costo total**, ya que $O(n \log n)$ crece más rápidamente que las fases lineales.

4. **Cálculo del costo total $CRF_{\Pi}$**  
   Una vez ordenada la lista, se recorre secuencialmente para calcular:

   - Cálculo del final del riego:
     
     
     $finRiego_i = t_{actual} + tr_i$
   
     
   - Cálculo del retraso:

        $$\text{retraso}_i = \max(0, \text{finRiego}_i - ts_i)$$
     
   - Cálculo de la penalización:

        $$\text{penalización}_i = p_i \cdot \text{retraso}_i$$
     
   - Se acumula el costo total (`costoTotal += penalizacion`):

        $$CRF_{\Pi} = \sum_{i=1}^{n} \text{penalización}_i$$

   Este cálculo se realiza una sola vez para cada tablón, por lo tanto, el proceso que realiza es lineal. Su costo lineal es de: $$T_3(n) = O(n)$$

Sumando los tres pasos realizados:

$$
T(n) = T_1(n) + T_2(n) + T_3(n) = O(n) + O(n \log n) + O(n)
$$

Por lo tanto, la complejidad total del algoritmo viene dada por:

$$
\boxed{T(n) = O(n \log n)}
$$

El costo dominante proviene del **ordenamiento**, lo que hace que la estrategia voraz tenga una **complejidad temporal de `O(n log n)`**. 


Esto la convierte en una solución **eficiente en tiempo**, especialmente adecuada para entradas grandes (por ejemplo, 1000 tablones). Sin embargo, como en todo enfoque voraz, **no garantiza una solución óptima global**, ya que la decisión de ordenamiento se basa en una **heurística local**.

---

### Complejidad espacial

Durante la ejecución se utilizan las siguientes estructuras:

1. **Listas y arreglos de entrada:**  
   - `ts`, `tr`, `p` → cada uno de tamaño $O(n)$.
2. **Lista auxiliar de índices (`List<Integer> indices`)**  
   Almacena los identificadores de los tablones a ordenar → $O(n)$.
3. **Arreglo `orden[]` con la programación final**  
   Guarda el orden en que se riegan los tablones → $O(n)$.
4. **Variables escalares:**  
   `tiempoActual`, `costoTotal`, `index`, etc. → $O(1)$.

El espacio crece linealmente con el número de tablones, no se utilizan matrices ni estructuras adicionales complejas. Por lo tanto, la complejidad total es de:

$$
\boxed{S(n) = O(n)}
$$

 
Como se dice anteriormente la estrategia voraz requiere **espacio lineal**, directamente proporcional al número de tablones procesados. Es eficiente y manejable incluso para instancias de gran tamaño (p. ej., 1000 o más tablones).

---

### Corrección

##### **¿El algoritmo voraz siempre da la respuesta correcta?**

No siempre. Ya que el algoritmo voraz **no garantiza la solución óptima global**, ya que toma decisiones **locales** en función de la heurística:

$$
h(i) = \frac{ts_i}{p_i \cdot tr_i}
$$


Esto prioriza los tablones que ofrecen una mejor relación entre supervivencia, prioridad y tiempo de riego, pero sin considerar el efecto acumulado de las decisiones sobre el resto del conjunto.


##### **Casos donde el algoritmo es correcto**

El algoritmo produce la **solución óptima** cuando se cumplen las siguientes condiciones:

1. Los tablones son **independientes** entre sí (regar uno no afecta el tiempo de supervivencia de los demás).
2. La heurística preserva la **propiedad de optimalidad local**, es decir, cada elección local es compatible con la óptima global.
3. Los valores $ts_i, tr_i, p_i$ presentan una **relación monótona**, por ejemplo, los tablones de mayor prioridad también tienen mayor supervivencia o menor tiempo de riego.

En estos casos, la decisión voraz minimiza efectivamente el costo total $CRF_{\Pi}$.


##### **Casos donde el algoritmo falla**

El algoritmo puede **no obtener la solución óptima** cuando:

1. Existen **interdependencias temporales**: regar un tablón tarde puede aumentar significativamente la penalización de otro.  
2. Se presentan tablones con **alta prioridad y baja supervivencia**: la heurística puede posponerlos en favor de otros con mejor razón $\frac{ts}{p \cdot tr}$, generando una penalización global más alta.  
3. Los valores $ts_i, tr_i, p_i$ son **irregulares** o no proporcionales, provocando que la decisión local lleve a un **óptimo local**, pero no al global.


El enfoque voraz es **rápido y eficiente** $O(n \log n)$ y genera resultados **cercanos al óptimo** para la mayoría de los casos prácticos.  Sin embargo, al depender de una heurística local, **no garantiza exactitud global** en todas las configuraciones de los datos.

---

### **Ejemplo**

Ejemplo de ejecución para 10 tablones (`10_tablones.txt`):

![ejecución para 10 tablones](/docs/imagenes/Ejemplo_10tablones.png)

Esta evidencia demuestra la correcta ejecución del algoritmo, su tiempo eficiente y el cálculo final del costo total $CRF_{\Pi}$.

---

## 4. Resumen comparativo

| Estrategia            | Complejidad temporal | Complejidad espacial                   |
| --------------------- | -------------------- | -------------------------------------- |
| Fuerza bruta          | $$O(n \cdot n!)$$   | $$O(n)$$                               |
| Programación dinámica | $$O(n^2 \cdot 2^n)$$   | $$O(n \cdot 2^n)$$  |
| Voraz                 | $$O(n \log n)$$      | $$O(n)$$                               |

---

### 4.1 Analisis mediante gráficas

Comparacion teorica de complejidades en tiempo y espacio para las tres estrategias implementadas.
- Fuerza Bruta: El tiempo de ejecución se dispara rápidamente (crecimiento factorial). La medición solo fue viable para instancias muy pequeñas, confirmando su inviabilidad.

  ![Comparativa de tiempo](/docs/imagenes/tiempoFuerzaBruta.png)

- Programación Dinámica: Muestra un crecimiento exponencial significativo. Es más escalable que Fuerza Bruta, pero solo viable para tamaños de problema medianos.

  ![Comparativa de tiempo](/docs/imagenes/tiempoDinamica.png)

- Programacion Voraz: El tiempo de ejecución se mantiene en un rango bajo (microsegundos/milsegundos) incluso para 10,000 tablones. Confirma que es la solución más eficiente y escalable debido a su complejidad pero no garantiza una solución optima 

  ![Comparativa de tiempo](/docs/imagenes/tiempoVoraz.png)

### Analisis y comparación teórica para las tres estretegias implementadas

Este gráfico compara las funciones de costo teórico en una escala logarítmica para un rango de $n$ de 2 a 20:

| Curva | Complejidad Teórica | Significado |
| :--- | :--- | :--- |
| **Exponencial (Amarillo)** | $$n!$$ | Representa el costo de **Fuerza Bruta**. Es la que crece más rápido, volviéndose inmanejable después de $n=12$ (la línea se dispara verticalmente a partir de $n=19$). |
| **Dinámica (Azul)** | $$n^2 \cdot 2^n$$ | Representa el costo de **Programación Dinámica**. Crece de manera exponencial, pero mucho más lentamente que la factorial, confirmando que es una mejora significativa sobre Fuerza Bruta. |
| **Voraz (Verde)** | $$n \cdot \log_2(n)$$ | Representa el costo del algoritmo **Voraz**. La curva es casi plana en la escala logarítmica. Demuestra que su crecimiento es casi lineal, siendo el **más eficiente** de los tres. |

![Comparativa DP](/docs/imagenes/comparacionTeorica.png)

El enfoque Voraz es el más rápido y escalable $O(n \log n)$, mientras que Programación Dinámica y Fuerza Bruta tienen un costo ($O(n^2 \cdot 2^n)$ y $O(n \cdot n!)$ respectivamente), limitando su uso a instancias de tamaño pequeño o mediano.

### Comparación de resultados experimentales vs complejidad teórica

| n   | Estrategia            | CRF (experimental) | Tiempo (ms) | Complejidad teórica     | Unidades teóricas aproximadas*      |
|-----|-----------------------|-------------------:|------------:|------------------------:|------------------------------------:|
| 10  | Voraz                 | 187                | 23.95       | O(n log n)             | ≈ 10 × log₂10 ≈ **33**              |
| 10  | Fuerza Bruta          | 114                | 142.44      | O(n · n!)              | 10 × 10! = **36,288,000**           |
| 10  | Programación Dinámica | 114                | 72.93       | O(n² · 2ⁿ)             | 10² × 2¹⁰ = **102,400**             |
| 100 | Voraz                 | 36,766             | 6.80        | O(n log n)             | ≈ 100 × log₂100 ≈ **664**           |
| 100 | Fuerza Bruta          | — (no factible)    | —           | O(n · n!)              | ≈ 100 × 100! ≈ **9.33×10¹⁵⁹**       |
| 100 | Programación Dinámica | — (no factible)    | —           | O(n² · 2ⁿ)             | 100² × 2¹⁰⁰ ≈ **1.26×10³⁴**         |

### Discusión de resultados

**Al comparar los resultados experimentales con la complejidad teórica, se observa una clara coherencia entre ambos comportamientos:**

Para 10 tablones, tanto Fuerza Bruta como Programación Dinámica obtuvieron el mismo costo óptimo (CRF = 114), confirmando su precisión teórica.
Sin embargo, la Fuerza Bruta tardó más del doble en ejecutarse, tal como predice su complejidad factorial O(n·n!), frente al crecimiento más controlado de la Programación Dinámica.

En contraste, la estrategia Voraz fue mucho más rápida (≈24 ms frente a 142 ms y 73 ms), cumpliendo su complejidad teórica O(n log n).
Aunque su costo (CRF = 187) fue mayor, sigue siendo una aproximación razonable cuando el tamaño del problema aumenta.

Para 100 tablones, solo la estrategia Voraz pudo ejecutarse exitosamente.
La Fuerza Bruta y la Programación Dinámica resultaron inviables en tiempo y memoria, validando que su crecimiento exponencial o factorial las hace impracticables a gran escala.

# 5. Conclusiones

- La técnica de **fuerza bruta** es conceptualmente sencilla y garantiza la solución óptima, ya que explora todo el espacio de permutaciones. Sin embargo, para valores moderados de \(n\) se vuelve inviable debido a su crecimiento factorial (\(O(n \cdot n!)\)): el coste de tiempo crece muy rápidamente y lo hace impráctico para instancias de tamaño medio o grande.

- La técnica de **programación dinámica** ofrece una solución exacta, basada en una exploración de todos los subconjuntos posibles (o de estados relevantes) junto con memoización, lo que reduce el coste respecto a la fuerza bruta. Aun así, su complejidad sigue siendo exponencial en el número de tareas/tablones (por ejemplo, \(O(n^2 \cdot 2^n)\) en nuestro caso) y además puede depender de un parámetro de tiempo acumulado \(W\) o similar, lo que la hace de carácter **pseudopolinomial** en algunos escenarios. Por tanto, aunque es una opción mucho mejor que la fuerza bruta para tamaños moderados, también se enfrenta a límites reales cuando \(n\) crece o cuando el dominio de los parámetros es grande.

- El **algoritmo voraz** (greedy) se destaca por su eficiencia en tiempo y espacio: suele tener coste polinómico y es muy viable computacionalmente incluso para instancias grandes. No obstante, su principal debilidad es que **no garantiza la solución óptima** en todos los casos, ya que toma decisiones locales “óptimas” sin reconsiderar el impacto global futuro. Por ello, es una buena heurística cuando la rapidez es prioridad, pero debe emplearse con consciencia de sus limitaciones.

---
