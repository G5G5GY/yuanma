package com.yuanma.module.utils;

/**
 * @Author zsheng
 * @Date 2022/5/3 13:08
 * @Version 1.0
 * @Describe:
 */
import java.lang.reflect.Array;
import java.util.*;

public class EmptyUtils {
    public static boolean isEmptyByString(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmptyByMap(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    public static boolean isEmptyByIter(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    public static boolean isEmptyByIter(java.util.Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

    public static boolean isEmpty(Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

    public static boolean isEmptyByArr(Object array) throws Exception {
        if (null == array) {
            return true;
        } else if (isArray(array)) {
            return 0 == Array.getLength(array);
        } else {
            throw new Exception("Object to provide is not a Array !");
        }
    }

    public static boolean isArray(Object obj) {
        return null == obj ? false : obj.getClass().isArray();
    }

    public static boolean isEmpty(Enumeration<?> enumeration) {
        return null == enumeration || !enumeration.hasMoreElements();
    }
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotNull(Object obj) {
        Boolean result = (null != obj && !obj.equals((Object)null)) ;
        return !result?result: !(obj instanceof List) || (obj instanceof List && !((List)obj).isEmpty());
    }

    public static boolean isNotEmpty(Object obj){
        try {
            return !isEmpty(obj);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmptyWithTryCatch(Object obj)  {
        try {
            return isEmpty(obj);
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isEmpty(Object obj) throws Exception {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return EmptyUtils.isEmptyByString((CharSequence)obj);
        } else if (obj instanceof Map) {
            return EmptyUtils.isEmptyByMap((Map)obj);
        } else if (obj instanceof Iterable) {
            return EmptyUtils.isEmptyByIter((Iterable)obj);
        } else if (obj instanceof Iterator) {
            return EmptyUtils.isEmptyByIter((Iterator)obj);
        } else {
            return EmptyUtils.isArray(obj) ? EmptyUtils.isEmptyByArr(obj) : false;
        }
    }

    public static boolean isNull(Object obj) {
        return null == obj || obj.equals((Object)null);
    }

}

