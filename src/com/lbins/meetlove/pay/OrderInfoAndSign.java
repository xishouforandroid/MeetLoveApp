package com.lbins.meetlove.pay;

/**
 * Created by Administrator on 2015/8/14.
 */
public class OrderInfoAndSign {
    private String orderInfo;//订单编号
    private String sign;//支付宝签名
    private String out_trade_no;//支付宝 微信订单ID

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
