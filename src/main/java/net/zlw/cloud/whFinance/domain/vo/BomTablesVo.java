package net.zlw.cloud.whFinance.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: hjc
 * @date: 2021-02-25 17:31
 * @desc:
 */
@Data
public class BomTablesVo {

    @JsonProperty("BomTableInfomation")
    private List<BomTableVo> BomTableInfomation;

}
