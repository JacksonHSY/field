package com.yuminsoft.ams.system.dao.approve;

import com.yuminsoft.ams.system.domain.approve.StaffOrderAbility;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface StaffOrderAbilityMapper {

    int save(StaffOrderAbility staffOrderAbility);

    int delete(Long id);

    int update(StaffOrderAbility staffOrderAbility);

    StaffOrderAbility findById(Long id);

    StaffOrderAbility findOne(Map<String, Object> map);

    List<StaffOrderAbility> findAll(Map<String, Object> map);

    /**
     * 根据员工编码删除
     *
     * @param staffCode
     * @return
     * @author dmz
     * @date 2017年3月29日
     */
    int deleteByStaffCode(String staffCode);

    /**
     * 查出为该用户分配的产品code列表
     *
     * @param userCode
     * @return
     * @author JiaCX
     * @date 2017年4月6日 下午3:33:15
     */
    List<String> findAllProductCodeByUserCode(@Param("userCode") String userCode);

    /**
     * 根据用户编号查询接单能力更新时间(接单能力配置显示时间用)
     *
     * @param staffCode
     * @return
     */
    StaffOrderAbility findStaffOrderAbilityByStaffCode(String staffCode);

    /**
     * 根据员工工号获取能力列表
     *
     * @param list
     * @return
     * @author Jia CX
     * @date 2017年12月19日 上午8:47:52
     * @notes
     */
    List<StaffOrderAbility> findByUserCodes(List<String> list);

    /**
     * 获取员工接单能力
     * @param staffCode 员工工号
     * @param abilityes 改派List
     * @return  员工接单能力
     */
    List<StaffOrderAbility> findByMultiProductAndArea(@Param("staffCode") String staffCode, @Param("abilityes") List<StaffOrderAbility> abilityes);

}