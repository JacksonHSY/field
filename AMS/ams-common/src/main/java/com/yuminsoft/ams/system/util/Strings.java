package com.yuminsoft.ams.system.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具
 */
public abstract class Strings {

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	/**
	 * 判断字符串是否为空<br>
	 * 若字符串为空则返回默认字符串
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String empty(String str, String defaultValue) {
		return isEmpty(str) ? defaultValue : str;
	}

	/**
	 * 判断字符串是否为空白
	 * 
	 * @param str
	 * @return
	 */
	public static boolean blank(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断字符串是否为空白<br>
	 * 若为空白则返回默认值
	 * 
	 * @param str
	 * @param defaultValue
	 * @return
	 */
	public static String blank(String str, String defaultValue) {
		return (blank(str)) ? defaultValue : str;
	}

	/**
	 * 大写首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String toUpperCaseInitial(String str) {
		if (!isEmpty(str)) {
			char[] chars = str.toCharArray();
			chars[0] = Character.toUpperCase(chars[0]);
			return new String(chars);
		}
		return str;
	}

	/**
	 * 合并成字符串
	 * 
	 * @param separator
	 * @param strs
	 * @return
	 */
	public static String join(String separator, String... strs) {
		StringBuilder str = new StringBuilder();
		for (String s : strs) {
			str.append(separator).append(String.valueOf(s));
		}
		return str.substring(separator.length());
	}

	/**
	 * 合并成字符串
	 * 
	 * @param separator
	 * @param strs
	 * @return
	 */
	public static String join(String separator, Object... objs) {
		String[] strs = new String[objs.length];
		for (int i = 0; i < objs.length; i++) {
			strs[i] = String.valueOf(objs[i]);
		}
		return join(separator, strs);
	}

	/**
	 * 截断字符串
	 * 
	 * @param str
	 * @param len
	 * @param suffix
	 * @return
	 */
	public static String truncate(String str, int len, String suffix) {
		if (!isEmpty(str) && str.length() > len) {
			str = str.substring(0, len);
			str = str + empty(suffix, "");
		}
		return str;
	}

	/**
	 * 随机字符串
	 * 
	 * @param len
	 * @param set
	 * @param sets
	 * @return
	 */
	public static String random(int len, String set, String... sets) {
		// 初始化
		Random random = new Random();
		StringBuilder strset = new StringBuilder(set).append(join("", sets));
		StringBuilder builder = new StringBuilder(len);

		// 生成随机字符串
		for (int i = 0; i < len; i++) {
			builder.append(strset.charAt(random.nextInt(strset.length())));
		}

		// 返回结果
		return builder.toString();
	}

	/**
	 * 转换成字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj instanceof List) {
			return String.format("%d rows list", ((List<?>) obj).size());
		} else if (obj instanceof Map) {
			return String.format("%d rows map", ((Map<?, ?>) obj).size());
		} else {
			return String.valueOf(obj);
		}
	}

	/**
	 * UUID
	 * 
	 * @return
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 生成批次号
	 * 
	 * @return
	 */
	public static String generateBatchNumber() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = formatter.format(new Date()) + System.currentTimeMillis();
		return time;
	}

	public static void byte2hex(byte paramByte, StringBuffer paramStringBuffer) {
		char[] arrayOfChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int i = (paramByte & 0xF0) >> 4;
		int j = paramByte & 0xF;
		paramStringBuffer.append(arrayOfChar[i]);
		paramStringBuffer.append(arrayOfChar[j]);
	}

