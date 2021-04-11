package net.zlw.cloud.settleAccounts.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.DateUtil;
import net.tec.cloud.common.util.FileUtil;
import net.tec.cloud.common.util.IdUtil;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.librarian.dao.ThoseResponsibleDao;
import net.zlw.cloud.librarian.model.ThoseResponsible;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.OtherInfo;
import net.zlw.cloud.settleAccounts.model.vo.AccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.BaseAccountsVo;
import net.zlw.cloud.settleAccounts.model.vo.PageVo;
import net.zlw.cloud.settleAccounts.service.SettleAccountsService;
import net.zlw.cloud.snsEmailFile.controller.FileInfoController;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

//@RequestMapping("/accounts")
@RestController
public class SettleAccountsController extends BaseController {
    @Resource
    private SettleAccountsService settleAccountsService;
    //造价单位名称
    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private LastSettlementReviewDao lastSettlementReviewDao;
    @Resource
    private FileInfoMapper fileInfoMapper;
    @Resource
    private ThoseResponsibleDao thoseResponsibleDao;
    @Value("${audit.wujiang.sheji.designHead}")
    private String wjsjh;
    @Value("${audit.wujiang.sheji.designManager}")
    private String wjsjm;
    @Value("${audit.wujiang.zaojia.costHead}")
    private String wjzjh;
    @Value("${audit.wujiang.zaojia.costManager}")
    private String wjzjm;

    @Value("${audit.wuhu.sheji.designHead}")
    private String whsjh;
    @Value("${audit.wuhu.sheji.designManager}")
    private String whsjm;
    @Value("${audit.wuhu.zaojia.costHead}")
    private String whzjh;
    @Value("${audit.wuhu.zaojia.costManager}")
    private String whzjm;


    //查询所有结算
//    @PostMapping("/findAllAccounts")
    @RequestMapping(value = "/accounts/findAllAccounts",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllAccounts(PageVo pageVo){
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo,getLoginUser());

        PageInfo<AccountsVo> accountsVoPageInfo = new PageInfo<>(allAccounts);
        return RestUtil.page(accountsVoPageInfo);
    }

