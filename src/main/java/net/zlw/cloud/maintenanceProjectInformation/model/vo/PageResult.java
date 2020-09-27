package net.zlw.cloud.maintenanceProjectInformation.model.vo;

/**
 * @Author dell
 * @Date 2020/9/27 14:13
 * @Version 1.0
 */
public class PageResult {

    //0 失败 1 成功
    private Integer code;
    // 失败原因  或者 成功说明
    private String msg;
    //数据
    private Object data;

    // 数据的条总数
    private Long total;

    public PageResult() {
    }

    public PageResult(Integer code, String msg, Object data, Long total) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.total = total;
    }
}
