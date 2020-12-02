package net.zlw.cloud.clearProject.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.zlw.cloud.clearProject.model.CallForBids;
import net.zlw.cloud.clearProject.model.vo.PageVo;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.MaintenanceProjectInformationReturnVo;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import net.zlw.cloud.clearProject.mapper.CallForBidsMapper;
import tk.mybatis.mapper.entity.Example;

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
            callForBids.setFileNameOfBid(snd.getFileName());
        }
        //中标文件
        FileInfo tdr = fileInfoMapper.findByCodeAndType("tdr", callForBids.getId());
        if(tdr != null){
            callForBids.setFileIdOfWin(tdr.getId());
            callForBids.setFileNameOfWin(tdr.getFileName());
        }
        return callForBids;
    }

}
