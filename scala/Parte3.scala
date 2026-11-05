// Parte 3 - Tipos de problemas resueltos con funciones anónimas (Scala).
// Ejecutar (desde la raíz del repo):  scala-cli run scala/Datos.scala scala/Parte3.scala

import java.util.Locale

object Parte3 {
  def main(args: Array[String]): Unit = {
    Locale.setDefault(Locale.US) // para que los decimales usen punto
    val productos = Datos.productos

    // 1. SELECCIÓN (filter): productos Tech que cuestan más de $50
    println("== 1. Selección (filter) ==")
    productos
      .filter(p => p.categoria == "Tech" && p.precio > 50)
      .foreach(println)

    // 2. TRANSFORMACIÓN (map): "Nombre: $PrecioConIVA" con IVA 12 %
    println("\n== 2. Transformación (map) ==")
    productos
      .map(p => "%s: $%.2f".format(p.nombre, p.precio * 1.12))
      .foreach(println)

    // 3. CONTEO (count): productos con stock crítico (< 20 unidades)
    println("\n== 3. Conteo (count) ==")
    println(productos.count(p => p.stock < 20))

    // 4. EXTREMOS (maxBy / minBy): el más caro y el más barato
    println("\n== 4. Extremos (maxBy / minBy) ==")
    println("Max: " + productos.maxBy(_.precio))
    println("Min: " + productos.minBy(_.precio))
  }
}
