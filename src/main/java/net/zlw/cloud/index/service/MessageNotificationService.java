package net.zlw.cloud.index.service;

import net.zlw.cloud.designProject.mapper.AnhuiMoneyinfoMapper;
import net.zlw.cloud.designProject.mapper.DesignInfoMapper;
import net.zlw.cloud.designProject.mapper.WujiangMoneyInfoMapper;
import net.zlw.cloud.designProject.model.AnhuiMoneyinfo;
import net.zlw.cloud.designProject.model.DesignInfo;
import net.zlw.cloud.designProject.model.WujiangMoneyInfo;
import net.zlw.cloud.followAuditing.model.vo.PageVo;
import net.zlw.cloud.index.mapper.MessageNotificationDao;
import net.zlw.cloud.index.model.DesignSum;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.index.model.vo.StatisticalData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MessageNotificationService {
    @Resource
    private MessageNotificationDao messageNotificationDao;
    @Resource
    private AnhuiMoneyinfoMapper anhuiMoneyinfoMapper;
    @Resource
    private WujiangMoneyInfoMapper wujiangMoneyInfoMapper;
    @Resource
    private DesignInfoMapper designInfoMapper;


    public List<MessageNotification> findMessage() {
       return messageNotificationDao.findMessage();
    }



    public StatisticalData findAllStatisticalData(PageVo pageVo) {
        StatisticalData statisticalData = new StatisticalData();
        DesignSum designSum = new DesignSum();
        Integer numberProject = 0;
        Double gincome = 0.00;
        Double spending = 0.00;
        List<AnhuiMoneyinfo> anhuiMoneyinfos = anhuiMoneyinfoMapper.selectAll();
        List<WujiangMoneyInfo> wujiangMoneyInfos = wujiangMoneyInfoMapper.selectAll();
        //项目总数量
        numberProject+=anhuiMoneyinfos.size();
        numberProject+=wujiangMoneyInfos.size();
        for (AnhuiMoneyinfo anhuiMoneyinfo : anhuiMoneyinfos) {
            BigDecimal revenue = anhuiMoneyinfo.getRevenue();
            double v = revenue.doubleValue();
            gincome+=v;
            DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(anhuiMoneyinfo.getBaseProjectId());
            spending+=designInfo.getOutsourceMoney().doubleValue();
        }
        for (WujiangMoneyInfo wujiangMoneyInfo : wujiangMoneyInfos) {
            BigDecimal revenue = wujiangMoneyInfo.getRevenue();
            double v = revenue.doubleValue();
            gincome+=v;
            DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(wujiangMoneyInfo.getBaseProjectId());
            spending+=designInfo.getOutsourceMoney().doubleValue();
        }
        designSum.setNumberProjects(numberProject);
        designSum.setGeneralIncome(gincome);
        designSum.setOutsourcingSpending(spending);
        designSum.setOperatingIncome(gincome-spending);
        statisticalData.setDesignSum(designSum);
        return statisticalData;


    }

    public StatisticalData findStatisticalDataWujiang(PageVo pageVo) throws ParseException {
        StatisticalData statisticalData = new StatisticalData();
        DesignSum designSum = new DesignSum();
        Integer numberProject = 0;
        Double gincome = 0.00;
        Double spending = 0.00;
        List<WujiangMoneyInfo> wujiangMoneyInfos = wujiangMoneyInfoMapper.selectAll();
        //项目总数量
        numberProject+=wujiangMoneyInfos.size();
        for (WujiangMoneyInfo wujiangMoneyInfo : wujiangMoneyInfos) {
            BigDecimal revenue = wujiangMoneyInfo.getRevenue();
            double v = revenue.doubleValue();
            gincome+=v;
            DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(wujiangMoneyInfo.getBaseProjectId());
            spending+=designInfo.getOutsourceMoney().doubleValue();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Date xxx = simpleDateFormat.parse("xxx");
        designSum.setNumberProjects(numberProject);
        designSum.setGeneralIncome(gincome);
        designSum.setOutsourcingSpending(spending);
        designSum.setOperatingIncome(gincome-spending);
        statisticalData.setDesignSum(designSum);
        return statisticalData;
    }

    public StatisticalData findStatisticalDataAnhui(PageVo pageVo) {
        StatisticalData statisticalData = new StatisticalData();
        DesignSum designSum = new DesignSum();
        Integer numberProject = 0;
        Double gincome = 0.00;
        Double spending = 0.00;
        Double oincome = 0.00;
        List<AnhuiMoneyinfo> anhuiMoneyinfos = anhuiMoneyinfoMapper.selectAll();
        for (AnhuiMoneyinfo anhuiMoneyinfo : anhuiMoneyinfos) {
            BigDecimal revenue = anhuiMoneyinfo.getRevenue();
            double v = revenue.doubleValue();
            gincome+=v;
            DesignInfo designInfo = designInfoMapper.selectByPrimaryKey(anhuiMoneyinfo.getBaseProjectId());
            spending+=designInfo.getOutsourceMoney().doubleValue();
        }
        designSum.setNumberProjects(numberProject);
        designSum.setGeneralIncome(gincome);
        designSum.setOutsourcingSpending(spending);
        designSum.setOperatingIncome(gincome-spending);
        statisticalData.setDesignSum(designSum);
        return statisticalData;
    }
}
