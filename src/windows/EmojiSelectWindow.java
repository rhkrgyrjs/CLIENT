package windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import Client.Start;
import form.ChatForm;
import socket.SendObject;
import windows.SpectWindow.Close;

public class EmojiSelectWindow extends JFrame
{
	// 직렬화 처리 
	private static final long serialVersionUID = 1L;
	
	// 싱글톤 처리 
	private static EmojiSelectWindow single_instance = null;
	public static EmojiSelectWindow getInstance()
	{
		if (single_instance == null) single_instance = new EmojiSelectWindow();
		return single_instance;
	}
	
	JButton smile = null;
	JButton sung = null;
	JButton heye = null;
	JButton trfd = null;
	JButton angry = null;
	JButton curse = null;
	JButton dev = null;
	JButton trw = null;
	
	private EmojiSelectWindow()
	{
		setTitle("이모티콘 전송");
		setResizable(false);
		setSize(210, 140);
		setLayout(null);

		this.addWindowListener(new Close(this));

		smile = new JButton("😀");
		sung = new JButton("😎");
		heye = new JButton("😍");
		trfd = new JButton("😱");
		angry = new JButton("😡");
		curse = new JButton("🤬");
		dev = new JButton("😈");
		trw = new JButton("🤢");
		
		smile.setBounds(5, 5, 50, 50);
		sung.setBounds(55, 5, 50, 50);
		heye.setBounds(105, 5, 50, 50);
		trfd.setBounds(155, 5, 50, 50);
		angry.setBounds(5, 55, 50, 50);
		curse.setBounds(55, 55, 50, 50);
		dev.setBounds(105, 55, 50, 50);
		trw.setBounds(155, 55, 50, 50);
		
		smile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😀");
			}
		});

		sung.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😎");
			}
		});
		
		heye.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😍");
			}
		});

		trfd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😱");
			}
		});

		angry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😡");
			}
		});

		curse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("🤬");
			}
		});

		dev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("😈");
			}
		});
		
		trw.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				sendEmoji("🤢");
			}
		});
		
		add(smile);
		add(sung);
		add(heye);
		add(trfd);
		add(angry);
		add(curse);
		add(dev);
		add(trw);
		
		setVisible(false);
	}
	
	class Close extends WindowAdapter
	{
		EmojiSelectWindow emsw = null;
		Close(EmojiSelectWindow emsw) {this.emsw = emsw;}
		
        @Override
        public void windowClosing(WindowEvent e) 
        {
        	emsw.setVisible(false);
        }
	}
	
	private void sendEmoji(String emoji)
	{
		ChatForm toSend = new ChatForm(1, Start.roomId, Start.myId, Start.myNickname, emoji);
		SendObject.withSocket(Start.connSocket, toSend);
		this.setVisible(false);
	}
	
	public static void main(String[] args)
	{
		EmojiSelectWindow.getInstance().setVisible(true);
	}
	
}
