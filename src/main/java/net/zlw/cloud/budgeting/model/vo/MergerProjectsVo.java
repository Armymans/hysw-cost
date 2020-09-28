package net.zlw.cloud.budgeting.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class MergerProjectsVo {
    //虚拟项目编号
    private String serialNumbers;
    private List<String > projectNumList;


}
