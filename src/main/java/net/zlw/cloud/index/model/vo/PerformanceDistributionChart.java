package net.zlw.cloud.index.model.vo;

import lombok.Data;
import net.zlw.cloud.index.model.IssuedDuringMmonth;
import net.zlw.cloud.index.model.PerformanceProvision;

import java.util.ArrayList;
import java.util.List;

@Data
public class PerformanceDistributionChart {
    //绩效计提
    private List<PerformanceProvision> plist = new ArrayList<PerformanceProvision>();
    //当月发放
    private List<IssuedDuringMmonth> ilist = new ArrayList<IssuedDuringMmonth>();
}
