package net.zlw.cloud.whFinance.domain.vo;

import lombok.Data;

/**
 * @author: hjc
 * @date: 2021-02-25 18:48
 * @desc:
 */

@Data
public class BomTableVo2 {

    private String id;

    private String material_code;

    private String item_name;

    private String specifications_models;

    private String unit;

    private String univalence;

    private String quantity;

    private String combined_price;

    private String remark;

    private String bomTableInfomationId;

    private String createTime;

    private String updateTime;

    private String founderId;

    private String founderCompanyId;

    private String delFlag;

}
