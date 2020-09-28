package net.zlw.cloud.maintenanceProjectInformation.model.vo;

/**
 * @Author dell
 * @Date 2020/9/27 14:08
 * @Version 1.0
 */
public class PageRequest {
    //页码
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
    // 内容
    private String keyWord;

    //所属地区
    private String address;
    // 维修类型
    private String maintenanceType;
    //状态:待审核，处理中，未通过，待确认
    private String pageStaus;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    public PageRequest() {
    }

    public PageRequest(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public String getPageStaus() {
        return pageStaus;
    }

    public void setPageStaus(String pageStaus) {
        this.pageStaus = pageStaus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
