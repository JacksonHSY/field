package com.yuminsoft.ams.system.service.pic;

import com.yuminsoft.ams.system.util.Result;
import com.yuminsoft.ams.system.vo.pic.PapersTypeVo;
import com.yuminsoft.ams.system.vo.pic.PicRequestVo;
import com.yuminsoft.ams.system.vo.pic.AttachmentVo;

import java.util.List;

/**
 * PIC接口服务
 * Created by YM10195 on 2017/7/19.
 */
public interface PicService {

    /**
     * 获取附件列表
     * @param queryVo 附件查询VO
     * @author wulinjie
     * @return 附件列表
     */
    public List<AttachmentVo> getAllAttachment(PicRequestVo queryVo);

    /**
     * 获取文件类型集合（文件夹、目录）接口
     * @param requestVo 查询VO
     * @author wulinjie
     * @return 文件类型集合（含目录）
     */
    public List<PapersTypeVo> getPapersTypeList(PicRequestVo requestVo);

    /**
     * 删除附件
     * @param requestVo
     */
    public void deleteAttachment(PicRequestVo requestVo);

    /**
     * 判断两个申请件是否都有附件
     * @param loanNo1
     * @param loanNo2
     * @return
     */
    public Result<String> getCompareAttachment(String loanNo1, String loanNo2);

}
