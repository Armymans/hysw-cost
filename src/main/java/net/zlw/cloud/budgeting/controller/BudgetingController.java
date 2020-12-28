package net.zlw.cloud.budgeting.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.DateUtil;
import net.tec.cloud.common.util.FileUtil;
import net.tec.cloud.common.util.IdUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.budgeting.model.vo.*;
import net.zlw.cloud.budgeting.service.BudgetingService;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.excel.dao.PartTableQuantitiesDao;
import net.zlw.cloud.excel.dao.SummaryShenjiDao;
import net.zlw.cloud.excel.dao.SummaryUnitsDao;
import net.zlw.cloud.excel.service.BudgetCoverService;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.controller.FileInfoController;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
//@RequestMapping("/budgeting")
public class BudgetingController extends BaseController {
    @Resource
    private BudgetingService budgetingService;
    private static String founderId;



    @Autowired
    private MemberManageDao memberManageDao;
    @Autowired
    private AuditInfoDao auditInfoDao;
    @Resource
    private MkyUserMapper mkyUserMapper;
    @Resource
    private FileInfoService fileInfoService;
    @Resource
    private BudgetCoverService budgetCoverService;
    @Resource
    private SummaryShenjiDao summaryShenjiDao;
    @Resource
    private SummaryUnitsDao summaryUnitsDaol;
    @Resource
    private PartTableQuantitiesDao partTableQuantitiesDao;
    @Resource
    private FileInfoMapper fileInfoMapper;



