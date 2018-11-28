package com.yuminsoft.ams.system.util;

import com.yuminsoft.ams.system.common.QualityEnum;
import com.yuminsoft.ams.system.vo.system.SelectOptionVo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * 枚举工具类 Created by wulinjie
 */
public class EnumerationUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(EnumerationUtils.class);

  /**
   * 根据枚举类获取所有枚举值
   *
   * @param clzz 枚举类
   * @return
   * @author wulinjie
   */
  public static List<SelectOptionVo> getOptions(Class clzz) {
    List<SelectOptionVo> list = new LinkedList<SelectOptionVo>();

    try {
      String className = clzz.getSimpleName();
      LOGGER.info("根据枚举值，获取下拉框选项, className:{}", className);

      Object[] consts = clzz.getEnumConstants();
      for (Object const1 : consts) {
        String code = BeanUtils.getProperty(const1, "code");
        String value = BeanUtils.getProperty(const1, "value");

        SelectOptionVo option = new SelectOptionVo();
        option.setCode(code);
        option.setValue(value);

        list.add(option);
      }
    } catch (Exception e) {
      LOGGER.error("通过枚举获取下拉框选项异常", e);
    }

    return list;
  }

  /**
   * 根据枚举类的code获取value
   *
   * @param clzz 枚举类
   * @param code 枚举Code
   * @return
   */
  public static String getValueByCode(Class clzz, String code) {
    try {
      Object[] consts = clzz.getEnumConstants();
      for (Object item : consts) {
        String _code = BeanUtils.getProperty(item, "code");
        if(_code.equals(code)) {//修复了code为null报错问题
          return BeanUtils.getProperty(item, "value");
        }
      }
    } catch (Exception e) {
      LOGGER.error("根据Code获取枚举Value异常", e);

    }

    return "";
  }

  public static void main(String[] args) {
    List<SelectOptionVo> list = getOptions(QualityEnum.ApprovalStatus.class);
    for (SelectOptionVo selectVo : list) {
      System.out.println(selectVo.getValue() + "," + selectVo.getValue());
    }
  }

}
