package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

@Data
public class PageBVo {
    private int pageNum;
    private int pageSize;
    private String district;
    private String waterSupplyType;
    private String projectNature;
    private String shouldBe;
    private String whetherAccount;
    private String startTime;
    private String endTime;
    private String keyword;
    private String budgetingStatus;
    private String designCategory;
    private String founderId;

    private String currentPeople;


    private String projectNum;
    private String projectName;
    private String constructionUnit;
    private String waterAddress;
    private String attributionShow;



    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWaterSupplyType() {
        return waterSupplyType;
    }

    public void setWaterSupplyType(String waterSupplyType) {
        this.waterSupplyType = waterSupplyType;
    }

    public String getProjectNature() {
        return projectNature;
    }

    public void setProjectNature(String projectNature) {
        this.projectNature = projectNature;
    }

    public String getShouldBe() {
        return shouldBe;
    }

    public void setShouldBe(String shouldBe) {
        this.shouldBe = shouldBe;
    }

    public String getWhetherAccount() {
        return whetherAccount;
    }

    public void setWhetherAccount(String whetherAccount) {
        this.whetherAccount = whetherAccount;
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getBudgetingStatus() {
        return budgetingStatus;
    }

    public void setBudgetingStatus(String budgetingStatus) {
        this.budgetingStatus = budgetingStatus;
    }

    public String getDesignCategory() {
        return designCategory;
    }

    public void setDesignCategory(String designCategory) {
        this.designCategory = designCategory;
    }

    public String getFounderId() {
        return founderId;
    }

    public void setFounderId(String founderId) {
        this.founderId = founderId;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getConstructionUnit() {
        return constructionUnit;
    }

    public void setConstructionUnit(String constructionUnit) {
        this.constructionUnit = constructionUnit;
    }

    public String getWaterAddress() {
        return waterAddress;
    }

    public void setWaterAddress(String waterAddress) {
        this.waterAddress = waterAddress;
    }
}
