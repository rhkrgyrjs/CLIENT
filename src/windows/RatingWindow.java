package windows;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Client.Start;
import form.LoginReplyForm;
import form.LoginRequestForm;
import hash.SHA256;
import login.LoginRequest;
import swing.ShowMessage;

public class RatingWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static RatingWindow single_instance = null;
	public static RatingWindow getInstance()
	{
		if (single_instance == null) single_instance = new RatingWindow();
		return single_instance;
	}
	
	private JTextArea ratings = null;
	private JScrollPane ratingScroll = null;
	
	private String[] info = null;

	private RatingWindow()
	{
		// 창 기본 세팅 
		setTitle("랭킹");
		setResizable(false);
		setSize(300, 500);
		setLayout(null);
		setLocationRelativeTo(null);
		
		this.addWindowListener(new WindowAdapter() 
		{
	        @Override
	        public void windowClosing(WindowEvent e) 
	        {setVisible(false);}});
		
		ratings = new JTextArea();
		ratings.setEditable(false);
		ratingScroll = new JScrollPane(ratings);
		
		ratingScroll.setBounds(5, 5, 290, 490);
		
		add(ratingScroll);
		
		setVisible(false);
		
	}
	
	public void refresh()
	{
		ratings.setText("");
		LoginRequestForm toSend = new LoginRequestForm();
		toSend.setReqType(11);
		LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
		if (received.getResult())
		{
			// 로그인 성공 -> 로비 띄우고 새로고침, 로그인 창 초기화하고 닫기
			for(int i=0; i<received.getSearchResult().length; i++)
			{
				ratings.append(" " + (i+1) + "등 : " + received.getSearchResult()[i][0] + "\n");
			}
		}
		else
		{
			// 로그인 실패 
			ratings.append("레이팅 정보를 불러올수 없음.");
		}
	}
	
	public static void main(String[] args)
	{
		RatingWindow.getInstance().setVisible(true);
	}
}
