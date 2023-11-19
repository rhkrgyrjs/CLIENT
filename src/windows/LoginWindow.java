package windows;

import javax.swing.*;

import Client.Start;
import form.LoginReplyForm;
import form.LoginRequestForm;
import login.LoginRequest;
import swing.ShowMessage;
import hash.SHA256;

import java.awt.event.*;

public class LoginWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static LoginWindow single_instance = null;
	public static LoginWindow getInstance()
	{
		if (single_instance == null) single_instance = new LoginWindow();
		return single_instance;
	}
	
	// 로그인 창 컴포넌트들 
	private JLabel mainImage = null;
	private JLabel idLabel = null;
	private JLabel pwLabel = null;
	private JTextField idInput = null;
	private JTextField pwInput = null;
	private JButton loginButton = null;
	private JButton signupButton = null;
	private JButton pwFindButton = null;
	
	// 전환될 창 저장 
	private LobbyWindow lobbyWindow = null;
	private ChatWindow chatWindow = null;
	private PwFindWindow pwFindWindow = null; 
	private SignupWindow signupWindow = null;
	
	public void setLobbyWindow(LobbyWindow lbw) {this.lobbyWindow = lbw;}
	public void setChatWindow(ChatWindow cw) {this.chatWindow = cw;}
	public void setPwFindWindow(PwFindWindow pfw) {this.pwFindWindow = pfw;}
	public void setSignupWindow(SignupWindow sw) {this.signupWindow = sw;}
	
	public void clear()
	{
		this.idInput.setText("");
		this.pwInput.setText("");
	}
	
	public void logedIn()
	{
		this.setVisible(false);
		this.clear();
		this.lobbyWindow.setVisible(true);
		this.chatWindow.setLocation(this.lobbyWindow.getX() + this.lobbyWindow.getWidth(), this.lobbyWindow.getY());
		this.chatWindow.getConnection();
		this.chatWindow.setVisible(true);
		this.lobbyWindow.requestFocus();
	}
	
	private LoginWindow()
	{	
		// 기본적인 창 설정 
		setTitle("로그인");
		setResizable(false);
		setSize(500, 300);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 이미지 불러오기 
		// 경로 문제 존재함. 나중에 보자. 
		ImageIcon img = new ImageIcon("img/login.png");
	
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		// 컴포넌트 객체 생성 
		mainImage = new JLabel(img);
		idLabel = new JLabel("ID : ");
		pwLabel = new JLabel("PW : ");
		idInput = new JTextField(20);
		pwInput = new JPasswordField(20);
		loginButton = new JButton("로그인");
		signupButton = new JButton("회원가입");
		pwFindButton = new JButton("비번찾기");
		
		// 컴포넌트 바운딩 
		mainImage.setBounds(135, -50, 200, 200);
		idLabel.setBounds(100, 100, 35, 20);
		pwLabel.setBounds(100, 130, 35, 20);
		idInput.setBounds(125, 102, 200, 20);
		pwInput.setBounds(125, 132, 200, 20);
		loginButton.setBounds(335, 102, 50, 50);
		signupButton.setBounds(225, 170, 100, 30);
		pwFindButton.setBounds(135, 170, 100, 30);
		
		// 컴포넌트 이벤트 리스너 추가 
		idInput.setFocusTraversalKeysEnabled(false);
		pwInput.setFocusTraversalKeysEnabled(false);
		idInput.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_TAB) pwInput.requestFocus();
			}
		});
		pwInput.addKeyListener(new Login(this));
		loginButton.addActionListener(new Login(this));
		signupButton.addActionListener(new ShowSignupWindow(this));
		pwFindButton.addActionListener(new ShowPwFindWindow(this));
		
		// 컴포넌트 add 
		add(mainImage);
		add(idLabel);
		add(pwLabel);
		add(idInput);
		add(pwInput);
		add(loginButton);
		add(signupButton);
		add(pwFindButton);
		
		// 창 생성시 visible 여부 
		setVisible(true);
	}
	
	class Login implements ActionListener, KeyListener
	{
		private LoginWindow lw = null;
		Login(LoginWindow lw) {this.lw = lw;}
		
		private void sendLoginRequest()
		{
			LoginRequestForm toSend = new LoginRequestForm();
			toSend.setReqType(1);
			toSend.setId(lw.idInput.getText());
			toSend.setPw(SHA256.toString(lw.pwInput.getText()));
			LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
			if (received.getResult())
			{
				// 로그인 성공 -> 로비 띄우고 새로고침, 로그인 창 초기화하고 닫기
				Start.myId = received.getId();
				Start.myNickname = received.getNickName();
				lw.logedIn();
			}
			else
			{
				// 로그인 실패 
				ShowMessage.warning("로그인", "로그인에 실패했습니다.");
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			sendLoginRequest();
		}
		
		@Override
		public void keyTyped(KeyEvent e)
		{
			//if (e.getKeyCode() == KeyEvent.VK_ENTER)
			//sendLoginRequest();
		}
		
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			sendLoginRequest();
		}
		
		@Override
		public void keyReleased(KeyEvent e)
		{
			//if (e.getKeyCode() == KeyEvent.VK_ENTER)
			//sendLoginRequest();
		}
	}
	
	class ShowSignupWindow implements ActionListener
	{
		LoginWindow lw = null;
		ShowSignupWindow(LoginWindow lw) {this.lw = lw;}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			lw.clear();
			lw.setVisible(false);
			lw.signupWindow.setVisible(true);
			lw.signupWindow.cancelButton.requestFocus();
		}
	}
	
	class ShowPwFindWindow implements ActionListener
	{
		LoginWindow lw = null;
		ShowPwFindWindow(LoginWindow lw) {this.lw = lw;}
		@Override
		public void actionPerformed(ActionEvent e)
		{
			lw.clear();
			lw.setVisible(false);
			lw.pwFindWindow.setVisible(true);
		}
	}
}
