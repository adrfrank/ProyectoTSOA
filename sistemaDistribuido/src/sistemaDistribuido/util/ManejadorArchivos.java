package sistemaDistribuido.util;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class ManejadorArchivos {
	public boolean crear(String nombre){
		File archivo = new File(nombre);
		BufferedWriter bw;
		try {
			if(!archivo.exists()){
				bw = new BufferedWriter(new FileWriter(archivo));
	            bw.write("");
	            bw.close();
				return true;
			}else
				return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
		
	}
	public boolean eliminar(String nombre){
		File archivo = new File(nombre);
		if(archivo.delete()) return true;
		return false;
	}
	public String leer(String nombre){
		File archivo = new File(nombre);
		BufferedReader br ;
		if(archivo.exists()){
			try {
				br = new BufferedReader(new FileReader(archivo));
				String cad = br.readLine();
				br.close();
				return cad;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		return null;
	}
	public boolean escribir(String nombre, String contenido){
		File archivo = new File(nombre);
		BufferedWriter bw;
		try {
			if(archivo.exists()){
				bw = new BufferedWriter(new FileWriter(archivo));
	            bw.write(contenido);
	            bw.close();
				return true;
			}else
				return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return false;
		}
	}
}
