package login;

import form.LoginRequestForm;
import form.LoginReplyForm;
import socket.Request;

public class LoginRequest 
{
	private static final int LOGIN_PORT = 8010;
	private LoginRequest() {}
	
	public static boolean toServer(LoginRequestForm toSend)
	{
		LoginReplyForm result = (LoginReplyForm) Request.toServer(LOGIN_PORT, toSend);
		System.out.println("서버의 로그인 답장 : " + result.getMsg());
		return result.getResult();
	}
	
	public static LoginReplyForm toServer_getObj(LoginRequestForm toSend)
	{
		LoginReplyForm result = (LoginReplyForm) Request.toServer(LOGIN_PORT, toSend);
		System.out.println("서버의 로그인 답장 : " + result.getMsg());
		return result;
	}
}
