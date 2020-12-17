package net.zlw.cloud.budgetTask.domain.vo;

import lombok.Data;
import net.zlw.cloud.budgetTask.domain.LabelMeansList;

import java.util.List;

/**
 * @Classname PriceControlVo
 * @Description //控价封装类
 * @Date 2020/11/6 18:04
 * @Created by sjf
 */
@Data
public class PriceControlVo {
    private String application_num;
    private String total_price_control_label;
    private String label_vat_amount;
    private String price_control_compilers;
    private String price_control_time;
    private String price_control_remark;
    private String price_examine_result;
    private String price_examine_opinion;
    private String price_examiner;
    private String price_examine_time;
    private String status;


    private List<LabelMeansList> LabelMeansList;

}
