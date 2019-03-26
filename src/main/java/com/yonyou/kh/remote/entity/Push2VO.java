package com.yonyou.kh.remote.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * U浼氬憳鎺ㄩ�杩囨潵鐨勫弬鏁癡O
 * @author wangjian
 *
 */
public class Push2VO {
	
	/**鏄庣粏ID*/
	private String id;
	/**浼氬憳ID*/
	private String mid;  
	/**绯荤粺鏉ユ簮鏍囪瘑*/
	private String source; 
	/**
	 * 浜ゆ槗绫诲瀷
	 * (
	 * 0 锛�U8闆跺敭鏈熷垵(浣滃簾) 
	 * 1 锛�鍏呭�
	 * 2 锛�娑堣垂 
	 * 3 锛�娑堣垂閫� 
	 * 4 锛�鍏呭�閫�
	 * 6 锛�闆跺敭绯荤粺鎹㈠崱瀛樺湪鏂板崱鏈夊垵濮嬮噾棰� 
	 * 7 锛�璋冩暣鍗曡皟鏁�姝ｅ悜 
	 * 8 锛�璋冩暣鍗曡皟鏁�璐熷悜 
	 * 10 锛�U8闆跺敭閫�揣鍏呭�
	 * 11 锛�U8闆跺敭澶辩鎹㈠崱(浣滃簾) 
	 * 13 锛�U8闆跺敭鍏朵粬鍘熷洜鎹㈠崱(浣滃簾) 
	 * 17 锛�U8闆跺敭鍞崱(浣滃簾) 
	 * 18 锛�U8闆跺敭閫�崱(浣滃簾) 
	 * 19 锛�U8闆跺敭璧犲崱(浣滃簾) 
	 * 22 锛�闆跺敭绯荤粺缁戝崱瀛樺湪鏈熷垵浣欓 
	 * 99 锛�浼氬憳瀵煎叆 
	 * null 锛�绫诲瀷涓虹┖ 閿欒鏁版嵁 
	 * )
	 * */
	private Integer type; 
	/**澶栭儴浜ゆ槗鍗曞彿*/
	private String out_pay_code; 
	/**浜ゆ槗閲戦*/
	private BigDecimal sum;
	/**缁撶畻閲戦*/
	private BigDecimal settle_sum;
	/**鎶樻墸閲戦*/
	private BigDecimal disaccount;
	/**鍏呭�鎶垫墸绉垎*/
	private Integer points;
	/**鍏呭�鎶垫墸绉垎鍏戞崲閲戦*/
	private BigDecimal point_ex_money; 
	/**闂ㄥ簵id*/
	private String store_id;
	/**闂ㄥ簵缂栫爜*/
	private String store_code;
	/**闂ㄥ簵鍚嶇О*/
	private String store_name;
	/**搴楀憳ID*/
	private String store_employee_id;
	/**搴楀憳鍚嶇О*/
	private String store_employee_name;
	/**褰撳墠浣欓*/
	private BigDecimal balance_sum;
	/**褰撳墠浣欓鎶樻墸棰*/
	private BigDecimal balance_disaccount_sum;
	/**鎽樿*/
	private String digest;
	/**鏀粯鏂瑰紡*/
	private String payment;
	/**鏁版嵁鏉ユ簮1*/
	private String source1; 
	/**鏁版嵁鏉ユ簮2*/
	private String source2;
	/**鍒涘缓鏃堕棿*/
	private String create_time;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getOut_pay_code() {
		return out_pay_code;
	}
	public void setOut_pay_code(String out_pay_code) {
		this.out_pay_code = out_pay_code;
	}
	public BigDecimal getSum() {
		return sum;
	}
	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}
	public BigDecimal getSettle_sum() {
		return settle_sum;
	}
	public void setSettle_sum(BigDecimal settle_sum) {
		this.settle_sum = settle_sum;
	}
	public BigDecimal getDisaccount() {
		return disaccount;
	}
	public void setDisaccount(BigDecimal disaccount) {
		this.disaccount = disaccount;
	}
	public Integer getPoints() {
		return points;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public BigDecimal getPoint_ex_money() {
		return point_ex_money;
	}
	public void setPoint_ex_money(BigDecimal point_ex_money) {
		this.point_ex_money = point_ex_money;
	}
	public String getStore_id() {
		return store_id;
	}
	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}
	public String getStore_code() {
		return store_code;
	}
	public void setStore_code(String store_code) {
		this.store_code = store_code;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getStore_employee_id() {
		return store_employee_id;
	}
	public void setStore_employee_id(String store_employee_id) {
		this.store_employee_id = store_employee_id;
	}
	public String getStore_employee_name() {
		return store_employee_name;
	}
	public void setStore_employee_name(String store_employee_name) {
		this.store_employee_name = store_employee_name;
	}
	public BigDecimal getBalance_sum() {
		return balance_sum;
	}
	public void setBalance_sum(BigDecimal balance_sum) {
		this.balance_sum = balance_sum;
	}
	public BigDecimal getBalance_disaccount_sum() {
		return balance_disaccount_sum;
	}
	public void setBalance_disaccount_sum(BigDecimal balance_disaccount_sum) {
		this.balance_disaccount_sum = balance_disaccount_sum;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getSource1() {
		return source1;
	}
	public void setSource1(String source1) {
		this.source1 = source1;
	}
	public String getSource2() {
		return source2;
	}
	public void setSource2(String source2) {
		this.source2 = source2;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	@Override
	public String toString() {
		return "{\"id\":\"" + id + "\", \"mid\":\"" + mid + "\", \"source\":\"" + source + "\", \"type\":\""
				+ type + "\", \"out_pay_code\":\"" + out_pay_code + "\", \"sum\":\"" + sum + "\", \"settle_sum\":\""
				+ settle_sum + "\", \"disaccount\":\"" + disaccount + "\", \"points\":\"" + points
				+ "\", \"point_ex_money\":\"" + point_ex_money + "\", \"store_id\":\"" + store_id
				+ "\", \"store_code\":\"" + store_code + "\", \"store_name\":\"" + store_name
				+ "\", \"store_employee_id\":\"" + store_employee_id + "\", \"store_employee_name\":\""
				+ store_employee_name + "\", \"balance_sum\":\"" + balance_sum + "\", \"balance_disaccount_sum\":\""
				+ balance_disaccount_sum + "\", \"digest\":\"" + digest + "\", \"payment\":\"" + payment
				+ "\", \"source1\":\"" + source1 + "\", \"source2\":\"" + source2 + "\", \"create_time\":\""
				+ create_time + "\"}";
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}

	
	
	
	
}
