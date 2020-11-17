package net.zlw.cloud.clearProject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.tec.cloud.common.bean.UserInfo;
import net.zlw.cloud.clearProject.mapper.CallForBidsMapper;
import net.zlw.cloud.clearProject.mapper.ClearProjectMapper;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.clearProject.model.ClearProject;
import net.zlw.cloud.clearProject.model.vo.ClearProjectVo;
import net.zlw.cloud.designProject.mapper.BudgetingMapper;
import net.zlw.cloud.designProject.model.Budgeting;
import net.zlw.cloud.maintenanceProjectInformation.mapper.ConstructionUnitManagementMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
public class ClearProjectService {

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

    public void addClearProject(ClearProjectVo clearProjectVo, UserInfo userInfo) {

        CallForBids callForBids = callForBidsMapper.selectByPrimaryKey(clearProjectVo.getId());

        Budgeting budgeting = budgetingMapper.selectByPrimaryKey(clearProjectVo.getBaseId());

        BaseProject baseProject = baseProjectMapper.selectByPrimaryKey(budgeting.getBaseProjectId());

        ClearProject clearProject = new ClearProject();
        String uuId = UUID.randomUUID().toString().replaceAll("-", "");
        clearProject.setId(uuId);
        clearProject.setProjectNum(callForBids.getBidProjectNum());
        clearProject.setProjectName(callForBids.getBidProjectName());
        clearProject.setBudgetingId(budgeting.getId());
//        创建人id
        clearProject.setFounderId(userInfo.getId());
//        创建人公司id
        clearProject.setFounderCompanyId(userInfo.getCompanyId());
        clearProject.setDelFlag("0");

        //创建时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = simpleDateFormat.format(new Date());

        clearProject.setCreateTime(createTime);

        callForBids.setClearProjectId(clearProject.getId());
        callForBids.setConstructionUnit(baseProject.getConstructionUnit());
        callForBids.setBidProjectAddress(baseProject.getWaterAddress());
        callForBidsMapper.updateByPrimaryKeySelective(callForBids);
        //添加到数据库
        clearProjectMapper.insertSelective(clearProject);
    }


    /**
     * 分页查询所有
     *
     * @param pageRequest
     * @param userInfo
     * @return
     */
//    public List<ClearProject> findAllClearProject(PageRequest pageRequest,UserInfo userInfo){
    public PageInfo<ClearProject> findAllClearProject(PageRequest pageRequest, UserInfo userInfo) {
        //        设置分页助手
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());


        Example example = new Example(ClearProject.class);

        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("delFlag", "0");


        // 查询条件，内容
        if (pageRequest.getKeyWord() != null && (!"".equals(pageRequest.getKeyWord()))) {
            criteria.andLike("projectName", "%" + pageRequest.getKeyWord() + "%");
        }

        List<ClearProject> clearProjects = clearProjectMapper.selectByExample(example);

        for (ClearProject clearProject : clearProjects) {


            if (clearProject.getBudgetingId() != null) {
//                Budgeting budgeting = budgetingMapper.findById(clearProject.getBudgetingId());
//
//                BaseProject baseProject = baseProjectMapper.fingById(budgeting.getBaseProjectId());
//
//                clearProject.setProjectAddress(baseProject.getDistrict());
                CallForBids callForBids = callForBidsMapper.selectByPrimaryKey(clearProject.getBudgetingId());

            }

            // 清标人
            if (userInfo != null) {
                String userId = userInfo.getId();

                clearProject.setFounderName(userInfo.getUsername());
            } else {
                //TODO 待修改
                clearProject.setFounderName("123");
            }


        }

        PageInfo<ClearProject> projectPageInfo = new PageInfo<>(clearProjects);

        return projectPageInfo;
//        return projectPageInfo.getList();

    }

    public PageInfo<ClearProject> findAll(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<ClearProject> allClearProject = clearProjectMapper.findAllClearProject(pageRequest);
        PageInfo<ClearProject> pageInfo = new PageInfo<>(allClearProject);
        return pageInfo;
    }


    /**
     * 删除
     *
     * @param id
     */
    public void deleteClearProject(String id) {
        clearProjectMapper.deleteClearProject(id);
    }

}
