package net.zlw.cloud.VisaChange.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class VisaReturnStatistic {
   private List<VisaChangeStatisticVo> list = new ArrayList<>();

   private Integer totalChangeNum;
   private BigDecimal totalVisaChangeUpAmount;
   private BigDecimal totalVisaChangeDownAmount;
   private String totalVisaChangeUpProportionContract;
   private String totalVisaChangeDownProportionContract;

}
