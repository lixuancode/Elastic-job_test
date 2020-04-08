package com.rj.bd.pojo;
/**
 * @desc 模拟 elastic-job 分布式 调度
 * @author 漓炫
 * @time 2020-04-06
 *
 */
public class User {
	private String name;//用户的名称
	private String id;//用户的id
	private boolean flag = false;//是否发送提醒邮件
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public User() {
		super();
	}
	public User(String name, String id, boolean flag) {
		super();
		this.name = name;
		this.id = id;
		this.flag = flag;
	}
	
	
	
}
