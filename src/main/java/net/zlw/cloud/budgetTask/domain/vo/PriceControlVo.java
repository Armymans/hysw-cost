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
    private String applicationNum;
    private String totalPriceControlLabel;
    private String labelVatAmount;
    private String priceControlCompilers;
    private String priceControlTime;
    private String priceControlRemark;
    private String priceExamineResult;
    private String priceExamineOpinion;
    private String priceExaminer;
    private String priceExamineTime;
    private String status;


    private List<LabelMeansList> labelMeansList;

}
