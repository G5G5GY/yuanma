package com.yuanma.webmvc.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static <T> boolean isNotEmpty(Collection<T> collection){
        return null != collection && !collection.isEmpty();
    }

    public static <T> boolean isEmpty(Map map){
        return null == map || map.isEmpty();
    }

    public static <T> boolean isEmpty(Collection<T> collection){
        return null == collection || collection.isEmpty();
    }

    public static <T,R> List<R> collect(Function<? super T, ? extends R> mapper,Collection<T> collection){
        return collection.stream().map(mapper).collect(Collectors.toList());
    }

}
