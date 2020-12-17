package net.zlw.cloud.budgetTask.domain;

import lombok.Data;

/***
 * 成本编制/附件资料集合
 */
@Data
public class TotalMeansList {

    private String id;
    private String total_means_name;
    private String total_file_name;
    private String total_file_drawing;

}
