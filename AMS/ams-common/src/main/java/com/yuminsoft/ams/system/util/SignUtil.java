package com.yuminsoft.ams.system.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author fuhongxing
 */
public class SignUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(SignUtil.class);
	private SignUtil(){
		
	}
	
	/**
	 * 数字签名
	 * @param signature
	 * @param messageDigest
	 * @return
	 * @throws Exception 
	 */
	public static String signature(String key,  Map<String,String> map) throws Exception {
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		String[] arr = list.toArray(new String[list.size()]);
		// 将参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if(null != map.get(arr[i])){
				content.append(arr[i] + "=" + map.get(arr[i]));
			}
			
		}
		content.append("secret=");
		content.append(key);
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] digest = messageDigest.digest(content.toString().getBytes("utf-8"));
		String sign = byteToStr(digest);
		LOGGER.info("签名结果："+ sign);
		return sign;
	}
	
	/**
	 * 数字签名(secret Key加入排序)
	 * @param signature
	 * @param messageDigest
	 * @return
	 * @throws Exception 
	 */
	public static String signature(Map<String,String> map) throws Exception {
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		String[] arr = list.toArray(new String[list.size()]);
		// 将参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if(null != map.get(arr[i])){
				content.append(arr[i] + "=" + map.get(arr[i])).append("&");
			}
		}
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] digest = messageDigest.digest(content.deleteCharAt(content.length() - 1).toString().getBytes("utf-8"));
		String sign = byteToStr(digest);
		LOGGER.info("签名结果："+ sign);
		return sign;
	}
	
	public static String signature(String ORG_KEY, String timestamp, String nonce) {
        try {
            String[] arr = new String[] { timestamp, nonce, ORG_KEY };
            Arrays.sort(arr);//参数值做字典排序
            String s = arr[0] + arr[1] + arr[2];
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(s.getBytes("utf-8"));
            return byteToStr(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce,String token) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("验证签名异常", e);
		}

		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}
	
	/**
	 * hash计算
	 * @param param
	 * @param orgSecretKey
	 */
	public static void hash(Map<String, String> param, String orgSecretKey) {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            sb.append(param.get(iterator.next()));
        }
        param.put("hash", md5(sb.toString() + orgSecretKey, "UTF-8").toUpperCase());
    }
	
	/**
	 * MD5加密
	 * @param msg
	 * @param charset
	 * @return
	 */
	public static String md5(String msg, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] hashedBytes = digest.digest(msg.getBytes(charset));
            String rst = byteToStr(hashedBytes);
            return rst;
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
        	LOGGER.error("MD5 Sign Exception...");
        }
        return "";
    }
	
}