package com.java2nb.test.domain;

import java.io.Serializable;


import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.java2nb.common.jsonserializer.LongToStringSerializer;


import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;



/**
 * 付呗-订单信息表
 * 
 * @author xiongxy
 * @email 1179705413@qq.com
 * @date 2019-11-25 11:57:16
 */
public class OrderDO implements Serializable {
	private static final long serialVersionUID = 1L;

	
	//主键
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long id;
	//付呗商户号
			private String fbMerchantCode;
	//第三方商户的订单号
			private String merchantOrderSn;
	//付呗订单号
			private String orderSn;
	//平台方订单号
			private String platformOrderNo;
	//商户单号
			private String tradeNo;
	//订单状态，1：未支付，2：支付成功，3：支付失败，4：支付取消
			private Integer orderState;
	//蜂鸟优惠卷抵扣
			private Double fnCoupon;
	//红包抵扣
			private BigDecimal redPacket;
	//实收金额(元)
			private BigDecimal totalFee;
	//订单金额
			private BigDecimal orderPrice;
	//手续费(元)
			private BigDecimal fee;
	//对商品或交易的描述
			private String body;
	//附加数据
			private String attach;
	//付呗系统的门店id
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long storeId;
	//付呗系统的收银员id
		//java中的long能表示的范围比js中number大,也就意味着部分数值在js中存不下(变成不准确的值)
	//所以通过序列化成字符串来解决
	@JsonSerialize(using = LongToStringSerializer.class)
			private Long cashierId;
	//设备终端号
			private String deviceNo;
	//微信顾客支付授权的“open_id”或者支付宝顾客的“buyer_user_id”
			private String userId;
	//支付宝顾客的账号
			private String userLogonId;
	//交易成功的时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date payTime;
	//支付通道:1微信、2支付宝、3银联
			private Integer payChannel;
	//免充值代金券金额(元)
			private BigDecimal noCashCouponFee;
	//预充值代金券金额(元)
			private BigDecimal cashCouponFee;
	//顾客实际支付金额(元)
			private BigDecimal cashFee;
	//签名
			private String sign;
	//其它选项
			private String options;
	//创建时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
	//推送时间
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
		private Date pushTime;
	//推送IP
			private String pushIp;
	//商户id
			private BigDecimal mchtId;
	//QR编号
			private String sn;

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：付呗商户号
	 */
	public void setFbMerchantCode(String fbMerchantCode) {
		this.fbMerchantCode = fbMerchantCode;
	}
	/**
	 * 获取：付呗商户号
	 */
	public String getFbMerchantCode() {
		return fbMerchantCode;
	}
	/**
	 * 设置：第三方商户的订单号
	 */
	public void setMerchantOrderSn(String merchantOrderSn) {
		this.merchantOrderSn = merchantOrderSn;
	}
	/**
	 * 获取：第三方商户的订单号
	 */
	public String getMerchantOrderSn() {
		return merchantOrderSn;
	}
	/**
	 * 设置：付呗订单号
	 */
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	/**
	 * 获取：付呗订单号
	 */
	public String getOrderSn() {
		return orderSn;
	}
	/**
	 * 设置：平台方订单号
	 */
	public void setPlatformOrderNo(String platformOrderNo) {
		this.platformOrderNo = platformOrderNo;
	}
	/**
	 * 获取：平台方订单号
	 */
	public String getPlatformOrderNo() {
		return platformOrderNo;
	}
	/**
	 * 设置：商户单号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	/**
	 * 获取：商户单号
	 */
	public String getTradeNo() {
		return tradeNo;
	}
	/**
	 * 设置：订单状态，1：未支付，2：支付成功，3：支付失败，4：支付取消
	 */
	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	/**
	 * 获取：订单状态，1：未支付，2：支付成功，3：支付失败，4：支付取消
	 */
	public Integer getOrderState() {
		return orderState;
	}
	/**
	 * 设置：蜂鸟优惠卷抵扣
	 */
	public void setFnCoupon(Double fnCoupon) {
		this.fnCoupon = fnCoupon;
	}
	/**
	 * 获取：蜂鸟优惠卷抵扣
	 */
	public Double getFnCoupon() {
		return fnCoupon;
	}
	/**
	 * 设置：红包抵扣
	 */
	public void setRedPacket(BigDecimal redPacket) {
		this.redPacket = redPacket;
	}
	/**
	 * 获取：红包抵扣
	 */
	public BigDecimal getRedPacket() {
		return redPacket;
	}
	/**
	 * 设置：实收金额(元)
	 */
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * 获取：实收金额(元)
	 */
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	/**
	 * 设置：订单金额
	 */
	public void setOrderPrice(BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	/**
	 * 获取：订单金额
	 */
	public BigDecimal getOrderPrice() {
		return orderPrice;
	}
	/**
	 * 设置：手续费(元)
	 */
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	/**
	 * 获取：手续费(元)
	 */
	public BigDecimal getFee() {
		return fee;
	}
	/**
	 * 设置：对商品或交易的描述
	 */
	public void setBody(String body) {
		this.body = body;
	}
	/**
	 * 获取：对商品或交易的描述
	 */
	public String getBody() {
		return body;
	}
	/**
	 * 设置：附加数据
	 */
	public void setAttach(String attach) {
		this.attach = attach;
	}
	/**
	 * 获取：附加数据
	 */
	public String getAttach() {
		return attach;
	}
	/**
	 * 设置：付呗系统的门店id
	 */
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}
	/**
	 * 获取：付呗系统的门店id
	 */
	public Long getStoreId() {
		return storeId;
	}
	/**
	 * 设置：付呗系统的收银员id
	 */
	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}
	/**
	 * 获取：付呗系统的收银员id
	 */
	public Long getCashierId() {
		return cashierId;
	}
	/**
	 * 设置：设备终端号
	 */
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	/**
	 * 获取：设备终端号
	 */
	public String getDeviceNo() {
		return deviceNo;
	}
	/**
	 * 设置：微信顾客支付授权的“open_id”或者支付宝顾客的“buyer_user_id”
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：微信顾客支付授权的“open_id”或者支付宝顾客的“buyer_user_id”
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：支付宝顾客的账号
	 */
	public void setUserLogonId(String userLogonId) {
		this.userLogonId = userLogonId;
	}
	/**
	 * 获取：支付宝顾客的账号
	 */
	public String getUserLogonId() {
		return userLogonId;
	}
	/**
	 * 设置：交易成功的时间
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	/**
	 * 获取：交易成功的时间
	 */
	public Date getPayTime() {
		return payTime;
	}
	/**
	 * 设置：支付通道:1微信、2支付宝、3银联
	 */
	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}
	/**
	 * 获取：支付通道:1微信、2支付宝、3银联
	 */
	public Integer getPayChannel() {
		return payChannel;
	}
	/**
	 * 设置：免充值代金券金额(元)
	 */
	public void setNoCashCouponFee(BigDecimal noCashCouponFee) {
		this.noCashCouponFee = noCashCouponFee;
	}
	/**
	 * 获取：免充值代金券金额(元)
	 */
	public BigDecimal getNoCashCouponFee() {
		return noCashCouponFee;
	}
	/**
	 * 设置：预充值代金券金额(元)
	 */
	public void setCashCouponFee(BigDecimal cashCouponFee) {
		this.cashCouponFee = cashCouponFee;
	}
	/**
	 * 获取：预充值代金券金额(元)
	 */
	public BigDecimal getCashCouponFee() {
		return cashCouponFee;
	}
	/**
	 * 设置：顾客实际支付金额(元)
	 */
	public void setCashFee(BigDecimal cashFee) {
		this.cashFee = cashFee;
	}
	/**
	 * 获取：顾客实际支付金额(元)
	 */
	public BigDecimal getCashFee() {
		return cashFee;
	}
	/**
	 * 设置：签名
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * 获取：签名
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * 设置：其它选项
	 */
	public void setOptions(String options) {
		this.options = options;
	}
	/**
	 * 获取：其它选项
	 */
	public String getOptions() {
		return options;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：推送时间
	 */
	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * 获取：推送时间
	 */
	public Date getPushTime() {
		return pushTime;
	}
	/**
	 * 设置：推送IP
	 */
	public void setPushIp(String pushIp) {
		this.pushIp = pushIp;
	}
	/**
	 * 获取：推送IP
	 */
	public String getPushIp() {
		return pushIp;
	}
	/**
	 * 设置：商户id
	 */
	public void setMchtId(BigDecimal mchtId) {
		this.mchtId = mchtId;
	}
	/**
	 * 获取：商户id
	 */
	public BigDecimal getMchtId() {
		return mchtId;
	}
	/**
	 * 设置：QR编号
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * 获取：QR编号
	 */
	public String getSn() {
		return sn;
	}
}
