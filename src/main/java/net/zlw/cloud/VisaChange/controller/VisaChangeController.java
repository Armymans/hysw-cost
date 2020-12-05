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
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    //查询所有

    @RequestMapping(value = "/visachange/findAllVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findAllVisa(PageVo pageVo){
        String id = getLoginUser().getId();
//        String id = "user309";
        pageVo.setUserId(id);
        //TODO id测试写死
        PageHelper.startPage(pageVo.getPageNum(),pageVo.getPageSize());
        List<VisaChangeListVo> list =  vcisService.findAllVisa(pageVo);
        PageInfo<VisaChangeListVo> visaChangeListVoPageInfo = new PageInfo<>(list);


        return RestUtil.page(visaChangeListVoPageInfo);
    }

    @RequestMapping(value = "/visachange/selectVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> selectVisa(PageVo pageVo){
        Page page = new Page();
        pageVo.setStatus("");
        Map<String, Object> allVisa = findAllVisa(pageVo);


        return RestUtil.success();
    }

    /**
        * @Author sjf
        * @Description //签证变更 模糊搜索
        * @Date 10:34 2020/11/22
        * @Param
        * @return
     **/

    //新增签证变更
    @RequestMapping(value = "/visachange/addVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> addVisa(VisaChangeVo visaChangeVo){
        vcisService.addVisa(visaChangeVo,getLoginUser());

        return RestUtil.success();
    }

    //根据id查询签证变更
    @RequestMapping(value = "/visachange/findVisaById",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> findVisaById(@RequestParam(name = "baseId") String baseId,@RequestParam(name = "visaNum1") String visaNum){
       VisaChangeVo visaChangeVo =  vcisService.findVisaById(baseId,visaNum,getLoginUser());
       return RestUtil.success(visaChangeVo);
    }

    //编辑
    @RequestMapping(value = "/visachange/updateVisa",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> updateVisa(VisaChangeVo visaChangeVo){
        vcisService.updateVisa(visaChangeVo,getLoginUser());
        return RestUtil.success();
    }
    //批量审核
    @RequestMapping(value = "/visachange/batchReview",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> batchReview(BatchReviewVo batchReviewVo){
        vcisService.batchReview(batchReviewVo,getLoginUser());
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
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            System.err.println(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            if (visaChangeStatisticVo.getVisaChangeDownProportionContract()!=null && !"".equals(visaChangeStatisticVo.getVisaChangeDownProportionContract())){
                downPro+=Double.parseDouble(visaChangeStatisticVo.getVisaChangeDownProportionContract());
            }
        }
        if (list.size()>=1){
            visaReturnStatistic.setTotalChangeNum(list.get(list.size()-1).getChangeNum());
        }
        int upPro1 = (int) upPro;
        int downPro1 = (int)downPro;
        visaReturnStatistic.setTotalVisaChangeUpAmount(upAmount);
        visaReturnStatistic.setTotalVisaChangeDownAmount(downAmount);
        visaReturnStatistic.setTotalVisaChangeUpProportionContract(upPro1);
        visaReturnStatistic.setTotalVisaChangeDownProportionContract(downPro1);
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
        vcisService.deleteVisa(baseId);
        return RestUtil.success();
    }

    // 编制人回显造价部人员
    @RequestMapping(value = "/public/costOfPersonnel",method = {RequestMethod.POST,RequestMethod.GET},produces = MediaTypes.JSON_UTF_8)
    public Map<String,Object> costOfPersonnel(@RequestParam(name = "baseId") String baseId){
        List<MemberManage> memberManageNames = vcisService.costOfPersonnel();
        return RestUtil.success(memberManageNames);
    }
















}
