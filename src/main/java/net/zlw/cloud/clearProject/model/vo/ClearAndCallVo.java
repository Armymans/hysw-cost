package net.zlw.cloud.clearProject.model.vo;

import lombok.Data;
import net.zlw.cloud.clearProject.model.CallForBids;

/**
 * @Classname ClearAndCallVo
 * @Description TODO
 * @Date 2020/12/19 16:53
 * @Created by sjf
 */
@Data
public class ClearAndCallVo {

    private String projectNum;
    private String projectName;
    private CallForBids callForBids;

}
