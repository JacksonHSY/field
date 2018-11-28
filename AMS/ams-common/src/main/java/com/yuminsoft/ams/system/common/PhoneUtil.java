package com.yuminsoft.ams.system.common;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.ymkj.ams.api.vo.request.apply.ContactInfoVO;
import com.yuminsoft.ams.system.util.Strings;
import com.yuminsoft.ams.system.vo.ContactVO;
/**
 * 手机归属地/运营商 查询工具类
 * @author fuhongxing
 */
public class PhoneUtil {

    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    
    /** 直辖市*/
    private static final String[] CITIES = {"北京市","上海市","天津市","重庆市"};
    
    /** 自治区*/
    private static final String[] PROVINCES = {"新疆","西藏","宁夏","内蒙古","广西"};
    /**
     * 中国城市code
     */
    private static int code = 86;

    /**
     * 根据国家代码和手机号  判断手机号是否有效
     * @param phoneNumber 手机号码
     * @param countryCode 国家code(86)
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber, String countryCode){
        int code = Integer.valueOf(countryCode);
        Long phone = null;
        //针对固定电话处理
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        return phoneNumberUtil.isValidNumber(pn);
    }

    /**
     * 查询手机归属地和运营商信息
     * @param phoneNumber 手机号码
     * @param countryCode 国家code(中国86)
     * @return 返回归属地和运营商信息
     */
    public static String getCityAndCarrier(String phoneNumber, String countryCode){
    	int code = Integer.valueOf(countryCode);
    	Long phone = null;
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        String carrierZh = "";
        //获取归属地
        carrierZh += geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
        switch (carrierEn) {
	        case "China Mobile":
	            carrierZh += "移动";
	            break;
	        case "China Unicom":
	            carrierZh += "联通";
	            break;
	        case "China Telecom":
	            carrierZh += "电信";
	            break;
	        default:
	            break;
        }
        return carrierZh;
    }
    
    /**
     *  查询手机运营商信息
     * @param phoneNumber 手机号码
     * @param countryCode 国家code(中国86)
     * @return 返回运营商信息
     */
    public static String getCarrier(String phoneNumber, String countryCode){
    	int code = Integer.valueOf(countryCode);
    	Long phone = null;
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        //获取运营商
        String carrierZh = "";
        switch (carrierEn) {
	        case "China Mobile":
	            carrierZh += "移动";
	            break;
	        case "China Unicom":
	            carrierZh += "联通";
	            break;
	        case "China Telecom":
	            carrierZh += "电信";
	            break;
	        default:
	            break;
        }
        return carrierZh;
    }
    
    /**
     *  查询中国手机运营商信息
     * @param phoneNumber 手机号码
     * @return 返回运营商信息
     */
    public static String getCarrier(String phoneNumber){
    	if(phoneNumber == null || "".equals(phoneNumber)){
    		return "";
    	}
    	
    	Long phone = null;
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        //返回结果只有英文，自己转成成中文
        String carrierEn = carrierMapper.getNameForNumber(pn, Locale.ENGLISH);
        //获取运营商
        String carrierZh = "";
        switch (carrierEn) {
	        case "China Mobile":
	            carrierZh += "移动";
	            break;
	        case "China Unicom":
	            carrierZh += "联通";
	            break;
	        case "China Telecom":
	            carrierZh += "电信";
	            break;
	        default:
	            break;
        }
        return carrierZh;
    }

	/**
	 * 查询中国手机运营商类型(1-移动,2-联通,3-电信)
	 * @param phoneNumber 手机号码
	 * @return 返回运营商类型
	 */
	public static Integer getCarrierType(String phoneNumber){
		String carrier = getCarrier(phoneNumber);
		Integer type = 1;
		switch (carrier){
			case "移动":
				type = 1;
				break;
			case "联通":
				type = 2;
				break;
			case "电信":
				type = 3;
				break;
			default:
				type = 1;
		}

		return type;
	}

    /**
     * 
    * 根据国家代码和手机号查询手机归属地信息
    * @param phoneNumber 手机号码
    * @param countryCode 国家code(中国86)
    * @return    省市
     */
    public static String getCity(String phoneNumber, String countryCode){
    	int code = Integer.valueOf(countryCode);
    	Long phone = null;
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        //获取归属地
        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }
    
    /**
     * 
    * 查询中国手机号归属地信息
    * @param phoneNumber 手机号码
    * @return    省市
     */
    public static String getCity(String phoneNumber){
    	if(phoneNumber == null || "".equals(phoneNumber)){
    		return "";
    	}
    	Long phone = null;
		//针对固定电话处理
        if(phoneNumber.contains("-")){
			String [] arrays = StringUtils.split(phoneNumber, "-");
			phoneNumber = arrays.length > 2 ? arrays[0] + arrays[1] : phoneNumber;
        	phone = Long.valueOf(phoneNumber.replace("-", ""));
        }else{
        	phone = Long.valueOf(phoneNumber);
        }
        PhoneNumber pn = new PhoneNumber();
        pn.setCountryCode(code);
        pn.setNationalNumber(phone);
        //获取归属地
        return geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }
    
