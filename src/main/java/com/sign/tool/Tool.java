package com.sign.tool;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class Tool {

	static Logger log= LoggerFactory.getLogger(Tool.class);

	static int tag=0;

	//post请求
	public static String post(String data, String URL, boolean head, String authorization, Integer[] list) {
		OutputStreamWriter out = null ;
		BufferedReader in = null;
		StringBuilder result = new StringBuilder();
		//做延时，部分网站有cf拦截，所以不能快速请求，容易被抓到，我做了延时

		try {
			URL realUrl = new URL(URL);
			// 打开和URL之间的连接  
			URLConnection conn = realUrl.openConnection();
			//设置通用的请求头属性
			conn.setRequestProperty("accept", "*/*");
			if(head) {
				conn.setRequestProperty("authorization", authorization);
			}
			conn.setConnectTimeout(2000);
			conn.setReadTimeout(5000);
			conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36");
			// 发送POST请求必须设置如下两行   否则会抛异常（java.net.ProtocolException: cannot write to a URLConnection if doOutput=false - call setDoOutput(true)）
			conn.setDoOutput(true);
			conn.setDoInput(true);
			//获取URLConnection对象对应的输出流并开始发送参数
			out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			//添加参数
			if(data!=null) {
				out.write(data);
			}
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result.append(line);
			}
		}catch(IOException e) {
			//判断token是否过期时，token已经过期
			Timer timer = new Timer();
			if(e.getMessage().equals("Server returned HTTP response code: 403 for URL: "+Param.url[list[0]]+Param.getUserInfo)) {
				String url= Param.url[list[0]];
				Tool.tag++;
				if(tag>3){
					//获取不到token异常
					Email.Send("脚本获取token异常",Param.url[list[0]]);
					return null;
				}
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						Param.token.get(list[0]).set(list[1], "Bearer " + JSONObject.parseObject(Tool.post(Param.user.get(list[0]).get(list[1]), url + Param.tokenUrl, false, null, list)).getString("token"));
						timer.cancel();
					}
					//随机时间，为了更像人操作的，毕竟我也不清楚cf判断ip的算法，所以就只能写的尽量不像机器。  延时时间区间为5-10秒
				},(int)(Math.random()*5000)+5000);

			}else if (e.getMessage().equals("Server returned HTTP response code: 403 for URL: "+Param.url[list[0]]+Param.commentUrl)){
				//如果评论重复什么的
				int size= Param.list.get(list[0]).size();
				//防止无限循环
				if(size< Param.number.get(list[0])) {
					if(!("https://mcy.ink/").equals(Param.url[list[0]])){
						Email.Send("帖子评论循环满了",Param.url[list[0]]);
					}
				}else{
                    timer.schedule(new TimerTask() {
						@Override
						public void run() {
							Param.tag.set(list[0],Param.tag.get(list[0])+1);
							if(Param.tag.get(list[0])>=size){
								Param.tag.set(list[0],0);
							}
							Param.number.set(list[0],Param.number.get(list[0])+1);
							Request.commentsReturn(URL, Param.list.get(list[0]).get(Param.tag.get(list[0])), authorization, list);
							timer.cancel();
						}
					},(int)(Math.random()*5000)+5000);

				}
			}else{
				log.error("网站："+Param.url[list[0]]+"   发生了异常，异常为："+e.getMessage());
				Email.Send("脚本异常，当前异常为:"+e.getMessage(),Param.url[list[0]]);
			}
			return null;
		}finally {
			// 使用finally块来关闭输出流、输入流  
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		Tool.tag=0;
		return result.toString();
	}


	//获取当前时间日期格式为yyyy-MM-dd HH-mm-ss
	public static String getTime() {
		SimpleDateFormat sim=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sim.format(new Date());
	}



	//随机获取时间毫秒数   用来在间隔时间进行签到  间隔在10分钟到2小时10分钟之间
	public static long getIntTime(){
		long math=(long) (Math.random()*(1000*60*60*2))+1000*60*10;
        return math;
	}




}
