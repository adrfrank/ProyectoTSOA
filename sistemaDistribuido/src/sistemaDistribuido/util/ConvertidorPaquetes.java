package sistemaDistribuido.util;

import java.util.Stack;

public class ConvertidorPaquetes {
	
	/*	Estructura de mensaje
	 *  Id del emisor: 4 bytes
	 *  Id del receptor: 4 bytes
	 *  Cod de operación: 2 bytes
	 *  Datos: 1014 bytes
	 * 
	 * */
	
	byte[] bytes;
	static final int EMISOR_INDEX=0;
	static final int RECEPTOR_INDEX=4;
	static final int OPCODE_INDEX=8;
	static final int SHORT_SIZE=2;
	static final int DATA_INDEX=10;
	static final int DATA_LENGTH=1014;
	static final int INT_SIZE=4;
	public static final int SOL_LENGTH=1024;
	public ConvertidorPaquetes(byte[] bytes){
		this.bytes = bytes;
	}
	public int getEmisor(){
		byte[] btmp = new byte[INT_SIZE];
		System.arraycopy(bytes, EMISOR_INDEX, btmp, 0, INT_SIZE);
		int tmp = Parametros.desempaquetarInt(btmp);
		return tmp;
	}
	public void setEmisor(int id){
		byte[] btmp = Parametros.empaquetar(id);
		System.arraycopy(btmp, 0, bytes, EMISOR_INDEX, INT_SIZE);
	}
	public int getReceptor(){
		byte[] btmp = new byte[INT_SIZE];
		System.arraycopy(bytes, RECEPTOR_INDEX, btmp, 0, INT_SIZE);
		int tmp = Parametros.desempaquetarInt(btmp);
		return tmp;
		
	}
	public void setReceptor(int id){
		byte[] btmp = Parametros.empaquetar(id);
		System.arraycopy(btmp, 0, bytes, RECEPTOR_INDEX, INT_SIZE);
	}
	
	public void setOptCode(short code){
		byte[] btmp = Parametros.empaquetar(code);
		System.arraycopy(btmp, 0, bytes, OPCODE_INDEX, SHORT_SIZE);
	}
	
	public short getOptCode(){
		byte[] btmp = new byte[SHORT_SIZE];
		System.arraycopy(bytes, OPCODE_INDEX, btmp, 0, SHORT_SIZE);
		short tmp = Parametros.desempaquetarShort(btmp);
		return tmp;
	}
	
	public byte[] getData(){
		byte[] btmp = new byte[Math.min(DATA_LENGTH, bytes.length-10)];
		System.arraycopy(bytes, DATA_INDEX, btmp, 0, Math.min(DATA_LENGTH, bytes.length-10) );
		return btmp;
	}
	public void setData(byte[] data){
		System.arraycopy(data, 0, bytes, DATA_INDEX, data.length);
	}
	
	
	public void setData(String data){
		byte[] msgData = new byte[data.length()+1];
		msgData[0] = (byte) data.length();
		byte[] cadbytes = data.getBytes();
		System.arraycopy(cadbytes, 0, msgData, 1, cadbytes.length);
		setData(msgData);
	}
	public String getStringData(){
		byte[] btmp = getData();
		String cad = new String(btmp,1,btmp[0]);
		return cad;
	}
	
	public void setOperationParameters(short nParams, Stack<Integer> stack){
		byte[] param = Parametros.empaquetar(nParams);
		int index = DATA_INDEX;
		System.arraycopy(param, 0, bytes, index , param.length);//Number of params
		index += param.length;
		while(nParams-- > 0){
			param = Parametros.empaquetar(stack.pop().intValue());
			System.arraycopy(param, 0, bytes, index, param.length);
			index += INT_SIZE;
		}
	}
	public void extractOperationParameters(Stack<Integer> stack){
		byte[] op= new byte[SHORT_SIZE];
		int index = DATA_INDEX;
		System.arraycopy(bytes, index, op, 0, SHORT_SIZE);
		index += SHORT_SIZE;
		short nParams = Parametros.desempaquetarShort(op);
		op= new byte[INT_SIZE];
		int param;
		while(nParams-- > 0){
			System.arraycopy(bytes, index, op, 0, INT_SIZE);
			index += INT_SIZE;
			param=Parametros.desempaquetarInt(op);
			stack.push(param);
		}
	}
	
	public int[] getOperationParameters(){
		
		
		byte[] op= new byte[SHORT_SIZE];
		int index = DATA_INDEX;
		System.arraycopy(bytes, index, op, 0, SHORT_SIZE);
		index += SHORT_SIZE;
		short nParams = Parametros.desempaquetarShort(op);
		int[] parameters = new int[nParams];
		int i=0;
		op= new byte[INT_SIZE];
		int param;
		while(nParams-- > 0){
			System.arraycopy(bytes, index, op, 0, INT_SIZE);
			index += INT_SIZE;
			param=Parametros.desempaquetarInt(op);
			parameters[i++] = param;
		}
		return parameters;
	}
}
