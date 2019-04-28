package com.yychatserver.controller;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.naming.spi.DirStateFactory.Result;

import com.yychat.model.Message;
import com.yychat.model.User;
public class StartServer {
	public static HashMap hmSocket=new HashMap<String,Socket>();
 ServerSocket ss;//服务器
 Socket s;
 String userName;
 String passWord;
 Message mess;
 
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
	  

		//使用数据库进行用户身份认证
		//1、加载驱动程序
		Class.forName("com.mysql.jdbc.Driver");
		System.out.println("已经加载了数据库驱动！");
		//2、连接数据库
		String url="jdbc:mysql://127.0.0.1:3306/yychat";
		//中文用户名必须用下面的url
		//String url="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
		String dbUser="root";
		String dbPass="";				
		Connection conn=DriverManager.getConnection(url,dbUser,dbPass);
		
		//3、创建PreparedStatement对象，用来执行SQL语句
		String user_Login_Sql="select * from user where username=? and password=?";
		PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
		ptmt.setString(1, userName);
		ptmt.setString(2, passWord);
		
		//4、执行查询，返回结果集
		ResultSet rs=ptmt.executeQuery();
		
		//5、根据结果集来判断是否能登录
		boolean loginSuccess=rs.next();	
	  
	  //实现密码验证功能
	  mess=new Message();
	  mess.setSender("Server");
	  mess.setReceiver(userName);
	  if(passWord.equals("123456")){//对象
	   //告诉客户端密码验证通过的消息，可以创建一个Massage类
	   mess.setMessageType(Message.message_LoginSuccess);//"1"为验证通过
	  }else{
	   mess.setMessageType(Message.message_LoginFailure);//"0"为验证失败
	  }
	  sendMessage(s, mess);
	  //ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
	  // oos.writeObject(mess);
	   
	  //接收聊天信息，？？不可以，应该新建一个接收线程
	  if(passWord.equals("123456")){
		  //激活新上线用户图标步骤1.在此处把自己登陆成功的消息发送给之前登陆成功的所有用户
		  //构建了要发送的信息
		  mess.setMessageType(Message.message_NewOnlineFriend);//类型
		  mess.setSender("Server");
		  mess.setContent(this.userName);//发送消息的内容
		  
		  //拿到已经在线用户的名字
		  Set onlineFriendSet=hmSocket.keySet();
		  Iterator it=onlineFriendSet.iterator();
		  String friendName;
		  while(it.hasNext()){//向全部在线用户发送新用户上线的消息
			  friendName=(String)it.next();
			  mess.setReceiver(friendName);
			  //向friendName发送消息
			  Socket s1=(Socket)hmSocket.get(friendName);
			  sendMessage(s1,mess);
		  }
		  
		  hmSocket.put(userName,s);
		  new ServerReceiverThread(s).start();//就绪，每一个用户都有一个对应的线程
	  }
	  
  }
 
 }catch (IOException e){
  e.printStackTrace();//处理异常
 }catch (ClassNotFoundException e){
  e.printStackTrace();
 } catch (SQLException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  }

public void sendMessage(Socket s, Message mess) throws IOException{
		ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
}
}