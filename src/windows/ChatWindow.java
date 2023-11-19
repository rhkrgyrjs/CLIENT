package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Client.Start;
import form.ChatForm;
import socket.SendObject;
import swing.ShowMessage;
import socket.ReceiveObject;

public class ChatWindow extends JFrame 
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 포트 하드코딩 
	private static final int CHAT_PORT = 8011;
	
	// 싱글톤 처리 
	private static ChatWindow single_instance = null;
	public static ChatWindow getInstance()
	{
		if (single_instance == null) single_instance = new ChatWindow();
		return single_instance;
	}
	
	private JScrollPane chatScroll = null;
	private JTextArea chatArea = null;
	private JTextField chatInput = null;
	private JButton sendButton = null;
	
	private String chatRoomId = null;
	private Socket socket = null;
	
	private ChatWindow()
	{
		// 기본적인 창 설정 
		setTitle("채팅");
		setResizable(false);
		setSize(300, 400);
		setLayout(null);
		//setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// 컴포넌트 객체 생성
		chatArea = new JTextArea();
		chatScroll = new JScrollPane(chatArea);
		chatInput = new JTextField();
		sendButton = new JButton("전송");
		
		// 컴포넌트 바운
		chatScroll.setBounds(5, 5, 290, 340);
		chatInput.setBounds(5, 348, 250, 20);
		sendButton.setBounds(248, 346, 53, 25);
		
		// 이벤트 처리 
		chatArea.setEditable(false);
		sendButton.addActionListener(new Send(this));
		chatInput.addKeyListener(new Send(this));
		
		// 컴포넌트 add 
		add(chatScroll);
		add(chatInput);
		add(sendButton);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	public void getConnection()
	{
		this.chatRoomId = "@ServerMain";
		ChatForm toSend = new ChatForm(this.chatRoomId, Start.myId, Start.myNickname, "["+Start.myNickname+"] 님이 입장했습니다.");
		this.socket = SendObject.send_noSocketClose(CHAT_PORT, toSend);
		ChatReceiveThread chatReceiveThread = new ChatReceiveThread(socket, this);
		chatReceiveThread.start();
	}
	
	public void moveChatRoom(String roomId)
	{
		this.chatRoomId = roomId;
	}
	
	class Send implements ActionListener, KeyListener
	{
		ChatWindow cw = null;
		Send(ChatWindow cw) {this.cw = cw;}

		private void sendMsg()
		{
			if (!cw.chatInput.getText().equals(""))
			{
				ChatForm toSend = new ChatForm(cw.chatRoomId, Start.myId, Start.myNickname, cw.chatInput.getText());
				SendObject.withSocket(cw.socket, toSend);
			}
			cw.chatInput.setText("");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			sendMsg();
		}

		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			sendMsg();
		}

		@Override
		public void keyReleased(KeyEvent e) {}
	}
	
	class ChatReceiveThread extends Thread
	{
		Socket socket = null;
		ChatWindow cw = null;
		ChatForm received = null;
		ChatReceiveThread(Socket socket, ChatWindow cw) 
		{
			this.socket = socket;
			this.cw = cw;
		}
		
		@Override
		public void run()
		{
			while(true)
			{
				try {received = (ChatForm) ReceiveObject.withSocket_throws(socket);}
				catch (IOException e) 
				{
					ShowMessage.warning("오류", "서버와의 접속이 끊어졌습니다.");
					// + 로그아웃 루틴 
				}
				if (received.getRoomId().equals(cw.chatRoomId))
				{
					cw.chatArea.append("["+received.getId()+"@"+received.getNickName()+"] :");
					cw.chatArea.append("\n");
					cw.chatArea.append(received.getMsg());
					cw.chatArea.append("\n");
					cw.chatArea.append("\n");
					cw.chatArea.setCaretPosition(cw.chatArea.getDocument().getLength());
				}
			}
		}
	}
}

