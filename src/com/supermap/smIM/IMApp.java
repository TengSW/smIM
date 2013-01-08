package com.supermap.smIM;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class IMApp {
	public static void main(String args[]) {

		if( args.length < 2 ){
			System.out.println( "缺失参数，参数格式为： 用户名  群组(频道)名" );   // 
			return ;			
		}

		String strMQServer="1.202.165.40";//"PC.Java";
		if( args.length > 2 ){
			strMQServer = args[2];
		}
		
		String strName=args[0];
		String strExchange = args[1];
		
		// 消息服务相关的来啦
		IMWorker smbWorker = new IMWorker();
		smbWorker.setSender(strName); 			// 发送者
		smbWorker.setInExchange(strExchange);	 	// 接收频道
		smbWorker.setTargetExchange(strExchange); // 目的频道
		smbWorker.setMQServer( strMQServer );
		
		System.out.println("检测网络中...");
		smbWorker.Connect("public", "public");// 目前只能用guest/guest连接
		try{ 
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in) );  
		
			String strText = null;
			while( true ){
				if( smbWorker.getStatus() > 0 ){
					System.out.println("请输入要发送的信息:"); 
					strText = in.readLine();
					
					smbWorker.PublishText( strText ); // 发送文字消息
					System.out.println( "已发送>--" + strText );  
				}
				else{
					Thread.sleep(20);
				}
			}
		}catch(Exception e) { 
			e.printStackTrace(); 
		}
	}	
}
