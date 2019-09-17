package com.itdr.services.impl;

import com.itdr.common.ServerResponse;
import com.itdr.mappers.CategoryMapper;
import com.itdr.pojo.Category;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    static int categoryId;

    //根据分类ID查询所有的子类(包括本身)
    @Override
    public ServerResponse getDeepCategory(Integer categoryId) {
        if (categoryId == null || categoryId <0){
            return ServerResponse.defeatedRS("非法的参数");
        }
        List<Integer> li = new ArrayList<>();
        li.add(categoryId);
        getAll(categoryId,li);
        ServerResponse sr = ServerResponse.successRS(li);
        return sr;
    }

    //修改品类名字
    @Override
    public ServerResponse setCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || categoryId <0){
            return ServerResponse.defeatedRS("非法的参数");
        }
        if (categoryName == null || "".equals(categoryName)){
            return ServerResponse.defeatedRS("分类名不能为空");
        }
        int i = categoryMapper.selectByCategoryId(categoryId);
        if (i<=0){
            return ServerResponse.defeatedRS("分类Id不存在");
        }
        int i2 = categoryMapper.selectByCategoryName(categoryName);
        if (i2>0){
            return ServerResponse.defeatedRS("分类名称已存在");
        }
        int i3 = categoryMapper.updateByCategoryIdAndCategoryName(categoryId,categoryName);;
        if (i3 <=0){
            return ServerResponse.defeatedRS("修改品类名字失败");
        }
        return ServerResponse.successRS("修改成功");
    }

    //添加商品分类节点
    @Override
    public ServerResponse addCategory(Integer parentId, String categoryName) {
        if (parentId == null || "".equals(parentId)){
            parentId = 0;
        }
        if (parentId <0 || categoryName == null || "".equals(categoryName)){
            return ServerResponse.defeatedRS("非法的参数");
        }
        int i = categoryMapper.selectByCategoryId(parentId);
        if (i<=0 && parentId != 0){
            return ServerResponse.defeatedRS("父分类Id不存在");
        }
        int i2 = categoryMapper.selectByCategoryName(categoryName);
        if (i2>0){
            return ServerResponse.defeatedRS("分类名称已存在");
        }
        int i3 = categoryMapper.insertByParentIdAndCategoryName(parentId,categoryName);
        if (i3 <=0){
            return ServerResponse.defeatedRS("添加商品分类节点失败");
        }
        return ServerResponse.successRS("添加成功");
    }

    //获取品类子节点(平级)
    @Override
    public ServerResponse getCategory(Integer categoryId) {
        if (categoryId == null || categoryId <0){
            return ServerResponse.defeatedRS("非法的参数");
        }
        int i = categoryMapper.selectByCategoryId(categoryId);
        if (i<=0 && categoryId != 0){
            return ServerResponse.defeatedRS("分类Id不存在");
        }
        int i2 = categoryMapper.selectIntByParentId(categoryId);
        if (i2<=0){
            return ServerResponse.defeatedRS("该分类没有子节点");
        }
        List<Category> li = categoryMapper.selectByParentId(categoryId);
        if (li == null){
            return ServerResponse.defeatedRS("查询失败");
        }
        ServerResponse sr = ServerResponse.successRS(li);
        return sr;
    }

    //删除分类
    @Override
    public ServerResponse delectCategory(Integer categoryId) {
        if (categoryId == null || categoryId <=0){
            return ServerResponse.defeatedRS("非法的参数");
        }
        int i = categoryMapper.selectByCategoryId(categoryId);
        if (i<=0){
            return ServerResponse.defeatedRS("没有此分类节点");
        }
        int i2 = categoryMapper.delectByCategoryId(categoryId);
        if (i2<=0){
            return ServerResponse.defeatedRS("删除失败");
        }
        return ServerResponse.defeatedRS("删除成功");
    }


    //工具方法：递归循环查询
    private void getAll(Integer pid, List<Integer> list){
        List<Category> li = categoryMapper.selectByParentId(pid);
        if (li != null && li.size() != 0){
            for (Category categorys : li) {
                list.add(categorys.getId());
                getAll(categorys.getId(),list);
            }
        }
    }
}











