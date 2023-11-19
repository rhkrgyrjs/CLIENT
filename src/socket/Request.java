package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Request 
{
	// 서버 IP 하드코딩 
	private final static String serverIP = "127.0.0.1";
	
	private Request() {}
	
	public static Object toServer(int port, Object toSend)
	{
		Object result = null;
		Socket socket = null;
		InputStream is = null;
		OutputStream os = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try
		{
			socket = new Socket(serverIP, port);
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(toSend);
			oos.flush();
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
			result = ois.readObject();
		}
		catch (IOException e) {System.out.println("서버 접속 불가");}
		catch (ClassNotFoundException e) {e.printStackTrace();}
		finally
		{
			try {socket.close();}
			catch (IOException e) {e.printStackTrace();}
		}
		return result;
	}

}
