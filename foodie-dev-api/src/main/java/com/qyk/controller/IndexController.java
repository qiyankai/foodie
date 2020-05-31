package com.qyk.controller;

import com.qyk.enums.YesOrNo;
import com.qyk.pojo.Carousel;
import com.qyk.pojo.Category;
import com.qyk.pojo.vo.CategoryVO;
import com.qyk.pojo.vo.NewItemsVO;
import com.qyk.service.CarouselService;
import com.qyk.service.CategoryService;
import com.qyk.utils.JSONResult;
import com.qyk.utils.JsonUtils;
import com.qyk.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Api(value = "首页", tags = {"首页展示相关的接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "获取首页轮播图列表", notes = "获取首页轮播图列表", httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel() {
        List<Carousel> list = new ArrayList<>();

        String carouseJsonStr = redisOperator.get("carousel");
        //先去判断redis是否有缓存
        if (StringUtils.isNotBlank(carouseJsonStr)) {
            list = JsonUtils.jsonToList(carouseJsonStr, Carousel.class);
        } else {
            list = carouselService.queryAll(YesOrNo.YES.type);
            // 将数据放到redis
            redisOperator.set("carousel", JsonUtils.objectToJson(list));
        }
        return JSONResult.ok(list);

    }

    /**
     * 首页的分类展示的需求：
     * 1. 第一刷新主页面查询大分类，渲染展示到首页
     * 2. 如果鼠标移动到某一个大类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @ApiOperation(value = "获取商品分类（一级分类）", notes = "获取商品分类（一级分类）", httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats() {
        List<Category> list = new ArrayList<>();

        //先去判断redis是否有缓存
        String carouseJsonStr = redisOperator.get("cats");

        if (StringUtils.isNotBlank(carouseJsonStr)) {
            list = JsonUtils.jsonToList(carouseJsonStr, Category.class);
        } else {
            list = categoryService.queryAllRootLevelCat();
            // 将数据放到redis
            redisOperator.set("cats", JsonUtils.objectToJson(list));
        }
        return JSONResult.ok(list);
    }

    /**
     * 根据一级分类id获取商品子分类
     *
     * @param rootCatId
     * @return
     */
    @ApiOperation(value = "获取商品子分类", notes = "获取商品子分类", httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(@ApiParam(name = "rootCatId", value = "一级分类id", required = true) @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            JSONResult.errorMsg("分类不存在！");
        }


        List<CategoryVO> list = new ArrayList<>();

        //先去判断redis是否有缓存
        String catsJsonStr = redisOperator.get("cats-id-" + rootCatId);

        if (StringUtils.isNotBlank(catsJsonStr)) {
            list = JsonUtils.jsonToList(catsJsonStr, CategoryVO.class);
        } else {
            list = categoryService.getSubCatList(rootCatId);
            // 将数据放到redis,为了防止缓存穿透，在此我们将查询回来为空的数据，也进行一个缓存
            if (list != null && list.size() > 0) {
                redisOperator.set("cats-id-" + rootCatId, JsonUtils.objectToJson(list));
            } else {
                redisOperator.set("cats-id-" + rootCatId, JsonUtils.objectToJson(list), 5 * 60);
            }
        }
        return JSONResult.ok(list);
    }

    /**
     * 查询每一级分类下的最新6条商品数据
     *
     * @param rootCatId
     * @return
     */
    @ApiOperation(value = "查询每一级分类下的最新6条商品数据", notes = "查询每一级分类下的最新6条商品数据", httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(@ApiParam(name = "rootCatId", value = "一级分类id", required = true) @PathVariable Integer rootCatId) {
        if (rootCatId == null) {
            JSONResult.errorMsg("分类不存在！");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JSONResult.ok(list);
    }


}
