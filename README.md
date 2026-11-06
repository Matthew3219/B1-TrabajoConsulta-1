# Trabajo de Consulta: Funciones Anónimas
**Materia:** Programación Funcional  
**Lenguajes:** Scala • Java • Python  
**Autor:** Matthew Castillo

---

## Parte 1 – Explorar: La programación funcional en el mundo real

### Caso 1: Apache Spark (Procesamiento de Big Data)
* **Problema que resuelven:** Procesar terabytes de datos distribuidos en clústeres de manera rápida y tolerante a fallos.
* **Lenguaje/Herramienta:** Scala (y APIs de Python/Java).
* **Ventaja del enfoque funcional:** Spark usa RDDs y DataFrames. Las transformaciones (`map`, `filter`, `reduceByKey`) se definen mediante funciones anónimas (lambdas). Esto permite que el motor de Spark serialice estas funciones y las envíe a los nodos del clúster para ejecutarlas en paralelo (inmutabilidad y ausencia de estado compartido evitan condiciones de carrera).
* **Fuente:** [Documentación oficial de Apache Spark - RDD Programming Guide](https://spark.apache.org/docs/latest/rdd-programming-guide.html)
* **Fragmento de código real:**
  ```scala
  // Contar palabras en un archivo de texto distribuido
  val texto = sc.textFile("hdfs://datos/archivo.txt")
  val conteo = texto.flatMap(line => line.split(" ")) 
                    .map(word => (word, 1))           
                    .reduceByKey(_ + _)              

* **Caso 2: Java Streams en Sistemas Bancarios (Enterprise)**
* **Problema que resuelven:** Filtrado y auditoría de millones de transacciones financieras en tiempo real para detectar fraudes o calcular impuestos.
* **Lenguaje/Herramienta:** Java 8+ (API Streams).
* **Ventaja del enfoque funcional:** Permite escribir código declarativo. En lugar de bucles for con variables mutables (propensas a errores en sistemas críticos), se usan pipelines de streams con lambdas, facilitando la lectura, el mantenimiento y permitiendo paralelización automática con .parallelStream().

* **Parte 2 – Entender: ¿Qué es una función anónima?**

* **¿Qué es y de dónde viene el nombre lambda?**

Una función anónima es un bloque de código que realiza una tarea específica pero que no está vinculada a un identificador (nombre) al ser definida. El término "lambda" proviene del Cálculo Lambda, un sistema formal de lógica matemática introducido por Alonzo Church en los años 1930 para investigar la definición de función y la aplicación de funciones, el cual sentó las bases de la programación funcional.

* **Diferencia con una función nombrada:**
// Función nombrada: Tiene un identificador ('calcularIVA').
def calcularIVA(precio: Double): Double = precio * 0.12

// Función anónima (Lambda): No tiene nombre.
(precio: Double) => precio * 0.12

* **Ciudadanos de primera clase:**
Significa que las funciones pueden ser tratadas como cualquier otra variable: pueden ser asignadas a variables, pasadas como argumentos a otras funciones y devueltas como resultado. Esto es lo que permite pasar una lambda directamente a métodos como map o filter, ya que estos métodos esperan una función como parámetro para aplicar su lógica interna.

* **¿Qué es un closure?**

Es una función anónima que "captura" o recuerda el estado de las variables que estaban en su entorno léxico en el momento de su creación, incluso si esa variable ya no existe en el scope donde se ejecuta la lambda.

val tasaImpuesto = 0.15 // Variable externa
val aplicarImpuesto = (precio: Double) => precio + (precio * tasaImpuesto) // Closure
// La lambda 'recuerda' el valor de tasaImpuesto.

**Datos usados en todos los ejemplos:**

| nombre | categoria | precio | stock |
|---|---|---|---|
| Laptop | Tech | 1200.0 | 10 |
| Mouse | Tech | 25.0 | 150 |
| Teclado | Tech | 45.0 | 80 |
| Monitor | Tech | 300.0 | 20 |
| Silla | Muebles | 150.0 | 30 |
| Escritorio | Muebles | 250.0 | 15 |
| Lámpara | Muebles | 40.0 | 60 |
| Libro | Papelería | 15.0 | 200 |
| Cuaderno | Papelería | 5.0 | 500 |
| Bolígrafo | Papelería | 2.0 | 1000 |

### 3.1 Selección (`filter`)

**Situación real:** un e-commerce necesita mostrar en el catálogo solo los productos de la categoría "Tech" que cuesten más de $50.

```scala
// Scala
productos.filter(p => p.categoria == "Tech" && p.precio > 50)
```
```java
// Java
productos.stream().filter(p -> p.getCategoria().equals("Tech") && p.getPrecio() > 50).toList();
```
```python
# Python
list(filter(lambda p: p.categoria == "Tech" and p.precio > 50, productos))
```

**Salida (igual en los tres):**
```
Producto(Laptop,Tech,1200.0,10)
Producto(Monitor,Tech,300.0,20)
```


### 3.2 Transformación (`map`)

**Situación real:** el sistema de facturación genera textos "Nombre: $PrecioConIVA" (IVA 12 %) para imprimir tickets rápidos en caja.

```scala
// Scala
productos.map(p => "%s: $%.2f".format(p.nombre, p.precio * 1.12))
```
```java
// Java
productos.stream().map(p -> String.format("%s: $%.2f", p.getNombre(), p.getPrecio() * 1.12)).toList();
```
```python
# Python
list(map(lambda p: f"{p.nombre}: ${p.precio * 1.12:.2f}", productos))
```

**Salida (igual en los tres):**
```
Laptop: $1344.00
Mouse: $28.00
Teclado: $50.40
Monitor: $336.00
Silla: $168.00
Escritorio: $280.00
Lámpara: $44.80
Libro: $16.80
Cuaderno: $5.60
Bolígrafo: $2.24
```

> Se usa `%.2f` en los tres lenguajes: sin formato, multiplicar por `1.12` produce decimales como `28.000000000000004` por la representación de `Double`.


### 3.3 Conteo (`count`)

**Situación real:** el almacén necesita saber cuántos productos tienen stock crítico (menos de 20 unidades) para generar una orden de compra automática al proveedor.

```scala
// Scala
productos.count(p => p.stock < 20)
```
```java
// Java
productos.stream().filter(p -> p.getStock() < 20).count();
```
```python
# Python
len(list(filter(lambda p: p.stock < 20, productos)))
```

**Salida (igual en los tres):** `2` (corresponde a Laptop, con 10, y Escritorio, con 15).


### 3.4 Extremos (`maxBy` / `minBy`)

**Situación real:** en el dashboard de ventas se debe destacar el producto más caro (promociones premium) y el más barato (producto gancho).

```scala
// Scala
productos.maxBy(_.precio)   // y productos.minBy(_.precio)
```
```java
// Java
productos.stream().max(Comparator.comparingDouble(Producto::getPrecio)).orElseThrow();
productos.stream().min(Comparator.comparingDouble(Producto::getPrecio)).orElseThrow();
```
```python
# Python
max(productos, key=lambda p: p.precio)   # y min(productos, key=lambda p: p.precio)
```

**Salida (igual en los tres):**
```
Max: Producto(Laptop,Tech,1200.0,10)
Min: Producto(Bolígrafo,Papelería,2.0,1000)
```


### Capturas de ejecución

Cada captura muestra la salida completa de los cuatro problemas (3.1 a 3.4) en un lenguaje. Las tres coinciden con las salidas documentadas arriba.

| Scala | Java | Python |
|---|---|---|
| ![Ejecución en Scala](img/scala.png) | ![Ejecución en Java](img/java.png) | ![Ejecución en Python](img/python.png) |

[↑ Volver al índice](#contenido)

---

## Parte 4 – Ampliar: cuatro operaciones nuevas

Código completo y ejecutable en [`operaciones/Operaciones.scala`](operaciones/Operaciones.scala). Todos los ejemplos usan la misma lista de la [Parte 3](#parte-3--aplicar-tipos-de-problemas), y cada ficha indica a qué tipo de problema de esa parte se parece.

### 4.1 y 4.2 Fichas de las operaciones

#### Operación 1: `groupBy`

* **Qué hace:** agrupa los elementos en un `Map` donde la clave es el resultado de aplicar una función a cada elemento.
* **Lambda que recibe:** una función `A => K`, donde `K` es el tipo de la clave de agrupación.
* **Ejemplo en Scala:** `productos.groupBy(_.categoria)`
* **Salida:**
  ```
  Muebles -> List(Silla, Escritorio, Lámpara)
  Papelería -> List(Libro, Cuaderno, Bolígrafo)
  Tech -> List(Laptop, Mouse, Teclado, Monitor)
  ```
* **Problema que resuelve:** organizar el catálogo por secciones para armar los menús laterales de la tienda. Tipo: **Agrupación** (tipo nuevo, no está en la tabla 3.1).

#### Operación 2: `sortBy`

* **Qué hace:** ordena los elementos de menor a mayor según el valor que devuelve una función de extracción de clave.
* **Lambda que recibe:** una función `A => B`, donde `B` debe ser un tipo ordenable.
* **Ejemplo en Scala:** `productos.sortBy(_.precio)`
* **Salida:**
  ```
  Bolígrafo ($2.0), Cuaderno ($5.0), Libro ($15.0), Mouse ($25.0), Lámpara ($40.0),
  Teclado ($45.0), Silla ($150.0), Escritorio ($250.0), Monitor ($300.0), Laptop ($1200.0)
  ```
* **Problema que resuelve:** mostrar los productos de la web ordenados por precio cuando el usuario elige "Ordenar por: menor precio". Tipo: **Ordenamiento** (tipo nuevo).

#### Operación 3: `find`

* **Qué hace:** devuelve el primer elemento que cumple una condición, dentro de un `Option` (`Some` si existe, `None` si no), lo que evita errores por `null`.
* **Lambda que recibe:** un predicado `A => Boolean`.
* **Ejemplo en Scala:** `productos.find(p => p.nombre == "Monitor")`
* **Salida:**
  ```
  Some(Producto(Monitor,Tech,300.0,20))
  None            // al buscar "Tablet", que no existe
  ```
* **Problema que resuelve:** verificar si un producto existe en el inventario antes de agregarlo al carrito. Se parece al tipo **Validación** (`exists`) pero además devuelve el elemento.

#### Operación 4: `zipWithIndex`

* **Qué hace:** empareja cada elemento con su posición, generando tuplas `(elemento, índice)`.
* **Lambda que recibe:** ninguna directamente; se encadena con `map`, cuya lambda recibe la tupla `(producto, índice)`.
* **Ejemplo en Scala:** `productos.zipWithIndex.map { case (p, i) => s"${i + 1}. ${p.nombre}" }`
* **Salida:**
  ```
  1. Laptop, 2. Mouse, 3. Teclado, 4. Monitor, 5. Silla,
  6. Escritorio, 7. Lámpara, 8. Libro, 9. Cuaderno, 10. Bolígrafo
  ```
* **Problema que resuelve:** generar una lista numerada o ranking para un reporte en PDF o consola. Se parece al tipo **Transformación** (`map`), con la diferencia de que usa la posición del elemento.

### 4.3 Combinar operaciones

**Pregunta:** ¿qué categorías tienen productos con precio mayor a $100, cuántos productos cumplen esto en cada una, ordenadas de mayor a menor cantidad?

```scala
productos
  .filter(p => p.precio > 100)                       // 1. vista en clase
  .groupBy(_.categoria)                              // 2. NUEVA
  .map { case (cat, lista) => (cat, lista.size) }    // 3. vista en clase
  .toList
  .sortBy { case (cat, n) => (-n, cat) }             // 4. NUEVA
```

**Qué hace cada paso:**

1. [`filter`](#31-selección-filter) deja solo los productos con precio > 100: Laptop, Monitor, Silla y Escritorio.
2. `groupBy` los agrupa por categoría: `Tech -> (Laptop, Monitor)` y `Muebles -> (Silla, Escritorio)`.
3. [`map`](#32-transformación-map) reemplaza cada lista por su tamaño, quedando pares `(categoría, cantidad)`.
4. `sortBy` ordena por cantidad descendente (`-n`). Como ambas categorías tienen 2, se agrega la categoría como criterio de desempate para que el resultado sea siempre el mismo.

**Salida:** `List((Muebles,2), (Tech,2))`

La cadena usa dos operaciones nuevas (`groupBy`, `sortBy`) y dos vistas en clase (`filter`, `map`).

[↑ Volver al índice](#contenido)

---

## Parte 5 – Comparar y concluir

### 5.1 Scala, Java y Python frente a frente

La comparación se basa en las soluciones de la [Parte 3](#parte-3--aplicar-tipos-de-problemas) (código en [`scala/`](scala/), [`java/`](java/) y [`python/`](python/)).

| Criterio | Scala | Java | Python |
|---|---|---|---|
| Sintaxis de la lambda | `x => x + 1` | `x -> x + 1` | `lambda x: x + 1` |
| Cómo se declara el tipo | Inferencia fuerte (declararlo es opcional) | Inferencia en lambdas, tipos estrictos en métodos | Dinámico, no se declara |
| Operaciones con listas disponibles | Nativas en la colección, inmutables, API muy rica | A través de la API externa `Stream`, perezosa | Funciones built-in (`map`, `filter`), menos fluidas |
| Restricción del lenguaje (ver [closure](#qué-es-un-closure)) | Permite capturar `var`, pero lo idiomático es capturar solo `val` inmutables | Las variables externas deben ser *effectively final* | Permite leer variables externas; para reasignarlas dentro de la función hace falta `nonlocal` |
| Legibilidad (opinión) | Muy alta: sintaxis limpia, el `.` encadena de forma natural | Alta, pero verbosa (`.stream()`, `.toList()`) | Media: `map`/`filter` a veces se leen peor que las *list comprehensions* |

### Reflexión

**¿En qué lenguaje fue más natural?** En Scala, porque las operaciones funcionales son nativas de las colecciones y la sintaxis es muy concisa (por ejemplo `_.precio`).

**¿En qué tipo de problema hubo más diferencias?** En **Extremos** (`maxBy` / `minBy`) y en **Transformación**. En Scala y Python el lenguaje ofrece la operación directa (`maxBy` o `key=`), mientras que en Java hubo que pasar por `Streams` y construir un `Comparator` explícito (`Comparator.comparingDouble(...)`), lo que agrega "ruido" visual. En Transformación, Java y Scala necesitaron además dar formato al decimal con `String.format`.

### 5.2 Conclusión

Las funciones anónimas son la pieza central para trabajar con listas porque permiten separar el "cómo" se recorre una colección (el bucle tradicional) del "qué" se quiere hacer con cada elemento (la lógica de negocio). Así el código es declarativo, más fácil de leer y menos propenso a errores por estado mutable, y además es la base para ejecutar esa lógica en paralelo o de forma distribuida.

El [Caso 1 de Apache Spark](#caso-1-apache-spark-procesamiento-de-big-data) (Parte 1) lo muestra: Spark recibe la lógica como lambdas (`line => line.split(" ")`, `word => (word, 1)`), las serializa y las envía a los nodos del clúster. Funciones sin estado compartido son justamente las que se pueden repartir con seguridad. Por eso las lambdas no son solo comodidad sintáctica: son el mecanismo que permite que el mismo estilo de código funcione desde una lista en memoria hasta un clúster de muchas máquinas.

[↑ Volver al índice](#contenido)

---

## Referencias

* Apache Spark. *RDD Programming Guide*. https://spark.apache.org/docs/latest/rdd-programming-guide.html
* Oracle. *Aggregate Operations* (Java Tutorials). https://docs.oracle.com/javase/tutorial/collections/streams/index.html
* Oracle. *Lambda Expressions* (Java Tutorials). https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
* Documentación oficial de Scala. https://docs.scala-lang.org
* Python Software Foundation. *Functional Programming HOWTO*. https://docs.python.org/3/howto/functional.html
* Winitzki, S. *The Science of Functional Programming: A Tutorial, with Examples in Scala*. https://github.com/winitzki/sofp
* **Uso de IA:** se usó Claude (Anthropic) como asistente para organizar y revisar el README y el código. Las salidas de Java y Python se ejecutaron y compararon; el código fue revisado por el autor.
