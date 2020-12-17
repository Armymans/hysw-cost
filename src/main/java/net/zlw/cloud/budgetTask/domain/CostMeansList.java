package net.zlw.cloud.budgetTask.domain;

import lombok.Data;

/***
 * 预算编制/附件资料集合
 */
@Data
public class CostMeansList {

    private String id;
    private String cost_means_name;
    private String cost_file_name;
    private String cost_file_drawing;
}
