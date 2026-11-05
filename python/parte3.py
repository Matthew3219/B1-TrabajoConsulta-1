"""Parte 3 - Tipos de problemas resueltos con lambda (Python).
Ejecutar (desde la raíz del repo):  python python/parte3.py
"""
from dataclasses import dataclass


@dataclass
class Producto:
    nombre: str
    categoria: str
    precio: float
    stock: int

    def __repr__(self):  # mismo formato que la case class de Scala
        return f"Producto({self.nombre},{self.categoria},{self.precio},{self.stock})"


productos = [
    Producto("Laptop", "Tech", 1200.0, 10),
    Producto("Mouse", "Tech", 25.0, 150),
    Producto("Teclado", "Tech", 45.0, 80),
    Producto("Monitor", "Tech", 300.0, 20),
    Producto("Silla", "Muebles", 150.0, 30),
    Producto("Escritorio", "Muebles", 250.0, 15),
    Producto("Lámpara", "Muebles", 40.0, 60),
    Producto("Libro", "Papelería", 15.0, 200),
    Producto("Cuaderno", "Papelería", 5.0, 500),
    Producto("Bolígrafo", "Papelería", 2.0, 1000),
]

# 1. SELECCIÓN (filter)
print("== 1. Selección (filter) ==")
for p in filter(lambda p: p.categoria == "Tech" and p.precio > 50, productos):
    print(p)

# 2. TRANSFORMACIÓN (map), IVA 12 %
print("\n== 2. Transformación (map) ==")
for linea in map(lambda p: f"{p.nombre}: ${p.precio * 1.12:.2f}", productos):
    print(linea)

# 3. CONTEO (filter + len): stock crítico (< 20)
print("\n== 3. Conteo (count) ==")
print(len(list(filter(lambda p: p.stock < 20, productos))))

# 4. EXTREMOS (max / min con key=)
print("\n== 4. Extremos (max / min) ==")
print("Max:", max(productos, key=lambda p: p.precio))
print("Min:", min(productos, key=lambda p: p.precio))
