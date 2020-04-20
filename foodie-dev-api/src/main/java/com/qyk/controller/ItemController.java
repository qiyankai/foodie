package com.qyk.controller;

import com.qyk.pojo.Items;
import com.qyk.pojo.ItemsImg;
import com.qyk.pojo.ItemsParam;
import com.qyk.pojo.ItemsSpec;
import com.qyk.pojo.vo.CommentLevelCountsVO;
import com.qyk.pojo.vo.ItemInfoVO;
import com.qyk.pojo.vo.ShopcartVO;
import com.qyk.service.ItemService;
import com.qyk.utils.JSONResult;
import com.qyk.utils.PagedGridResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemController extends BaseController{

    @Autowired
    private ItemService itemService;

    /**
     * 查询商品详情
     * 前端路径： serverUrl + '/items/info/' + itemId, {})
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品详情", notes = "查询商品详情", httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @PathVariable("itemId") String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(items);
        itemInfoVO.setItemImgList(itemsImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return JSONResult.ok(itemInfoVO);
    }

    /**
     * 查询商品评价等级
     * 前端路径： /items/commentLevel?itemId=
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品评价等级", notes = "查询商品评价等级", httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);

        return JSONResult.ok(countsVO);
    }

    /**
     * 查询商品评价
     * 前端路径： /items/comments?itemId=
     * @param itemId
     * @return
     */
    @ApiOperation(value = "查询商品评价", notes = "查询商品评价", httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId", value = "商品id", required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level", value = "评价等级", required = false)
            @RequestParam Integer level,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的记录数", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMENT_PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.queryPagedComments(itemId, level, page, pageSize);

        return JSONResult.ok(gridResult);
    }

    /**
     * 搜索商品列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "搜索商品列表", notes = "搜索商品列表", httpMethod = "GET")
    @GetMapping("/search")
    public JSONResult search(
            @ApiParam(name = "keywords", value = "关键字", required = true)
            @RequestParam String keywords,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的记录数", required = false)
            @RequestParam Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return JSONResult.ok(gridResult);
    }

    /**
     * 根据分类搜索商品列表
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "根据分类Id搜索商品列表", notes = "根据分类Id搜索商品列表", httpMethod = "GET")
    @GetMapping("/catItems")
    public JSONResult search(
            @ApiParam(name = "catId", value = "三级分类Id", required = true)
            @RequestParam Integer catId,
            @ApiParam(name = "sort", value = "排序", required = false)
            @RequestParam String sort,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的记录数", required = false)
            @RequestParam Integer pageSize) {
        if (catId == null) {
            return JSONResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }
        PagedGridResult gridResult = itemService.searchItems(catId, sort, page, pageSize);
        return JSONResult.ok(gridResult);
    }

    /**
     * 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
     * @return
     */
    @ApiOperation(value = "根据商品规格ID查询最新的商品数据", notes = "根据商品规格ID查询最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public JSONResult refresh(@ApiParam(name = "itemSpecIds", value = "拼接的规格Ids", required = true, example = "1001, 1002")
                                   @RequestParam String itemSpecIds) {
        if (StringUtils.isBlank(itemSpecIds)) {
            return JSONResult.ok();
        }
        List<ShopcartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return JSONResult.ok(list);
    }


}
