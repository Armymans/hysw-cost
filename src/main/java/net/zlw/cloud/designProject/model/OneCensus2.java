package net.zlw.cloud.designProject.model;

import lombok.Data;

@Data
public class OneCensus2 {
    private String yeartime;
    private String monthTime;
    private Integer budget;
    private Integer track;
    private Integer visa;
    private Integer progresspayment;
    private Integer settleaccounts;
    private Integer total;

    private Double totals;
}
