package net.zlw.cloud.index.model.vo;

import lombok.Data;
import net.zlw.cloud.index.model.CostSum;
import net.zlw.cloud.index.model.DesignSum;

@Data
public class StatisticalData {
    private DesignSum designSum;
    private CostSum costSum;

}
