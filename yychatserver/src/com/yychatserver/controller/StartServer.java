package com.yychatserver.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import com.yychat.model.Message;
import com.yychat.model.User;
public class StartServer {
 ServerSocket ss;
 String userName;
 String passWord;
 
 public StartServer(){
  try{//捕获异常
  ss=new ServerSocket(3456);
  System.out.println("服务器已经启动，监听3456端口");//1024以上的端口
  while(true){//?Thread多线程
	  Socket s=ss.accept();//接收客户端连接请求
	  System.out.println("连接成功:"+s);
	  
	  
	  //接收user对象
	  ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
	  User user=(User)ois.readObject();
	  userName=user.getUserName();
	  passWord=user.getPassWord();
	  System.out.println(userName);
	  System.out.println(passWord);
	  
	  //实现密码验证功能
	  Message mess=new Message();
	  mess.setSender("Server");
	  mess.setReceiver(userName);
	  if(passWord.equals("123456")){//对象
	   //告诉客户端密码验证通过的消息，可以创建一个Massage类
	   mess.setMessageType("1");//"1"为验证通过
	  }else{
	   mess.setMessageType("0");//"0"为验证失败
	  }
	  ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
	  oos.writeObject(mess);
	    
  }
 
 }catch (IOException e){
  e.printStackTrace();
 }catch (ClassNotFoundException e){
  e.printStackTrace();
 }
  }
}