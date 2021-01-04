package net.zlw.cloud.whDesignTask.model.vo;

import lombok.Data;
import net.zlw.cloud.jbDesignTask.domain.DiameterInfo;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.whDesignTask.model.*;
import springfox.documentation.service.ApiListing;

import java.util.List;

/**
 * @Classname DesignVo
 * @Description TODO
 * @Date 2020/10/29 9:23
 * @Created sjf
 */
@Data
public class DesignVo {

    private String id;
    private String base_project_id;
    private String application_num;
    private String distinct;
    private String project_name;
    private String construction_unit;
    private String customer_name;
    private String phone;
    private String subject;
    private String project_nature;
    private String project_category;
    private String water_address;
    private String water_supply_type;
    private String agent;
    private String contact_number_operator;
    private String application_date;
    private String business_location;
    private String business_types;
    private String water_uses;
    private String application_number;
    private String should_be;
    private String status;
    private String project_remark;
    private String water_supply;
    private String this_task;
    private String special_user;
    private String userOf_day;
    private String timeOf_user;
    private String number_of_building;
    private String num_building;
    private String building_layers;
    private String notes_drawing_time;
    private String blueprint_countersign_time;
    private String blueprint_start_time;
    private String virtual_code;


    private List<WatherList> waterList;
    private List<DeclarationInformation> declaration_information;
    private List<OperationSubmitType> OperationSubmitType;
    private List<SubmitOperationDept> SubmitOperationDept;
    private List<ProjectDesign> projectDesign;
    private List<CustomerProvidedFile> customerProvidedFile;
    private List<MangerDemand> MangerDemand;







}
