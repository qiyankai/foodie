package com.qyk.config;

import com.qyk.service.OrderService;
import com.qyk.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    @Autowired
    private OrderService orderService;

    /**
     * 设置定时器，弊端
     * 1、时间差
     *      10。39下单，11点检查，间隔不够，不处理，再检查12点了。超过半个小时了
     * 2、不支持集群
     *      单机没毛病，集群后像个弱智，还得用一台机子，刷新所有的库。
     * 3、性能低下
     *      全表搜索（性能低下，占用大量资源）
     * 定时任务，小型轻量项目
     *
     * 使用消息队列
     *      延时队列
     *      10：10下单，一小时后扫描订单，状态为10则关闭
     */
    @Scheduled(cron = "0 0 0/1 * * ?")
//    @Scheduled(cron = "0/3 * * * * ?")
    public void autoCloseOrders(){
        orderService.closeTimeOutOrder();
        System.out.println(DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
