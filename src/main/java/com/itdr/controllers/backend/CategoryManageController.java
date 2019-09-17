package com.itdr.controllers.backend;


import com.itdr.common.ServerResponse;
import com.itdr.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    CategoryService categoryService;

    //根据分类ID查询所有的子类(包括本身)
    @RequestMapping("get_deep_category.do")
    public ServerResponse getDeepCategory(Integer categoryId){
        return categoryService.getDeepCategory(categoryId);
    }


    //修改品类名字
    @RequestMapping("set_category_name.do")
    private ServerResponse setCategoryName(Integer categoryId,String categoryName) {
        return categoryService.setCategoryName(categoryId,categoryName);
    }

    //添加商品分类节点
    @RequestMapping("add_category.do")
    private ServerResponse addCategory(Integer parentId,String categoryName) {
        return categoryService.addCategory(parentId,categoryName);
    }

    //获取品类子节点(平级)
    @RequestMapping("get_category.do")
    private ServerResponse getCategory(Integer categoryId) {
        return categoryService.getCategory(categoryId);
    }

    //删除分类
    @RequestMapping("Delect_category.do")
    private ServerResponse delectCategory(Integer categoryId) {
        return categoryService.delectCategory(categoryId);
    }

}
















