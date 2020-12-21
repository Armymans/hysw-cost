package net.zlw.cloud.clearProject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.clearProject.mapper.CallForBidsMapper;
import net.zlw.cloud.clearProject.mapper.ClearProjectMapper;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.clearProject.model.ClearProject;
import net.zlw.cloud.clearProject.model.vo.ClearAndCallVo;
import net.zlw.cloud.clearProject.model.vo.PageVo;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author dell
 * @Date 2020/11/9 20:15
 * @Version 1.0
 */
@Service
public class CallForBidsService{

    @Resource
    private CallForBidsMapper callForBidsMapper;
    @Resource
    private BaseProjectDao baseProjectDao;
    @Resource
    private ClearProjectMapper clearProjectMapper;
    @Resource
    private BudgetingDao budgetingDao;

    @Autowired
    private FileInfoMapper fileInfoMapper;


    public PageInfo<CallForBids> findAll(PageVo pageVo){

        //        设置分页助手
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        Example example = new Example(CallForBids.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status","0");
        if(pageVo.getKeyWord() != null && !"".equals(pageVo.getKeyWord())){
            criteria.andLike("bidProjectName","%"+pageVo.getKeyWord()+"%");
        }
        criteria.andIsNull("clearProjectId");

        List<CallForBids> callForBids = callForBidsMapper.selectByExample(example);

        PageInfo<CallForBids> pageInfo = new PageInfo<>(callForBids);

        return pageInfo;
    }


    public CallForBids findById(String id){
        CallForBids callForBids = callForBidsMapper.selectByPrimaryKey(id);
        //招标文件
        FileInfo snd = fileInfoMapper.findByCodeAndType("snd", callForBids.getId());
        if(snd != null){
            callForBids.setFileIdOfBid(snd.getId());
            callForBids.setFileNameOfBid(snd.getFileName()+"."+snd.getFileType());
        }
        //中标文件
        FileInfo tdr = fileInfoMapper.findByCodeAndType("tdr", callForBids.getId());
        if(tdr != null){
            callForBids.setFileIdOfWin(tdr.getId());
            callForBids.setFileNameOfWin(tdr.getFileName()+"."+tdr.getFileType());
        }
        return callForBids;
    }

    public ClearAndCallVo selectOneClearProject(String id) {
        ClearAndCallVo callVo = new ClearAndCallVo();
        ClearProject clearProject = clearProjectMapper.selectByPrimaryKey(id);
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(clearProject.getBudgetingId());
        BaseProject baseProject = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
        if (baseProject != null){
            callVo.setProjectNum(baseProject.getProjectNum());
            callVo.setProjectName(baseProject.getProjectName());
        }
        Example example = new Example(CallForBids.class);
        Example.Criteria c = example.createCriteria();
        c.andEqualTo("clearProjectId",clearProject.getId());
        c.andEqualTo("status","0");
        CallForBids callForBids = callForBidsMapper.selectOneByExample(example);
        if (callForBids != null){
            callVo.setCallForBids(callForBids);
        }
        FileInfo snd = fileInfoMapper.findByCodeAndType("snd", callForBids.getId());
        if(snd != null){
            callForBids.setFileIdOfBid(snd.getId());
            callForBids.setFileNameOfBid(snd.getFileName()+"."+snd.getFileType());
        }
        //中标文件
        FileInfo tdr = fileInfoMapper.findByCodeAndType("tdr", callForBids.getId());
        if(tdr != null){
            callForBids.setFileIdOfWin(tdr.getId());
            callForBids.setFileNameOfWin(tdr.getFileName()+"."+tdr.getFileType());
        }



        return callVo;
    }
}
