package com.supermap.smIM;

public class IMApp {
	public static void main(String args[]) {

		if( args.length < 2 ){
			System.out.println( "缺失参数，参数格式为： 用户名  群组(频道)名" );   // 
			return ;			
		}

		String strMQServer="";//"PC.Java";
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
		
		smbWorker.run();
	}	
}
