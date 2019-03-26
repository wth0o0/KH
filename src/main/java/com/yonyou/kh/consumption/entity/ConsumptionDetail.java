package com.yonyou.kh.consumption.entity;

import java.math.BigDecimal;
import java.util.Date;
/**
 * 消耗明细（消费单/充值单）
 * */
public class ConsumptionDetail {
    private String id;

    private String srcId;

    private String uId;

    private String uName;

    private String tsStoreId;

    private String tsStoreName;

    private Integer transactionType;

    private BigDecimal transactionAmount;

    private Date lastModifiedTime;

    private Date createTime;
    
    private BigDecimal balance;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id =id = id == null ? null : id.trim();;
    }

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId == null ? null : srcId.trim();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId == null ? null : uId.trim();
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName == null ? null : uName.trim();
    }

    public String getTsStoreId() {
        return tsStoreId;
    }

    public void setTsStoreId(String tsStoreId) {
        this.tsStoreId = tsStoreId == null ? null : tsStoreId.trim();
    }

    public String getTsStoreName() {
        return tsStoreName;
    }

    public void setTsStoreName(String tsStoreName) {
        this.tsStoreName = tsStoreName == null ? null : tsStoreName.trim();
    }

    public Integer getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(Integer transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(BigDecimal transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}