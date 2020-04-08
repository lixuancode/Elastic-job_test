package com.rj.bd.job;

import java.util.ArrayList;
import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.rj.bd.pojo.User;

/**
 * @desc elastic-job 分布式任务调度
 * @author 漓炫
 * @time 2020-04-06
 *
 */
public class MyJob implements SimpleJob{
	//全部用户
	public static List<User> listUsers = new ArrayList<User>();
	//每次获取用户的个数
	public final int userSize = 1;
	
	
	public void execute(ShardingContext shardingContext) {
		//1.获取分片信息
		int shardingItem = shardingContext.getShardingItem();
		System.out.println("分片："+shardingItem);
		
		//2.获取未发送邮件的用户信息
		List<User> users = getEmailSendState(userSize);
		
		//3.发送邮件
		sendEmail(users);
	}


	/**
	 * @desc 发送邮件
	 * @param users
	 */
	private void sendEmail(List<User> users) {
		for (User user : users) {
			if (!user.isFlag()) {//在并发的作用下 可能会造成脏读  需要进行判断
				user.setFlag(true);
				System.out.println(user.getName()+"的提醒邮件已发送！");
				System.out.println("*******************************");
			}
			
		}
		//如果发送失败 需要将原有用户再次添加到listUsers中
	}


	/**
	 * @desc 获取未发送的用户
	 * @param userSize2
	 * @return
	 */
	private List<User> getEmailSendState(int userSize) {
		//存放用户的集合
		List<User> list = new ArrayList<User>();
		for (User user : listUsers) {
			if (list.size()>=userSize) {
				break;
			}
			
			if (!user.isFlag()) {//未发送的用户
				list.add(user);
				System.out.println("获取的用户为："+user.getName());
				//listUsers.remove(user);
			}
		}
		System.out.println("共获取的用户个数为："+list.size());
		return list;
	}

}
