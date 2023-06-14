package com.yuanma.webmvc.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DaBeanUtils {

    public static <T> T convertBean(Class<T> clz,Object source){
        T target = BeanUtils.instantiateClass(clz);
        BeanUtils.copyProperties(source,target);
        return target;
    }

    public static <T> List<T> convertListBean(Class<T> clz,List<?> sourceDataList ){
        List<T> dataList = new ArrayList<>();
        for(Object source:sourceDataList){
            dataList.add(convertBean(clz,source));
        }
        return dataList;
    }

    public static <T> Page<T> convertPage(Class<T> clz,Page<?> page){
        Page<T> newPage = convertBean(Page.class,page);
        BeanUtils.copyProperties(page,newPage);
        List<T> dataList = convertListBean(clz,page.getRecords());
        newPage.setRecords(dataList);
        return newPage;
    }


    public static <T> List<T> convertTree(Class<T> clz,List<T> list, String pId) {
        TreeNode treeNode = clz.getAnnotation(TreeNode.class);
        List<T> dataList = new ArrayList<>();
        Iterator<T> it = list.iterator();
        while (it.hasNext()) {
            T data = it.next();
            String parentId = (String)getVal(data,treeNode.pid());
            if ( (null == parentId && null == pId) || StringUtils.equals(parentId,pId)) {
                dataList.add(data);
                it.remove();
            }
        }
        for(T data:dataList){
            String ownerId = (String)getVal(data,treeNode.id());
            setVal(data,treeNode.children(), convertTree(clz,list,ownerId));
        }
        return dataList;
    }

    public static Object getVal(Object bean,String fieldName){
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(bean);
        }catch (Exception e){
            return null;
        }
    }

    public static void setVal(Object bean,String fieldName,Object value){
        try {
            Field field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(bean,value);
        }catch (Exception e){

        }
    }
}
