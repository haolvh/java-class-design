package test;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JTextArea;

public class ClientThread implements Runnable{
	private Vector<String> uList;
	private Socket socket;
	private JTextArea txtMsg, txtSend;
	private String strmsg;
	private StringTokenizer tokenizer;
	private Parameterx parameterx;
	private JList lUser;
	public ClientThread(Socket socket, JTextArea area, Vector<String> list, JTextArea txtSend, JList lUser){
		this.socket = socket;
		this.txtMsg = area;
		this.uList = list;
		this.txtSend = txtSend;
		this.parameterx = new Parameterx();
		this.lUser = lUser;
		uList = new Vector<String>();
	}
	public void run(){
		try{
			while(true){
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
				strmsg = dataInputStream.readUTF();
				tokenizer = new StringTokenizer(strmsg, "|");
				String head = tokenizer.nextToken();
				
				if(head.equalsIgnoreCase("message")){
					String data = tokenizer.nextToken();
					String userName = tokenizer.nextToken();
					String msg = tokenizer.nextToken();
					data += (userName + msg);
					txtMsg.append(data + "\r\n");
					txtSend.setText("");
					
					String fName = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
					File file = new File(parameterx.path + "\\" + fName + ".txt");
					BufferedWriter bWriter = new BufferedWriter(new FileWriter(file, true));
					bWriter.write(data);
					bWriter.newLine();
					bWriter.flush();
					bWriter.close();		
				}
				else if(head.equalsIgnoreCase("renew")){
					head = tokenizer.nextToken();
					int length = Integer.parseInt(head);
					uList = new Vector<String>();
					for(int ad = 0; ad < length; ad++){
						head = tokenizer.nextToken();
						uList.add(head);
					}
					lUser.setListData(uList);
				}						
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
