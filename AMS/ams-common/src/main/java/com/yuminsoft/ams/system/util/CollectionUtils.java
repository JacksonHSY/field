package com.yuminsoft.ams.system.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/6/20.
 */
public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionUtils.class);

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * 根据属性，集合去重
     * @param sourceList
     * @param <T>
     * @return
     */
    @SneakyThrows
    public static <T> List<T> removeDuplicateByProperty(final List<T> sourceList, final String property){
        if(isEmpty(sourceList)){
            return null;
        }

        List<T> targetList = Lists.newArrayList();
        Set<String> unionSet = Sets.newHashSet();

        try{
            for (T item : sourceList) {
                if(!unionSet.contains(BeanUtils.getProperty(item, property))){
                    unionSet.add(BeanUtils.getProperty(item, property));
                    targetList.add(item);
                }
            }

        }catch (Exception e){
            LOGGER.error("collection去重异常", e);

        }

        return targetList;
    }


}
