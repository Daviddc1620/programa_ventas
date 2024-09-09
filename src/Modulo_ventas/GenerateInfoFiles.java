package Modulo_ventas;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

public class GenerateInfoFiles {
	
	public static void main(String[] args) {
		Scanner sn = new Scanner(System.in);
		System.out.print("Ingrese Tipo de Documento cc/nit/ce: ");
		String tipodocumento = sn.nextLine();
		System.out.print("Ingrese el Numero de Documento: ");
		String numerodocumento = sn.nextLine();
		System.out.print("Ingrese el nombre del vendedor: ");
		String nombrevendedor = sn.nextLine();
		System.out.print("Ingrese Apellidos del vendedor: ");
		String apellidovendedor = sn.nextLine();
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("Informacion_Vendedores.txt", true))) {
			writer.write(tipodocumento + "," + numerodocumento + "," + nombrevendedor + "," + apellidovendedor);
			writer.newLine();
			System.out.println("Datos guardados correctamente en Informacion_Vendedores.txt");
		} catch (IOException e) {
			System.err.println("Error al escribir en el archivo: " + e.getMessage());
		}
		
		try (BufferedReader readerproductos = new BufferedReader(new FileReader("Informacion_Productos.txt"))) {
			String lineaProductos;
			System.out.println("\nInformación de Productos:\nID_Producto;Nombre;Precio:");
			while ((lineaProductos = readerproductos.readLine()) != null) {
                System.out.println(lineaProductos);
			}
		} catch (IOException e) {
			System.err.println("Error al leer el archivo: " + e.getMessage());
		} finally {
			sn.close();
		}
	}
}

	
            