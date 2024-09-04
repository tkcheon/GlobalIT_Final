package com.githrd.FinalProject.vo;

import lombok.Data;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Data
@Alias("order")
public class OrderVo {
    private int orders_id;
    private String orders_payment;
    private int orders_price;
    private String orders_srequest;
    private String orders_drequest;
    private Date orders_cdate;
    private String orders_status;
    private int shop_id;
    private int addr_id;
    private String shop_name;
    private String deliveries_method;
    private String shop_status;
}