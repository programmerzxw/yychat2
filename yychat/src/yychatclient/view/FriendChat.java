//package com.yychatclient.view;
//
//import java.awt.Color;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//
//import javax.swing.*;
//
//import com.yychat.model.Message;
//import com.yychatserver.controller.ClientConnect;
//
//public  class FriendChat extends JFrame implements ActionListener,Runnable{
//      
//	
//
//	//Center����
//	JScrollPane jsp;
//	JTextArea jta;
//	
//	//South����
//	JPanel jp;
//	JTextField jtf;
//	JButton jb;
//	
//	String sender;
//	String receiver;
//	String content;
//	
//	public FriendChat(String sender,String receiver){
//		this.sender=sender;
//		this.receiver=receiver;
//		
//		jta =new JTextArea();//�ı����� 
//		jta.setEditable(false);
//		jta.setForeground(Color.red);
//		jsp =new JScrollPane(jta);
//		this.add(jsp,"Center");
//		
//		jp=new JPanel();
//		jtf=new JTextField(15);
//		jtf.setForeground(Color.red);
//		jb=new JButton("����");
//		jb.addActionListener(this);
//		jp.add(jtf);
//		jp.add(jb);
//		this.add(jp,"South");
//		
//		this.setSize(350,240);
//		this.setTitle(sender+"���ں�"+receiver+"�����������");
//		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setLocationRelativeTo(null);//������ʾ����
//	    this.setVisible(true); 
//	
//	}
//	public static void main(String[] args){
//		FriendChat friendChat=new FriendChat("1","2");
//	}
//
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		if(e.getSource()==jb){
//			jta.append(jtf.getText()+"\r\n");
//	Message mess=new Message();
//		mess.setSender(sender);
//		mess.setReceiver(receiver);
//		mess.setContent(jtf.getText());
//		mess.setMessageType(Message.message_Common);
//		ObjectOutputStream oos;
//		try {
//			oos = new ObjectOutputStream(ClientConnect.s.getOutputStream());
//			oos.writeObject(mess);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		   }
//		}
//      }
//	@Override
//	public void run() {
//		ObjectInputStream ois;
//		while(true){
//			try{
//			ois=new ObjectInputStream(ClientConnect.s.getInputStream());
//		     Message mess=(Message)ois.readObject();//����������Ϣ���߳�����
//		String showMessage=mess.getSender()+"��"+mess.getReceiver()+"˵"+mess.getContent();
//		System.out.println(showMessage);
//		jta.append(showMessage+"\r\n");
//		}catch (IOException | ClassNotFoundException e){
//			e.printStackTrace();
//		}
//		}
//	}
//}
//
//
//	
//	
//		
//	
//
//	
//
