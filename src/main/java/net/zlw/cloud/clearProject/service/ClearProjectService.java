package net.zlw.cloud.clearProject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.clearProject.mapper.CallForBidsMapper;
import net.zlw.cloud.clearProject.model.BaseProject;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.clearProject.model.ClearProject;
import net.zlw.cloud.clearProject.model.vo.ClearProjectVo;
import net.zlw.cloud.designProject.mapper.BudgetingMapper;
import net.zlw.cloud.designProject.model.Budgeting;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.ConstructionUnitManagement;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import net.zlw.cloud.clearProject.mapper.ClearProjectMapper;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author dell
 * @Date 2020/9/28 16:11
 * @Version 1.0
 */
@Service
public class ClearProjectService{

    @Resource
    private ClearProjectMapper clearProjectMapper;

    @Resource
    private BudgetingMapper budgetingMapper;

    @Resource
    private BaseProjectDao baseProjectMapper;

    @Resource
    private ConstructionUnitManagementMapper constructionUnitManagementMapper;
    @Resource
    private CallForBidsMapper callForBidsMapper;

    /**
     * 新增--确定
     */

    public void addClearProject(ClearProjectVo clearProjectVo, UserInfo userInfo){

        String projectId = clearProjectVo.getProjectId();

//        Budgeting budgeting = budgetingMapper.findById(projectId);
//
//        String baseProjectId = budgeting.getBaseProjectId();
//
//        BaseProject baseProject = baseProjectMapper.fingById(baseProjectId);


        CallForBids callForBids = callForBidsMapper.selectByPrimaryKey(clearProjectVo.getId());


        ClearProject clearProject = new ClearProject();
        String uuId = UUID.randomUUID().toString().replaceAll("-","");
        clearProject.setId(uuId);
//        clearProject.setProjectNum(clearProjectVo.getProjectNum());
        clearProject.setProjectName(callForBids.getBidProjectName());

// TODO
//        //招标人
//        clearProject.setTenderer(clearProjectVo.getTenderer());
//        //招标代理机构
//        clearProject.setProcuratorialAgency(clearProjectVo.getProcuratorialAgency());
//        clearProject.setBidder(clearProjectVo.getBidder());
//        clearProject.setBidPrice(clearProjectVo.getBidPrice());
//        clearProject.setBudgetingId(callForBids.getId());
//        // TODO 待修改
//        clearProject.setProjectAddress("123");


        if(userInfo != null){
            //创建人id
            clearProject.setFounderId(userInfo.getId());
            //创建人公司id
            clearProject.setFounderCompanyId(userInfo.getCompanyId());
        }else{
            clearProject.setFounderId("user312");

            clearProject.setFounderCompanyId("com0");
        }


        clearProject.setDelFlag("0");

        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        clearProject.setCreateTime(createTime);

        clearProject.setFounderTime(createTime);


        callForBids.setClearProjectId(clearProject.getId());

        callForBidsMapper.updateByPrimaryKeySelective(callForBids);
        //添加到数据库
        clearProjectMapper.insertSelective(clearProject);
    }


    /**
     * 分页查询所有
     * @param pageRequest
     * @param userInfo
     * @return
     */
//    public List<ClearProject> findAllClearProject(PageRequest pageRequest,UserInfo userInfo){
    public PageInfo<ClearProject> findAllClearProject(PageRequest pageRequest,UserInfo userInfo){
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());



        Example example = new Example(ClearProject.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag","0");



        // 查询条件，内容
        if(pageRequest.getKeyWord() != null && (!"".equals(pageRequest.getKeyWord()))){
            criteria.andLike("projectName","%"+pageRequest.getKeyWord()+"%");
        }

        List<ClearProject> clearProjects = clearProjectMapper.selectByExample(example);

        for (ClearProject clearProject : clearProjects) {
// TODO
//            if(clearProject.getConstructionUnitId()!= null){
//                ConstructionUnitManagement constructionUnitManagement = constructionUnitManagementMapper.selectById(clearProject.getConstructionUnitId());
//
//                // 建设单位
//                clearProject.setConstructionUnitName(constructionUnitManagement.getConstructionUnitName());
//            }


            if(clearProject.getBudgetingId() != null){
//                Budgeting budgeting = budgetingMapper.findById(clearProject.getBudgetingId());
//
//                BaseProject baseProject = baseProjectMapper.fingById(budgeting.getBaseProjectId());
//
//                clearProject.setProjectAddress(baseProject.getDistrict());
                CallForBids callForBids = callForBidsMapper.selectByPrimaryKey(clearProject.getBudgetingId());
// TODO
//                if(callForBids!= null){
//                    clearProject.setProjectAddress(callForBids.getBidProjectAddress());
//                }
            }

            // 清标人
            if(userInfo != null){
                String userId = userInfo.getId();

                clearProject.setFounderName(userInfo.getUsername());
            }else{
                //TODO 待修改
                clearProject.setFounderName("123");
            }



        }

        PageInfo<ClearProject> projectPageInfo = new PageInfo<>(clearProjects);

        return projectPageInfo;
//        return projectPageInfo.getList();

    }


    /**
     * 删除
     * @param id
     */
    public void deleteClearProject(String id){
       clearProjectMapper.deleteClearProject(id);
    }

}
