package com.atguigu.edu.client;

import com.atguigu.commonutils.vo.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name="service-ucenter",fallback = UcenterClientImpl.class)
public interface UcenterClient {
    //根据用户id获取用户信息
    @GetMapping("/educenter/member/getInfoUc/{memberId}")
    public UcenterMember getUcenterPay(@PathVariable("memberId") String memberId);
}