    /**
	 * 设置手机归属地信息
	 * @param contactInfo
	 * @param city
	 * @param flag 是否需要查询归属地和运营商信息
	 */
	public static JSONArray setPhoneCity(ContactInfoVO contactInfo, String province, String city, boolean flag) {
		JSONArray array = new JSONArray();
		ContactVO contact = null;
		//TODO 代码有冗余，先实现功能
		try {
			//1、常用手机
			if(StringUtils.isNotEmpty(contactInfo.getContactCellPhone())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setPhone(contactInfo.getContactCellPhone());
				searchCarrierAndCity(city, province, flag, contact);
				contact.setRemark(AmsCode.PHONEENUM.常用手机.name());
				array.add(contact);
			}
			
			//2、备用手机
			if(StringUtils.isNotEmpty(contactInfo.getContactCellPhone_1())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setPhone(contactInfo.getContactCellPhone_1());
				searchCarrierAndCity(city, province, flag, contact);
				contact.setRemark(AmsCode.PHONEENUM.备用手机.name());
				array.add(contact);
			}
			
			//3、宅电
			if(StringUtils.isNotEmpty(contactInfo.getHomePhone())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setPhone(contactInfo.getHomePhone());
				searchCarrierAndCity(city, province, flag, contact);
				contact.setRemark(AmsCode.PHONEENUM.宅电.name());
				array.add(contact);
			}
			
			//4、单位电话1
			if(StringUtils.isNotEmpty(contactInfo.getContactCorpPhone())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setPhone(contactInfo.getContactCorpPhone());
				searchCarrierAndCity(city, province, flag, contact);
				contact.setRemark(AmsCode.PHONEENUM.单位电话1.name());
				array.add(contact);
			}
			//5、单位电话2
			if(StringUtils.isNotEmpty(contactInfo.getContactCorpPhone_1())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setPhone(contactInfo.getContactCorpPhone_1());
				searchCarrierAndCity(city, province, flag, contact);
				contact.setRemark(AmsCode.PHONEENUM.单位电话2.name());
				array.add(contact);
			}
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return array;
	}
	
	private static void searchCarrierAndCity(String city, String province, boolean flag, ContactVO contact) {
		if(flag) {
			contact.setCarrier(PhoneUtil.getCarrier(contact.getPhone()));
			contact.setPhoneCity(PhoneUtil.getCity(contact.getPhone()));
			//营业部城市与电话归属地对比
			String pc = Strings.contactStr(handleSpecialPro(province), city);
			if(Strings.isAllNotEmpty(contact.getPhoneCity(), pc) && contact.getPhoneCity().contains(pc)){
				contact.setMatchBrach(true);
			}
		}
	}
	
	/**
	 * 针对特殊的省份进行给处理
	 * 1如果是直辖市，则转换为""
	 * 2如果是自治区，则去掉"省"这个字
	 * 
	 * @author Jia CX
	 * @date 2017年12月25日 上午11:58:34
	 * @notes
	 * 
	 * @param province
	 * @return
	 */
	public static String handleSpecialPro(String province) {
		//如果是直辖市，把省转换为""（因为平台把直辖市的省也补充了数据）
		if(Strings.isEqualsEvenOnce(province, CITIES)) {
			province = "";
		}
		//如果是自治区，则把省字去掉（如广西省桂林市---->广西桂林市）（平台把自治区改为省了，如“广西省”）
		if(StringUtils.startsWithAny(province, PROVINCES) && StringUtils.endsWith(province, "省")) {
			province = StringUtils.substring(province, 0, -1);
		}
		return province;
	}
	
	 /**
	  * 设置手机归属地信息
	  * @param contactInfo
	*/
	public static ContactVO setPhoneCity(ContactVO contactInfo, String city) {
		ContactVO contact = null;
		try {
			//1、常用手机
			if(StringUtils.isNotEmpty(contactInfo.getPhone())){
				contact = new ContactVO();
				BeanUtils.copyProperties(contact, contactInfo);
				contact.setCarrier(PhoneUtil.getCarrier(contact.getPhone()));
				contact.setPhoneCity(PhoneUtil.getCity(contact.getPhone()));
				contact.setRemark("常用手机");
				contact.setMatchBrach(true);
			}
			
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return contact;
	}
    
    public static void main(String[] args) {
    	System.out.println(handleSpecialPro("河南省"));
    	System.out.println(StringUtils.startsWithAny("新疆省", new String[]{"新疆","西藏","宁夏","内蒙古","广西"}));
    	System.out.println(StringUtils.endsWith("广西省", "省"));
    	System.out.println(StringUtils.substring("广西省", 0, -1));
//        System.out.println(PhoneUtil.getCity("021-20282126"));
//        System.out.println(PhoneUtil.getCityAndCarrier("18666662224","86"));
//        System.out.println(PhoneUtil.getCity("0411-2454770"));
//        System.out.println(PhoneUtil.getCity("0951-6938603"));
//        System.out.println(PhoneUtil.checkPhoneNumber("0411-2454770", "86"));
//        String[] provinces = {"13503600000","13228908888","15804715555","18895082685","15899250569","18611112224","18539407061","15271343096","13469019288","15127887788","18078418596","0371-65332525","18666662224"};
//        for (String string : provinces) {
//        	System.out.println(PhoneUtil.getCity(string,"86"));
//		}
//        System.out.println(PhoneUtil.getCarrier("021-20282126"));
    }

}