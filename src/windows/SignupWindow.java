package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import form.LoginRequestForm;
import hash.SHA256;
import login.LoginRequest;
import regex.RegexCheck;
import swing.JHintTextField;
import swing.ShowMessage;

import parse.EndsWithImg;
import image.Blob;
import image.PicResize;

public class SignupWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static SignupWindow single_instance = null;
	public static SignupWindow getInstance()
	{
		if (single_instance == null) single_instance = new SignupWindow();
		return single_instance;
	}
	
	private JLabel idLabel = null;
	private JLabel nickNameLabel = null;
	private JLabel pwLabel = null;
	private JLabel pwCheckLabel = null;
	private JLabel emailLabel = null;
	private JLabel phoneNumLabel = null;
	private JLabel addressLabel = null;
	private JHintTextField idInput = null;
	private JHintTextField nickNameInput = null;
	private JHintTextField pwInput = null;
	private JHintTextField pwCheckInput = null;
	private JHintTextField emailInput = null;
	private JHintTextField phoneNumInput = null;
	private JTextField addressInput = null;
	private JButton idDupCheckButton = null;
	private JButton profilPicButton = null;
	JButton zipcodeButton = null;
	private JButton confirmButton = null;
	JButton cancelButton = null;
	private JFileChooser picChooser = null;
	
	ImageIcon img = new ImageIcon("img/profile.png");
	byte[] imgBlob = null;

	private Font gainFont = new Font("Tahoma", Font.PLAIN, 11);  
	private Font lostFont = new Font("Tahoma", Font.ITALIC, 11);  
	
	private boolean idDupChecked = false;
	private boolean nickNameChecked = false;
	private boolean passwordChecked = false;
	private boolean emailChecked = false;
	private boolean phoneNumChecked = false;
	private boolean profilePicChecked = false;
	boolean postcodeChecked = false;
	private boolean addresschecked = true;
	
	// 전환할 창 지정 
	private LoginWindow loginWindow = null;
	private ZipcodeWindow zipcodeWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	public void setZipcodeWindow(ZipcodeWindow zw) {this.zipcodeWindow = zw;}
	
	public void clear()
	{	
		this.idDupChecked = false;
		this.nickNameChecked = false;
		this.passwordChecked = false;
		this.emailChecked = false;
		this.phoneNumChecked = false;
		this.profilePicChecked = false;
		this.postcodeChecked = false;
		this.addresschecked = true;
		img = new ImageIcon("img/profile.png");
		this.idInput.setText("8~16자, 영어 + 숫자");
	    this.idInput.setFont(lostFont);  
	    this.idInput.setForeground(Color.GRAY); 
		this.nickNameInput.setText("8~16자, 한글/영어/숫자 허용");
	    this.nickNameInput.setFont(lostFont);  
	    this.nickNameInput.setForeground(Color.GRAY); 
		this.pwInput.setText("8~20자, 특수문자/대문자 포함");
	    this.pwInput.setFont(lostFont);  
	    this.pwInput.setForeground(Color.GRAY); 
		this.pwCheckInput.setText("패스워드 확인");
	    this.pwCheckInput.setFont(lostFont);  
	    this.pwCheckInput.setForeground(Color.GRAY); 
		this.emailInput.setText("예) user@example.com ");
	    this.emailInput.setFont(lostFont);  
	    this.emailInput.setForeground(Color.GRAY); 
		this.phoneNumInput.setText("예) 010-0000-0000");
	    this.phoneNumInput.setFont(lostFont);  
	    this.phoneNumInput.setForeground(Color.GRAY); 
	    this.addressInput.setText("");
		this.profilPicButton.setIcon(img);
		this.zipcodeButton.setText("우편번호 찾기");
	    this.idInput.setEnabled(true);
		this.idDupCheckButton.setEnabled(false);
	}
	
	private SignupWindow()
	{
		// 기본적인 창 설정 
		setTitle("회원가입");
		setResizable(false);
		setSize(375, 290);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// 이미지 불러오기 
		// 이미지 파일 불러오기는 경로 이슈 있음.
		
		// 컴포넌트 객체 생성 
		idLabel = new JLabel("ID");
		nickNameLabel = new JLabel("닉네임");
		pwLabel = new JLabel("PW");
		pwCheckLabel = new JLabel("PW 확인");
		emailLabel = new JLabel("이메일");
		phoneNumLabel = new JLabel("전화번호");
		addressLabel = new JLabel("상세주소");
		idInput = new JHintTextField("8~16자, 영어 + 숫자");
		nickNameInput = new JHintTextField("8~16자, 한글/영어/숫자 허용");
		pwInput = new JHintTextField("8~20자, 특수문자/대문자 포함");
		pwCheckInput = new JHintTextField("패스워드 확인");
		emailInput = new JHintTextField("예) user@example.com ");
		phoneNumInput = new JHintTextField("예) 010-0000-0000");
		addressInput = new JTextField();
		idDupCheckButton = new JButton("중복체크");
		profilPicButton = new JButton(img);
		zipcodeButton = new JButton("우편번호 찾기");
		confirmButton = new JButton("확인");
		cancelButton = new JButton("취소");
		picChooser = new JFileChooser();
		
		// 컴포넌트 바운딩 
		idLabel.setBounds(15, 15, 50, 20);
		nickNameLabel.setBounds(15, 45, 50, 20);
		pwLabel.setBounds(15, 75, 50, 20);
		pwCheckLabel.setBounds(15, 105, 50, 20);
		emailLabel.setBounds(15, 135, 50, 20);
		phoneNumLabel.setBounds(15, 165, 50, 20);
		addressLabel.setBounds(15, 195, 50, 20);
		idInput.setBounds(70, 15, 145, 20);
		nickNameInput.setBounds(70, 45, 200, 20);
		pwInput.setBounds(70, 75, 200, 20);
		pwCheckInput.setBounds(70, 105, 200, 20);
		emailInput.setBounds(70, 135, 200, 20);
		phoneNumInput.setBounds(70, 165, 200, 20);
		addressInput.setBounds(70, 195, 200, 20);
		profilPicButton.setBounds(280, 50, 80, 92);
		zipcodeButton.setBounds(280, 145, 80, 20);
		idDupCheckButton.setBounds(215, 15, 50, 20);
		confirmButton.setBounds(100, 230, 80, 25);
		cancelButton.setBounds(200, 230, 80, 25);
		
		// 이벤트 처리 
		idInput.setFocusTraversalKeysEnabled(false);
		idInput.addKeyListener(new IdRegexCheck(this));
		nickNameInput.addKeyListener(new NickNameRegexCheck(this));
		pwInput.addKeyListener(new PasswordRegexCheck(this));
		pwCheckInput.addKeyListener(new PasswordRegexCheck(this));
		emailInput.addKeyListener(new EmailRegexCheck(this));
		phoneNumInput.addKeyListener(new PhoneNumRegexCheck(this));
		emailInput.setFocusTraversalKeysEnabled(false);
		phoneNumInput.setFocusTraversalKeysEnabled(false);
		idInput.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_TAB) nickNameInput.requestFocus();
			}
		});
		emailInput.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_TAB) phoneNumInput.requestFocus();
			}
		});
		profilPicButton.addActionListener(new ChooseProfilePic(this));
		zipcodeButton.addActionListener(new ShowZipcodeWindow(this));
		idDupCheckButton.addActionListener(new IdDupCheck(this));
		confirmButton.addActionListener(new Signup(this));
		cancelButton.addActionListener(new ShowLoginWindow(this));
		
		// filechooser 설정 
		picChooser.setFileFilter(new FileNameExtensionFilter("jpg", "jpg"));
		picChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// 컴포넌트 add 
		add(idLabel);
		add(nickNameLabel);
		add(pwLabel);
		add(pwCheckLabel);
		add(emailLabel);
		add(phoneNumLabel);
		add(addressLabel);
		add(idInput);
		add(nickNameInput);
		add(pwInput);
		add(pwCheckInput);
		add(emailInput);
		add(phoneNumInput);
		add(addressInput);
		add(profilPicButton);
		add(zipcodeButton);
		add(idDupCheckButton);
		add(confirmButton);
		add(cancelButton);
		
		idDupCheckButton.setEnabled(false);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	class Signup implements ActionListener
	{
		SignupWindow sw = null;
		Signup(SignupWindow sw) {this.sw = sw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (sw.idDupChecked && sw.nickNameChecked && sw.passwordChecked && sw.emailChecked && sw.phoneNumChecked && sw.profilePicChecked &&sw.postcodeChecked && sw.addresschecked)
			{	
				// 회원가입 신청이 성공하면, 이미지 파일도 보낸다.
				LoginRequestForm toSend = new LoginRequestForm();
				toSend.setReqType(2);
				toSend.setId(sw.idInput.getText());
				toSend.setNickname(sw.nickNameInput.getText());
				toSend.setPw(SHA256.toString(sw.pwInput.getText()));
				toSend.setEmail(sw.emailInput.getText());
				toSend.setPhonenumber(sw.phoneNumInput.getText());
				toSend.setZipcode(sw.zipcodeButton.getText());
				toSend.setPicBlob(imgBlob);
				toSend.setAddress(sw.addressInput.getText());
				
				if (LoginRequest.toServer(toSend))
				{
					sw.setVisible(false);
					sw.clear();
					sw.loginWindow.setVisible(true);
					ShowMessage.information("회원가입", "회원가입 성공");
				}
				else
				{
					ShowMessage.warning("회원가입", "회원가입에 실패했습니다.");
				}
			}
			else
			{
				ShowMessage.warning("입력 확인", "모든 정보를 제대로 입력했나 확인해주세요.");
			}
			
		}
	}
	
	class IdDupCheck implements ActionListener
	{
		SignupWindow sw = null;
		IdDupCheck(SignupWindow sw) {this.sw = sw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			LoginRequestForm toSend = new LoginRequestForm();
			toSend.setReqType(3);
			toSend.setId(sw.idInput.getText());
			if (LoginRequest.toServer(toSend))
			{
				// 중복체크 성공 -> id 사용가능 
				sw.idInput.setEnabled(false);
				sw.idDupCheckButton.setEnabled(false);
				sw.idDupChecked = true;
				ShowMessage.information("ID 중복체크", "사용 가능한 ID입니다.");
				sw.nickNameInput.requestFocus();
			}
			else
			{
				// 중복체크 실패 -> id 사용 불가능 
				sw.idDupChecked = false;
				ShowMessage.warning("ID 중복체크", "사용할 수 없는 ID입니다.");
			}
			
		}
	}
	
	class IdRegexCheck implements KeyListener
	{
		SignupWindow sw = null;
		IdRegexCheck(SignupWindow sw) {this.sw = sw;}
		
		private void idRegexCheck(String id)
		{
			if (RegexCheck.isId(id))
			{
				sw.idDupCheckButton.setEnabled(true);
			}
			else
			{
				sw.idDupCheckButton.setEnabled(false);
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			idRegexCheck(sw.idInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			idRegexCheck(sw.idInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			idRegexCheck(sw.idInput.getText());
		}
	}
	
	class NickNameRegexCheck implements KeyListener
	{
		SignupWindow sw = null;
		NickNameRegexCheck(SignupWindow sw) {this.sw = sw;}
		
		private void nickNameRegexCheck(String nickName)
		{
			if (RegexCheck.isNickName(nickName))
			{
				// 닉네임 사용가능 
				sw.nickNameChecked = true;
			}
			else
			{
				// 닉네임 사용불가능 
				sw.nickNameChecked = false;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			nickNameRegexCheck(sw.nickNameInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			nickNameRegexCheck(sw.nickNameInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			nickNameRegexCheck(sw.nickNameInput.getText());
		}
	}
	
	class PasswordRegexCheck implements KeyListener
	{
		SignupWindow sw = null;
		PasswordRegexCheck(SignupWindow sw) {this.sw = sw;}
		
		private void passwordRegexCheck(String pw)
		{
			if (RegexCheck.isPw(pw))
			{
				// 비번 사용가능 
				if (sw.pwInput.getText().equals(sw.pwCheckInput.getText()))
				{
					// 비번 확인도 통과 
					sw.passwordChecked = true;	
				}
				else
				{
					// 비번 확인 통과 못함 
					sw.passwordChecked = false;
				}
			}
			else
			{
				// 비번 사용불가능 
				sw.passwordChecked = false;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			passwordRegexCheck(sw.pwInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			passwordRegexCheck(sw.pwInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			passwordRegexCheck(sw.pwInput.getText());
		}
	}
	
	class EmailRegexCheck implements KeyListener
	{
		SignupWindow sw = null;
		EmailRegexCheck(SignupWindow sw) {this.sw = sw;}
		
		private void emailRegexCheck(String email)
		{
			if (RegexCheck.isEmail(email))
			{
				// 이메일 사용가능 
				sw.emailChecked = true;
			}
			else
			{
				// 이메일 사용불가능 
				sw.emailChecked = false;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			emailRegexCheck(sw.emailInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			emailRegexCheck(sw.emailInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			emailRegexCheck(sw.emailInput.getText());
		}
	}
	
	class PhoneNumRegexCheck implements KeyListener
	{
		SignupWindow sw = null;
		PhoneNumRegexCheck(SignupWindow sw) {this.sw = sw;}
		
		private void phoneNumRegexCheck(String phoneNum)
		{
			if (RegexCheck.isPhoneNum(phoneNum))
			{
				// 전화번호 사용가능 
				sw.phoneNumChecked = true;
			}
			else
			{
				// 전화번호 사용불가능 
				sw.phoneNumChecked = false;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			phoneNumRegexCheck(sw.phoneNumInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			phoneNumRegexCheck(sw.phoneNumInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			phoneNumRegexCheck(sw.phoneNumInput.getText());
		}
	}
	
	class AddressLengthCheck implements KeyListener
	{
		SignupWindow sw = null;
		AddressLengthCheck(SignupWindow sw) {this.sw = sw;}
		
		private void addressCheck(String address)
		{
			if (RegexCheck.isAddress(address))
			{
				// 주소 사용가능 
				sw.addresschecked = true;
			}
			else
			{
				// 주소 사용불가능 
				sw.addresschecked = false;
			}
		}
		
		@Override
		public void keyTyped(KeyEvent e) 
		{
			addressCheck(sw.addressInput.getText());
		}
		
		@Override
		public void keyPressed(KeyEvent e) 
		{
			addressCheck(sw.addressInput.getText());
		}
		
		@Override
		public void keyReleased(KeyEvent e) 
		{
			addressCheck(sw.addressInput.getText());
		}
	}
	
	class ChooseProfilePic implements ActionListener
	{
		SignupWindow sw = null;
		ChooseProfilePic(SignupWindow sw) {this.sw = sw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (sw.picChooser.showOpenDialog(null) == 0)
			{
				// 파일 불러오기 창 열림 
				// sw.picChooser.getSelectedFile(); // 대충 경로로 설정된 파일 열어오기 느낌일듯?
				// 파싱해서 확장자 png or jpg 아니면 컷하기 
				if (EndsWithImg.isJpg((sw.picChooser.getSelectedFile().getAbsolutePath())))
				{
					try {
					BufferedImage resized = PicResize.getProfilePic(sw.picChooser.getSelectedFile().getAbsolutePath());
			    	BufferedImage bufim = ImageIO.read(new File(sw.picChooser.getSelectedFile().getAbsolutePath()));
					sw.imgBlob = Blob.toByteArray(bufim, "jpg");
					sw.img = new ImageIcon(resized);
					sw.profilPicButton.setIcon(img);
					sw.profilePicChecked = true;} catch (IOException a) {a.printStackTrace();}
				}
				else
				{
					sw.profilePicChecked = false;
				}
			}
		}
	}
	
	class ShowLoginWindow implements ActionListener
	{
		SignupWindow sw = null;
		ShowLoginWindow(SignupWindow sw) {this.sw = sw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			sw.setVisible(false);
			sw.clear();
			sw.loginWindow.setVisible(true);
		}
	}
	
	class ShowZipcodeWindow implements ActionListener
	{
		SignupWindow sw = null;
		ShowZipcodeWindow(SignupWindow sw) {this.sw = sw;}
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			sw.setVisible(false);
			sw.zipcodeWindow.setVisible(true);
			sw.zipcodeWindow.cancelButton.requestFocus();
		}
		
	}
}


