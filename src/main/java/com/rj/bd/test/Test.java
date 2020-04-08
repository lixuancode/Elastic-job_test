package com.rj.bd.test;

import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.rj.bd.job.MyJob;
import com.rj.bd.pojo.User;
import com.rj.bd.zk.ZookeeperUtils;

/**
 * @desc 测试elast-job +zookeeper实现分布式调度
 * @author 漓炫
 * @time 2020-04-06
 *
 */
public class Test {
	public static void main(String[] args) {
		//1.创建测试数据
		createData();
		//2.获取zookeeper注册中心
		CoordinatorRegistryCenter registryCenter = ZookeeperUtils.getCoordinatorRegistryCenter();		
		//开启调度
		ZookeeperUtils.startJob(registryCenter);
	}
	
	/**
	 * @desc 创建测试数据
	 */
	public static void createData() {
		for (int i = 0; i < 50; i++) {
			User user = new User("用户"+i, i+"", false);
			MyJob.listUsers.add(user);
		}
	}
}
