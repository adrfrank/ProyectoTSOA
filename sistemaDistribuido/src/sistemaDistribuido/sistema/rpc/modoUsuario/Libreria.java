/*
 * Laura Teresa García López    212354614
 * Sección: D05
 * Practica Java #3
 */

package sistemaDistribuido.sistema.rpc.modoUsuario;

import java.util.Stack;
import sistemaDistribuido.util.Escribano;

public abstract class Libreria{
	private Escribano esc;
        public Stack pila = new Stack();
        public int x;

	/**
	 * 
	 */
	public Libreria(Escribano esc){
		this.esc=esc;
	}

	/**
	 * 
	 */
	protected void imprime(String s){
		esc.imprime(s);
	}

	/**
	 * 
	 */
	protected void imprimeln(String s){
		esc.imprimeln(s);
	}

	/**
	 * Ejemplo para el paso intermedio de parametros en pila.
	 * Esto es lo que esta disponible como interfaz al usuario programador
	 */
	/*public int suma(int sum1,int sum2){
    //...
    suma();
    //...
    return 0;
  }*/

	public int suma(int sum1,int sum2){
		return sum1+sum2;
	}

	public int resta(int minuendo,int sustraendo){
		return minuendo-sustraendo;
	}

	public int multiplicacion(int multiplicando,int multiplicador){
		return multiplicando*multiplicador;
	}

	public int division(int dividendo,int divisor){
		return dividendo/divisor;
	}

	/**
	 * Servidor suma verdadera generable por un compilador estandar
	 * o resguardo de la misma por un compilador de resguardos.
	 */
	protected abstract void suma();
        protected abstract void cuadrado();
        protected abstract void raiz();
        protected abstract void ordena();
        protected abstract void promedio();
        
    int cuadrado(int cuadrado) {
        pila.push(cuadrado);
        cuadrado();
        int resultado =(Integer) pila.pop();
        return resultado;
    }

    int raiz(int raizcuadrada) {
        pila.push(raizcuadrada);
        raiz();
        int resultado =(Integer) pila.pop();
        return resultado;    
    
    }

    int [] ordena(int[] ordenar) {
        pila.push(ordenar);
        ordena();
        int [] resultado =(int []) pila.pop();
        return resultado;     
    }

    int promedio(int[] promedio) {
        pila.push(promedio);
        promedio();
        int resultado= (Integer)pila.pop();
        return resultado;    
    }
}