package socket;

import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendObject 
{
	// 서버 IP 하드코딩 
	private final static String serverIP = "127.0.0.1";
	
	private SendObject() {}
	
	public static Object send(int port, Object toSend)
	{
		Object result = null;
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try
		{
			socket = new Socket(serverIP, port);
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(toSend);
			oos.flush();
		}
		catch (IOException e) {System.out.println("서버 접속 불가");}
		finally
		{
			try {socket.close();}
			catch (IOException e) {e.printStackTrace();}
		}
		return result;
	}
	
	public static Socket send_noSocketClose(int port, Object toSend)
	{
		Socket socket = null;
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try
		{
			socket = new Socket(serverIP, port);
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(toSend);
			oos.flush();
		}
		catch (IOException e) {System.out.println("서버 접속 불가");}
		return socket;
	}
	

	public static void withSocket(Socket socket, Object toSend)
	{
		OutputStream os = null;
		ObjectOutputStream oos = null;
		try
		{
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
			oos.writeObject(toSend);
			oos.flush();
		}
		catch (IOException e) {e.printStackTrace();}
	}

}
