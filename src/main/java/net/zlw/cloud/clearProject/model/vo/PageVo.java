package net.zlw.cloud.clearProject.model.vo;

import lombok.Data;

/**
 * @Author dell
 * @Date 2020/11/9 20:20
 * @Version 1.0
 */

public class PageVo {
    //页码
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
    // 内容
    private String keyWord;

    public PageVo() {
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
}
