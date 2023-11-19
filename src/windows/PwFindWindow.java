package windows;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import form.LoginRequestForm;
import login.LoginRequest;
import swing.ShowMessage;

import java.awt.event.*;

public class PwFindWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static PwFindWindow single_instance = null;
	public static PwFindWindow getInstance()
	{
		if (single_instance == null) single_instance = new PwFindWindow();
		return single_instance;
	}
	
	private JLabel idLabel = null;
	private JLabel phoneNumLabel = null;
	private JLabel zipcodeLabel = null;
	private JTextField idInput = null;
	private JTextField phoneNumInput = null;
	private JTextField zipcodeInput = null;
	private JButton confirmButton = null;
	private JButton cancelButton = null;
	
	private LoginWindow loginWindow = null;
	private PwChangeWindow pwChangeWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	public void setPwChangeWindow(PwChangeWindow pcw) {this.pwChangeWindow = pcw;}
	
	public void clear()
	{
		this.idInput.setText("");
		this.phoneNumInput.setText("");
		this.zipcodeInput.setText("");
	}
	
	private PwFindWindow()
	{
		// 기본적인 창 설정 
		setTitle("본인인증");
		setResizable(false);
		setSize(250, 160);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// 컴포넌트 객체 생성 
		idLabel = new JLabel("ID");
		phoneNumLabel = new JLabel("전화번호");
		zipcodeLabel = new JLabel("우편번호");
		idInput = new JTextField(20);
		phoneNumInput = new JTextField(20);
		zipcodeInput = new JTextField(20);
		confirmButton = new JButton("확인");
		cancelButton = new JButton("취소");
		
		// 컴포넌트 바운딩
		idLabel.setBounds(10, 10, 50, 20);
		phoneNumLabel.setBounds(10, 40, 50, 20);
		zipcodeLabel.setBounds(10, 70, 50, 20);
		idInput.setBounds(60, 10, 180, 20);
		phoneNumInput.setBounds(60, 40, 180, 20);
		zipcodeInput.setBounds(60, 70, 180, 20);
		confirmButton.setBounds(50, 100, 80, 25);
		cancelButton.setBounds(140, 100, 80, 25);
		
		// 이벤트 처리
		zipcodeInput.setFocusTraversalKeysEnabled(false);
		confirmButton.addActionListener(new Verification(this));
		cancelButton.addActionListener(new ShowLoginWindow(this));
		
		// 컴포넌트 add 
		add(idLabel);
		add(phoneNumLabel);
		add(zipcodeLabel);
		add(idInput);
		add(phoneNumInput);
		add(zipcodeInput);
		add(confirmButton);
		add(cancelButton);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	class ShowLoginWindow implements ActionListener
	{
		PwFindWindow pfw = null;
		ShowLoginWindow(PwFindWindow pfw) {this.pfw = pfw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			pfw.setVisible(false);
			pfw.clear();
			pfw.loginWindow.setVisible(true);
		}
	}
	
	class Verification implements ActionListener
	{
		PwFindWindow pfw = null;
		Verification(PwFindWindow pfw) {this.pfw = pfw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			LoginRequestForm toSend = new LoginRequestForm();
			toSend.setReqType(6);
			toSend.setId(pfw.idInput.getText());
			toSend.setPhonenumber(pfw.phoneNumInput.getText());
			toSend.setZipcode(pfw.zipcodeInput.getText());
			if (LoginRequest.toServer(toSend))
			{
				pfw.setVisible(false);
				pfw.pwChangeWindow.setMyid(pfw.idInput.getText());
				pfw.clear();
				pfw.pwChangeWindow.setVisible(true);
			}
			else
			{
				ShowMessage.warning("본인인증", "본인인증에 실패했습니다.");
			}
			
		}
	}
}

