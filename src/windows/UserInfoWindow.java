package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import form.LoginReplyForm;
import form.LoginRequestForm;
import image.Blob;
import image.PicResize;
import login.LoginRequest;
import swing.ShowMessage;

public class UserInfoWindow extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static UserInfoWindow single_instance = null;
	public static UserInfoWindow getInstance()
	{
		if (single_instance == null) single_instance = new UserInfoWindow();
		return single_instance;
	}
	
	private JLabel profilepicLabel = null;
	private JLabel idNnicknameLabel = null;
	private JLabel emailLabel = null;
	private JLabel phonenumberLabel = null;
	private JLabel winNloseNdrawLabel = null;
	private JLabel eloLabel = null;
	private JButton closeButton = null;
	
	private UserInfoWindow()
	{
		// 기본적인 창 설정 
		setTitle("유저 정보");
		setResizable(false);
		setSize(300, 530);
		setLayout(null);
		setLocationRelativeTo(null);
		
		try 
		{
			BufferedImage buf = PicResize.getIngameProfile(ImageIO.read(new File("img/sample_profile.jpg")));
			ImageIcon profile = new ImageIcon(buf);
			profilepicLabel = new JLabel(profile);
			profilepicLabel.setBounds(10, 10, 280, 280);
			add(profilepicLabel);
		}
		catch (IOException e) {e.printStackTrace();}
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener(); -> 로그아웃 하시겠습니까 메시지창. 
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		idNnicknameLabel = new JLabel("아이디 @ 닉네임");
		emailLabel = new JLabel("test@example.com");
		phonenumberLabel = new JLabel("010-0000-0000");
		winNloseNdrawLabel = new JLabel("000승 000무 000패");
		eloLabel = new JLabel("elo Rating : 0000");
		closeButton = new JButton("닫기");
		
		idNnicknameLabel.setBounds(50, 300, 200, 20);
		emailLabel.setBounds(50, 320, 200, 20);
		phonenumberLabel.setBounds(50, 340, 200, 20);
		winNloseNdrawLabel.setBounds(50, 390, 200, 20);
		eloLabel.setBounds(50, 410, 200, 20);
		closeButton.setBounds(75, 450, 150, 30);
		
		closeButton.addActionListener(new CloseWindow(this));
		
		add(idNnicknameLabel);
		add(emailLabel);
		add(phonenumberLabel);
		add(winNloseNdrawLabel);
		add(eloLabel);
		add(closeButton);
		
		setVisible(false);
	}
	
	public void loadInfo(String id)
	{
		LoginRequestForm toSend = new LoginRequestForm();
		toSend.setReqType(10);
		toSend.setId(id);
		LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
		if (received.getResult())
		{
			// 정보 불러오기 성공 
			BufferedImage profile = PicResize.getIngameProfile(Blob.toBufferedImage(received.getPicBlob()));
			ImageIcon pic = new ImageIcon(profile);
			this.profilepicLabel.setIcon(pic);
			this.idNnicknameLabel.setText(received.getId() + " @ " + received.getNickName());
			this.emailLabel.setText(received.getSearchResult()[0][2]);
			this.phonenumberLabel.setText(received.getSearchResult()[0][3]);
			this.winNloseNdrawLabel.setText(received.getSearchResult()[0][0]);
			this.eloLabel.setText(received.getSearchResult()[0][1]);
			this.setVisible(true);
		}
		else
		{
			// 정보 불러오기 실패 
			ShowMessage.warning("오류", "유저의 정보를 불러올 수 없습니다.");
		}
	}
	
	class CloseWindow implements ActionListener
	{
		UserInfoWindow uifw = null;
		CloseWindow(UserInfoWindow uifw) {this.uifw = uifw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			uifw.setVisible(false);
		}
		
	}
	
	
}
