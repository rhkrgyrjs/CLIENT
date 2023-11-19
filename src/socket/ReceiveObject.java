package socket;

import java.io.*;
import java.net.Socket;

public class ReceiveObject 
{
	private ReceiveObject() {}
	
	public static Object withSocket(Socket socket)
	{
		InputStream is = null;
		ObjectInputStream ois = null;
		Object result = null;
		try
		{
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			result = ois.readObject();
		}
		catch (Exception e) { try{socket.close();} catch(IOException a) {a.printStackTrace();}}
		//catch (IOException e) {e.printStackTrace();}
		//catch (ClassNotFoundException e) {e.printStackTrace();}
		return result;
	}
}
