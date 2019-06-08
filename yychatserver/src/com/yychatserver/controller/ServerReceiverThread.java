package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;




import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	
	ObjectInputStream ois;
	ObjectOutputStream oos;
	Message mess;
	String sender;
	public ServerReceiverThread(Socket s){//s���뷢�������Ӧ�ķ�����Socket����
		this.s=s;
	}
	public void run(){
		while(true){
		try {
			ois=new ObjectInputStream(s.getInputStream());
		      mess=(Message)ois.readObject();
		      sender=mess.getSender();
		System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵: "+mess.getContent());
		
		
		if(mess.getMessageType().equals(Message.message_Common)){
	Socket s1=(Socket)StartServer.hmSocket.get(mess.getReceiver());
		//oos=new ObjectOutputStream(s1.getOutputStream());
		//oos.writeObject(mess);
		sendMessage(s1,mess);
		}
		
		//�ڶ��������������ܵ�����������ߺ�����Ϣ
				if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
				Set friendSet=StartServer.hmSocket.keySet();
				Iterator it=friendSet.iterator();
				String friendName;
				String friendString=" ";
				while(it.hasNext()){
					friendName=(String)it.next();
					if(!friendName.equals(mess.getSender()));
					friendString=friendName+" "+friendString;
				}
					System.out.println("ȫ�����ѵ����֣�"+friendString);
					
					
					//���ͺ������ֵ��ͻ���
					mess.setContent(friendString);
	                mess.setMessageType(Message.message_OnlineFriend);			
				mess.setSender("Server");
				mess.setReceiver(sender);
				sendMessage(s,mess);
				}
				
		}
	
		
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
	
			e.printStackTrace();
		}
	    
	
	}	
}
      public void sendMessage(Socket s,Message mess) throws IOException {
    	  oos=new ObjectOutputStream(s.getOutputStream());
    	  oos.writeObject(mess);
      }
}