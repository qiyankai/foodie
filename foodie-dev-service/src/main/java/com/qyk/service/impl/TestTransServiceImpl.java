package com.qyk.service.impl;

import com.qyk.service.StuService;
import com.qyk.service.TestTransService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestTransServiceImpl implements TestTransService {

    @Autowired
    private StuService stuService;

    /**
     * 演示验证事务的传播机制
     * Required:
     *  1. 只要添加了传播机制为 Required 的事务，就会有事务，这个事务级别的作用范围是当前方法及其子方法，
     *     也就是说当前添加这个级别的事务的方法，或者其子方法发生的时候，父子方法都会被回滚；
     * 	2. 如果父方法存在事务，子方法不管有没有添加事务，都会加入到父方法的事务里，父亲方法或者子方法只要有一者发生异常就会被回滚；
     * 	3. 如果父方法不存在事务，子方法有添加这个级别的事务，那么事务就作用在这个子方法及其子方法；
     * 	4. 如果父方法不存在事务，子方法没有添加事务，那就是都没有事务了
     * Supports:
     *  1. 父方法不使用事务，子方法使用 级别为Supports 的事务：
     *     当子方法发生异常的话，子方法无法进行回滚。supports随外层事务，如果这个时候外层没有事务，里层添加级别为supports的事务其实也相当于没有添加事务
     * 	2. 父方法使用事务，子方法使用 级别为Supports 的事务：
     * 	   当子方法发生异常的话，子方法进行回滚。supports随外层事务，如果这个时候外层有事务，里层添加级别为supports的事务其实也相当于使用父亲方法的事务
     * Mandatory:
     *  1. 谁调用我谁就必须有事务，如果没有事务，我就会抛出异常
     * 	2. 当调用的方法方法添加事务，如果为Required，这个方法将被纳入到父亲方法事务的作用范围内，
     * 	3. 这个其实这个所谓Mandatory 级别的事务就仅仅作为一个标识而已
     */

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void testPropagationTrans() {

        stuService.saveParent();
        stuService.saveChildren();
        //int a = 1 / 0;

    }
}
