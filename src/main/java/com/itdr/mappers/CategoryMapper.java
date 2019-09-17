package com.itdr.mappers;

import com.itdr.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    //根据父ID查询所有一级子ID
    List<Category> selectByParentId(Integer pid);

    //根据父ID查询是否有子分类
    int selectIntByParentId(Integer categoryId);

    //根据分类ID查询是否存在
    int selectByCategoryId(Integer categoryId);

    //根据分类名称查询是否存在
    int selectByCategoryName(String categoryName);

    //根据分类ID修改品类名字
    int updateByCategoryIdAndCategoryName(@Param("categoryId") Integer categoryId,@Param("categoryName")String categoryName);

    //添加商品分类节点
    int insertByParentIdAndCategoryName(@Param("parentId")Integer parentId,@Param("categoryName") String categoryName);

    //删除分类
    int delectByCategoryId(Integer categoryId);
}