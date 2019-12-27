package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.*;

public class ServerThread implements Runnable{
	private DataOutputStream outputStream;
	private ArrayList<Socket> list;
	private Socket socket;
	private JTextArea txtMsg;
	private JTextField txtpeople;
	private Vector<String> userList;
	public ServerThread(Socket socket, ArrayList<Socket> list, JTextArea txtMsg, JTextField txtpeople,Vector<String> userList){
		this.socket = socket;
		this.list = list;
		this.txtMsg = txtMsg;
		this.userList = userList;
		this.txtpeople = txtpeople;
	}
	
	public void sendMsg(String msg){
		try{
			for (Socket socket : list) {
				outputStream = new DataOutputStream(socket.getOutputStream());
				outputStream.writeUTF(msg);
				outputStream.flush();
			}
			txtMsg.append(msg + "\r\n");
		}catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void sendRenewMsg() throws IOException{
		String msg = "renew|" + userList.size();
		for(int index = 0; index < userList.size(); index++){
			msg += "|" + userList.get(index).toString() ;
		}
		for (Socket socket : list) {
			
			outputStream = new DataOutputStream(socket.getOutputStream());
			outputStream.writeUTF(msg);
			outputStream.flush();
		}
		String a = String.valueOf(userList.size());
		txtpeople.setText(a);
	}
	public void run(){
		try{
			while(true){
				DataInputStream inputStream = new DataInputStream(socket.getInputStream());
				String str = null;
				
				try {
					str = inputStream.readUTF();
					
					StringTokenizer tokenizer = new StringTokenizer(str, "|");
					
					String head = tokenizer.nextToken();			
					
					if(head.equals("login")){
						String userName1 = tokenizer.nextToken();
						
						boolean exit = false;
						for(int i = 0; i < userList.size(); i++){
							if(userList.get(i).equalsIgnoreCase(userName1)){
								exit = true;
							}
						}
						
						
						if(exit){
							sendMsg("login|false");
							txtMsg.append("用户 " + userName1 + " 于 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" 登录失败\r\n");
						}else{
							userList.add(userName1);
							sendMsg("login|ok");
							
							txtMsg.append("用户 " + userName1 + " 于 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+" 登录成功\r\n");
							String b = String.valueOf(userList.size());
							txtpeople.setText(b);
						}
					}
					if(head.equals("message")){
						String word = tokenizer.nextToken();
						str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) + " " + "|" + tokenizer.nextToken() + "|" + "说:" + word;
						sendMsg("message|" + str);
					}
					if(head.equalsIgnoreCase("renew")){
						sendRenewMsg();
					}
					
				} catch (IOException e) {
					for(int inded = 0;inded < list.size();inded++){
						if(socket == list.get(inded)){
							list.remove(inded);
							if(userList.size()>inded){
								txtMsg.append("用户 " + userList.get(inded) +" 于 "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())+ " 退出登录\r\n");
								userList.remove(inded);
							}
						}
					}
					sendRenewMsg();
					break;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
