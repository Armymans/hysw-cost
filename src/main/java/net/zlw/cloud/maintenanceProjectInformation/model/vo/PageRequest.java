package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;

/**
 * @Author dell
 * @Date 2020/9/27 14:08
 * @Version 1.0
 */
@Data
public class PageRequest {
    //页码
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
    // 内容
    private String keyWord;

    //所属地区
    private String district;
    // 维修类型
    private String maintenanceItemType;
    //状态:待审核，处理中，未通过，待确认
    private String type;
    //开始时间
    private String startTime;
    //结束时间1
    private String endTime;

    public PageRequest() {
    }

    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

}
