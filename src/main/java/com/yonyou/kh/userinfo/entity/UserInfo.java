package com.yonyou.kh.userinfo.entity;

import java.math.BigDecimal;
import java.util.Date;

public class UserInfo {
    private String id;
    
    private String mid;

    private String uName;

    private String phone;

    private String card;

    private BigDecimal lifeFund;

    private BigDecimal funFund;

    private BigDecimal healthyFund;

    private BigDecimal currencyAmount;

    private BigDecimal cyAmount;
    
    private BigDecimal kyAmount;

    private Date lastModifiedTime;

    private Date createTime;

    
    @Override
	public String toString() {
		return "UserInfo [id=" + id + ", mid=" + mid + ", uName=" + uName + ", phone=" + phone + ", card=" + card
				+ ", lifeFund=" + lifeFund + ", funFund=" + funFund + ", healthyFund=" + healthyFund
				+ ", currencyAmount=" + currencyAmount + ", cyAmount=" + cyAmount + ", kyAmount=" + kyAmount
				+ ", lastModifiedTime=" + lastModifiedTime + ", createTime=" + createTime + "]";
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName == null ? null : uName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card == null ? null : card.trim();
    }

    public BigDecimal getLifeFund() {
        return lifeFund;
    }

    public void setLifeFund(BigDecimal lifeFund) {
        this.lifeFund = lifeFund;
    }

    public BigDecimal getFunFund() {
        return funFund;
    }

    public void setFunFund(BigDecimal funFund) {
        this.funFund = funFund;
    }

    public BigDecimal getHealthyFund() {
        return healthyFund;
    }

    public void setHealthyFund(BigDecimal healthyFund) {
        this.healthyFund = healthyFund;
    }

    public BigDecimal getCurrencyAmount() {
        return currencyAmount;
    }

    public void setCurrencyAmount(BigDecimal currencyAmount) {
        this.currencyAmount = currencyAmount;
    }

    public BigDecimal getKyAmount() {
        return kyAmount;
    }

    public void setKyAmount(BigDecimal kyAmount) {
        this.kyAmount = kyAmount;
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

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public BigDecimal getCyAmount() {
		return cyAmount;
	}

	public void setCyAmount(BigDecimal cyAmount) {
		this.cyAmount = cyAmount;
	}
}