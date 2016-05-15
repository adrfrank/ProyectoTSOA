/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #3
 */

package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Arrays;
import sistemaDistribuido.sistema.rpc.modoUsuario.Libreria;
import sistemaDistribuido.util.Escribano;

public class LibreriaServidor extends Libreria{

	/**
	 * 
	 */
	public LibreriaServidor(Escribano esc){
		super(esc);
	}

	/**
	 * Ejemplo de servidor suma verdadera
	 */
	protected void suma(){
		//saca parametros de pila

		//devuelve valor izquierdo
	}

    @Override
    protected void cuadrado() {
        int param = (Integer)pila.pop();
        int resultado = param*param;
        pila.push(resultado);
    }

    @Override
    protected void raiz() {
        int param = (Integer)pila.pop();
        int resultado = (int) Math.sqrt(param);
        pila.push(resultado);    
    }

    @Override
    protected void ordena() {
        int [] resultado = (int [])pila.pop();
        Arrays.sort(resultado); 
        pila.push(resultado);      
    }

    @Override
    protected void promedio() {       
        int[] parametros = (int [])pila.pop();
        int resultado=0,cantidad = 0;
        for (int i=0; i < parametros.length;i++){
            resultado+= parametros[i];
            cantidad++;
        }
        resultado = resultado/cantidad;
        pila.push(resultado);
    }

}