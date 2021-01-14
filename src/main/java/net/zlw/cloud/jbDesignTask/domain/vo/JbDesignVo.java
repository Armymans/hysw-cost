package net.zlw.cloud.jbDesignTask.domain.vo;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;

import java.util.List;

/***
 * 江北设计vo
 */
@Data
public class JbDesignVo {

    @JsonProperty("Id")
    private String Id;
    private String project_id;
    private String project_name;
    private String address;
//    private String area;
    private String customer_name;
    private String customer_address;
    private String design_category;
    private String subject;
    private String project_nature;
    private String contact_number;
    private String customer_phone;
//    private String customerEmail;
    private String agent;
    private String cea_num;
    private String amount_paid;
    private String designer;
    private String take_time;
    private String founder_id;
    private String create_time  ;
    private String remarks;
    private String exploration_ideal;
    private String exploration_time;
    private String scout;
    private String remark;

    @JsonProperty("DiameterInfo")
    private List<DiameterInfos> DiameterInfo;
    @JsonProperty("FileInfo")
    private List<FileInfos> FileInfo;

}
