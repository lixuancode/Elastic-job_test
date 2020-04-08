package com.rj.bd.zk;
/**
 * @desc Zookeeper工具类
 * @author 漓炫
 * @time 2020-04-06
 *
 */

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.rj.bd.job.MyJob;

public class ZookeeperUtils {
	//zookeeper端口
	private static final int ZOOKEEPER_PORT = 2181;
	
	//zookeeper ip
	private static final String ZOOKEEPER_IP = "127.0.0.1";
	
	//zookeeper space(命名空间)
	private static final String ZOOKEEPER_Space = "elastic-job-job01";
	
	/**
	 * @desc 获取/配置zookeeper注册中心
	 * @return
	 */
	public static CoordinatorRegistryCenter getCoordinatorRegistryCenter() {
		//连接地址
		String server = ZOOKEEPER_IP+":"+ZOOKEEPER_PORT;
		//命名空间
		String namespace = ZOOKEEPER_Space;
		
		System.out.println(server);
		
		//zookeeper的连接配置
		ZookeeperConfiguration zookeeperConfiguration = new ZookeeperConfiguration(server , namespace);
		
		//设置超时时间
		zookeeperConfiguration.setConnectionTimeoutMilliseconds(100);
		
		//创建zookeeper注册中心
		CoordinatorRegistryCenter zookeeperRegistryCenter = new ZookeeperRegistryCenter(zookeeperConfiguration);
		
		//初始化注册中心
		zookeeperRegistryCenter.init();
		
		return zookeeperRegistryCenter;
	}
	
	/**
	 * @desc 配置启动任务
	 * @param center
	 */
	public static void startJob(CoordinatorRegistryCenter center) {		
		//1.获取/创建 任务的执行配置
		//1.1 job名称
		String jobName = "job-01";
		//1.2cron 表达式  设置执行的周期
		String cron = "0/3 * * ? * *";
		//1.3分片数量
		int shardingTotalCount = 2;
		/**
		 * 分片数量  尤为重要  因为分片决定了 一次获取几次任务
		 * 在分布式集群中 多台服务器中，分几片就是几台服务器来运行 
		 * 如仅有一台服务器 但分片大于1  那么这台服务器 一次会执行所有的分片任务
		 * 如分片等于1 但多台服务器运行时 仅其中一台执行  另一台等待这台执行完毕后 执行
		 **/
		JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobName , cron, shardingTotalCount).build();
		
		//2.获取simpleJobConfiguration 创建作业类型配置  需要传入任务的配置（jobCoreConfiguration） 及其任务类的 地址
		SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration, MyJob.class.getCanonicalName());
		
		//3.获取 LiteJobConfiguration 创建 Lite作业配置 传入 SimpleJobConfiguration（作业类型配置）
		LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration.newBuilder(simpleJobConfiguration)
				.overwrite(true)//是否覆盖zookeeper原有的配置
				.build();
		
		//4.启动任务  传入zookeeper注册中心  与LiteJobConfiguration（作业配置）
		JobScheduler jobScheduler = new JobScheduler(center,liteJobConfiguration);
		//初始化启动任务
		jobScheduler.init();
	}

	
}
