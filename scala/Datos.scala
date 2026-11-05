// Datos compartidos por todos los ejemplos de Scala (Parte 3 y Parte 4).

case class Producto(nombre: String, categoria: String, precio: Double, stock: Int)

object Datos {
  val productos: List[Producto] = List(
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
}
