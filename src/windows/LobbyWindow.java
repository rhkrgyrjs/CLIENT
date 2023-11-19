package windows;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;


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
	
	// 전환할 창 설정 
	private LoginWindow loginWindow = null;
	// private GameWindow gameWindow = null;
	
	public void setLoginWindow(LoginWindow lw) {this.loginWindow = lw;}
	
	private String[] tableHeader = {"방 이름", "방장", "인원", "게임중 여부"};

	String[][] fortest = {
			{"초보만", "kovox@zoo@1234", "1/4", "게임중 아님"},
			{"고수만", "zop@123212", "4/4", "게임중"}
	};
	
	
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
		roomTable= new JTable(fortest, tableHeader) // 항목의 수정 가능 여부 관련 Override 
				{
					@Override
					public boolean isCellEditable(int row, int column) {return false;}
				};
		roomTable.getColumn("방 이름").setPreferredWidth(250);
		roomTable.getColumn("방장").setPreferredWidth(50);
		roomTable.getColumn("인원").setPreferredWidth(5);
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
}
