package net.zlw.cloud.designTask.model.vo;

import lombok.Data;
import net.zlw.cloud.designTask.model.*;
import net.zlw.cloud.snsEmailFile.model.FileInfo;

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
    private String baseProjectId;
    private String applicationNum;
    private String distinct;
    private String projectName;
    private String constructionUnit;
    private String customerName;
    private String phone;
    private String subject;
    private String projectNature;
    private String projectCategory;
    private String waterAddress;
    private String waterSupplyType;
    private String agent;
    private String contactNumberOperator;
    private String applicationDate;
    private String businessLocation;
    private String businessTypes;
    private String waterUses;
    private String applicationNumber;
    private String shouldBe;
    private String status;
    private String projectRemark;
    private String waterSupply;
    private String thisTask;
    private String specialUser;
    private String userOfDay;
    private String timeOfUser;
    private String numberOfBuilding;
    private String numBuilding;
    private String buildingLayers;
    private String notesDrawingTime;
    private String blueprintCountersignTime;
    private String blueprintStartTime;
    private String virtualCode;


    private List<WatherList> waterList;
    private List<DeclarationInformation> declarationInformation;
    private List<OperationSubmitType> operationSubmitType;
    private List<SubmitOperationDept> submitOperationDept;
    private List<ProjectDesign> projectDesign;
    private List<CustomerProvidedFile> customerProvidedFile;
    private List<MangerDemand> mangerDemand;







}
