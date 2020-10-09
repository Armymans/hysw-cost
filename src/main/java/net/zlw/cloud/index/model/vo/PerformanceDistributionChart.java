package net.zlw.cloud.index.model.vo;

import lombok.Data;
import net.zlw.cloud.index.model.IssuedDuringMmonth;
import net.zlw.cloud.index.model.PerformanceProvision;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceDistributionChart {
    //年
    private String YearTime;
    //月
    private String MonthTime;
    //绩效计提
    private BigDecimal PerformanceProvision;
    //当月发放
    private BigDecimal IssuedDuringMmonth;
}
