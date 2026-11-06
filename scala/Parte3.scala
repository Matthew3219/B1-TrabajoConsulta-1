import java.util.Locale

case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)

object Parte3 {
  def main(args: Array[String]): Unit = {
    Locale.setDefault(Locale.US)

    val productos = List(
      Producto("Laptop", "Tech", 1200.0, 10),
      Producto("Mouse", "Tech", 25.0, 150),
      Producto("Teclado", "Tech", 45.0, 80),
      Producto("Monitor", "Tech", 300.0, 20),
      Producto("Silla", "Muebles", 150.0, 30),
      Producto("Escritorio", "Muebles", 250.0, 15),
      Producto("Lámpara", "Muebles", 40.0, 60),
      Producto("Libro", "Papelería", 15.0, 200),
      Producto("Cuaderno", "Papelería", 5.0, 500),
      Producto("Bolígrafo", "Papelería", 2.0, 1000)
    )

    println("== 1. Selección (filter) ==")
    productos.filter(p => p.categoria == "Tech" && p.precio > 50).foreach(println)

    println("\n== 2. Transformación (map) ==")
    productos.map(p => "%s: $%.2f".format(p.nombre, p.precio * 1.12)).foreach(println)

    println("\n== 3. Conteo (count) ==")
    println(productos.count(p => p.stock < 20))

    println("\n== 4. Extremos (maxBy / minBy) ==")
    println("Max: " + productos.maxBy(_.precio))
    println("Min: " + productos.minBy(_.precio))
  }
}