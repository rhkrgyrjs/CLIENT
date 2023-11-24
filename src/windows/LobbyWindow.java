package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import form.LoginReplyForm;
import form.LoginRequestForm;
import login.LoginRequest;
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
	private JTable roomTable = null;
	private JScrollPane roomScrollPane = null;
	private DefaultTableModel tableModel = null;
	
	// 전환할 창 설정 
	private LoginWindow loginWindow = null;
	private GameRoomWindow gameRoomWindow = null;
	// private GameWindow gameWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	public void setGameRoomWindow(GameRoomWindow grw) {this.gameRoomWindow = grw;}
	
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
		
		// 컴포넌트 생성하기
		roomNameLabel = new JLabel("방 이름 : ");
		roomNameInput = new JTextField(20);
		searchButton = new JButton("검색");
		refreshButton = new JButton("새로고침");
		makeroomButton = new JButton("방 만들기");
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
		
		// 컴포넌트 바운딩 
		roomNameLabel.setBounds(115, 14, 50, 20);
		roomNameInput.setBounds(165, 14, 200, 20);
		searchButton.setBounds(370, 14, 50, 20);
		refreshButton.setBounds(5, 10, 70, 30);
		makeroomButton.setBounds(470, 10, 75, 30);
		roomScrollPane.setBounds(10, 50, 530, 310);
		
		// 이벤트 처리 
		refreshButton.addActionListener(new RefreshLobby(this));
		makeroomButton.addActionListener(new ShowGameRoomWindow(this));
		searchButton.addActionListener(new SearchRoom(this));
		roomNameInput.addKeyListener(new SearchRoom(this));		
		roomTable.addMouseListener(new JoinGame(this));
		
		// 컴포넌트 add 
		add(roomNameLabel);
		add(roomNameInput);
		add(searchButton);
		add(refreshButton);
		add(makeroomButton);
		add(roomScrollPane);
		
		
		// 창 생성시 visible 여부 
		setVisible(false);
		
	}
	
	public void refresh()
	{
		LoginRequestForm toSend = new LoginRequestForm();
		toSend.setReqType(8);
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

                // 클릭한 셀의 값을 출력
                String clickedValue = (String) lbw.roomTable.getValueAt(row, 1);
                // 테스트용 메시지박스 출력 
                ShowMessage.information("테스트용", "클릭한 방의 방장ID : " + clickedValue);
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
}
