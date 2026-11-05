// Parte 4 - Cuatro operaciones nuevas (groupBy, sortBy, find, zipWithIndex)
// y la combinación de la sección 4.3.
// Ejecutar (desde la raíz del repo):
//   scala-cli run scala/Datos.scala operaciones/Operaciones.scala

object Operaciones {
  def main(args: Array[String]): Unit = {
    val productos = Datos.productos

    // --- Operación 1: groupBy (lambda A => K, la clave de agrupación) ---
    println("== groupBy ==")
    productos
      .groupBy(_.categoria)
      .toList.sortBy(_._1) // solo para imprimir en orden estable
      .foreach { case (cat, lista) => println(s"$cat -> ${lista.map(_.nombre)}") }

    // --- Operación 2: sortBy (lambda A => B, B ordenable) ---
    println("\n== sortBy ==")
    productos
      .sortBy(_.precio)
      .foreach(p => println(s"${p.nombre} ($$${p.precio})"))

    // --- Operación 3: find (predicado A => Boolean, devuelve Option) ---
    println("\n== find ==")
    println(productos.find(p => p.nombre == "Monitor"))
    println(productos.find(p => p.nombre == "Tablet")) // no existe -> None

    // --- Operación 4: zipWithIndex (no recibe lambda; se encadena con map) ---
    println("\n== zipWithIndex ==")
    productos
      .zipWithIndex
      .map { case (p, i) => s"${i + 1}. ${p.nombre}" }
      .foreach(println)

    // --- 4.3 Combinación ---
    // ¿Qué categorías tienen productos con precio > $100, cuántos son en cada una,
    // ordenadas de mayor a menor cantidad?
    println("\n== 4.3 Combinación ==")
    val resultado = productos
      .filter(p => p.precio > 100)                              // 1. vista en clase
      .groupBy(_.categoria)                                     // 2. NUEVA
      .map { case (cat, lista) => (cat, lista.size) }           // 3. vista en clase
      .toList
      .sortBy { case (cat, n) => (-n, cat) }                    // 4. NUEVA (desempate por nombre)
    println(resultado)
  }
}
