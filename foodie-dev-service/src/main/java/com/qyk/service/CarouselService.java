package com.qyk.service;

import com.qyk.pojo.Carousel;

import java.util.List;

public interface CarouselService {

    /**
     * 查询所有的轮播图列表
     * @return
     */
    List<Carousel> queryAll(Integer isShow);
}