    //添加预算信息
//    @PostMapping("/addBudgeting")
    @RequestMapping(value = "/budgeting/addBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addBudgeting(BudgetingVo budgetingVo){
        try {
            UserInfo loginUser = getLoginUser();
            budgetingService.addBudgeting(budgetingVo,loginUser,request);
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("添加成功");
    }
    //新增回显预算编制人
//    @PostMapping("/addBudgeting")
    @RequestMapping(value = "/budgeting/budgetingPeople",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> budgetingPeople(){
        Budgeting  budgeting =  budgetingService.budgetingPeople(getLoginUser().getId());
        return RestUtil.success(budgeting);
    }
    //根据ID查询预算信息
//    @GetMapping("/selectBudgetingById/{id}")
    @RequestMapping(value = "/budgeting/selectBudgetingById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectBudgetingById(@RequestParam(name = "id") String id){
        BudgetingVo budgetingVo = budgetingService.selectBudgetingById(id,getLoginUser());
        return RestUtil.success(budgetingVo);
    }
    //编辑预算信息
//    @PutMapping("/updateBudgeting")
    @RequestMapping(value = "/budgeting/updateBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateBudgeting(BudgetingVo budgetingVo){
        budgetingService.updateBudgeting(budgetingVo,getLoginUser(),request);
        return RestUtil.success("修改成功");
    }
    //批量审核
//    @PostMapping("/batchReview")
    @RequestMapping(value = "/budgeting/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        budgetingService.batchReview(batchReviewVo,getLoginUser(),request);
        return RestUtil.success("审核成功");
    }

    //预算到账
    @RequestMapping(value = "/budgeting/intoAccount",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> intoAccount(@RequestParam(name = "ids") String ids){

        try {
            budgetingService.intoAccount(ids,getLoginUser().getId());
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success("成功");
    }
    //查询所有
    @RequestMapping(value = "/budgeting/findAllBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllBudgeting(PageBVo pageBVo){
        UserInfo id1 = getLoginUser();
        if (id1!=null){
            founderId = id1.getId();
        }
        if (founderId!=null){
            pageBVo.setFounderId(founderId);
        }

        PageHelper.startPage(pageBVo.getPageNum(),pageBVo.getPageSize());
        List<BudgetingListVo> list = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
//        PageInfo<BudgetingListVo> list = budgetingService.findAllBudgeting(pageBVo,"user324");
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(list);

        return RestUtil.page(budgetingListVoPageInfo);
    }

    /**
        * @Author sjf
        * @Description //预算列表 模糊搜索
        * @Date 9:41 2020/11/22
        * @Param
        * @return
     **/
    @RequestMapping(value = "/budgeting/selectByBudgeting",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectByBudgeting(PageBVo pageBVo){
        //全部
        Page page = new Page();
        pageBVo.setBudgetingStatus("");
        List<BudgetingListVo> allBudgeting = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo = new PageInfo<>(allBudgeting);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        //待审核
        Page page1 = new Page();
        pageBVo.setBudgetingStatus("1");
        List<BudgetingListVo> allBudgeting1 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo1 = new PageInfo<>(allBudgeting1);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());

        //处理中
        Page page2 = new Page();
        pageBVo.setBudgetingStatus("2");
        List<BudgetingListVo> allBudgeting2 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo2 = new PageInfo<>(allBudgeting2);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());

        //未通过
        Page page3 = new Page();
        pageBVo.setBudgetingStatus("3");
        List<BudgetingListVo> allBudgeting3 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo3 = new PageInfo<>(allBudgeting3);
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());

        //已完成
        Page page4 = new Page();
        pageBVo.setBudgetingStatus("4");
        List<BudgetingListVo> allBudgeting4 = budgetingService.findAllBudgeting(pageBVo, getLoginUser().getId());
        PageInfo<BudgetingListVo> pageInfo4 = new PageInfo<>(allBudgeting4);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);

        return RestUtil.success(map);

    }

    //选择项目查看所有预算
    @RequestMapping(value = "/budgeting/findBudgetingAll",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findBudgetingAll(PageBVo pageBVo,@RequestParam(name = "sid",required = false) String sid){

        PageHelper.startPage(pageBVo.getPageNum(),999);
        List<BudgetingListVo> list = budgetingService.findBudgetingAll(pageBVo,sid);
        PageInfo<BudgetingListVo> budgetingListVoPageInfo = new PageInfo<>(list);
        return RestUtil.page(budgetingListVoPageInfo);
    }
    //预算合并查询
    @RequestMapping(value = "/budgeting/unionQuery",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> unionQuery(@RequestParam(name = "baseId") String id){
       UnionQueryVo unionQueryVo =  budgetingService.unionQuery(id,getLoginUser());
       return RestUtil.success(unionQueryVo);
    }
    //预算编制单条审核
    @RequestMapping(value = "/budgeting/singleAudit",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> singleAudit(SingleAuditVo singleAuditVo){
        budgetingService.singleAudit(singleAuditVo);
        return RestUtil.success();
    }
    //归属
    @RequestMapping(value = "/budgeting/addAttribution",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addAttribution(@RequestParam(name = "baseId") String id,@RequestParam(name = "designCategory") String designCategory,@RequestParam(name = "district") String district,@RequestParam(name = "prePeople") String prePeople){
        budgetingService.addAttribution(id,designCategory,district,prePeople);
        return RestUtil.success();
    }
    //选择项目查看设计
    @RequestMapping(value = "/budgeting/findDesignAll",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findDesignAll(PageBVo pageBVo){
        PageHelper.startPage(pageBVo.getPageNum(),pageBVo.getPageSize());
       List<DesignInfo> list = budgetingService.findDesignAll(pageBVo);
        for (DesignInfo designInfo : list) {
            String designer = designInfo.getDesigner();
            MkyUser mkyUser = mkyUserMapper.selectByPrimaryKey(designer);
            if (mkyUser!=null){
                designInfo.setDesigner(mkyUser.getUserName());
            }
        }
        PageInfo<DesignInfo> designInfoPageInfo = new PageInfo<>(list);
        return RestUtil.page(designInfoPageInfo);
    }
    //删除预算
    @RequestMapping(value = "/budgeting/deleteBudgeting",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteBudgeting(@RequestParam(name = "id") String id){
        budgetingService.deleteBudgeting(id,getLoginUser(),request);
        return RestUtil.success();
    }
    //新建删除所有文件
    @RequestMapping(value = "/budgeting/deleteBudgetingFile",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteBudgetingFile(@RequestParam(name = "id") String id){
        budgetingService.deleteBudgetingFile(id);
        return RestUtil.success();
    }
    //编辑CEA编号
    @RequestMapping(value = "/budgeting/updateCEA",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateCEA(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "ceaNum") String ceaNum){
        budgetingService.updateCEA(baseId,ceaNum);
        return RestUtil.success();
    }
    /**
        * @Author sjf
        * @Description // 委外金额
        * @Date 15:41 2020/12/28
        * @Param
        * @return
     **/
    @RequestMapping(value = "/budgeting/editOutSourceMoney",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editOutSourceMoney(@RequestParam(name = "id") String id,@RequestParam(name = "amountOutsourcing") String amountOutsourcing){
        budgetingService.editOutSourceMoney(id,amountOutsourcing);
        return RestUtil.success();
    }
    //回显设计图纸
    @RequestMapping(value = "/budgeting/selectOneFile",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectOneByFileInfo(@RequestParam(name = "id") String id){
        List<FileInfo> fileInfos = budgetingService.selectById(id);
        return RestUtil.success(fileInfos);
    }

    //编制人下拉框
    @RequestMapping(value = "/budgeting/findPreparePeople",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findPreparePeople(){
      List<MkyUser> list =   budgetingService.findPreparePeople(getLoginUser().getId());
      return RestUtil.success(list);
    }



//    @RequestMapping(value = "/updateFileName", method = {RequestMethod.GET,RequestMethod.POST})
//    public Map<String, Object> updateFileName(@RequestParam("file") MultipartFile multipartFile,@RequestParam(name = "code") String code, @RequestParam(name = "id") String id, @RequestParam(name = "name") String name, @RequestParam(name = "remark") String remark, @RequestParam(name = "rtype") String rtype) {
//        FileInfo fileInfo = fileInfoService.getByKey(id);
//        fileInfo.setName(name);
//        fileInfo.setRemark(remark);
//        fileInfo.setPlatCode(code);
//        fileInfo.setUpdateTime(DateUtil.getDateTime());
//        fileInfoService.updateFileName(fileInfo);
//
//        //新增
//        if (rtype.equals("1")){
//
//
//            budgetCoverService.addbudgetAll(id,null);
//        }else {
//            Example example = new Example(SummaryShenji.class);
//            Example.Criteria criteria = example.createCriteria();
//            criteria.andEqualTo("budgetId",id);
//            criteria.andEqualTo("delFlag","0");
//            List<SummaryShenji> summaryShenjis = summaryShenjiDao.selectByExample(example);
//            for (SummaryShenji summaryShenji : summaryShenjis) {
//
//
//                summaryShenjiDao.deleteByPrimaryKey(summaryShenji);
//            }
//            Example example1 = new Example(SummaryUnits.class);
//            Example.Criteria criteria1 = example1.createCriteria();
//            criteria1.andEqualTo("budgetingId",id);
//            criteria1.andEqualTo("delFlag","0");
//            List<SummaryUnits> summaryUnits = summaryUnitsDaol.selectByExample(example1);
//            for (SummaryUnits summaryUnit : summaryUnits) {
//                summaryUnitsDaol.deleteByPrimaryKey(summaryUnit);
//            }
//            Example example2 = new Example(PartTableQuantities.class);
//            Example.Criteria criteria2 = example2.createCriteria();
//            criteria2.andEqualTo("foreignKey",id);
//            criteria2.andEqualTo("delFlag","0");
//            List<PartTableQuantities> partTableQuantities = partTableQuantitiesDao.selectByExample(example2);
//            for (PartTableQuantities partTableQuantity : partTableQuantities) {
//                partTableQuantitiesDao.deleteByPrimaryKey(partTableQuantity);
//            }
//            budgetCoverService.addbudgetAll(id,fileInfo.getFilePath());
//        }
//
//        return RestUtil.success("修改成功");
//    }



    //预算文件上传()预算汇总表
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    private static final transient Logger log = LoggerFactory.getLogger(FileInfoController.class);
    @Value("${app.attachPath}")
    private String LixAttachDir;
    @Value("${app.testPath}")
    private String WinAttachDir;

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Map<String, Object> applyUpload1(@RequestParam("file") MultipartFile file, String type) throws IOException {

        MultipartFile aaa = file;

        log.info(getLogInfo("upload", file));
        FileInfo attachInfo = new FileInfo();
        try {

            String fileName = new String(FileUtil.getFileName(file).getBytes(), "UTF-8");
            String fileType = FileUtil.getFileExtName(file);

            String fileDir = "/" + sdf2.format(new Date());

            String tmpFileName = IdUtil.uuid2().substring(0, 15) + sdf.format(new Date()) + "." + fileType;
            String filePath = fileDir + "/" + tmpFileName;
            //上传路径
            String path = "";
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                path = WinAttachDir;
            } else {
                path = LixAttachDir;
            }

            File outDir = new File(path + fileDir);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            log.info(outDir.getAbsolutePath());
            File targetFile = new File(outDir.getAbsolutePath(), tmpFileName);

            attachInfo.setFileName(fileName);
            attachInfo.setFilePath(filePath);
            attachInfo.setFileSource("1");
            attachInfo.setFileType(fileType);
            attachInfo.setType(type);
            attachInfo.setCreateTime(DateUtil.getDateTime());
            attachInfo.setStatus("1");
            attachInfo.setUserId(getLoginUser().getId());
            attachInfo.setStatus("0");
            attachInfo.setCompanyId(getLoginUser().getCompanyId());
            //添加到数据库


            FileInputStream inputStream = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream2 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream3 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream4 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream5 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream6 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream7 = (FileInputStream) aaa.getInputStream();


            //将文件与企业材料管理进行关联
            try {
                attachInfo.setId("file"+ UUID.randomUUID().toString().replaceAll("-", ""));
                fileInfoMapper.insert(attachInfo);

                if (aaa.getOriginalFilename().contains("吴江")){

                }else if(aaa.getOriginalFilename().contains("安徽")){

                    if (aaa.getOriginalFilename().contains("神机")){
                        //神机
                        budgetCoverService.addbudgetAll(attachInfo.getId(),inputStream,inputStream2,inputStream3);
                    }else if(aaa.getOriginalFilename().contains("新点")){
                        //新点
                        budgetCoverService.addbudgetAllXindian(attachInfo.getId(),inputStream,inputStream2,inputStream3,inputStream4,inputStream5,inputStream6,inputStream7);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error("操作异常,请联系管理员!");
        }





        Map<String, String> map = new HashMap<>();
        map.put("id",attachInfo.getId());
        map.put("name",attachInfo.getFileName()+"."+attachInfo.getFileType());
        return RestUtil.success(map);
    }

    //物料清单导入
    @RequestMapping(value = "/uploadFile1", method = RequestMethod.POST)
    public Map<String, Object> uploadFile1(@RequestParam("file") MultipartFile file, String type) throws IOException {

        MultipartFile aaa = file;

        log.info(getLogInfo("upload", file));
        FileInfo attachInfo = new FileInfo();
        try {

            String fileName = new String(FileUtil.getFileName(file).getBytes(), "UTF-8");
            String fileType = FileUtil.getFileExtName(file);

            String fileDir = "/" + sdf2.format(new Date());

            String tmpFileName = IdUtil.uuid2().substring(0, 15) + sdf.format(new Date()) + "." + fileType;
            String filePath = fileDir + "/" + tmpFileName;
            //上传路径
            String path = "";
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                path = WinAttachDir;
            } else {
                path = LixAttachDir;
            }

            File outDir = new File(path + fileDir);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            log.info(outDir.getAbsolutePath());
            File targetFile = new File(outDir.getAbsolutePath(), tmpFileName);

            attachInfo.setFileName(fileName);
            attachInfo.setFilePath(filePath);
            attachInfo.setFileSource("1");
            attachInfo.setFileType(fileType);
            attachInfo.setType(type);
            attachInfo.setCreateTime(DateUtil.getDateTime());
            attachInfo.setStatus("1");
            attachInfo.setUserId(getLoginUser().getId());
            attachInfo.setStatus("0");
            attachInfo.setCompanyId(getLoginUser().getCompanyId());
            //添加到数据库


            FileInputStream inputStream = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream2 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream3 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream4 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream5 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream6 = (FileInputStream) aaa.getInputStream();
            FileInputStream inputStream7 = (FileInputStream) aaa.getInputStream();


            //将文件与企业材料管理进行关联
            try {
                attachInfo.setId("file"+ UUID.randomUUID().toString().replaceAll("-", ""));
                fileInfoMapper.insert(attachInfo);

                if (aaa.getOriginalFilename().contains("吴江")){

                }else if(aaa.getOriginalFilename().contains("安徽")){
                    budgetCoverService.bomTableImport(attachInfo.getId(),inputStream);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error("操作异常,请联系管理员!");
        }





        Map<String, String> map = new HashMap<>();
        map.put("id",attachInfo.getId());
        map.put("name",attachInfo.getFileName()+"."+attachInfo.getFileType());
        return RestUtil.success(map);
    }

}


