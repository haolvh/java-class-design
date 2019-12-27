package test;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

public class ChatHistory extends JFrame{

	private JSplitPane jsp;
	private JList jl;
	private JScrollPane leftJP;
	private JScrollPane rightJP;
	private JList friends;
	private JTextArea JTrecord;
	private String txtName;
	private Parameterx parameterx;
	private File file;
	private ArrayList<String> fileName;
public ChatHistory(){
		
		this.setTitle("聊天记录查询");
		init();
		setBounds(100,100,600,400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}
	public void init(){
		parameterx = new Parameterx();
		file = new File(parameterx.path);
		File[] fa = file.listFiles();
		String []data = new String[fa.length];
		int strleng = 0;
		for (File file : fa) {
			if(strleng < fa.length){
				data[strleng] = new String(file.getName());
				strleng++;
			}
		}	
		jsp = new JSplitPane();//底容器
		jsp.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		leftJP = new JScrollPane();//列表的滚动条
		rightJP = new JScrollPane();
		JTrecord = new JTextArea();
		
		jsp.setLeftComponent(leftJP);
		jsp.setRightComponent(rightJP);
		friends = new JList(data);

		leftJP.getViewport().add(friends);
		friends.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					JTrecord.setText("");
					String name = friends.getSelectedValue().toString();
					txtName = parameterx.path + "\\" + name;
					//txtName += ".txt";
					fileReader();
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		rightJP.getViewport().add(JTrecord);
		
		add(jsp);

	}
	public void fileReader(){
		try{
			
			FileReader fr = new FileReader(txtName);
			BufferedReader br = new BufferedReader(fr);
			
			String s;
			while((s = br.readLine())!=null){
				JTrecord.append(s + "\n");
			}
			br.close();
		}catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(this, "找不到聊天记录");
			return ;
		}catch(IOException e){
			JOptionPane.showMessageDialog(this, "读文件出错");
			return ;
		}
	}
}
