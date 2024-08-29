package com.delivery.finalproject.service;

import com.delivery.finalproject.mapper.RiderMapper;
import com.delivery.finalproject.vo.AddrVo;
import com.delivery.finalproject.vo.OrderVo;
import com.delivery.finalproject.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RiderService {

    @Autowired
    private RiderMapper riderMapper;

    @Transactional
    public void updateOrderStatus(int orders_id, String status) {
        riderMapper.updateOrderStatus(orders_id, status);
    }

    public OrderVo getOrderById(int orders_id) {
        return riderMapper.getOrderById(orders_id);
    }

    public AddrVo getAddrById(int addr_id) {
        return riderMapper.getAddrById(addr_id);
    }

    public ShopVo getShopById(int shop_id) {
        return riderMapper.getShopById(shop_id);
    }

    public List<OrderVo> getOrdersByStatus(String status) {
        return riderMapper.findOrdersByStatus(status);
    }

    public List<OrderVo> getOrdersByRiderAndStatus(int riders_id, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("riders_id", riders_id);
        params.put("status", status);
        return riderMapper.findOrdersByRiderAndStatus(params);
    }

    @Transactional
    public synchronized boolean assignOrderToRider(int orders_id, int riders_id, String method) {
        OrderVo order = riderMapper.getOrderById(orders_id);
        if (order == null || !order.getOrders_status().equals("배차 대기")) {
            return false; // 이미 수락된 주문이거나 존재하지 않는 경우
        }

        riderMapper.updateOrderStatus(orders_id, "배차 완료");

        int deliveries_id = riderMapper.assignDelivery(riders_id, method, orders_id);
        int delivery_history_id = riderMapper.insertDeliveryHistory("배차 완료", deliveries_id);

        return delivery_history_id > 0;
    }

    public List<OrderVo> getCompletedOrdersByRider(int riders_id) {
        return riderMapper.findCompletedOrdersByRider(riders_id);
    }

}
