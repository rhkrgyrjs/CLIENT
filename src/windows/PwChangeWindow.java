package windows;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import form.LoginRequestForm;
import hash.SHA256;
import login.LoginRequest;

import java.awt.event.*;

import regex.RegexCheck;
import swing.ShowMessage;

public class PwChangeWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static PwChangeWindow single_instance = null;
	public static PwChangeWindow getInstance()
	{
		if (single_instance == null) single_instance = new PwChangeWindow();
		return single_instance;
	}
	
	private JLabel pwLabel = null;
	private JLabel pwCheckLabel = null;
	private JTextField pwInput = null;
	private JTextField pwCheckInput = null;
	private JButton confirmButton = null;
	private JButton cancelButton = null;
	
	// 전환할 창 설정 
	private LoginWindow loginWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	
	private String myid = null;
	public void setMyid(String id) {this.myid = id;}
	
	public void clear()
	{
		this.myid = null;
		this.pwInput.setText("");
		this.pwCheckInput.setText("");
	}
	
	private PwChangeWindow()
	{
		// 기본적인 창 설정 
		setTitle("비밀번호 변경");
		setResizable(false);
		setSize(250, 130);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// 컴포넌트 객체 생성 
		pwLabel = new JLabel("새 PW");
		pwCheckLabel = new JLabel("PW 확인");
		pwInput = new JTextField(20);
		pwCheckInput = new JTextField(20);
		confirmButton = new JButton("확인");
		cancelButton = new JButton("취소");
		
		// 컴포넌트 바운딩
		pwLabel.setBounds(10, 10, 50, 20);
		pwCheckLabel.setBounds(10, 40, 50, 20);
		pwInput.setBounds(60, 10, 180, 20);
		pwCheckInput.setBounds(60, 40, 180, 20);
		confirmButton.setBounds(50, 70, 80, 25);
		cancelButton.setBounds(140, 70, 80, 25);
		
		// 이벤트 처리
		pwCheckInput.setFocusTraversalKeysEnabled(false);
		confirmButton.addActionListener(new ChangePw(this));
		cancelButton.addActionListener(new ShowLoginWindow(this));
		
		// 컴포넌트 add 
		add(pwLabel);
		add(pwCheckLabel);
		add(pwInput);
		add(pwCheckInput);
		add(confirmButton);
		add(cancelButton);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	class ShowLoginWindow implements ActionListener
	{
		PwChangeWindow pcw = null;
		ShowLoginWindow(PwChangeWindow pcw) {this.pcw = pcw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			pcw.setVisible(false);
			pcw.clear();
			pcw.loginWindow.setVisible(true);
		}
		
	}
	
	class ChangePw implements ActionListener
	{
		PwChangeWindow pcw = null;
		ChangePw(PwChangeWindow pcw) {this.pcw = pcw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (RegexCheck.isPw(pcw.pwInput.getText()) && pcw.pwInput.getText().equals(pcw.pwCheckInput.getText()) )
			{
				// 서버로 보내
				LoginRequestForm toSend = new LoginRequestForm();
				toSend.setReqType(7);
				toSend.setId(pcw.myid);
				toSend.setPw(SHA256.toString(pcw.pwInput.getText()));
				if (LoginRequest.toServer(toSend))
				{
					ShowMessage.warning("비밀번호 변경", "비밀번호가 변경되었습니다.");
					pcw.setVisible(false);
					pcw.clear();
					pcw.loginWindow.setVisible(true);
				}
				else
				{
					ShowMessage.warning("비밀번호 변경", "비밀번호 변경에 실패했습니다.");
				}
			}
			else
			{
				ShowMessage.warning("비밀번호 변경", "입력이 올바른지 다시 확인해주세요.");
			}
				
		}
	}
}

