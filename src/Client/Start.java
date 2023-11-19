package Client;

import windows.*;

public class Start 
{
	// 내정보 저장
	public static String myId = null;
	public static String myNickname = null;
		
	public static void main(String[] args)
	{
		
		// 창들 인스턴스 받아오기 
		LoginWindow loginWindow = LoginWindow.getInstance();
		SignupWindow signupWindow = SignupWindow.getInstance();
		ZipcodeWindow zipcodeWindow = ZipcodeWindow.getInstance();
		PwFindWindow pwFindWindow = PwFindWindow.getInstance();
		PwChangeWindow pwChangeWindow = PwChangeWindow.getInstance();
		LobbyWindow lobbyWindow = LobbyWindow.getInstance();
		ChatWindow chatWindow = ChatWindow.getInstance();
		// GameWindow gameWindow = GameWindow.getInstance();
		
		// 창 전환 의존성 설정 
		loginWindow.setLobbyWindow(lobbyWindow);
		loginWindow.setChatWindow(chatWindow);
		loginWindow.setPwFindWindow(pwFindWindow);
		loginWindow.setSignupWindow(signupWindow);
		
		signupWindow.setLoginWindow(loginWindow);
		signupWindow.setZipcodeWindow(zipcodeWindow);
		
		zipcodeWindow.setSignupWindow(signupWindow);
		
		pwFindWindow.setLoginWindow(loginWindow);
		pwFindWindow.setPwChangeWindow(pwChangeWindow);
		
		pwChangeWindow.setLoginWindow(loginWindow);
		
		lobbyWindow.setLoginWindow(loginWindow);
		// lobbyWindow.setGameWindow(gameWindow);
		
		// gameWindow.setLobbyWindow(lobbyWindow);
		
		
	}

}
