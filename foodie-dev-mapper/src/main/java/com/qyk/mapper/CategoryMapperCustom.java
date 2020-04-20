package com.qyk.mapper;


import com.qyk.pojo.vo.CategoryVO;
import com.qyk.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 不需要继承通用模板  extends MyMapper<Category>
 */
public interface CategoryMapperCustom  {

    public List<CategoryVO> getSubCatList(Integer rootCatId);

    public List <NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}