	public static String bytes2hex(byte[] paramArrayOfByte) {
		String str1 = "";
		String str2 = "";
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			str2 = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
			if (str2.length() == 1) {
				str2 = "0" + str2;
			}
			str1 = str1 + str2;
		}
		return str1.toUpperCase();
	}

	/**
	 * 字符串集合
	 */
	public static final class StringSet {

		/** 数字 */
		public static final String NUMERIC = "1234567890";

		/** 小写字母 */
		public static final String LOWER_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

		/** 大写字母 */
		public static final String UPPER_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

		/** 符号 */
		public static final String SYMBOL = "!@#$%^&*_+-=|:;?";
	}

	/**
	 * 验证手机号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMobile(String str) {
		Pattern p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
		Matcher m = p.matcher(str);
		return m.matches();
	}

	public static boolean validateForFifteen(String str) {
		String regex = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{2}[xX\\d]$";
		return match(regex, str);
	}

	public static boolean validateForEighteen(String str) {
		String regex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[xX\\d]$";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 通过反射获取所有字段的值
	 * 
	 * @param t
	 * @param padChar
	 *            拼接参数
	 * @return
	 */
	public static <T> String reflectionToString(T t, String padChar) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (short i = 0; i < fields.length; i++) {
				Field field = fields[i];
				String fieldName = field.getName();
				if ("serialVersionUID".equals(fieldName)) {
					continue;
				}
				// 拼接get方法名
				String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				Class<? extends Object> tCls = t.getClass();
				Method getMethod = tCls.getMethod(getMethodName, new Class[] {});// 获取方法
				Object value = getMethod.invoke(t, new Object[] {});// 调用方法返回值
				if (i == fields.length - 1) {
					strBuilder.append(value == null ? "" : value.toString());
				} else {
					strBuilder.append(value == null ? "" : value.toString() + padChar);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return setEncoding(strBuilder.toString(), "gbk");
	}

	/**
	 * 设置字符编码
	 * 
	 * @param str
	 * @param encoding
	 * @return
	 */
	public static String setEncoding(String str, String encoding) {
		try {
			// 使用传递的编码格式获取字节，设置新的字符编码
			return new String(str.getBytes(encoding), encoding);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 过滤空字符串
	 * 
	 * Strings.defaultString(null) = "" Strings.defaultString("null") = ""
	 * Strings.defaultString("") = "" Strings.defaultString("123") = "123"
	 * 
	 * @param obj
	 * @return
	 */
	public static <T extends Object> String parseString(T obj) {
		if (obj == null) {
			return StringUtils.EMPTY;
		}
		String str = obj.toString().trim();
		if (obj instanceof String) {
			if ("null".equalsIgnoreCase(str) || "".equalsIgnoreCase(str)) {
				return StringUtils.EMPTY;
			} else {
				return str;
			}
		}
		return str;
	}

	/**
	 * 转换到指定数据类型
	 * 
	 * Strings.convertValue("",String.class) = ""
	 * Strings.convertValue("123",Long.class) = 123(返回Long类型)
	 * Strings.convertValue("aaa",Long.class) = 抛异常
	 * Strings.convertValue("123",String.class) = "123"
	 * 
	 * @param obj
	 * @param cls
	 * @return
	 */
	@SuppressWarnings(value = { "unchecked" })
	public static <T extends Object> T convertValue(Object obj, Class<T> cls) {
		String value = parseString(obj);
		if (obj == null) {
			return (T) StringUtils.EMPTY;
		} else {
			if (cls == Integer.class) {
				return (T) Integer.valueOf(value);
			} else if (cls == Long.class) {
				return (T) Long.valueOf(value);
			} else if (cls == Float.class) {
				return (T) Float.valueOf(value);
			} else if (cls == Double.class) {
				return (T) Double.valueOf(value);
			} else if (cls == String.class) {
				return (T) String.valueOf(value);
			}
		}
		return null;
	}

	/**
	 * 判断左边的字符串是否等于给定的任意一个字符串
	 * 
	 * @param a
	 *            字符串
	 * @param bs
	 *            字符串，可指定多个
	 * @return true 如果匹配任意一个返回true
	 * @author JiaCX
	 * @date 2017年4月27日 上午9:03:52
	 */
	public static boolean isEqualsEvenOnce(String a, String... bs) {
		for (String b : bs) {
			if (StringUtils.equals(a, b)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isEqualsEvenOnce(String a, List<String> bs) {
		for (String b : bs) {
			if (StringUtils.equals(a, b)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断左边给定的字符串是否包含给定list中的每一项
	 * 
	 * @author Jia CX
	 * <p>2018年2月6日 下午1:43:25</p>
	 * 
	 * @param a
	 * @param bs
	 * @return	true 如果全部包含返回true
	 */
	public static boolean isConstainsEveyone(String a, List<String> bs) {
		for (String b : bs) {
			if (!a.contains(b)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 身份证号码统一显示*1234格式
	 * 
	 * @param idNo原身份证号
	 * @author zw
	 * @date 2017年05月18日
	 * @return
	 */
	public static String hideIdCard(String idNo) {
		String idCard = "";
		if (!isEmpty(idNo)) {
			idCard = "*" + idNo.substring(idNo.length() - 4, idNo.length());
		}
		return idCard;
	}
	
	/**
	 * 判断给定字符串列表是否全部都是空的
	 * 
	 * @author Jia CX
	 * @date 2017年12月21日 上午10:16:07
	 * @notes
	 * 
	 * @param bs
	 * @return true是
	 */
	public static boolean isAllEmpty(String... bs) {
		for (String b : bs) {
			if(StringUtils.isNotEmpty(b)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断给定字符串列表是否全部都bu是空的
	 * 
	 * @author Jia CX
	 * @date 2017年12月21日 上午10:20:19
	 * @notes
	 * 
	 * @param bs
	 * @return
	 */
	public static boolean isAllNotEmpty(String... bs) {
		for (String b : bs) {
			if(StringUtils.isEmpty(b)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 把给定字符串列表拼接起来，如果为空的转换为""
	 * 
	 * @author Jia CX
	 * @date 2017年12月21日 上午10:53:44
	 * @notes
	 * 
	 * @param bs
	 * @return
	 */
	public static String contactStr(String... bs) {
		StringBuilder sb = new StringBuilder("");
		for (String b : bs) {
			sb.append(blank(b, ""));
		}
		return sb.toString();
	}
	
	/**
	 * 比较传入的两个参数是否不相等
	 * 
	 * @author Jia CX
	 * @date 2018年1月3日 上午9:20:46
	 * @notes
	 * 
	 * @param sot String类型
	 * @param emp 可能是Long,Integer,String
	 * @return true 表明不相等
	 */
	public static <T> boolean  isDiff(String sot, T emp){
		if(null == sot && null != emp) {
			return true;
		} else if(null != sot && null == emp) {
			return true;
		} else if(null != sot && null != emp) {
			if(emp instanceof Long || emp instanceof Integer) {
				return !sot.equals(emp.toString());
			} else if(emp instanceof String) {
				return !sot.equals(emp);
			}
		}
		return false;
	}

}
