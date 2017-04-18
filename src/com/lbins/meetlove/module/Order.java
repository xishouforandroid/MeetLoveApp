package com.lbins.meetlove.module;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/14.
 */
public class Order implements Serializable {
    private String order_no;//订单no
    private String empid;//会员ID
    private String payable_amount;//支付金额
    private String create_time;//创建订单时间--毫秒
    private String pay_time;//支付时间--毫秒
    private String status;//状态---1生成订单,2支付订单,3取消订单,4作废订单,5完成订单',
    private String pay_status;//支付状态 0：未支付，1：已支付，2：退款'
    private String trade_no;
    private String taxes;//税率
    private String out_trade_no;//支付宝 微信的订单id

    private String trade_type;//0支付宝  1微信
    private String is_dxk_order;//0认证服务费  1诚信保证金
    private String order_cont;//订单说明

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }


    public String getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(String payable_amount) {
        this.payable_amount = payable_amount;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getIs_dxk_order() {
        return is_dxk_order;
    }

    public void setIs_dxk_order(String is_dxk_order) {
        this.is_dxk_order = is_dxk_order;
    }

    public String getOrder_cont() {
        return order_cont;
    }

    public void setOrder_cont(String order_cont) {
        this.order_cont = order_cont;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

}
