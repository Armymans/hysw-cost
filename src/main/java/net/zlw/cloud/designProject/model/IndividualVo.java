package net.zlw.cloud.designProject.model;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class IndividualVo {
    private String id;
    private String district;
    private String startTime;
    private String endTime;
    private Integer pageSize;
    private Integer pageNum;
}
