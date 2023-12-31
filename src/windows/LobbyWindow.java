package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Client.Start;
import form.ChatForm;
import form.LoginReplyForm;
import form.LoginRequestForm;
import hash.SHA256;
import image.Blob;
import image.PicResize;
import login.LoginRequest;
import parse.EndsWithImg;
import socket.SendObject;
import swing.ShowMessage;


public class LobbyWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static LobbyWindow single_instance = null;
	public static LobbyWindow getInstance()
	{
		if (single_instance == null) single_instance = new LobbyWindow();
		return single_instance;
	}
	
	private JLabel roomNameLabel = null;
	private JTextField roomNameInput = null;
	private JButton searchButton = null;
	private JButton refreshButton = null;
	private JButton makeroomButton = null;
	private JButton ratingButton = null;
	private JTable roomTable = null;
	private JScrollPane roomScrollPane = null;
	private DefaultTableModel tableModel = null;
	private JButton replayButton = null;
	
	private JFileChooser picChooser = null;
	
	// 전환할 창 설정 
	private LoginWindow loginWindow = null;
	private GameRoomWindow gameRoomWindow = null;
	private GameBoardWindow gameBoardWindow = null;
	private ChatWindow chatWindow = null;
	private UserInfoWindow userInfoWindow = null;
	private RatingWindow ratingWindow = null;
	private SpectWindow spectWindow = null;
	private ReplayWindow replayWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	public void setGameRoomWindow(GameRoomWindow grw) {this.gameRoomWindow = grw;}
	public void setGameBoardWindow(GameBoardWindow gbw) {this.gameBoardWindow = gbw;}
	public void setChatWindow(ChatWindow cw) {this.chatWindow = cw;}
	public void setUserInfoWindow(UserInfoWindow uifw) {this.userInfoWindow = uifw;}
	public void setRatingWindow(RatingWindow rtw) {this.ratingWindow = rtw;}
	public void setSpectWindow(SpectWindow spw) {this.spectWindow = spw;}
	public void setReplayWindow(ReplayWindow rpw) {this.replayWindow = rpw;}
	
	private String[] tableHeader = {"방 이름", "방장", "게임중 여부"};

	String[][] lobInfo = {};
	String[][] searchInfo = {};
	
	
	private LobbyWindow()
	{
		// 기본적인 창 설정 
		setTitle("로비");
		setResizable(false);
		setSize(550, 400);
		setLayout(null);
		setLocationRelativeTo(null);
		
		// 창 닫았을때 이벤트 리스너 
		//this.addWindowListener(); -> 로그아웃 하시겠습니까 메시지창. 
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		picChooser = new JFileChooser();
		picChooser.setFileFilter(new FileNameExtensionFilter("할리갈리 로그 파일", "hglog"));
		picChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		// 컴포넌트 생성하기
		roomNameLabel = new JLabel("방 이름 : ");
		roomNameInput = new JTextField(20);
		searchButton = new JButton("검색");
		refreshButton = new JButton("새로고침");
		makeroomButton = new JButton("방 만들기");
		ratingButton = new JButton("순위 보기");
		tableModel = new DefaultTableModel(lobInfo, tableHeader);
		roomTable= new JTable(tableModel) // 항목의 수정 가능 여부 관련 Override 
				{
					@Override
					public boolean isCellEditable(int row, int column) {return false;}
				};
		roomTable.getColumn("방 이름").setPreferredWidth(250);
		roomTable.getColumn("방장").setPreferredWidth(50);
		roomTable.getColumn("게임중 여부").setPreferredWidth(20);
		roomScrollPane = new JScrollPane(roomTable);
		replayButton = new JButton("복기");
		
		// 컴포넌트 바운딩 
		roomNameLabel.setBounds(115, 14, 50, 20);
		roomNameInput.setBounds(165, 14, 180, 20);
		searchButton.setBounds(350, 14, 50, 20);
		refreshButton.setBounds(5, 10, 70, 30);
		makeroomButton.setBounds(470, 10, 75, 30);
		ratingButton.setBounds(402, 10, 75, 30);
		roomScrollPane.setBounds(10, 50, 530, 310);
		replayButton.setBounds(80, 10, 30, 30);
		
		// 이벤트 처리 
		refreshButton.addActionListener(new RefreshLobby(this));
		makeroomButton.addActionListener(new ShowGameRoomWindow(this));
		searchButton.addActionListener(new SearchRoom(this));
		roomNameInput.addKeyListener(new SearchRoom(this));		
		ratingButton.addActionListener(new ShowRating(this));
		roomTable.addMouseListener(new JoinGame(this));
		replayButton.addActionListener(new Replay(this));
		
		// 컴포넌트 add 
		add(roomNameLabel);
		add(roomNameInput);
		add(searchButton);
		add(refreshButton);
		add(makeroomButton);
		add(ratingButton);
		add(roomScrollPane);
		add(replayButton);
		
		
		// 창 생성시 visible 여부 
		setVisible(false);
		
	}
	
	public void refresh()
	{
		LoginRequestForm toSend = new LoginRequestForm();
		toSend.setReqType(8);
		toSend.setId(Start.myId);
		LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
		if (received.getResult() == true)
		{
			tableModel.setRowCount(0);
			this.roomNameInput.setText("");
			//this.searchButton.setEnabled(false);
			this.lobInfo = received.getSearchResult();
			for (int i=0; i<lobInfo.length; i++)
			{
				tableModel.addRow(this.lobInfo[i]);
			}
		}
		else
		{
			ShowMessage.warning("오류", "로비 정보를 불러올 수 없습니다.");
		}
	}
	
	private void search(String search)
	{
		tableModel.setRowCount(0);
		this.searchInfo = roomFilter(lobInfo, search);
		for (int i=0; i<searchInfo.length; i++)
		{
			tableModel.addRow(this.searchInfo[i]);
		}
	}
	
	// search() 구현을 위해
    private static String[][] roomFilter(String[][] array, String target) 
    {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] subArray : array) {if (subArray.length > 0 && subArray[0].contains(target)) {filteredList.add(subArray);}}
        // 리스트 배열로
        String[][] filteredArray = new String[filteredList.size()][];
        filteredArray = filteredList.toArray(filteredArray);
        return filteredArray;
    }
    
    class Replay implements ActionListener
    {
    	LobbyWindow lbw = null;
    	Replay(LobbyWindow lbw) {this.lbw = lbw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (lbw.picChooser.showOpenDialog(null) == 0)
			{
				lbw.replayWindow.initRepalyMode(lbw.picChooser.getSelectedFile().getAbsolutePath());
				lbw.chatWindow.setVisible(false);
				lbw.setVisible(false);
			}
		}
    	
    }
	
    class JoinGame extends MouseAdapter
    {
		LobbyWindow lbw = null;
		JoinGame(LobbyWindow lbw) {this.lbw = lbw;}
		
    	@Override
    	public void mouseClicked(MouseEvent e) 
    	{
            if (e.getClickCount() == 2) 
            {
                int row = lbw.roomTable.rowAtPoint(e.getPoint()); 
                int col = lbw.roomTable.columnAtPoint(e.getPoint());

                // 클릭한 셀의 값(방장 id, 즉 방 id) 받아오기 
                String clickedValue = (String) lbw.roomTable.getValueAt(row, 1);
                
                if (col == 0)
                {
                	if (ShowMessage.confirm("게임 입장", clickedValue + "의 게임에 입장하시겠습니까?"))
                	{
		                // 입장 요청 보내기 
		    			LoginRequestForm toSend = new LoginRequestForm();
		    			toSend.setReqType(9);
		    			toSend.setId(Start.myId);
		    			toSend.setNickname(Start.myNickname);
		    			toSend.setRoomName(clickedValue);
		    			LoginReplyForm received = LoginRequest.toServer_getObj(toSend);
		    			if (received.getResult())
		    			{
		    				// 로그인 성공 -> 로비 띄우고 새로고침, 로그인 창 초기화하고 닫기
		    				Start.roomId = clickedValue;
		    				// ShowMessage.information("게임 입장", clickedValue + "의 게임에 입장했습니다.");
		    				lbw.gameBoardWindow.setMyInfo();
		    				lbw.gameBoardWindow.setOpInfo(clickedValue);
		    				// 여기에 서버에가 나의 방 코드가 바뀌었음을 알려주는 메시지 하나 전달하기.
							ChatForm getInRoom = new ChatForm(3, Start.roomId, Start.myId, Start.myNickname, "");
							SendObject.withSocket(Start.connSocket, getInRoom);
							lbw.gameBoardWindow.gameLog.setText("");
							lbw.setVisible(false);
							lbw.chatWindow.setVisible(false);
							lbw.gameBoardWindow.gameLog.setText("");
							lbw.gameBoardWindow.gameLog.append("< 게임이 시작되었습니다! >\n");
							lbw.gameBoardWindow.gameLog.append("[" + clickedValue + "] 의 턴...\n\n");
		    				lbw.gameBoardWindow.setVisible(true);
		    			}
		    			else
		    			{
		    				// 로그인 실패 
		    				ShowMessage.warning("방 입장 실패", "더 이상 존재하지 않는 게임이거나, 이미 진행 중인 게임입니다.");
		    				lbw.refresh();
		    			}
                	}
                }
                else if (col == 1)
                {
                	// 방장 정보 불러오기. 
                	lbw.userInfoWindow.loadInfo(clickedValue);
                }
                else if (col == 2)
                {
                	if (ShowMessage.confirm("관전 입장", clickedValue + "의 게임을 관전하시겠습니까?"))
                	{
                		// 관전 들어가기. 
                		lbw.spectWindow.initSpect(clickedValue);
                	}
                }
            }
        }
    }
    
	class RefreshLobby implements ActionListener
	{
		LobbyWindow lbw = null;
		RefreshLobby(LobbyWindow lbw) {this.lbw = lbw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			lbw.refresh();
		}
		
	}
	
	class SearchRoom implements ActionListener, KeyListener
	{
		LobbyWindow lbw = null;
		SearchRoom(LobbyWindow lbw) {this.lbw = lbw;}

		private void searchInfo()
		{
			if (lbw.roomNameInput.getText().equals("") || lbw.roomNameInput.getText() == null)
			{
				lbw.refresh();
			}
			else
			{
				search(lbw.roomNameInput.getText());
			}
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			searchInfo();
		}

		@Override
		public void keyTyped(KeyEvent e) 
		{
		}

		@Override
		public void keyPressed(KeyEvent e) 
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER)
				searchInfo();
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
		}
		
	}
	
	class ShowGameRoomWindow implements ActionListener
	{
		LobbyWindow lbw = null;
		ShowGameRoomWindow(LobbyWindow lbw) {this.lbw = lbw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			lbw.gameRoomWindow.setVisible(true);
		}
	}
	
	class ShowRating implements ActionListener
	{
		LobbyWindow lbw = null;
		ShowRating(LobbyWindow lbw) {this.lbw = lbw;}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			lbw.ratingWindow.refresh();
			lbw.ratingWindow.setVisible(true);
		}
		
	}
}
