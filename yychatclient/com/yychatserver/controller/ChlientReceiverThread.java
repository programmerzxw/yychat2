package com.yychatserver.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.yychat.model.Message;
import com.yychatclient.view.ClientLogin;
import com.yychatclient.view.FriendChat1;
import com.yychatclient.view.FriendList;

public class ChlientReceiverThread  extends Thread{

	private Socket s;
	
	public  ChlientReceiverThread(Socket s){
		this.s=s;
		
	}
	
	public void run(){
		ObjectInputStream ois;
		while(true){
			try{
			ois=new ObjectInputStream(s.getInputStream());
		     Message mess=(Message)ois.readObject();//����������Ϣ���߳�����
		String showMessage=mess.getSender()+"��"+mess.getReceiver()+"˵: "+mess.getContent();
		System.out.println(showMessage);
		
		if(mess.getMessageType().equals(Message.message_Common)){
		//jta.append(showMessage+"\r\n");
		//�ں��ѽ�������ʾ������Ϣ
		//1����εõ������������
		FriendChat1  friendChat1=(FriendChat1)FriendList.hmFriendChat1.get(mess.getReceiver()+"to"+mess.getSender());
		
		
		//2����ʾ��Ϣ
		friendChat1.appendJta(showMessage);
			}
		
		//��������
		if(mess.getMessageType().equals(Message.message_OnlineFriend)){
			System.out.println("���ߺ���"+mess.getContent());
			
			
           FriendList friendList=(FriendList)ClientLogin.hmFriendlist.get(mess.getReceiver());	
		
		friendList.setEnableFriendIcon(mess.getContent());
}
		
		if(mess.getMessageType().equals(Message.message_NewOnlineFriend)){
			System.out.println("���û������ˣ��û���: "+mess.getContent());
			
			FriendList friendList=(FriendList)ClientLogin.hmFriendlist.get(mess.getReceiver());
			System.out.println("FriendList������: "+mess.getReceiver());
			
			friendList.setEnableNewFriendIcon(mess.getContent());
		}
		
	}catch (IOException | ClassNotFoundException e){
		e.printStackTrace();
	}
		}
	}
}
	
	
	
	
	
	
	
		




