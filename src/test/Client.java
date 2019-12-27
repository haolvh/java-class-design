package test;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class Client extends JFrame implements ActionListener {
	private JFrame frame;
	private JPanel jPanel = new JPanel();
	private JTextArea txtSend;
	private JTextArea txtMsg;
	private JButton btnSend, btnHistory, btnFsend, btnFsend1;
	private JList lUser;
	private Vector<String> list;
	private DataOutputStream outputStream;
	private Socket socket;
	private String userName;
	private Parameterx parameterx;

	public Client(Socket socket, String name) {
		this.userName = name;
		this.socket = socket;
		setBounds(200, 300, 800, 600);
		setLayout(new BorderLayout());
		setTitle(name + "的考勤终端");
		init();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public void init() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("resources/kaoqin.jpg"));
		txtSend = new JTextArea(4, 10);
		txtMsg = new JTextArea();
		txtMsg.setFont(new Font("宋体", Font.PLAIN, 17));
		txtMsg.setEditable(false);
		btnSend = new JButton("发送信息");
		btnFsend = new JButton("选择文件");
		btnFsend1 = new JButton("提交文件");
		jPanel.setLayout(new FlowLayout());
		jPanel.add(btnSend);
		jPanel.add(btnFsend);
		jPanel.add(btnFsend1);
		btnHistory = new JButton("消息日志");
		btnHistory.setBackground(Color.orange);
		btnHistory.addActionListener(this);
		btnSend.addActionListener(this);
		btnFsend1.addActionListener(this);
		btnFsend.addActionListener(this);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int result = JOptionPane.showConfirmDialog(frame, "请确认本次实验是否全部做完并已提交给老师", "确认退出", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result == JOptionPane.OK_OPTION) {
					System.exit(0);
				} else if (result == JOptionPane.NO_OPTION) {
					frame.setVisible(true);
				}
			}
		});
		JScrollPane jspSend = new JScrollPane(txtSend);
		jspSend.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		JPanel p1 = new JPanel(new BorderLayout());
		p1.setBorder(new TitledBorder("对话框"));
		JPanel pp = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		pp.add(btnHistory);
		p1.add(pp, "North");
		p1.add(jspSend, "Center");
		p1.add(jPanel, "South");


		list = new Vector<String>();
		list.add(userName);
		lUser = new JList(list);
		JScrollPane left = new JScrollPane(lUser);
		left.setBorder(new TitledBorder("在线用户"));
		left.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JScrollPane right = new JScrollPane(txtMsg);
		right.setBorder(new TitledBorder("消息框"));
		right.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JSplitPane p2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);
		p2.setDividerLocation(100);


		add(p2, "Center");
		add(p1, "South");

		parameterx = new Parameterx();

		try {
			outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF("renew|" + userName);
			System.out.println(userName);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new Thread(new ClientThread(socket, txtMsg, list, txtSend, lUser)).start();
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnHistory) {
			//this.dispose();
			new ChatHistory();
		} else if (e.getSource() == btnSend) {
			String strSend = txtSend.getText().trim();
			if (strSend.equals("")) {
				JOptionPane.showMessageDialog(null, parameterx.nomessage);
			} else {
				try {
					outputStream = new DataOutputStream(socket.getOutputStream());
					outputStream.writeUTF("message| " + strSend + " | " + userName);
					outputStream.flush();

				} catch (IOException ef) {
					// TODO: handle exception
				}
			}
		} else if (e.getSource() == btnFsend) {
			JFileChooser jFileChooser = new JFileChooser("C:\\Users\\name\\Desktop");
			int i = jFileChooser.showOpenDialog(frame);

			if (i == JFileChooser.APPROVE_OPTION) {
				File f1 = jFileChooser.getSelectedFile();
				txtSend.setText(f1.getAbsolutePath());
			}

		} else if (e.getSource() == btnFsend1) {
			String strSend = txtSend.getText().trim();
			System.out.println(strSend);
			File file = new File(strSend);
			if (strSend.equals("")) {
				JOptionPane.showMessageDialog(null, parameterx.nomessage);
			} else {
				try {
					Socket socket = new Socket("127.0.0.1", 8086);
					BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintStream ps = new PrintStream(socket.getOutputStream());
					ps.write((file.getName() + "\n").getBytes());

					//如果不存在, 定义FileInputStream读取文件, 写出到网络
					FileInputStream fis = new FileInputStream(file);

					byte[] buffer = new byte[1024];
					while (fis.read(buffer) != -1) {
						ps.write(buffer);
					}
					fis.close();
					ps.close();

					System.out.println("上传完毕");
					txtSend.setText("");

				}catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
	}
}
