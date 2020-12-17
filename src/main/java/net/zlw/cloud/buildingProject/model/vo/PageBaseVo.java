package net.zlw.cloud.buildingProject.model.vo;

import lombok.Data;

/**
 * @Classname PageVo
 * @Description TODO
 * @Date 2020/12/17 10:25
 * @Created by sjf
 */
@Data
public class PageBaseVo {

    private int pageNum;
    private int pageSize;
    private String startTime;
    private String endTime;
    private String designCategory;
    private String projectNature;
    private String mergeFlag;
    private String keyword;


}
