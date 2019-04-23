package com.nuc.admin.domain;

import com.soft.common.domain.BaseDomain;

public class Shops extends BaseDomain {
	/**
	 * @Fields serialVersionUID : TODO
	 */
	private static final long serialVersionUID = -5953538097579278829L;
	private int shops_id; //
	private String shops_no; //
	private String build_no; // 
	private String unit_no; // 
	private String door_no; // 
	private String shops_model; //
	private String shops_area; //
	private String shops_flag; // 已售,待售

	private int user_id; // 
	private String real_name; // 
	private String ids;
	private String random;

	@Override
	public String toString() {
		return "Shops{" +
				"shops_id=" + shops_id +
				", shops_no='" + shops_no + '\'' +
				", build_no='" + build_no + '\'' +
				", unit_no='" + unit_no + '\'' +
				", door_no='" + door_no + '\'' +
				", shops_model='" + shops_model + '\'' +
				", shops_area='" + shops_area + '\'' +
				", shops_flag='" + shops_flag + '\'' +
				", user_id=" + user_id +
				", real_name='" + real_name + '\'' +
				", ids='" + ids + '\'' +
				", random='" + random + '\'' +
				'}';
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public int getShops_id() {
		return shops_id;
	}

	public void setShops_id(int shops_id) {
		this.shops_id = shops_id;
	}

	public String getShops_no() {
		return shops_no;
	}

	public void setShops_no(String shops_no) {
		this.shops_no = shops_no;
	}

	public String getBuild_no() {
		return build_no;
	}

	public void setBuild_no(String build_no) {
		this.build_no = build_no;
	}

	public String getUnit_no() {
		return unit_no;
	}

	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}

	public String getDoor_no() {
		return door_no;
	}

	public void setDoor_no(String door_no) {
		this.door_no = door_no;
	}

	public String getShops_model() {
		return shops_model;
	}

	public void setShops_model(String shops_model) {
		this.shops_model = shops_model;
	}

	public String getShops_area() {
		return shops_area;
	}

	public void setShops_area(String shops_area) {
		this.shops_area = shops_area;
	}

	public String getShops_flag() {
		return shops_flag;
	}

	public void setShops_flag(String shops_flag) {
		this.shops_flag = shops_flag;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getReal_name() {
		return real_name;
	}

	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}

	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}
}
