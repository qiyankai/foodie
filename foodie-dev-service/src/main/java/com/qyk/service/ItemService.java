package com.qyk.service;

import com.qyk.pojo.Items;
import com.qyk.pojo.ItemsImg;
import com.qyk.pojo.ItemsParam;
import com.qyk.pojo.ItemsSpec;
import com.qyk.pojo.vo.CommentLevelCountsVO;
import com.qyk.pojo.vo.ItemCommentVO;
import com.qyk.pojo.vo.ShopcartVO;
import com.qyk.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

    /**
     * 根据商品Id查询详情
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品ID查询商品图片列表
     * @param itemId
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品ID查询商品规格
     * @param itemId
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品ID查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 根据商品ID查询商品的评价等级数量
     * @param itemId
     * @return
     */
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 根据商品ID查询评论内容（分页）
     * @param itemId
     * @param level
     * @return
     */
    PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 商品搜索列表
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 商品搜索列表(根据分类)
     * @param catId
     * @param sort
     * @param page
     * @param pageSize
     * @return
     */
    PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize);

    /**
     * 根据规格ids 查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据id获取相应的商品规格
     * @param specId
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据id获取相应的商品主图
     * @param specId
     * @return
     */
    String queryItemMainImgById(String itemId);

}
