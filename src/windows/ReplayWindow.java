package windows;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

import Client.Start;
import form.ChatForm;
import form.GameBoardInfoForm;
import form.LoginReplyForm;
import form.LoginRequestForm;
import image.Blob;
import image.PicResize;
import logging.ArrayListSave;
import login.LoginRequest;
import socket.SendObject;
import swing.ShowMessage;
import windows.GameBoardWindow.Close;
import windows.GameBoardWindow.FlipNRing;

public class ReplayWindow extends JFrame 
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static ReplayWindow single_instance = null;
	public static ReplayWindow getInstance()
	{
		if (single_instance == null) single_instance = new ReplayWindow();
		return single_instance;
	}
	
	private ArrayList<GameBoardInfoForm> LOG = null;
	private int CURSOR = 0;
	
	// 배경, 종 이미지 
	ImageIcon bgImg = null;
	ImageIcon bellImg = null;
	
	// 카드 이미지들 
	ImageIcon cardBackImg = null;
	ImageIcon blankCardImg = null;
	ImageIcon banana1Card = null;
	ImageIcon banana2Card = null;
	ImageIcon banana3Card = null;
	ImageIcon banana4Card = null;
	ImageIcon banana5Card = null;
	ImageIcon lime1Card = null;
	ImageIcon lime2Card = null;
	ImageIcon lime3Card = null;
	ImageIcon lime4Card = null;
	ImageIcon lime5Card = null;
	ImageIcon strawberry1Card = null;
	ImageIcon strawberry2Card = null;
	ImageIcon strawberry3Card = null;
	ImageIcon strawberry4Card = null;
	ImageIcon strawberry5Card = null;
	ImageIcon plum1Card = null;
	ImageIcon plum2Card = null;
	ImageIcon plum3Card = null;
	ImageIcon plum4Card = null;
	ImageIcon plum5Card = null;
	
	// 배경 패널 
	JPanel bgPanel = null;
	
	// 이미지 띄울 라벨 
	JLabel bellImgLabel = null;
	
	JLabel labelA = null;
	JLabel labelB = null;
	JLabel labelC = null;
	JLabel labelD = null;
	
	JLabel myDeck = null;
	JLabel opDeck = null;
	JLabel myDeckCount = null;
	JLabel opDeckCount = null;
	
	JLabel host = null;
	JLabel guest = null;
	
	JButton forwordButton = null;
	JButton backwordButton = null;
	
	// 다른 창들 제어
	ChatWindow chatWindow = null;
	LobbyWindow lobbyWindow = null;
	
	public void setChatWindow(ChatWindow cw) {this.chatWindow = cw;}
	public void setLobbyWindow(LobbyWindow lbw) {this.lobbyWindow = lbw;}

	public void clear()
	{
        LOG.clear();
        CURSOR = 0;
        
        myDeckCount.setText("00");
        opDeckCount.setText("00");
        
        labelA.setIcon(blankCardImg);
        labelB.setIcon(blankCardImg);
        labelC.setIcon(blankCardImg);
        labelD.setIcon(blankCardImg);
	}
	
	private ReplayWindow()
	{
		// 창 기본 세팅 
		setTitle("복기 모드");
		setResizable(false);
		setSize(1200, 800);
		setLayout(null);
		setLocationRelativeTo(null);
		
		LOG = new ArrayList<GameBoardInfoForm>();
		
		// 배경 이미지 로드/패널 처리 
		bgImg = new ImageIcon("img/background.png");
		bgPanel = new JPanel() 
		{
            @Override
            protected void paintComponent(Graphics g) 
            {
                super.paintComponent(g);
                bgImg.paintIcon(this, g, 0, 0);
            }
        };
        bgPanel.setLayout(null);
        
      
        backwordButton = new JButton("<");
        forwordButton = new JButton(">");
        
        // 종 이미지 로드/띄우기 
        bellImg = new ImageIcon("img/bell.png");
        bellImgLabel = new JLabel(bellImg);
        
        // 카드 이미지 로드 
        cardBackImg = new ImageIcon("img/cardback.png");
        blankCardImg = new ImageIcon("img/blank.png");
        banana1Card = new ImageIcon("img/banana1.png");
        banana2Card = new ImageIcon("img/banana2.png");
        banana3Card = new ImageIcon("img/banana3.png");
        banana4Card = new ImageIcon("img/banana4.png");
        banana5Card = new ImageIcon("img/banana5.png");
        lime1Card = new ImageIcon("img/lime1.png");
        lime2Card = new ImageIcon("img/lime2.png");
        lime3Card = new ImageIcon("img/lime3.png");
        lime4Card = new ImageIcon("img/lime4.png");
        lime5Card = new ImageIcon("img/lime5.png");
        strawberry1Card = new ImageIcon("img/strawberry1.png");
        strawberry2Card = new ImageIcon("img/strawberry2.png");
        strawberry3Card = new ImageIcon("img/strawberry3.png");
        strawberry4Card = new ImageIcon("img/strawberry4.png");
        strawberry5Card = new ImageIcon("img/strawberry5.png");
        plum1Card = new ImageIcon("img/plum1.png");
        plum2Card = new ImageIcon("img/plum2.png");
        plum3Card = new ImageIcon("img/plum3.png");
        plum4Card = new ImageIcon("img/plum4.png");
        plum5Card = new ImageIcon("img/plum5.png");
        
        // 카드 뒷면 띄우기 
        myDeck = new JLabel(cardBackImg);
        opDeck = new JLabel(cardBackImg);
        
        host = new JLabel("< 호스트 >");
        guest = new JLabel("< 게스트 >");
        
        // A, B, C, D 기본값 띄우기 
        labelA = new JLabel(blankCardImg);
        labelB = new JLabel(blankCardImg);
        labelC = new JLabel(blankCardImg);
        labelD = new JLabel(blankCardImg);
        
        // 나/상대의 덱 장수 표시하는 라벨 띄우기 
        myDeckCount = new JLabel("00");
        opDeckCount = new JLabel("00");
        
        // 컴포넌트 파운딩 
        bgPanel.setBounds(0, 0, 1200, 800);
        bellImgLabel.setBounds(510, 310, 180, 160);
        myDeck.setBounds(551, 620, 90, 120);
        opDeck.setBounds(551, 20, 90, 120);
        labelA.setBounds(405, 200, 90, 120);
        labelB.setBounds(700, 200, 90, 120);
        labelC.setBounds(405, 450, 90, 120);
        labelD.setBounds(700, 450, 90, 120);
        myDeckCount.setBounds(590, 150, 50, 20);
        opDeckCount.setBounds(590, 590, 50, 20);
        host.setBounds(566, 560, 80, 20);
        guest.setBounds(566, 180, 80, 20);
        
        forwordButton.setBounds(690, 700, 50, 50);
        backwordButton.setBounds(440, 700, 50, 50);
        backwordButton.setEnabled(false);
        
        forwordButton.addActionListener(new ActionListener() 
        {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.out.println("size : " + LOG.size());
				if (CURSOR == LOG.size()-1)
				{
					forwordButton.setEnabled(false);
				}
				else
				{
					CURSOR++;
					updateWindow(LOG.get(CURSOR));
					backwordButton.setEnabled(true);
				}
			}
        });
        
        backwordButton.addActionListener(new ActionListener()
        		{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						if (CURSOR == 0)
						{
							backwordButton.setEnabled(false);
						}
						else
						{
							CURSOR--;
							updateWindow(LOG.get(CURSOR));
							forwordButton.setEnabled(true);
						}
					}
        		});
        
        // 컴포넌트 add 
        bgPanel.add(bellImgLabel);
        
        bgPanel.add(myDeck);
        bgPanel.add(opDeck);
        bgPanel.add(labelA);
        bgPanel.add(labelB);
        bgPanel.add(labelC);
        bgPanel.add(labelD);
        
        bgPanel.add(myDeckCount);
        bgPanel.add(opDeckCount);
        
        bgPanel.add(forwordButton);
        bgPanel.add(backwordButton);
        bgPanel.add(host);
        bgPanel.add(guest);
        
        add(bgPanel);
		
		// 창 닫았을때 이벤트 리스너 
		this.addWindowListener(new Close(this));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		setVisible(false);
	}
	
	public void initRepalyMode(String logFilePath)
	{
		LOG = ArrayListSave.loadArrayListFromFile(logFilePath);
		updateWindow(LOG.get(CURSOR));
		this.setVisible(true);
	}
	
	public void updateWindow(GameBoardInfoForm boardInfo)
	{
		// 덱 카운트 갱신 루틴 추가하기. my가 상대(위쪽)꺼임.
		opDeckCount.setText(String.valueOf(boardInfo.getHostDeckCount()));
		myDeckCount.setText(String.valueOf(boardInfo.getGuestDeckCount()));
		
		
		// fruit : 0~4 사이의 값, 0-빈카드 1-바나나, 2-라임, 3-딸기, 4-자두 
		// number : 0~5 사이의 값 (0은 빈카드) 
		// A 카드 업데이트.
		switch(boardInfo.getAFront().getNumber())
		{
			case 0:
				this.labelA.setIcon(blankCardImg);
			break;
		
			case 1:
				switch(boardInfo.getAFront().getFruit())
				{
					case 1:
						this.labelA.setIcon(banana1Card);
					break;
					
					case 2:
						this.labelA.setIcon(lime1Card);
					break;
					
					case 3:
						this.labelA.setIcon(strawberry1Card);
					break;
					
					case 4:
						this.labelA.setIcon(plum1Card);
					break;
				}
			break;
			
			case 2:
				switch(boardInfo.getAFront().getFruit())
				{	
					case 1:
						this.labelA.setIcon(banana2Card);
					break;
					
					case 2:
						this.labelA.setIcon(lime2Card);
					break;
					
					case 3:
						this.labelA.setIcon(strawberry2Card);
					break;
					
					case 4:
						this.labelA.setIcon(plum2Card);
					break;
				}
			break;
			
			case 3:
				switch(boardInfo.getAFront().getFruit())
				{
					case 1:
						this.labelA.setIcon(banana3Card);
					break;
					
					case 2:
						this.labelA.setIcon(lime3Card);
					break;
					
					case 3:
						this.labelA.setIcon(strawberry3Card);
					break;
					
					case 4:
						this.labelA.setIcon(plum3Card);
					break;
				}
			break;
			
			case 4:
				switch(boardInfo.getAFront().getFruit())
				{
					case 1:
						this.labelA.setIcon(banana4Card);
					break;
					
					case 2:
						this.labelA.setIcon(lime4Card);
					break;
					
					case 3:
						this.labelA.setIcon(strawberry4Card);
					break;
					
					case 4:
						this.labelA.setIcon(plum4Card);
					break;
				}
			break;
			
			case 5:
				switch(boardInfo.getAFront().getFruit())
				{
					case 1:
						this.labelA.setIcon(banana5Card);
					break;
					
					case 2:
						this.labelA.setIcon(lime5Card);
					break;
					
					case 3:
						this.labelA.setIcon(strawberry5Card);
					break;
					
					case 4:
						this.labelA.setIcon(plum5Card);
					break;
				}
			break;
		}
		
		// B 카드 업데이트 
		switch(boardInfo.getBFront().getNumber())
		{
			case 0:
				this.labelB.setIcon(blankCardImg);
			break;
		
			case 1:
				switch(boardInfo.getBFront().getFruit())
				{
					case 1:
						this.labelB.setIcon(banana1Card);
					break;
					
					case 2:
						this.labelB.setIcon(lime1Card);
					break;
					
					case 3:
						this.labelB.setIcon(strawberry1Card);
					break;
					
					case 4:
						this.labelB.setIcon(plum1Card);
					break;
				}
			break;
			
			case 2:
				switch(boardInfo.getBFront().getFruit())
				{	
					case 1:
						this.labelB.setIcon(banana2Card);
					break;
					
					case 2:
						this.labelB.setIcon(lime2Card);
					break;
					
					case 3:
						this.labelB.setIcon(strawberry2Card);
					break;
					
					case 4:
						this.labelB.setIcon(plum2Card);
					break;
				}
			break;
			
			case 3:
				switch(boardInfo.getBFront().getFruit())
				{
					case 1:
						this.labelB.setIcon(banana3Card);
					break;
					
					case 2:
						this.labelB.setIcon(lime3Card);
					break;
					
					case 3:
						this.labelB.setIcon(strawberry3Card);
					break;
					
					case 4:
						this.labelB.setIcon(plum3Card);
					break;
				}
			break;
			
			case 4:
				switch(boardInfo.getBFront().getFruit())
				{
					case 1:
						this.labelB.setIcon(banana4Card);
					break;
					
					case 2:
						this.labelB.setIcon(lime4Card);
					break;
					
					case 3:
						this.labelB.setIcon(strawberry4Card);
					break;
					
					case 4:
						this.labelB.setIcon(plum4Card);
					break;
				}
			break;
			
			case 5:
				switch(boardInfo.getBFront().getFruit())
				{
					case 1:
						this.labelB.setIcon(banana5Card);
					break;
					
					case 2:
						this.labelB.setIcon(lime5Card);
					break;
					
					case 3:
						this.labelB.setIcon(strawberry5Card);
					break;
					
					case 4:
						this.labelB.setIcon(plum5Card);
					break;
				}
			break;
		}
		
		// C 카드 업데이트 
		switch(boardInfo.getCFront().getNumber())
		{
			case 0:
				this.labelC.setIcon(blankCardImg);
			break;
				
			case 1:
				switch(boardInfo.getCFront().getFruit())
				{
					case 1:
						this.labelC.setIcon(banana1Card);
					break;
							
					case 2:
						this.labelC.setIcon(lime1Card);
					break;
							
					case 3:
						this.labelC.setIcon(strawberry1Card);
					break;
							
					case 4:
						this.labelC.setIcon(plum1Card);
					break;
				}
			break;
					
					case 2:
						switch(boardInfo.getCFront().getFruit())
						{	
							case 1:
								this.labelC.setIcon(banana2Card);
							break;
							
							case 2:
								this.labelC.setIcon(lime2Card);
							break;
							
							case 3:
								this.labelC.setIcon(strawberry2Card);
							break;
							
							case 4:
								this.labelC.setIcon(plum2Card);
							break;
						}
					break;
					
					case 3:
						switch(boardInfo.getCFront().getFruit())
						{
							case 1:
								this.labelC.setIcon(banana3Card);
							break;
							
							case 2:
								this.labelC.setIcon(lime3Card);
							break;
							
							case 3:
								this.labelC.setIcon(strawberry3Card);
							break;
							
							case 4:
								this.labelC.setIcon(plum3Card);
							break;
						}
					break;
					
					case 4:
						switch(boardInfo.getCFront().getFruit())
						{
							case 1:
								this.labelC.setIcon(banana4Card);
							break;
							
							case 2:
								this.labelC.setIcon(lime4Card);
							break;
							
							case 3:
								this.labelC.setIcon(strawberry4Card);
							break;
							
							case 4:
								this.labelC.setIcon(plum4Card);
							break;
						}
					break;
					
					case 5:
						switch(boardInfo.getCFront().getFruit())
						{
							case 1:
								this.labelC.setIcon(banana5Card);
							break;
							
							case 2:
								this.labelC.setIcon(lime5Card);
							break;
							
							case 3:
								this.labelC.setIcon(strawberry5Card);
							break;
							
							case 4:
								this.labelC.setIcon(plum5Card);
							break;
						}
					break;
				}
		

		// D 카드 업데이트 
		switch(boardInfo.getDFront().getNumber())
		{
			case 0:
				this.labelD.setIcon(blankCardImg);
			break;
				
			case 1:
				switch(boardInfo.getDFront().getFruit())
				{
					case 1:
						this.labelD.setIcon(banana1Card);
					break;
							
					case 2:
						this.labelD.setIcon(lime1Card);
					break;
							
					case 3:
						this.labelD.setIcon(strawberry1Card);
					break;
							
					case 4:
						this.labelD.setIcon(plum1Card);
					break;
				}
			break;
					
					case 2:
						switch(boardInfo.getDFront().getFruit())
						{	
							case 1:
								this.labelD.setIcon(banana2Card);
							break;
							
							case 2:
								this.labelD.setIcon(lime2Card);
							break;
							
							case 3:
								this.labelD.setIcon(strawberry2Card);
							break;
							
							case 4:
								this.labelD.setIcon(plum2Card);
							break;
						}
					break;
					
					case 3:
						switch(boardInfo.getDFront().getFruit())
						{
							case 1:
								this.labelD.setIcon(banana3Card);
							break;
							
							case 2:
								this.labelD.setIcon(lime3Card);
							break;
							
							case 3:
								this.labelD.setIcon(strawberry3Card);
							break;
							
							case 4:
								this.labelD.setIcon(plum3Card);
							break;
						}
					break;
					
					case 4:
						switch(boardInfo.getDFront().getFruit())
						{
							case 1:
								this.labelD.setIcon(banana4Card);
							break;
							
							case 2:
								this.labelD.setIcon(lime4Card);
							break;
							
							case 3:
								this.labelD.setIcon(strawberry4Card);
							break;
							
							case 4:
								this.labelD.setIcon(plum4Card);
							break;
						}
					break;
					
					case 5:
						switch(boardInfo.getDFront().getFruit())
						{
							case 1:
								this.labelD.setIcon(banana5Card);
							break;
							
							case 2:
								this.labelD.setIcon(lime5Card);
							break;
							
							case 3:
								this.labelD.setIcon(strawberry5Card);
							break;
							
							case 4:
								this.labelD.setIcon(plum5Card);
							break;
						}
					break;
				}
		
	}

	class Close extends WindowAdapter
	{
		ReplayWindow rpw = null;
		Close(ReplayWindow rpw) {this.rpw = rpw;}
		
        @Override
        public void windowClosing(WindowEvent e) 
        {
        	if (ShowMessage.confirm("복기 종료", "복기 모드를 종료하시겠습니까?"))
        	{
        		rpw.setVisible(false);
        		rpw.clear();
        		rpw.backwordButton.setEnabled(false);
				rpw.chatWindow.setVisible(true);
				rpw.lobbyWindow.setVisible(true);
        	}
        	else
        	{
        		rpw.setVisible(true);
        	}
        }
	}
	
	public static void main(String[] args)
	{
		ReplayWindow.getInstance().initRepalyMode("myGames/20231213183734.hglog");
	}
}
