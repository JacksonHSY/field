package com.yuminsoft.ams.system.vo.pic;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * PIC 附件目录
 * /api/paperstype/list
 * Created by YM10195 on 2017/7/19.
 */
@Data
@ToString
public class PapersTypeVo implements Serializable{

    private static final long serialVersionUID = 7267050964807263673L;

    private Long id;                // ID
    private String name;            // 名称
    private String code;            // 编码

    private Integer fileSize;       // 文件大小
    private Integer fileNumber;     // 文件数量
    private String fileType;        // 文件类型 .jpg,.png,.bmp,.jpeg,.gif,.pdf

    private Integer fileAmount;      // 附件总数量
    private Integer wasteAmount;     // 作废数量
    private Integer patchBoltAmount; // 补件数量

    private Integer operateAdd;      // 上传
    private Integer operateMove;     // 移动
    private Integer operateRemove;   // 删除
    private Integer operateRename;   // 重命名
    private Integer operateDownload; // 下载
    private Integer operateWaste;    // 作废
    private Integer operatePatchBolt;// 退回

    private Integer maxSortNum;     //
    private Integer sortNum;        //  排序字段
}
