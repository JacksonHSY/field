package com.yuminsoft.ams.system.dao.quality;

import java.math.BigDecimal;
import java.util.Map;

import com.yuminsoft.ams.system.domain.quality.QualityExtractCase;

/**质检抽检系统抽单记录
 * @author YM10174
 *
 */
public interface QualityExtractCaseMapper {
		public int save(QualityExtractCase qualityExtractCase); //保存抽检信息
		public Map<String,BigDecimal> getCount(Map<String,Object> map); // 获取历史抽取记录
		public int update(Map<String,Object> map); // 更新抽检信息

}
