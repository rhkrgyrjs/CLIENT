package Client;

import java.net.Socket;

import windows.*;

public class Start 
{
	// 내정보 저장
	public static String myId = null;
	public static String myNickname = null;
	
	// 현재 자신이 소속된 방 아이디 저장. 방의 ID -> 방장의 id임.
	public static String roomId = "@ServerMain";
	
	public static Socket connSocket = null;
		
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
		GameRoomWindow gameRoomWindow = GameRoomWindow.getInstance();
		GameBoardWindow gameBoardWindow = GameBoardWindow.getInstance();
		UserInfoWindow userInfoWindow = UserInfoWindow.getInstance();
		
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
		lobbyWindow.setGameRoomWindow(gameRoomWindow);
		lobbyWindow.setGameBoardWindow(gameBoardWindow);
		lobbyWindow.setChatWindow(chatWindow);
		lobbyWindow.setUserInfoWindow(userInfoWindow);
		
		chatWindow.setGameBoardWindow(gameBoardWindow);
		
		gameRoomWindow.setGameBoardWindow(gameBoardWindow);
		gameRoomWindow.setLobbyWindow(lobbyWindow);
		gameRoomWindow.setChatWindow(chatWindow);
		
		gameBoardWindow.setChatWindow(chatWindow);
		gameBoardWindow.setLobbyWindow(lobbyWindow);
		
		
	}

}
