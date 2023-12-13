package windows;

import logging.ArrayListSave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyledDocument;

import Client.Start;
import form.ChatForm;
import image.Blob;
import image.PicResize;
import parse.EndsWithImg;
import socket.SendObject;
import swing.ShowMessage;
import windows.SignupWindow.ChooseProfilePic;
import socket.ReceiveObject;

import javax.swing.text.*;

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
	//private JTextArea chatArea = null;
	private JTextPane chatArea = null;
	private JTextField chatInput = null;
	private JButton sendButton = null;
	private JButton picSendButton = null;
	private JButton emojiSendButton = null;
	// 전송할 사진 선택을 위해 
	private JFileChooser picChooser = null;
	
	StyledDocument doc = null; 
	
	SimpleAttributeSet left = null;
    SimpleAttributeSet right = null;
    
    private GameBoardWindow gameBoardWindow = null;
    private SpectWindow spectWindow = null;
    private EmojiSelectWindow emojiSelectWindow = null;
    
    public void setGameBoardWindow(GameBoardWindow gbw) {this.gameBoardWindow = gbw;}
    public void setSpectWindow(SpectWindow spw) {this.spectWindow = spw;}
    public void setEmojiSelectWindow(EmojiSelectWindow emsw) {this.emojiSelectWindow = emsw;} 
	
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
		//chatArea = new JTextArea();
		chatArea = new JTextPane();
		chatScroll = new JScrollPane(chatArea);
		chatInput = new JTextField();
		sendButton = new JButton("전송");
		picSendButton = new JButton("🖼️");
		emojiSendButton = new JButton("😃");
		picChooser = new JFileChooser();
		
		doc = chatArea.getStyledDocument();
		left = new SimpleAttributeSet();
		right = new SimpleAttributeSet();
	    StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
	    StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
	    
	    chatArea.setPreferredSize(new Dimension(290, 340)); // JTextPane의 너비를 설정
	    
        doc.setParagraphAttributes(0, doc.getLength(), left, false);
        doc.setParagraphAttributes(0, doc.getLength(), right, false);
	    StyleConstants.setForeground(left, Color.BLACK);
	    StyleConstants.setForeground(right, Color.BLUE);
		
		// 컴포넌트 바운
		chatScroll.setBounds(5, 5, 290, 340);
		chatInput.setBounds(55, 348, 200, 20);
		sendButton.setBounds(248, 346, 53, 25);
		picSendButton.setBounds(5, 348, 20, 20);
		emojiSendButton.setBounds(30, 348, 20, 20);
		
		// 이벤트 처리 
		chatArea.setEditable(false);
		sendButton.addActionListener(new Send(this));
		chatInput.addKeyListener(new Send(this));
		picSendButton.addActionListener(new SendPic(this));
		emojiSendButton.addActionListener(new SendEmoji(this));

		picChooser.setFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
		picChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// 컴포넌트 add 
		add(chatScroll);
		add(chatInput);
		add(sendButton);
		add(picSendButton);
		add(emojiSendButton);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	public void getConnection()
	{
		Start.roomId = "@ServerMain";
		ChatForm toSend = new ChatForm(1, Start.roomId, Start.myId, Start.myNickname, "["+Start.myNickname+"] 님이 접속했습니다.");
		Start.connSocket = SendObject.send_noSocketClose(CHAT_PORT, toSend);
		ReplyReceiveThread chatReceiveThread = new ReplyReceiveThread(Start.connSocket, this);
		chatReceiveThread.start();
	}
	
	public void moveChatRoom(String roomId)
	{
		Start.roomId = roomId;
	}
	
	class Send implements ActionListener, KeyListener
	{
		ChatWindow cw = null;
		Send(ChatWindow cw) {this.cw = cw;}

		private void sendMsg()
		{
			if (!cw.chatInput.getText().equals(""))
			{
				ChatForm toSend = new ChatForm(1, Start.roomId, Start.myId, Start.myNickname, cw.chatInput.getText());
				SendObject.withSocket(Start.connSocket, toSend);
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
	
	class SendPic implements ActionListener
	{
		ChatWindow cw = null;
		SendPic(ChatWindow cw) {this.cw = cw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (cw.picChooser.showOpenDialog(null) == 0)
			{
				// 파일 불러오기 창 열림 
				// sw.picChooser.getSelectedFile(); // 대충 경로로 설정된 파일 열어오기 느낌일듯?
				// 파싱해서 확장자 jpg 아니면 컷하기 
				if (EndsWithImg.isJpg((cw.picChooser.getSelectedFile().getAbsolutePath())))
				{
					try {
					// 이건 받아서 리사이징하자. BufferedImage resized = PicResize.getChatImage(cw.picChooser.getSelectedFile().getAbsolutePath());
			    	BufferedImage bufim = ImageIO.read(new File(cw.picChooser.getSelectedFile().getAbsolutePath()));
					byte[] imageblob = Blob.toByteArray(bufim, "jpg");
					ChatForm toSend = new ChatForm(1, Start.roomId, Start.myId, Start.myNickname, cw.chatInput.getText());
					toSend.setPicBlob(imageblob);
					SendObject.withSocket(Start.connSocket, toSend);
					} catch (IOException a) {a.printStackTrace();}
				}
			}
			
		}
		
	}
	
	class SendEmoji implements ActionListener
	{
		ChatWindow cw = null;
		SendEmoji(ChatWindow cw) {this.cw = cw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			cw.emojiSelectWindow.setLocationRelativeTo(cw);
			cw.emojiSelectWindow.setVisible(true);
		}
		
	}
	
	class ReplyReceiveThread extends Thread
	{
		Socket socket = null;
		ChatWindow cw = null;
		ChatForm received = null;
		ReplyReceiveThread(Socket socket, ChatWindow cw) 
		{
			this.socket = socket;
			this.cw = cw;
		}
		
		public static String[] splitString(String input) 
		{
	        int chunkSize = 20;
	        int length = input.length();
	        int numRows = (int) Math.ceil((double) length / chunkSize);

	        String[] result = new String[numRows];

	        int index = 0;
	        for (int i = 0; i < numRows; i++) 
	        {
	            int endIndex = Math.min(index + chunkSize, length);
	            result[i] = input.substring(index, endIndex);
	            index = endIndex;
	        }

	        return result;
	    }
		
		// JTextpane에 사진 띄우는 메소드. 
		private void appendImage(ImageIcon imageIcon, int alignment) 
		{
		    StyledDocument doc = (StyledDocument) chatArea.getDocument();
		    Style style = doc.addStyle("StyleName", null);

		    try {
		        // 아이콘을 정렬로 설정
		        StyleConstants.setIcon(style, imageIcon);

		        // 문단의 속성을 설정하여 정렬
		        SimpleAttributeSet paragraphAttributes = new SimpleAttributeSet();
		        StyleConstants.setAlignment(paragraphAttributes, alignment);
		        doc.setParagraphAttributes(doc.getLength(), 1, paragraphAttributes, false);

		        // 텍스트와 함께 스타일 적용하여 추가
		        doc.insertString(doc.getLength(), "ignored text", style);
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
		 
		@Override
		public void run()
		{
			while(true)
			{
				try {received = (ChatForm) ReceiveObject.withSocket_throws(socket);}
				catch (IOException e) 
				{
					ShowMessage.warning("오류", "서버와의 접속이 끊어졌습니다. 프로그램을 종료합니다.");
					e.printStackTrace();
					System.exit(0); 
				}
				if (received.getRoomId().equals(Start.roomId))
				{
					if (received.getReqType() == 1)
						// 채팅 요청일 때 
					{
						if (received.getId().equals(Start.myId))
							// 자신이 보낸 채팅일 경우 
						{
							if (received.getPicBlob() == null)
							{
								// 자신이 보낸 메시지일 경우
								try {
								cw.doc.insertString(doc.getLength(), "[나]", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
					            
					            String[] split = splitString(received.getMsg());
					            for (int i=0; i<split.length; i++)
					            {
									cw.doc.insertString(doc.getLength(), " " + split[i], cw.right);
						            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
									cw.doc.insertString(doc.getLength(), "\n", cw.right);
						            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
					            }
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
								cw.chatArea.setCaretPosition(cw.chatArea.getDocument().getLength());} catch (BadLocationException e) {}
							
							}
							else if (received.getPicBlob() != null)
							{
								// 내가 보낸 사진 출력하는 루틴. 
								try {
								BufferedImage resizedImage = PicResize.getChatImage(Blob.toBufferedImage(received.getPicBlob()));
								cw.doc.insertString(doc.getLength(), "[나]", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
					            ImageIcon image = new ImageIcon(resizedImage);
					            appendImage(image, StyleConstants.ALIGN_RIGHT);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
								} catch (BadLocationException e) {}
								
							}
						}
						else 
						{
							if (received.getPicBlob() == null)
							{
								// 다른 사람이 보낸 메시지일 경우 
								try {
								cw.doc.insertString(doc.getLength(), "["+received.getId()+" # " +received.getNickName()+"]", cw.left);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
								cw.doc.insertString(doc.getLength(), "\n", cw.left);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
					            
					            String[] split = splitString(received.getMsg());
					            for (int i=0; i<split.length; i++)
					            {
					            	cw.doc.insertString(doc.getLength(), " " + split[i], cw.left);
					            	cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
					            	cw.doc.insertString(doc.getLength(), "\n", cw.left);
					            	cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
					            }
								cw.doc.insertString(doc.getLength(), "\n", cw.left);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
								cw.chatArea.setCaretPosition(cw.chatArea.getDocument().getLength());} catch (BadLocationException e) {}
							}
							else if (received.getPicBlob() != null)
							{
								// 다른 사람이 사진 보냈을 때의 루틴.
								try {
								BufferedImage resizedImage = PicResize.getChatImage(Blob.toBufferedImage(received.getPicBlob()));
								cw.doc.insertString(doc.getLength(), "["+received.getId()+" # " +received.getNickName()+"]", cw.left);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.left, false);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
					            ImageIcon image = new ImageIcon(resizedImage);
					            appendImage(image, StyleConstants.ALIGN_LEFT);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
								cw.doc.insertString(doc.getLength(), "\n", cw.right);
					            cw.doc.setParagraphAttributes(doc.getLength(), 1, cw.right, false);
								} catch (BadLocationException e) {}
								
							}
						}
					}
					else if (received.getReqType() == 2)
					{
						// 게임방 정보일 때 -> 게임화면 업데이트하기
						cw.gameBoardWindow.updateWindow(received);
						
						// 테스트 
						System.out.println("서버의 메시지 : " + received.getMsg());
						
					}
					else if (received.getReqType() == 3)
					{
						// 게스트가 들어왔음을 알리는 객체 수신했을 떄  
						// 수신하면 게스트의 유저정보 렌더링 
						cw.gameBoardWindow.gameLog.setText("");
						cw.gameBoardWindow.setOpInfo(received.getId());
					}
					else if (received.getReqType() == 4)
					{
						// 게임 끝났을때 받는 메시지.
						if (Start.myId.equals(received.getId()))
							ShowMessage.information("승리", "승리!");
						else if ("@Draw".equals(received.getId()))
							ShowMessage.information("무승부", "무승부!");
						else
							ShowMessage.information("패배", "패배!");

						Start.roomId = "@ServerMain";
						ChatForm getInRoom = new ChatForm(3, Start.roomId, Start.myId, Start.myNickname, "");
						SendObject.withSocket(Start.connSocket, getInRoom);
						cw.gameBoardWindow.setVisible(false);
						ArrayListSave.saveArrayListToFile(cw.gameBoardWindow.GAMELOG);
						cw.gameBoardWindow.clear();
						cw.gameBoardWindow.lobbyWindow.setVisible(true);
						cw.gameBoardWindow.chatWindow.setVisible(true);
					}
					else if (received.getReqType() == 5)
					{
						// 관전자가 받는 화면의 메시지 
						cw.spectWindow.updateWindow(received);
						System.out.println("서버의 메시지 : " + received.getMsg());
					}
					else if (received.getReqType() == 6)
					{
						// 관전자가 게임 끝났을때 받는 메시지 
						ShowMessage.information("게임 끝", received.getMsg());
						Start.roomId = "@ServerMain";
						ChatForm getInRoom = new ChatForm(3, Start.roomId, Start.myId, Start.myNickname, "");
						SendObject.withSocket(Start.connSocket, getInRoom);
						cw.spectWindow.setVisible(false);
						cw.spectWindow.clear();
						cw.spectWindow.lobbyWindow.setVisible(true);
						cw.spectWindow.chatWindow.setVisible(true);
					}
				}
			}
		}
	}
}

