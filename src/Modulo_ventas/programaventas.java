package Modulo_ventas;

import java.io.*;
import java.util.*;


class Vendedor {
    String tipoDocumento;
    long numeroDocumento;
    String nombres;
    String apellidos;
    double totalVentas;

    public Vendedor(String tipoDocumento, long numeroDocumento, String nombres, String apellidos) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.totalVentas = 0.0;
    }

    public double getTotalVentas() {
        return totalVentas;
    }
}


class Producto {
    String idProducto;
    String nombreProducto;
    double precioPorUnidad;
    int cantidadVendida;

    public Producto(String idProducto, String nombreProducto, double precioPorUnidad) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precioPorUnidad = precioPorUnidad;
        this.cantidadVendida = 0;
    }

    public int getCantidadVendida() {
        return cantidadVendida;
    }
}

public class programaventas {

    
    public List<Vendedor> leerArchivoVendedores(String rutaArchivo) throws IOException {
        List<Vendedor> vendedores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Vendedor vendedor = new Vendedor(datos[0], Long.parseLong(datos[1]), datos[2], datos[3]);
                vendedores.add(vendedor);
            }
        }
        return vendedores;
    }

    
    public List<Producto> leerArchivoProductos(String rutaArchivo) throws IOException {
        List<Producto> productos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                Producto producto = new Producto(datos[0], datos[1], Double.parseDouble(datos[2]));
                productos.add(producto);
            }
        }
        return productos;
    }

    
    public void procesarArchivoVentas(String rutaArchivo, Map<Long, Vendedor> vendedores, Map<String, Producto> productos) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = br.readLine(); // Primera línea con el documento del vendedor
            String[] vendedorInfo = linea.split(";");
            long numeroDocumento = Long.parseLong(vendedorInfo[1]);

            Vendedor vendedor = vendedores.get(numeroDocumento);
            if (vendedor != null) {
                double totalVentas = 0.0;

                while ((linea = br.readLine()) != null) {
                    String[] datosVenta = linea.split(";");
                    String idProducto = datosVenta[0];
                    int cantidadVendida = Integer.parseInt(datosVenta[1]);

                    Producto producto = productos.get(idProducto);
                    if (producto != null) {
                        totalVentas += producto.precioPorUnidad * cantidadVendida;
                        producto.cantidadVendida += cantidadVendida;
                    }
                }
                vendedor.totalVentas = totalVentas;
            }
        }
    }

    
    public void generarReporteVendedores(List<Vendedor> vendedores, String rutaSalida) throws IOException {
        vendedores.sort(Comparator.comparingDouble(Vendedor::getTotalVentas).reversed());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {
            for (Vendedor vendedor : vendedores) {
                writer.write(vendedor.nombres + " " + vendedor.apellidos + ";" + vendedor.totalVentas);
                writer.newLine();
            }
        }
    }

    
    public void generarReporteProductos(List<Producto> productos, String rutaSalida) throws IOException {
        productos.sort(Comparator.comparingInt(Producto::getCantidadVendida).reversed());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {
            for (Producto producto : productos) {
                writer.write(producto.nombreProducto + ";" + producto.precioPorUnidad + ";" + producto.cantidadVendida);
                writer.newLine();
            }
        }
    }

    
    public void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        String fileName = "ventas_" + name + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("CC;" + id);
            writer.newLine();
            Random random = new Random();
            for (int i = 0; i < randomSalesCount; i++) {
                writer.write("P" + (i + 1) + ";" + random.nextInt(10) + 1);
                writer.newLine();
            }
        }
    }

    
    public void createProductsFile(int productsCount) throws IOException {
        String fileName = "productos.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Random random = new Random();
            for (int i = 0; i < productsCount; i++) {
                writer.write("P" + (i + 1) + ";Producto_" + (i + 1) + ";" + (random.nextDouble() * 100));
                writer.newLine();
            }
        }
    }

    
    public void createSalesManInfoFile(int salesmanCount) throws IOException {
        String fileName = "vendedores.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < salesmanCount; i++) {
                writer.write("CC;" + (1000 + i) + ";Vendedor_" + (i + 1) + ";Apellido_" + (i + 1));
                writer.newLine();
            }
        }
    }

    // Método principal
    public static void main(String[] args) throws IOException {
        programaventas programa = new programaventas();

        
        programa.createSalesManInfoFile(3);
        programa.createProductsFile(5);
        programa.createSalesMenFile(3, "Juan", 1001);

        
        List<Vendedor> vendedores = programa.leerArchivoVendedores("vendedores.txt");
        List<Producto> productos = programa.leerArchivoProductos("productos.txt");

        
        Map<Long, Vendedor> mapaVendedores = new HashMap<>();
        for (Vendedor v : vendedores) {
            mapaVendedores.put(v.numeroDocumento, v);
        }

        Map<String, Producto> mapaProductos = new HashMap<>();
        for (Producto p : productos) {
            mapaProductos.put(p.idProducto, p);
        }

        
        programa.procesarArchivoVentas("ventas_Juan.txt", mapaVendedores, mapaProductos);

        
        programa.generarReporteVendedores(vendedores, "reporte_vendedores.csv");
        programa.generarReporteProductos(productos, "reporte_productos.csv");

        System.out.println("Reportes generados exitosamente.");
    }
}
