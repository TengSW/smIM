package com.supermap.smIM;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.supermap.smb.SMBCallback;
import com.supermap.smb.SMBClient;
import com.supermap.smb.SMBMessage;

public class IMWorker {

	public IMWorker() {
		m_Callback = new SMBCallback() {
			@Override
			public void onStatusChanged(int nStatus) 
			{// 指定 接收并处理状态变化的函数
				onSMBStatusChanged(nStatus); 
			}

			@Override
			public void onMessageReceived(SMBMessage smbMsg) 
			{// 指定 接收并处理消息的函数
				Doing(smbMsg);	
			}
		};	
		
		m_SMBClient = new SMBClient();
		m_SMBClient.setCallback(m_Callback); // 设置回调参数
	}
	
	public void setSender( String strSender )
	{// 设置 发送者
		m_strSender = strSender;
	}
	
	public void setInExchange( String strInExchange )
	{// 设置 接收频道
		m_strInExchange = strInExchange;
	}
	
	public void setTargetExchange( String strTargetExchange )
	{// 设置 目的频道
		m_strTargetExchange = strTargetExchange;
	}
	
	public void setMQServer( String strMQServer ){
		if( strMQServer != null && strMQServer.length() > 0 ){
			m_strMQServer = strMQServer;
		}
	}
	
	public void run(){
		System.out.println("检测网络中...");
		//Connect("public", "public");// 目前只能用guest/guest连接
//		Connect("guest", "guest");// 目前只能用guest/guest连接
		Connect("public", "supermapcloud");// 目前只能用guest/guest连接
		
		Chat();
	}
	
	private void Chat() {
		try{ 
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in) );  
		
			String strText = null;
			while( true ){
				if( getStatus() > 0 ){
					System.out.println("请输入要发送的信息:"); 
					strText = in.readLine();
					
					PublishText( strText ); // 发送文字消息
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
	
	private void onSMBStatusChanged(int nStatus) 
	{// 接收并处理状态变化的函数，回传状态值： 0--正在连接中； 1--已经连接成功
		if (nStatus == 0) {
			System.out.println("连接中...");
		} else if (nStatus == 1) {
			System.out.println("已连接！");
		}
	}
	
	private void Doing(SMBMessage smbMsg) 
	{// 接收并处理消息的函数
		if (smbMsg != null) {
			if (smbMsg.strSender != null) { // 发送者
				System.out.print(smbMsg.strSender + ":");
			}
			
			if (smbMsg.strLocation != null) {// 位置信息
				System.out.print("&" + smbMsg.strLocation + "&");
			}
			
			if (smbMsg.strSms != null) {// 文字信息
				System.out.print(smbMsg.strSms);
			}
			System.out.println("");// 换行
		}
	}

	public int getStatus() 
	{// 获取当前状态信息, 0--连接中； 1--已连接
		if (m_SMBClient != null) {
			return m_SMBClient.getStatus();
		} else {
			return 0;
		}
	}

	public void PublishText(String strText)
	{// 发送 文本信息
		if (m_SMBClient != null) {
			m_SMBClient.PublishText(strText);
		}
	}
	
	public void PublishLocation(double x, double y, double z, double dSpeed, double dBearing, int nOption)
    { // 发送 位置信息
        if (m_SMBClient != null)
        {
            m_SMBClient.PublishLocation(x, y, z, dSpeed, dBearing, nOption);
        }
    }	
	
	private boolean Connect(String strUserName, String strPassword) 
	{// 连接消息服务，需要根据回传的状态信息才能判断是否连接成功，参见 onSMBStatusChanged(...)方法
		m_SMBClient.setSender(m_strSender);  		// 发送者
		m_SMBClient.setInExchangeParam( m_strInExchange, "fanout", false, "" );
		m_SMBClient.setOutExchangeParam( m_strInExchange, "fanout", false, "" );
//		m_SMBClient.setOutExchangeParam( "MapDroid", "direct", true, "smGateWay");
		
		m_SMBClient.setTargetExchange(m_strTargetExchange); // 目的频道
		
		m_SMBClient.setExtServer( m_strMQServer );
		return m_SMBClient.Connect(strUserName, strPassword);
	}
	
	private SMBClient 		m_SMBClient = null;
	private SMBCallback 	m_Callback = null;	
	
	private String m_strSender = "";
	private String m_strInExchange="";
	private String m_strTargetExchange="";
	private String m_strMQServer = "1.202.165.40";
}
