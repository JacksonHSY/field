package com.yuminsoft.ams.system.util;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.util.StringUtils;

/**
 * generate string
 * @author fuhongxing
 */
public class GenerateUtils {
	
	private static final int DEF_COUNT = 20;


    /**
     * Generates a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generates an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return org.apache.commons.lang3.RandomStringUtils.randomNumeric(DEF_COUNT);
    }
	
	/**
	 * generate uuid
	 * @return
	 */
    public static String generateUUID() {
        return "zd"+UUID.randomUUID().toString().replaceAll("-", "");
    }
    
    
    /**
     * generate batch number
     * @return
     */
    public static String generateBatchNumber(){
    	return DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
    }
    
    /**
     * generate batch number
     * @param prefix
     * @return
     */
    public static String generateBatchNumber(String prefix){
    	return prefix + DateUtils.dateToString(new Date(), "yyyyMMddHHmmss") + System.currentTimeMillis();
    }
    
    /**
     * generate MD5 Hex
     * @param str
     * @return
     */
    public static String generateMD5Hex(String str){
    	if(StringUtils.isEmpty(str)){
    		return null;
    	}
    	return DigestUtils.md5Hex(str.getBytes());
    }
    
    /**
     * generate MD5 String
     * @param str
     * @return
     */
    public static String generateMD5(String str){
    	if(StringUtils.isEmpty(str)){
    		return null;
    	}
    	return Md5Crypt.md5Crypt(str.getBytes());
    }
    
    
}
