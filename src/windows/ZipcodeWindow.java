package windows;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import swing.JHintTextField;
import swing.ShowMessage;
import form.*;
import login.LoginRequest;

public class ZipcodeWindow extends JFrame 
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static ZipcodeWindow single_instance = null;
	public static ZipcodeWindow getInstance()
	{
		if (single_instance == null) single_instance = new ZipcodeWindow();
		return single_instance;
	}
	
	private JLabel addressLabel = null;
	private JHintTextField addressInput = null;
	private JList resultList = null;
	private JScrollPane resultScroll = null;
	private JButton searchButton = null;
	private JButton confirmButton = null;
	JButton cancelButton = null;

	private Font gainFont = new Font("Tahoma", Font.PLAIN, 11);  
	private Font lostFont = new Font("Tahoma", Font.ITALIC, 11);  
	
	// 전환할 창 저장 
	private SignupWindow signupWindow = null;
	
	public void setSignupWindow(SignupWindow sw) {this.signupWindow = sw;}

	public void clear()
	{
		DefaultListModel listModel = new DefaultListModel();
		this.resultList.setModel(listModel);
		this.address = null;
	    this.addressInput.setFont(lostFont);  
	    this.addressInput.setForeground(Color.GRAY); 
		this.addressInput.setText("예시) [도/광역시] [시/구/군] [도로명] [건물번호]");
		this.searchButton.setEnabled(false);
	}
	
	private String[][] address = null;
	
	private ZipcodeWindow()
	{
		// 기본적인 창 설정 
		setTitle("우편번호 검색");
		setResizable(false);
		setSize(400, 300);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener();
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		// 컴포넌트 객체 생성
		addressLabel = new JLabel("도로명주소");
		addressInput = new JHintTextField("예시) [도/광역시] [시/구/군] [도로명] [건물번호]");
		resultList = new JList();
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultScroll = new JScrollPane(resultList);
		searchButton = new JButton("검색");
		confirmButton = new JButton("확인");
		cancelButton = new JButton("취소");
		
		// 컴포넌트 바운딩 
		addressLabel.setBounds(10, 10, 55, 20);
		addressInput.setBounds(70, 10, 260, 20);
		resultScroll.setBounds(20, 35, 360, 200);
		searchButton.setBounds(330, 10, 60, 20);
		confirmButton.setBounds(90, 240, 100, 25);
		cancelButton.setBounds(210, 240, 100, 25);
		
		// 컴포넌트 add
		add(addressLabel);
		add(addressInput);
		add(resultScroll);
		add(searchButton);
		add(confirmButton);
		add(cancelButton);
		
		// 이벤트 처리 
		addressInput.addKeyListener(new ParseAddress(this));
		addressInput.addKeyListener(new SearchAddress(this));
		resultList.addListSelectionListener(new AddressSelected(this));
		searchButton.addActionListener(new SearchAddress(this));
		confirmButton.addActionListener(new ConfirmAddress(this));
		cancelButton.addActionListener(new ShowSignupWindow(this));
		
		searchButton.setEnabled(false);
		
		// 창 생성시 visible 여부 
		setVisible(false);
	}
	
	private void refresh(String[][] toadd)
	{
		DefaultListModel listModel = new DefaultListModel();
		for (int i=0; i<toadd.length; i++)
		{
			listModel.addElement("[" + toadd[i][0] + "] " + toadd[i][1]);
		}
		this.resultList.setModel(listModel);
	}
	
	class ParseAddress implements KeyListener
	{
		ZipcodeWindow zw = null;
		ParseAddress(ZipcodeWindow zw) {this.zw = zw;}
		
		private void parse(String str)
		{
			String[] words = str.split("\\s+");
			if (1 < words.length && words.length < 5)
			{
				zw.searchButton.setEnabled(true);
			}
			else
			{
				zw.searchButton.setEnabled(false);
			}
		}

		@Override
		public void keyTyped(KeyEvent e) 
		{
			parse(zw.addressInput.getText());
		}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			parse(zw.addressInput.getText());
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			parse(zw.addressInput.getText());
		}
	}

	class SearchAddress implements ActionListener, KeyListener
	{
		ZipcodeWindow zw = null;
		SearchAddress(ZipcodeWindow zw) {this.zw = zw;}

		private void search(String address)
		{
			// 단어가 2개이상 4개이하면 검색하자. 
			String[] words = address.split("\\s+");
			if (1 < words.length && words.length < 5)
			{
				LoginRequestForm toSend = new LoginRequestForm();
				switch(words.length) 
				{
				case 4:
					toSend.setZipNum(words[3]);
				case 3:
					toSend.setDoro(words[2]);
				case 2:
					toSend.setSi(words[1]);
					toSend.setDo(words[0]);
					toSend.setReqType(4);
					break;
				}
				// 폼에 파싱한거 다 넣음.
				LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
				if (received.getResult())
				{
					zw.address = received.getSearchResult();
					zw.refresh(zw.address);
				}
				else
				{
					// 요청 실패했을때 -> 아무것도 안함? 
					zw.addressInput.setText("");
				}
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			search(zw.addressInput.getText());
		}

		@Override
		public void keyTyped(KeyEvent e) 
		{
			// xx
		}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
			search(zw.addressInput.getText());
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			// xx
		}
		
	}
	
	class AddressSelected implements ListSelectionListener
	{
		ZipcodeWindow zw = null;
		AddressSelected(ZipcodeWindow zw) {this.zw = zw;}
		
		@Override
		public void valueChanged(ListSelectionEvent e) 
		{
			ShowMessage.information("우편번호 선택", "선택한 우편번호 : " + zw.address[zw.resultList.getSelectedIndex()][0]);
		}
		
	}
	
	class ConfirmAddress implements ActionListener
	{
		ZipcodeWindow zw = null;
		ConfirmAddress(ZipcodeWindow zw) {this.zw = zw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			zw.addressInput.setFont(lostFont);  
		    zw.addressInput.setForeground(Color.GRAY); 
			zw.addressInput.setText("예시) [도/광역시] [시/구/군] [도로명] [건물번호]");
			zw.signupWindow.postcodeChecked = true;
			zw.signupWindow.zipcodeButton.setText(zw.address[zw.resultList.getSelectedIndex()][0]);
			zw.setVisible(false);
			zw.signupWindow.setVisible(true);
			zw.clear();
		}
		
	}
	
	class ShowSignupWindow implements ActionListener
	{
		ZipcodeWindow zw = null;
		ShowSignupWindow(ZipcodeWindow zw) {this.zw = zw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			zw.setVisible(false);
			zw.clear();
			zw.signupWindow.setVisible(true);
		}
	}
}

