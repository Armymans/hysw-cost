package net.zlw.cloud.common;

import lombok.Data;

/**
 * @Author Armyman
 * @Description 搜索返回
 * @Date 2020/11/20 16:19
 **/
@Data
public class Page {
    private Object data;
    private Integer pageNum;
    private Integer pageSize;
    private Long totalCount;
}
