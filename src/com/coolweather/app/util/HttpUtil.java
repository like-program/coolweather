package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil
{
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				HttpURLConnection connection = null;
				try
				{
					URL url = new URL(address);
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET"); //从服务器获取数据
					connection.setConnectTimeout(8000); //连接超时
					connection.setReadTimeout(8000); //读取超时
					InputStream in = connection.getInputStream(); //获取输入流,获取从服务器传来的数据
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder(); //接收数据
					String line; //临时接收
					while((line = reader.readLine()) != null)
					{
						response.append(line);
					}
					if(listener != null)
					{
						//回调onFinish()方法
						listener.onFinish(response.toString());
					}
				}
				catch(Exception e)
				{
					if(listener != null)
					{
						//回调onError()方法
						listener.onError(e);
					}
				}
				finally
				{
					if(connection != null)
					{
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}