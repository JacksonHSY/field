package com.yuminsoft.ams.system.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author JiaCX
 * 2017年4月10日 下午3:08:58
 */
public class BeanUtil extends BeanUtils {

    /**
     * 创建一个新对象，并将一个对象的属性值按名称复制到新对象<br />
     * <i>如果被复制的对象为 null，返回 null</i>
     *
     * @param src     被复制的对象
     * @param destClz 需要复制属性的对象 Class
     * @return 需要复制属性的对象
     * @throws ApplicationException 创建新对象时发生错误，抛出该异常
     */
    public static <T> T copyProperties(Object src, Class<T> destClz) {
        if (src == null) {
            return null;
        }

        T dest;
        try {
            dest = destClz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }

        Map<String, Object> srcValues = getFieldValues(src);
        Field[] destFlds = getFields(dest);

        for (Field fld : destFlds) {
            if (srcValues.containsKey(fld.getName())) {
                try {
                    if (isEquals(fld.getName(), "serialVersionUID")) {
                        continue;
                    }

                    fld.set(dest, srcValues.get(fld.getName()));
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return dest;
    }

    /**
     * 将对象中所有属性的值取出来放到 Map 中
     *
     * @param obj 要取属性值的对象
     * @return 字段名-字段值的 Map
     */
    public static Map<String, Object> getFieldValues(Object obj) {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<>();
        Field[] flds = getFields(obj);
        for (Field fld : flds) {
            try {
                map.put(fld.getName(), fld.get(obj));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    /**
     * 获取对象的 Field 列表，并赋予可访问性
     *
     * @param obj 要获取 Field 的实例对象
     * @return Field 列表
     */
    public static Field[] getFields(Object obj) {
        if (obj == null) {
            return null;
        }

        return getFields(obj.getClass());
    }

    /**
     * 获取 Class 的 Field 列表，并赋予可访问性
     *
     * @param clz 要获取 Field 的 Class 反射对象
     * @return Field 列表
     */
    public static Field[] getFields(Class<?> clz) {
        if (clz == null) {
            return null;
        }

        Field[] flds = ArrayUtils.addAll(clz.getDeclaredFields(), (!clz.getSuperclass().equals(Object.class)) ? clz.getSuperclass().getDeclaredFields() : null);

        for (Field fld : flds) {
            fld.setAccessible(true);
        }
        return flds;
    }

    /**
     * 判断两个字符串是否相等
     *
     * @param a 字符串
     * @param b 字符串
     * @return 如果两个字符串内容相等，返回 true<br />
     * <i>都是 null，返回 true。</i><br />
     * <i>一个为空一个为 null，返回 false</i>
     */
    public static boolean isEquals(String a, String b) {
        return StringUtils.equals(a, b);
    }

    /**
     * 拷贝属性，忽略空格
     * @param dest
     * @param orig
     * @author wulj
     */
    public static void copyPropertiesIgnoreNull(Object dest, Object orig) {
        org.springframework.beans.BeanUtils.copyProperties(orig, dest, getNullPropertyNames(orig));
    }

    public static String[] getNullPropertyNames (Object source){
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];

        return emptyNames.toArray(result);
    }

}
