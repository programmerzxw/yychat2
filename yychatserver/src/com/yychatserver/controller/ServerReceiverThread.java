package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{
	Socket s;
	String sender;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	public ServerReceiverThread(Socket s){//s���뷢�������Ӧ�ķ�����Socket
		this.s=s;
		
	}
	public void run(){
		//ObjectInputStream ois;
		//ObjectOutputStream oos;
		Message mess;
		while(true){
			
		try {
			String sender;
			ois = new ObjectInputStream(s.getInputStream());
			mess=(Message)ois.readObject();//����������Ϣ,�߳�����
			sender=mess.getSender();
			System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵"+mess.getContent());
			
			if(mess.getMessageType().equals(Message.message_Common)){
			Socket s1=(Socket)StartServer.hmSocket.get(mess.getReceiver());//�õ�������������Ӧ�ķ�����socket����
			sendMessage(s1,mess);
			}
		
		
		//2.���������յ�������������ߺ�����Ϣ�����ͣ�message_OnlineFriend��
		if(mess.getMessageType().equals(Message.message_RequestOlineFriend)){
			Set friendSet=StartServer.hmSocket.keySet();//��ֵ��
			Iterator it=friendSet.iterator();
			String friendName;
			String friendString=" ";
			while(it.hasNext()){
				friendName=(String)it.next();
				if(!friendName.equals(mess.getSender()))
					
				friendString=friendName+" "+friendString;//Ϊʲô�ÿո�
			}
			System.out.println("ȫ�����ѵ����֣�"+friendString);
		    
			//����ȫ�����ѵ����ֵ��ͻ���
			mess.setContent(friendString);
			mess.setMessageType(Message.message_OnlineFriend);
			mess.setSender("Server");
	        mess.setReceiver(sender);
	        sendMessage(s,mess);
		}
			
		
		}catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
		}
	public void sendMessage(Socket s,Message mess) throws IOException{
		oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);//ת��������Ϣ
	}
	}
	
	
