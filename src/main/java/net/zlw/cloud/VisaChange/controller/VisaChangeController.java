package net.zlw.cloud.VisaChange.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.VisaChange.model.vo.*;
import net.zlw.cloud.VisaChange.service.VisaChangeService;
import net.zlw.cloud.budgeting.model.vo.BatchReviewVo;
import net.zlw.cloud.common.Page;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.progressPayment.service.BaseProjectService;
import net.zlw.cloud.settleAccounts.mapper.CostUnitManagementMapper;
import net.zlw.cloud.settleAccounts.model.CostUnitManagement;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 签证变更
 */
@RestController
public class VisaChangeController extends BaseController {

    @Autowired
    private VisaChangeService vcisService;

    @Autowired
    private BaseProjectService baseProjectService;

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

    @Resource
    private MkyUserMapper mkyUserMapper;
    @Resource
    private CostUnitManagementMapper costUnitManagementMapper;
    @Resource
    private VisaChangeService visaChangeService;

    //查询所有

    @RequestMapping(value = "/visachange/findAllVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllVisa(PageVo pageVo){
        String id = getLoginUser().getId();
//        String id = "200101005";
        pageVo.setUserId(id);
        //TODO id测试写死
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<VisaChangeListVo> list =  vcisService.findAllVisa(pageVo);
        for (VisaChangeListVo visaChangeListVo : list) {
            String outsourcing = visaChangeListVo.getOutsourcing();


            List<VisaChangeStatisticVo> list1 =  vcisService.findAllchangeStatistics(visaChangeListVo.getBaseProjectId());
            BigDecimal upAmount = new BigDecimal(0);
            BigDecimal downAmount = new BigDecimal(0);
            double upPro = 0.0;
            double downPro = 0.0;
            VisaReturnStatistic visaReturnStatistic = new VisaReturnStatistic();
            visaReturnStatistic.setList(list1);
            for (VisaChangeStatisticVo visaChangeStatisticVo : list1) {
                if (visaChangeStatisticVo.getVisaChangeUpAmount()!=null){
                    upAmount = upAmount.add(visaChangeStatisticVo.getVisaChangeUpAmount());
                }
                if (visaChangeStatisticVo.getVisaChangeDownAmount()!=null){
                    downAmount = downAmount.add(visaChangeStatisticVo.getVisaChangeDownAmount());
                }
                if (visaChangeStatisticVo.getVisaChangeUpProportionContract()!=null && !"".equals(visaChangeStatisticVo.getVisaChangeUpProportionContract())){
                    upPro+=Double.parseDouble(visaChangeStatisticVo.getVisaChangeUpProportionContract());
                }

                if (visaChangeStatisticVo.getVisaChangeDownProportionContract()!=null && !"".equals(visaChangeStatisticVo.getVisaChangeDownProportionContract())){
                   downPro+=Double.parseDouble(visaChangeStatisticVo.getVisaChangeDownProportionContract());
                }
            }
            if (list1.size()>=1){
                visaReturnStatistic.setTotalChangeNum(list1.get(list1.size()-1).getChangeNum());
            }
            double upPro1 = upPro;
            double downPro1 = downPro;
            double v = new BigDecimal(upPro1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double v2 = new BigDecimal(downPro1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            visaReturnStatistic.setTotalVisaChangeUpAmount(upAmount);
            visaReturnStatistic.setTotalVisaChangeDownAmount(downAmount);
            visaReturnStatistic.setTotalVisaChangeUpProportionContract(v+"");
            visaReturnStatistic.setTotalVisaChangeDownProportionContract(v2+"");

            visaChangeListVo.setAmountVisaChangeAddShang(visaReturnStatistic.getTotalVisaChangeUpAmount().toString());
            visaChangeListVo.setAmountVisaChangeAddXia(visaReturnStatistic.getTotalVisaChangeDownAmount().toString());

        }
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo = new PageInfo<>(list);


        return RestUtil.page(visaChangeListVoPageInfo);
    }

    /**
     * @Author sjf
     * @Description //签证变更 模糊搜索
     * @Date 10:34 2020/11/22
     * @Param
     * @return
     **/
    @RequestMapping(value = "/visachange/selectVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectVisa(PageVo pageVo){

        String id = getLoginUser().getId();
        pageVo.setUserId(id);
        Page page = new Page();
        pageVo.setStatus("");
        List<VisaChangeListVo> allVisa = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo = new PageInfo<>(allVisa);
        page.setData(visaChangeListVoPageInfo.getList());
        page.setPageNum(visaChangeListVoPageInfo.getPageNum());
        page.setPageSize(visaChangeListVoPageInfo.getPageSize());
        page.setTotalCount(visaChangeListVoPageInfo.getTotal());

        Page page1 = new Page();
        pageVo.setStatus("1");
        List<VisaChangeListVo> allVisa1 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo1 = new PageInfo<>(allVisa1);
        page1.setData(visaChangeListVoPageInfo1.getList());
        page1.setPageNum(visaChangeListVoPageInfo1.getPageNum());
        page1.setPageSize(visaChangeListVoPageInfo1.getPageSize());
        page1.setTotalCount(visaChangeListVoPageInfo1.getTotal());

        Page page2 = new Page();
        pageVo.setStatus("2");
        List<VisaChangeListVo> allVisa2 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo2 = new PageInfo<>(allVisa2);
        page2.setData(visaChangeListVoPageInfo2.getList());
        page2.setPageNum(visaChangeListVoPageInfo2.getPageNum());
        page2.setPageSize(visaChangeListVoPageInfo2.getPageSize());
        page2.setTotalCount(visaChangeListVoPageInfo2.getTotal());

        Page page3 = new Page();
        pageVo.setStatus("3");
        List<VisaChangeListVo> allVisa3 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo3 = new PageInfo<>(allVisa3);
        page3.setData(visaChangeListVoPageInfo3.getList());
        page3.setPageNum(visaChangeListVoPageInfo3.getPageNum());
        page3.setPageSize(visaChangeListVoPageInfo3.getPageSize());
        page3.setTotalCount(visaChangeListVoPageInfo3.getTotal());

        Page page4 = new Page();
        pageVo.setStatus("4");
        List<VisaChangeListVo> allVisa4 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo4 = new PageInfo<>(allVisa4);
        page4.setData(visaChangeListVoPageInfo4.getList());
        page4.setPageNum(visaChangeListVoPageInfo4.getPageNum());
        page4.setPageSize(visaChangeListVoPageInfo4.getPageSize());
        page4.setTotalCount(visaChangeListVoPageInfo4.getTotal());

        Page page5 = new Page();
        pageVo.setStatus("5");
        List<VisaChangeListVo> allVisa5 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo5 = new PageInfo<>(allVisa5);
        page5.setData(visaChangeListVoPageInfo5.getList());
        page5.setPageNum(visaChangeListVoPageInfo5.getPageNum());
        page5.setPageSize(visaChangeListVoPageInfo5.getPageSize());
        page5.setTotalCount(visaChangeListVoPageInfo5.getTotal());

        Page page6 = new Page();
        pageVo.setStatus("6");
        List<VisaChangeListVo> allVisa6 = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo6 = new PageInfo<>(allVisa6);
        page6.setData(visaChangeListVoPageInfo6.getList());
        page6.setPageNum(visaChangeListVoPageInfo6.getPageNum());
        page6.setPageSize(visaChangeListVoPageInfo6.getPageSize());
        page6.setTotalCount(visaChangeListVoPageInfo6.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);
        map.put("table2",page1);
        map.put("table3",page2);
        map.put("table4",page3);
        map.put("table5",page4);
        map.put("table6",page5);
        map.put("table7",page6);

        return RestUtil.success(map);
    }

    /**
     * 筛选条件查询2
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "/visachange/selectVisa2",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectVisa2(PageVo pageVo){

        String id = getLoginUser().getId();
        pageVo.setUserId(id);
        Page page = new Page();

        String status = pageVo.getStatus();
        if (StringUtils.isEmpty(status) || status.equals("0")){
            pageVo.setStatus("");
        }

        List<VisaChangeListVo> allVisa = vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo = new PageInfo<>(allVisa);
        page.setData(visaChangeListVoPageInfo.getList());
        page.setPageNum(visaChangeListVoPageInfo.getPageNum());
        page.setPageSize(visaChangeListVoPageInfo.getPageSize());
        page.setTotalCount(visaChangeListVoPageInfo.getTotal());

        HashMap<String, Object> map = new HashMap<>();
        map.put("table1",page);

        return RestUtil.success(map);
    }


    //新增签证变更
    @RequestMapping(value = "/visachange/addVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addVisa(VisaChangeVo visaChangeVo){
        System.err.println(visaChangeVo.getVisaChangeUp());
        vcisService.addVisa(visaChangeVo,getLoginUser(),request);

//        return null;
        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //委外金额
        * @Date 15:47 2020/12/28
        * @Param
        * @return
     **/
    @RequestMapping(value = "/visachange/editOutSourceMoney",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addVisa(String id,String upMoney,String downMoney){
       visaChangeService.editOutSourceMoney(id,upMoney,downMoney);
       return RestUtil.success("编辑成功");
    }

    //根据id查询签证变更
    @RequestMapping(value = "/visachange/findVisaById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findVisaById(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "visaNum1") String visaNum){
       VisaChangeVo visaChangeVo =  vcisService.findVisaById(baseId,visaNum,getLoginUser());
        String proportionContract = visaChangeVo.getVisaChangeUp().getProportionContract();
        String proportionContract1 = visaChangeVo.getVisaChangeDown().getProportionContract();
        if (proportionContract!=null && !"".equals(proportionContract) && !"NaN".equals(proportionContract)){
            BigDecimal bigDecimal = new BigDecimal(proportionContract);
            BigDecimal bigDecimal2 = bigDecimal.setScale(2, RoundingMode.HALF_UP);
            visaChangeVo.getVisaChangeUp().setProportionContract(bigDecimal2.toString());
        }
        if (proportionContract1!=null && !"".equals(proportionContract1) && !"NaN".equals(proportionContract1)){
            BigDecimal bigDecimal1 = new BigDecimal(proportionContract1);
            BigDecimal bigDecimal3 = bigDecimal1.setScale(2, RoundingMode.HALF_UP);
            visaChangeVo.getVisaChangeDown().setProportionContract(bigDecimal3.toString());
        }

        String nameOfCostUnit = visaChangeVo.getVisaChangeUp().getNameOfCostUnit();
        String nameOfCostUnit1 = visaChangeVo.getVisaChangeDown().getNameOfCostUnit();
        if (nameOfCostUnit!=null && !"".equals(nameOfCostUnit)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfCostUnit);
            if (costUnitManagement!=null){
                visaChangeVo.setNameShang(costUnitManagement.getCostUnitName());
            }
        }
        if (nameOfCostUnit1!=null && !"".equals(nameOfCostUnit1)){
            CostUnitManagement costUnitManagement = costUnitManagementMapper.selectByPrimaryKey(nameOfCostUnit1);
            if (costUnitManagement!=null){
                visaChangeVo.setNameXia(costUnitManagement.getCostUnitName());
            }
        }


        return RestUtil.success(visaChangeVo);
    }

    //编辑
    @RequestMapping(value = "/visachange/updateVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateVisa(@RequestBody VisaChangeVo visaChangeVo){
        if (visaChangeVo.getVisaChangeUp().getProportionContract().equals("NaN")){
            visaChangeVo.getVisaChangeUp().setProportionContract("0");
        }
        if (visaChangeVo.getVisaChangeDown().getProportionContract().equals("NaN")){
            visaChangeVo.getVisaChangeDown().setProportionContract("0");
        }
        vcisService.updateVisa(visaChangeVo,getLoginUser(),request);
        return RestUtil.success();
    }
    //批量审核
    @RequestMapping(value = "/visachange/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        vcisService.batchReview(batchReviewVo,getLoginUser(),request);
        return RestUtil.success();
    }
    //查询变更统计信息
    @RequestMapping(value = "/visachange/findAllchangeStatistics",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllchangeStatistics(@RequestParam(name = "baseId") String baseId){
       List<VisaChangeStatisticVo> list =  vcisService.findAllchangeStatistics(baseId);
        BigDecimal upAmount = new BigDecimal(0);
        BigDecimal downAmount = new BigDecimal(0);
        double upPro = 0.0;
        double downPro = 0.0;
        VisaReturnStatistic visaReturnStatistic = new VisaReturnStatistic();
        visaReturnStatistic.setList(list);
        for (VisaChangeStatisticVo visaChangeStatisticVo : list) {
            if (visaChangeStatisticVo.getVisaChangeUpAmount()!=null){
                upAmount = upAmount.add(visaChangeStatisticVo.getVisaChangeUpAmount());
            }
            if (visaChangeStatisticVo.getVisaChangeDownAmount()!=null){
                downAmount = downAmount.add(visaChangeStatisticVo.getVisaChangeDownAmount());
            }
            if (visaChangeStatisticVo.getVisaChangeUpProportionContract()!=null && !"".equals(visaChangeStatisticVo.getVisaChangeUpProportionContract())){
                upPro+=Double.parseDouble(visaChangeStatisticVo.getVisaChangeUpProportionContract());
            }

            if (visaChangeStatisticVo.getVisaChangeDownProportionContract()!=null && !"".equals(visaChangeStatisticVo.getVisaChangeDownProportionContract())){
                downPro+=Double.parseDouble(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            }
        }
        if (list.size()>=1){
            visaReturnStatistic.setTotalChangeNum(list.get(list.size()-1).getChangeNum());
        }
        double upPro1 = upPro;
        double downPro1 = downPro;
        double v = new BigDecimal(upPro1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double v2 = new BigDecimal(downPro1).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        visaReturnStatistic.setTotalVisaChangeUpAmount(upAmount);
        visaReturnStatistic.setTotalVisaChangeDownAmount(downAmount);
        visaReturnStatistic.setTotalVisaChangeUpProportionContract(v+"");
        visaReturnStatistic.setTotalVisaChangeDownProportionContract(v2+"");
        return RestUtil.success(visaReturnStatistic);

    }
    //查询统计列表
    @RequestMapping(value = "/visachange/findAllchangeStatisticsList",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllchangeStatisticsList(@RequestParam(name = "baseId") String baseId){
        List<VisaChangeStatisticVo> list =  vcisService.findAllchangeStatistics(baseId);
        VisaChangeStatisticVo temp;
        //冒泡排序
        int size = list.size();
        for (int i = 0; i < size - 1; i++) {
            for(int j = i+1; j<size; j++){
                if (list.get(i).getChangeNum()>list.get(j).getChangeNum()){
                    temp = list.get(i);
                    list.set(i,list.get(j));
                    list.set(j,temp);
                }
            }
        }
        return RestUtil.success(list);
    }
    //查看卡片显隐
    @RequestMapping(value = "/visachange/showHiddenCard",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> showHiddenCard(@RequestParam(name = "baseId") String baseId){
        HashMap<String, String> str = new HashMap<>();
        String i =  vcisService.showHiddenCard(getLoginUser().getId(),baseId);
        str.put("xx",i);
       return RestUtil.success(str);
    }


    //删除
    @RequestMapping(value = "/visachange/deleteVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> deleteVisa(@RequestParam(name = "baseId") String baseId){
        vcisService.deleteVisa(baseId, getLoginUser(),request);
        return RestUtil.success();
    }

    // 编制人回显造价部人员
    @RequestMapping(value = "/public/costOfPersonnel",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costOfPersonnel(@RequestParam(name = "baseId") String baseId){
        List<MemberManage> memberManageNames = vcisService.costOfPersonnel();
        return RestUtil.success(memberManageNames);
    }

    //返回恢复附件
    @RequestMapping(value = "/public/renewFile",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> renewFile(@RequestParam(name = "baseId") String baseId , @RequestParam(name = "visaNum1") String visaNum1){
        vcisService.renewFile(baseId,visaNum1);
        return RestUtil.success();
    }
    //确认完成
    @RequestMapping(value = "/visachange/visaSuccess",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> visaSuccess(@RequestParam(name = "ids") String ids){
        try {
            vcisService.visaSuccess(ids,getLoginUser().getId());
        } catch (Exception e) {
            return RestUtil.error(e.getMessage());
        }
        return RestUtil.success();
    }
    // 退回
    @RequestMapping(value = "/visachange/visaBack",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> visaBack(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "backOpnion") String backOpnion){
        vcisService.visaBack(baseId,backOpnion);
        return RestUtil.success();
    }
}
