package com.liangzhicheng.shop.modules.controller;

import com.liangzhicheng.shop.common.basic.ResponseResult;
import com.liangzhicheng.shop.common.constant.Constants;
import com.liangzhicheng.shop.entity.OrderInfo;
import com.liangzhicheng.shop.modules.service.IOrderInfoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/seckill/order")
public class OrderInfoController {

    @Resource
    private IOrderInfoService orderInfoService;

    /**
     * @description 秒杀接口
     *              问题：1.秒级别的并发量出现重复下单情况
     *                   2.秒级别的并发量出现库存负数情况
     *              解决：1.
     *                     (1).t_seckill_order表中user_id字段和seckill_id字段建立唯一索引且方法上贴上@Transactional(rollbackFor = Exception.class)
     *                       （但程序执行过程中会出现->Duplicate entry 'user_id的值-seckill_id的值' for key 'user_id'）会有串行执行
     *                        建立唯一索引（UNIQUE）：ALTER TABLE `t_seckill_order` ADD UNIQUE (`user_id`,`seckill_id`)
     *                        备注：存在性能问题
     *                     (2).通过redis单线程特性，方法setIfAbsent/increment(自增，例如每个用户只能下单3个商品之后得出的结果与3判断)实现重复下单校验
     *                         此校验只能作为预判断，如果其它业务逻辑处理失败，将标识删除
     *                         拓展：redis数据安全性问题，数据丢失；通过持久化机制（rdb/aof）处理，4.0新版本推出的混合持久化，也就是利用rdb/aof的特性组成的持久化；
     *                         需要做redis集群，因为会存在单机故障的问题，需要异地多活（跨机房部署），其中一个机房可能会因X因素，使得整个机房无法访问，集群+跨机房部署确保redis数据不丢失
     *                   2.
     *                     (1).通过乐观锁思想，修改时sql语句用库存大于0作为条件，大于0减1操作，否则不操作
     *                       （但请求上千甚至上万，都会执行秒杀中前置的所有判断，包括减库存判断，减库存判断是操作数据库）
     *                        备注：存在性能问题
     *                     (2).通过redis将秒杀商品的库存缓存，程序执行过程中操作redis中的缓存进行库存预减，方法increment
     *                        （但请求上千甚至上万，还是会有请求到redis）
     *                        备注：redis压力过大
     *                     (3).通过二级缓存（本地缓存，缓存到JVM中），定义一个map标识作为库存是否售完，得出结果来拦截绝大部分不可能秒杀成功的请求
     *
     * @param token
     * @param seckillId
     * @return ResponseResult<String>
     */
    @PostMapping(value = "/doSeckill/{random}")
    public ResponseResult<String> doSeckill(@CookieValue(name = Constants.USER_LOGIN_TOKEN) String token,
                                            @PathVariable("random") String random,
                                            Long seckillId,
                                            String uuid){
        //9.开始进行秒杀
        orderInfoService.doSeckill(token, random, seckillId, uuid);
        //10.异步下单，返回正在下单中
        return ResponseResult.success("正在下单中...");
    }

    @GetMapping(value = "/get/{orderNo}")
    public ResponseResult<OrderInfo> getByOrderNo(@CookieValue(name = Constants.USER_LOGIN_TOKEN) String token,
                                                  @PathVariable("orderNo") String orderNo){
        OrderInfo orderInfo = orderInfoService.get(token, orderNo);
        return ResponseResult.success(orderInfo);
    }

}
