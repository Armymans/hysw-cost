package net.zlw.cloud.maintenanceProjectInformation.model.vo;

import lombok.Data;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/8 19:40
 **/
@Data
public class StatisticalFigureVo {
    private Integer param1;
    private Integer param2;
    private Integer param3;
    private Integer param4;
    private Integer param5;
    private Integer param6;
    private Integer param7;
    private Integer param8;
    private Integer param9;
    private Integer param10;
    private String yeartime; //年份
    private String monthtime; //月份

    public StatisticalFigureVo() {
    }

    public StatisticalFigureVo(Integer param1, Integer param2, Integer param3, Integer param4, Integer param5, Integer param6, Integer param7, Integer param8, Integer param9, Integer param10) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.param8 = param8;
        this.param9 = param9;
        this.param10 = param10;
    }

    public StatisticalFigureVo(Integer param1, Integer param2, Integer param3, Integer param4, Integer param5, Integer param6, Integer param7, Integer param8, Integer param9, Integer param10, String yeartime, String monthtime) {
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.param5 = param5;
        this.param6 = param6;
        this.param7 = param7;
        this.param8 = param8;
        this.param9 = param9;
        this.param10 = param10;
        this.yeartime = yeartime;
        this.monthtime = monthtime;
    }
}
