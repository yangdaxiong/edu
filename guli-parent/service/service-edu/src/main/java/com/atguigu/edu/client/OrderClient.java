package com.atguigu.edu.client;

import com.atguigu.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-order",fallback = OrderFile.class)
public interface OrderClient {
    //查询订单信息
    @GetMapping("/eduorder/order/isBuyCourse/{memberid}/{id}")
    public Boolean isBuyCourse(@PathVariable("memberid") String memberid, @PathVariable("id") String id);
}