package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Login extends JFrame {
	private boolean flgAction = false;
	private JFrame frame = new JFrame();
	private JPanel panel_title = new JPanel();
	private JPanel panel_up = new JPanel();
	private JPanel panel_bottom = new JPanel();
	private JLabel lbl_username = new JLabel("姓名:");
	private JLabel lbl_passsword = new JLabel("学号:");
	private JPanel panel_small_1 = new JPanel();
	private JPanel panel_small_2 = new JPanel();
	private JLabel title = new JLabel("欢迎使用实验考勤系统（学生端）", JLabel.CENTER);
	private JTextField txtfd_username = new JTextField(20);
	private JTextField pswfd_password = new JTextField(20);
	private JButton btn_login = new JButton("登录");
	private JButton btn_cancel = new JButton("取消");
	private Socket socket;
	private Parameterx parameterx;

	
	public Login(){
		init();
		frame.setSize(400, 300);
		frame.setLayout(new BorderLayout());
		frame.add(panel_title, BorderLayout.NORTH);
		frame.add(panel_up, BorderLayout.CENTER);
		frame.add(panel_bottom, BorderLayout.SOUTH);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/kaoqin.jpg"));
		frame.setTitle("实验考勤系统");


		title.setFont(new Font("宋体", Font.BOLD, 20));
		panel_title.add(title);


		panel_up.setLayout(new GridLayout(2, 2));
		panel_up.add(panel_small_1);
		panel_up.add(panel_small_2);

		panel_small_1.setLayout(new FlowLayout());
		panel_small_1.add(lbl_username);
		panel_small_1.add(txtfd_username);
		panel_small_2.setLayout(new FlowLayout());
		panel_small_2.add(lbl_passsword);
		panel_small_2.add(pswfd_password);

		panel_bottom.setLayout(new FlowLayout());
		btn_login.setBackground(Color.GREEN);
		btn_cancel.setBackground(Color.RED);
		panel_bottom.add(btn_login);
		panel_bottom.add(btn_cancel);

		btn_login.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String user1name = txtfd_username.getText();
				String stuId = pswfd_password.getText();
				if(!user1name.equals("")&&!stuId.equals("")){
					flgAction = true;
				}else{
					JOptionPane.showMessageDialog(null, "请输入正确的用户信息", "错误", JOptionPane.ERROR_MESSAGE);

				}
			}
		});
		btn_cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				txtfd_username.setText("");
				pswfd_password.setText("");
			}
		});

		frame.setVisible(true);
		
		while(true){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(flgAction == true){
				try {
					frame.setVisible(false);
					String username =pswfd_password.getText()+txtfd_username.getText();
					DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
					String login ="login|" + username;
					outputStream.writeUTF(login);
					outputStream.flush();
					flgAction = false;
					DataInputStream inputStream = new DataInputStream(socket.getInputStream());
					String result = inputStream.readUTF();
					
					StringTokenizer tokenizer = new StringTokenizer(result, "|");
					String head = tokenizer.nextToken();
					if(head.equals("login")){
						head = tokenizer.nextToken();
						if(head.equalsIgnoreCase("ok")){
							new Client(socket,username);
							dispose();
						}else if(head.equalsIgnoreCase("false")){
							JOptionPane.showMessageDialog(null, "该用户已在使用中！");
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
		
	}
	public void init(){

		parameterx = new Parameterx();
		int kkl = parameterx.port;
		String klj = parameterx.ip;
		try {
			socket = new Socket(klj, kkl);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void main(String[] args) {
		new Login();
	}
}
