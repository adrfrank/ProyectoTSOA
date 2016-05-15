package sistemaDistribuido.util;

public class Parametros {

	public static byte[] empaquetar(short parametro){
		byte[] arr= new byte[2];
		arr[0] =(byte) parametro;
		parametro >>= 8;
		arr[1] = (byte) (parametro);
		return arr;
	}
	public static byte[] empaquetar(int parametro){
		byte[] arr= new byte[4];
		arr[0] =(byte) parametro;
		parametro >>= 8;
		arr[1] = (byte) (parametro);
		parametro >>= 8;
		arr[2] = (byte) (parametro);
		parametro >>= 8;
		arr[3] = (byte) (parametro);
		return arr;
	}
	
	public static short desempaquetarShort(byte[] parametro){
		short n=0x0000;
		n |= parametro[1];
		n <<= 8;		
		n |= (parametro[0] & 0x00ff);
		return n;
	}
	
	public static int desempaquetarInt(byte[] parametro){
		int n=0x00000000;
		n |= parametro[3];
		n <<=8;
		n |= (parametro[2] & 0x000000ff);
		n <<=8;
		n |= (parametro[1] & 0x000000ff);
		n <<=8;
		n |= (parametro[0] & 0x000000ff);
		return n;
	}
	

}