    //结算金额
//    @PostMapping("/findAllAccounts")
    @RequestMapping(value = "/accounts/editOutsourceMoney",method = {RequestMethod.GET,RequestMethod.POST},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> editOutsourceMoney(String id , String upOutMoney,String downOutMoney){
        settleAccountsService.editOutsourceMoney(id,upOutMoney,downOutMoney);
        return RestUtil.success("编辑成功");
    }

    /**
     * @return
     * @Author sjf
     * @Description //结算列表 模糊查找
     * @Date 10:59 2020/11/22
     * @Param
     **/
    @RequestMapping(value = "/accounts/selectAllAccounts", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectAccounts(PageVo pageVo) {
        //全部
        Page page = new Page();
        pageVo.setSettleAccountsStatus("");
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo = new PageInfo<>(allAccounts);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());
        //待审核
        Page page1 = new Page();
        pageVo.setSettleAccountsStatus("1");
        List<AccountsVo> allAccounts1 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo1 = new PageInfo<>(allAccounts1);
        page1.setData(pageInfo1.getList());
        page1.setPageNum(pageInfo1.getPageNum());
        page1.setPageSize(pageInfo1.getPageSize());
        page1.setTotalCount(pageInfo1.getTotal());
        //处理中
        Page page2 = new Page();
        pageVo.setSettleAccountsStatus("2");
        List<AccountsVo> allAccounts2 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo2 = new PageInfo<>(allAccounts2);
        page2.setData(pageInfo2.getList());
        page2.setPageNum(pageInfo2.getPageNum());
        page2.setPageSize(pageInfo2.getPageSize());
        page2.setTotalCount(pageInfo2.getTotal());
        //未通过
        Page page3 = new Page();
        pageVo.setSettleAccountsStatus("3");
        List<AccountsVo> allAccounts3 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo3 = new PageInfo<>(allAccounts3);
        page3.setData(pageInfo3.getList());
        page3.setPageNum(pageInfo3.getPageNum());
        page3.setPageSize(pageInfo3.getPageSize());
        page3.setTotalCount(pageInfo3.getTotal());
        //待确认
        Page page4 = new Page();
        pageVo.setSettleAccountsStatus("4");
        List<AccountsVo> allAccounts4 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo4 = new PageInfo<>(allAccounts4);
        page4.setData(pageInfo4.getList());
        page4.setPageNum(pageInfo4.getPageNum());
        page4.setPageSize(pageInfo4.getPageSize());
        page4.setTotalCount(pageInfo4.getTotal());
        //已完成
        Page page5 = new Page();
        pageVo.setSettleAccountsStatus("5");
        List<AccountsVo> allAccounts5 = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo5 = new PageInfo<>(allAccounts5);
        page5.setData(pageInfo5.getList());
        page5.setPageNum(pageInfo5.getPageNum());
        page5.setPageSize(pageInfo5.getPageSize());
        page5.setTotalCount(pageInfo5.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1", page);
        map.put("table2", page1);
        map.put("table3", page2);
        map.put("table4", page3);
        map.put("table5", page4);
        map.put("table6", page5);
        return RestUtil.success(map);
    }

    @RequestMapping(value = "/accounts/selectAllAccounts2", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectAccounts2(PageVo pageVo) {
        //全部
        Page page = new Page();
        if (StringUtils.isEmpty(pageVo.getSettleAccountsStatus()) || pageVo.getSettleAccountsStatus().equals("0")){
            pageVo.setSettleAccountsStatus("");
        }
        List<AccountsVo> allAccounts = settleAccountsService.findAllAccounts(pageVo, getLoginUser());
        PageInfo<AccountsVo> pageInfo = new PageInfo<>(allAccounts);
        page.setData(pageInfo.getList());
        page.setPageNum(pageInfo.getPageNum());
        page.setPageSize(pageInfo.getPageSize());
        page.setTotalCount(pageInfo.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1", page);

        return RestUtil.success(map);
    }

    //结算项目删除
//    @DeleteMapping("/deleteAcmcounts")
    @RequestMapping(value = "/accounts/deleteAcmcounts", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> deleteAcmcounts(@RequestParam(name = "id") String id) {
        try {
            settleAccountsService.deleteAcmcounts(id,getLoginUser(),request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error();
        }
        return RestUtil.success();
    }

    //结算项目到账
//    @PutMapping("/updateAccount")
    @RequestMapping(value = "/accounts/updateAccount", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateAccount(@RequestParam(name = "id",required = false) String id,@RequestParam(name = "checkWhether") String checkWhether,@RequestParam(name = "id2",required = false) String id2) {

        ThoseResponsible thoseResponsible = thoseResponsibleDao.selectByPrimaryKey("2");
        String personnel = thoseResponsible.getPersonnel();
        String user = getLoginUser().getId();
        boolean f = false;
        if (personnel!=null){
            String[] split = personnel.split(",");
            for (String s : split) {
                if (s.equals(user)){
                    f = true;
                }
            }
        }
        try {
            if (!user.equals(whzjh) && !user.equals(whzjm) && !user.equals(wjzjh) && !f){
                throw new RuntimeException("您没有权限进行此操作,请联系领导或领导指定人进行操作");
            }
        } catch (RuntimeException e) {
            return RestUtil.error(e.getMessage());
        }

        ArrayList<String> split = new ArrayList<>();
        if (id!=null){
            String[] split1 = id.split(",");
            for (String s : split1) {
                split.add(s);
            }
        }
        if (id2!=null){
            String[] split2 = id2.split(",");
            for (String s : split2) {
                split.add(s);
            }
        }


        for (String s : split) {
            try {
                BaseProject baseProject = baseProjectDao.selectByPrimaryKey(s);
                String designCategory = baseProject.getDesignCategory();
                String district = baseProject.getDistrict();
                Example example = new Example(LastSettlementReview.class);
                Example.Criteria criteria = example.createCriteria();
                criteria.andEqualTo("baseProjectId",s);
                criteria.andEqualTo("delFlag","0");
                LastSettlementReview lastSettlementReview = lastSettlementReviewDao.selectOneByExample(example);
                String preparePeople = lastSettlementReview.getPreparePeople();
                if (designCategory == null || "".equals(designCategory)){
                    throw new RuntimeException("您所选的项目中有未归属的项目,请填写完归属后在重新尝试");
                }
                if (district == null || "".equals(district)){
                    throw new RuntimeException("您所选的项目中有未归属的项目,请填写完归属后在重新尝试");
                }
                if (preparePeople == null || "".equals(preparePeople)){
                    throw new RuntimeException("您所选的项目中有未归属的项目,请填写完归属后在重新尝试");
                }

                settleAccountsService.updateAccount(s,getLoginUser(),checkWhether);
            } catch (Exception e) {
                return RestUtil.error(e.getMessage());
            }
        }

        return RestUtil.success();
    }

    //结算新增
//    @PostMapping("/addAccount")
    @RequestMapping(value = "/accounts/addAccount", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> addAccount(BaseAccountsVo baseAccountsVo) {
        try {
            settleAccountsService.addAccount(baseAccountsVo, getLoginUser(),request);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    //结算新增编制人回显
//    @PostMapping("/addAccount")
    @RequestMapping(value = "/accounts/selectPeople", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectPeople() {
        LastSettlementReview lastSettlementReview = settleAccountsService.selectPeople(getLoginUser());
        return RestUtil.success(lastSettlementReview);
    }

    //根据ID查询结算
//    @GetMapping("/findAccountById/{id}")
    @RequestMapping(value = "/accounts/findAccountById", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> findAccountById(@RequestParam(name = "id", required = false) String id) {
        if (id == null) {
            return RestUtil.error();
        }
        BaseAccountsVo accountsVo = settleAccountsService.findAccountById(id, getLoginUser());
        String nameOfTheCost = accountsVo.getLastSettlementReview().getNameOfTheCost();
        if (nameOfTheCost!=null && !"".equals(nameOfTheCost)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfTheCost);
            if (costUnitManagement!=null){
                accountsVo.getLastSettlementReview().setNameOfTheCost(costUnitManagement.getCostUnitName());
            }
        }
        String nameOfTheCost1 = accountsVo.getSettlementAuditInformation().getNameOfTheCost();
        if (nameOfTheCost1!=null && !"".equals(nameOfTheCost1)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfTheCost1);
            if (costUnitManagement!=null){
                accountsVo.getSettlementAuditInformation().setNameOfTheCost(costUnitManagement.getCostUnitName());
            }
        }
        String sumbitMoney = accountsVo.getSettlementInfo().getSumbitMoney();
        BigDecimal subtractTheNumber = accountsVo.getSettlementAuditInformation().getSubtractTheNumber();
        BigDecimal bigDecimal = null;
        if (sumbitMoney!=null){
            bigDecimal = new BigDecimal(sumbitMoney);
        }
        System.err.println(subtractTheNumber);
        System.err.println(bigDecimal);
        if (subtractTheNumber!=null && bigDecimal!=null){
            BigDecimal divide = subtractTheNumber.divide(bigDecimal,4,RoundingMode.HALF_UP);
            BigDecimal multiply = divide.multiply(new BigDecimal(100));
            BigDecimal bigDecimal1 = multiply.setScale(2, RoundingMode.HALF_UP);
            accountsVo.getSettlementAuditInformation().setSubtractRate(bigDecimal1);
        }
        return RestUtil.success(accountsVo);
    }

    //结算编辑
//    @PutMapping("/updateAccountById")
    @RequestMapping(value = "/accounts/updateAccountById", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> updateAccountById(@RequestBody BaseAccountsVo baseAccountsVo) {
        try {
            settleAccountsService.updateAccountById(baseAccountsVo, getLoginUser(),request);
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }

    //结算批量审核
    @RequestMapping(value = "/accounts/batchReview", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> batchReview(BatchReviewVo batchReviewVo) {
        settleAccountsService.batchReview(batchReviewVo,getLoginUser(),request);
        return RestUtil.success();
    }

    //其他信息查看
    @RequestMapping(value = "/otherInfo/selectInfoList", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> selectInfoList(String baseId) {
        List<OtherInfo> otherInfos = settleAccountsService.selectInfoList(baseId);
        return RestUtil.success(otherInfos);
    }
    //归属
    @RequestMapping(value = "/accounts/addAttribution", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addAttribution(@RequestParam(name = "baseProjectId") String baseId,@RequestParam(name = "district") String district,@RequestParam(name = "designCategory") String designCategory,@RequestParam(name = "prePeople") String prePeople){
        settleAccountsService.addAttribution(baseId,district,designCategory,prePeople);
        return RestUtil.success();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    private static final transient Logger log = LoggerFactory.getLogger(FileInfoController.class);
    @Value("${app.attachPath}")
    private String LixAttachDir;
    @Value("${app.testPath}")
    private String WinAttachDir;
    //结算上家结算汇总表 导入
    @RequestMapping(value = "/accounts/addUniProjectImport", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addUniProjectImport(@RequestParam("file") MultipartFile file, String type){

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
                    settleAccountsService.addUniProjectImport(attachInfo.getId(),inputStream,inputStream2);
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
    //结算 下家结算汇总
    @RequestMapping(value = "/accounts/addsettleImport", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addsettleImport(@RequestParam("file") MultipartFile file, String type){

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
                    settleAccountsService.addsettleImport(attachInfo.getId(),inputStream,inputStream2,inputStream3,inputStream4,inputStream5);
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
    //确认完成
    @RequestMapping(value = "/accounts/accountsSuccess", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> accountsSuccess(@RequestParam(name = "ids") String ids){
        try {
            settleAccountsService.accountsSuccess(ids,getLoginUser().getId());
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }
    //退回
    @RequestMapping(value = "/accounts/accountsBack", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> accountsBack(@RequestParam(name = "id") String id,@RequestParam(name = "backOpnion") String backOpnion){
        settleAccountsService.backOpnion(id,backOpnion);
        return RestUtil.success();
    }
    //退回殷丽萍
    @RequestMapping(value = "/accounts/accountsBackB", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> accountsBackB(@RequestParam(name = "id") String id,@RequestParam(name = "backOpnion") String backOpnion){
        settleAccountsService.backOpnionB(id,backOpnion,getLoginUser().getId());
        return RestUtil.success();
    }
}
