package net.zlw.cloud.jbDesignTask.domain.vo;


import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;
import net.zlw.cloud.jbDesignTask.domain.FileInfos;

import java.util.List;

/***
 * 江北设计vo
 */
@Data
public class JbDesignVo {

    private String id;
    private String projectId;
    private String projectName;
    private String address;
    private String area;
    private String customerName;
    private String customerAddress;
    private String designCategory;
    private String projectNature;
    private String contactNumber;
    private String customerPhone;
    private String customerEmail;
    private String agent;
    private String ceaNum;
    private String amountPaid;
    private String designer;
    private String takeTime;
    private String founderId;
    private String createTime ;
    private String remarks;
    private String explorationIdeal;
    private String explorationTime;
    private String scout;
    private String remark;

    private List<DiameterInfo> diameterInfos;
    private List<FileInfos> fileInfos;

}
