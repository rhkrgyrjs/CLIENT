package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Client.Start;
import form.LoginRequestForm;
import login.LoginRequest;
import swing.ShowMessage;

public class GameRoomWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static GameRoomWindow single_instance = null;
	public static GameRoomWindow getInstance()
	{
		if (single_instance == null) single_instance = new GameRoomWindow();
		return single_instance;
	}
	
	JLabel gameRoomLabel = null;
	JTextField gameRoomInput = null;
	JButton createRoomButton = null;
	JButton cancelButton = null;
	
	public void clear()
	{
		this.gameRoomInput.setText("");
	}
	
	private GameRoomWindow()
	{
		// 기본적인 창 설정 
		setTitle("방 만들기");
		setResizable(false);
		setSize(350, 100);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// 컴포넌트 생성
		gameRoomLabel = new JLabel("방 이름 ");
		gameRoomInput = new JTextField();
		createRoomButton = new JButton("생성");
		cancelButton = new JButton("취소");
		
		// 바운딩 
		gameRoomLabel.setBounds(10, 10, 50, 30);
		gameRoomInput.setBounds(50, 16, 300, 20);
		createRoomButton.setBounds(90, 40, 80, 30);
		cancelButton.setBounds(190, 40, 80, 30);
		
		// 이벤트 처리 
		createRoomButton.addActionListener(new CreateRoom(this));
		cancelButton.addActionListener(new CloseWindow(this));
		
		
		add(gameRoomLabel);
		add(gameRoomInput);
		add(createRoomButton);
		add(cancelButton);
		
		setVisible(false);
	}
	
	class CreateRoom implements ActionListener
	{
		GameRoomWindow gw = null;
		CreateRoom(GameRoomWindow gw) {this.gw = gw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (gw.gameRoomInput.getText().equals("") || gw.gameRoomInput.getText() == null)
			{
				gw.gameRoomInput.setText("");
				ShowMessage.warning("오류", "게임 방의 이름을 입력해주세요.");
			}
			else
			{
				// 게임방 생성 루틴 
				LoginRequestForm toSend = new LoginRequestForm();
				toSend.setReqType(5);
				toSend.setId(Start.myId);
				toSend.setRoomName(gw.gameRoomInput.getText());
				if (LoginRequest.toServer(toSend))
				{
					// 여기에 게임으로 바로 넘어가는 루틴 넣기
					gw.setVisible(false);
					gw.clear();
					ShowMessage.information("방 생성됨", "임시 메시지 : 방 생성 성공. 게임으로 넘어가는 루틴 짜자.");
				}
				else
				{
					gw.clear();
					ShowMessage.warning("오류", "방 생성에 실패했습니다.");
				}
			}
		}
		
	}
	
	class CloseWindow implements ActionListener
	{
		GameRoomWindow gw = null;
		CloseWindow(GameRoomWindow gw) {this.gw = gw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			gw.setVisible(false);
			gw.clear();
		}
		
	}
	
	public static void main(String[] args)
	{
		GameRoomWindow.getInstance().setVisible(true);
	}

}
