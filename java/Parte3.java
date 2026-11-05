// Parte 3 - Tipos de problemas resueltos con lambdas y Streams (Java).
// Ejecutar (Java 17+, desde la carpeta java/):  java Parte3.java

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class Parte3 {

    // Clase de datos (equivalente a la case class de Scala)
    static class Producto {
        private final String nombre, categoria;
        private final double precio;
        private final int stock;

        Producto(String nombre, String categoria, double precio, int stock) {
            this.nombre = nombre; this.categoria = categoria;
            this.precio = precio; this.stock = stock;
        }
        public String getNombre()    { return nombre; }
        public String getCategoria() { return categoria; }
        public double getPrecio()    { return precio; }
        public int getStock()        { return stock; }

        @Override public String toString() {
            return "Producto(" + nombre + "," + categoria + "," + precio + "," + stock + ")";
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.US); // decimales con punto

        List<Producto> productos = List.of(
            new Producto("Laptop", "Tech", 1200.0, 10),
            new Producto("Mouse", "Tech", 25.0, 150),
            new Producto("Teclado", "Tech", 45.0, 80),
            new Producto("Monitor", "Tech", 300.0, 20),
            new Producto("Silla", "Muebles", 150.0, 30),
            new Producto("Escritorio", "Muebles", 250.0, 15),
            new Producto("Lámpara", "Muebles", 40.0, 60),
            new Producto("Libro", "Papelería", 15.0, 200),
            new Producto("Cuaderno", "Papelería", 5.0, 500),
            new Producto("Bolígrafo", "Papelería", 2.0, 1000)
        );

        // 1. SELECCIÓN (filter)
        System.out.println("== 1. Selección (filter) ==");
        productos.stream()
                 .filter(p -> p.getCategoria().equals("Tech") && p.getPrecio() > 50)
                 .forEach(System.out::println);

        // 2. TRANSFORMACIÓN (map), IVA 12 %
        System.out.println("\n== 2. Transformación (map) ==");
        productos.stream()
                 .map(p -> String.format("%s: $%.2f", p.getNombre(), p.getPrecio() * 1.12))
                 .forEach(System.out::println);

        // 3. CONTEO (count): stock crítico (< 20)
        System.out.println("\n== 3. Conteo (count) ==");
        System.out.println(productos.stream().filter(p -> p.getStock() < 20).count());

        // 4. EXTREMOS (max / min con Comparator)
        System.out.println("\n== 4. Extremos (max / min) ==");
        System.out.println("Max: " + productos.stream()
                .max(Comparator.comparingDouble(Producto::getPrecio)).orElseThrow());
        System.out.println("Min: " + productos.stream()
                .min(Comparator.comparingDouble(Producto::getPrecio)).orElseThrow());
    }
}
