package test;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;

public class Server extends JFrame implements ActionListener{
	private JFrame jFrame = new JFrame();
	private JTextArea txtMsg;
	private ServerSocket server;
	private Socket socket;
	private Parameterx p;
	private JLabel l1,l2,l3;
	private JPanel jPanel = new JPanel();
	private JScrollPane jScrollPane = new JScrollPane();
	private JTextField txtip,txtport,txtpeople;
	private JButton btnStart;
	ImageIcon icon = new ImageIcon("resources/退出.png");
	private JButton btn_exit = new JButton(icon);
	private ArrayList<Socket> list = new ArrayList<Socket>();
	private Vector<String> userList;
	private boolean flgStart = false;
	public Server(){
		setLayout(new FlowLayout());
		setBounds(50, 50, 500, 400);
		setTitle("考勤系统服务器");
		userList = new Vector<String>();
		add(jPanel);
		init();
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/kaoqin.jpg"));
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		run();
	}
	public void run(){
		try {
			int mk = p.port;
			server = new ServerSocket(mk);
			while(true){
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while(flgStart){
					socket = server.accept();
					list.add(socket);
					new Thread(new ServerThread(socket, list, txtMsg,txtpeople,userList)).start();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void init(){
		txtMsg = new JTextArea(16,35);
		/*jPanel.add(txtMsg);*/
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jScrollPane.setViewportView(txtMsg);
		jPanel.add(jScrollPane);
		txtMsg.setEditable(false);
		btnStart = new JButton("启动服务器");
		l1 = new JLabel("ip:");
		l2 = new JLabel("port:");
		l3 = new JLabel("在线人数:");
		txtip = new JTextField(6);
		txtport = new JTextField(5);
		txtpeople = new JTextField(5);
		p = new Parameterx();
		txtip.setText(p.ip);
		txtport.setText(p.port + "");
		txtip.setEditable(false);
		txtport.setEditable(false);
		txtpeople.setEditable(false);
		txtpeople.setText("0");
		btnStart.addActionListener(this);
		Box boxTop = Box.createHorizontalBox();
		boxTop.add(l1);
		boxTop.add(Box.createHorizontalStrut(5));
		boxTop.add(txtip);
		boxTop.add(Box.createHorizontalStrut(10));
		boxTop.add(l2);
		boxTop.add(Box.createHorizontalStrut(5));
		boxTop.add(txtport);
		boxTop.add(Box.createHorizontalStrut(5));
		boxTop.add(l3);
		boxTop.add(Box.createHorizontalStrut(3));
		boxTop.add(txtpeople);
		boxTop.add(Box.createHorizontalStrut(5));
		boxTop.add(btnStart);
		boxTop.add(Box.createHorizontalStrut(5));
		boxTop.add(btn_exit);
		add(boxTop);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int result = JOptionPane.showConfirmDialog(jFrame,"确认关闭服务器?","确认退出",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(result==JOptionPane.OK_OPTION){
					String f = txtMsg.getText();
					String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
					File f1 = new File("C://Users//Lh//Desktop//"+time+".txt");
					try {
						FileWriter fw = new FileWriter(f1);
						fw.write(f);
						fw.close();

					} catch (IOException ex) {
						ex.printStackTrace();
					}


					System.exit(0);
				}else if(result==JOptionPane.NO_OPTION){
					jFrame.setVisible(true);
				}
			}
		});
		btn_exit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				int result = JOptionPane.showConfirmDialog(jFrame,"                          确认关闭服务器?\n 本次实验记录将以文本文档的形式保存在桌面","确认退出",JOptionPane.YES_NO_OPTION,JOptionPane.PLAIN_MESSAGE);
				if(result==JOptionPane.OK_OPTION){
					String f = txtMsg.getText();
					String time = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
					File f1 = new File("C://Users//Lh//Desktop//"+time+".txt");
					try {
						FileWriter fw = new FileWriter(f1);
						fw.write(f);
						fw.close();

					} catch (IOException ex) {
						ex.printStackTrace();
					}
					System.exit(0);
				}else if(result==JOptionPane.NO_OPTION){
					jFrame.setVisible(true);
				}
			}
		});

	}
	
	public void actionPerformed(ActionEvent e){
		flgStart = true;
		btnStart.setEnabled(false);
		txtMsg.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" 启动服务器\r\n");
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
