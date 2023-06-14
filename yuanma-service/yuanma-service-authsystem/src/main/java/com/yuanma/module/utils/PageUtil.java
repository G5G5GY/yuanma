package com.yuanma.module.utils;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PageUtil {

    public static List toPage(int page, int size , List list) {
        int fromIndex = page * size;
        int toIndex = page * size + size;
        if(fromIndex > list.size()){
            return new ArrayList();
        } else if(toIndex >= list.size()) {
            return list.subList(fromIndex,list.size());
        } else {
            return list.subList(fromIndex,toIndex);
        }
    }

    public static Map<String,Object> toPage(Object object, Object totalElements) {
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",object);
        map.put("totalElements",totalElements);
        return map;
    }

    /**
     * Page 数据处理，预防redis反序列化报错
     */
    public static Map<String,Object> toPage(Page page) {
        Map<String,Object> map = new LinkedHashMap<>(2);
        map.put("content",page.getRecords());
        map.put("totalElements",page.getTotal());
        map.put("totalPages",page.getPages());
        return map;
    }

    public static final char UNDERLINE = '_';

    public static void addOrder(String strSort, String defaultSort, Page page){
        List<String> sorts = new ArrayList<>();
        if(StringUtils.isEmpty(strSort)){
            sorts.add(defaultSort);
        } else {
            sorts.add(strSort);
        }
        for(String sort:sorts){
            String[] arr = sort.split(",");
            if(arr.length > 1 && "desc".equals(arr[1])){
                page.addOrder(OrderItem.desc(camelToUnderline(arr[0])));
            }else{
                page.addOrder(OrderItem.asc(camelToUnderline(arr[0])));
            }
        }

    }


    public static <T> void addOrder(String strSort, String defaultSort, Page page,Class<T> entity){
        if(StringUtils.isEmpty(strSort)){
            addOrder(strSort, defaultSort,page);
        } else {
            String[] arr = strSort.split(",");
            Field declaredField = null;
            try {
                declaredField = entity.getDeclaredField(arr[0]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            if(null == declaredField){
                addOrder(null, defaultSort,page);
            } else {
                addOrder(strSort, defaultSort,page);
            }
        }
    }
    /**
     * 驼峰格式字符串转换为下划线格式字符串
     * @param param
     * @return
     */
    public static String camelToUnderline(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
                sb.append(Character.toLowerCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     * @param param
     * @return
     */
    public static String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
