package net.zlw.cloud.snsEmailFile.service;

import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.VisaChange.mapper.VisaChangeMapper;
import net.zlw.cloud.VisaChange.model.VisaChange;
import net.zlw.cloud.budgeting.mapper.BudgetingDao;
import net.zlw.cloud.budgeting.model.Budgeting;
import net.zlw.cloud.followAuditing.mapper.TrackAuditInfoDao;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackAuditInfo;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.maintenanceProjectInformation.mapper.MaintenanceProjectInformationMapper;
import net.zlw.cloud.maintenanceProjectInformation.model.MaintenanceProjectInformation;
import net.zlw.cloud.progressPayment.mapper.AuditInfoDao;
import net.zlw.cloud.progressPayment.mapper.BaseProjectDao;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.progressPayment.mapper.ProgressPaymentInformationDao;
import net.zlw.cloud.progressPayment.model.BaseProject;
import net.zlw.cloud.progressPayment.model.ProgressPaymentInformation;
import net.zlw.cloud.settleAccounts.mapper.LastSettlementReviewDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementAuditInformationDao;
import net.zlw.cloud.settleAccounts.mapper.SettlementInfoMapper;
import net.zlw.cloud.settleAccounts.model.LastSettlementReview;
import net.zlw.cloud.settleAccounts.model.SettlementAuditInformation;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.model.MkyUser;
import net.zlw.cloud.warningDetails.model.AuditInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description 文件service
 * @Date 2020/10/9 16:43
 **/
@Service
@Transactional
public class FileInfoService {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private FileInfoMapper fileInfoMapper;

    @Resource
    private TrackMonthlyDao trackMonthlyDao;
    @Resource
    private ProgressPaymentInformationDao progressPaymentInformationDao;
    @Resource
    private VisaChangeMapper visaChangeMapper;
//    @Resource
//    private SettlementInfoMapper settlementInfoMapper;
    @Resource
    private MaintenanceProjectInformationMapper maintenanceProjectInformationMapper;
    @Resource
    private TrackAuditInfoDao trackAuditInfoDao;

    @Resource
    private SettlementAuditInformationDao downSetDao;

    @Resource
    private LastSettlementReviewDao UpSetDao;

    @Resource
    private MkyUserMapper mkyUserMapper;

    @Resource
    private ProgressPaymentInformationDao proInformationDao;

    @Resource
    private BudgetingDao budgetingDao;

    @Resource
    private AuditInfoDao auditInfoDao;

    @Resource
    private BaseProjectDao baseProjectDao;




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


    public void uploadFile(FileInfo attachInfo) {
        try {
            attachInfo.setId("file"+UUID.randomUUID().toString().replaceAll("-", ""));
            fileInfoMapper.insert(attachInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FileInfo getByKey(String id) {
        return fileInfoMapper.selectByPrimaryKey(id);
    }

    public FileInfo findOne(String id){
//        TrackMonthly trackMonthly = trackMonthlyDao.selectByPrimaryKey(id);

        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("platCode",id);
        criteria.andEqualTo("type","gzsjxjsjyb");

        List<FileInfo> fileInfos = fileInfoMapper.selectByExample(example);

        return fileInfos.get(0);
    }

    public void updateFileName(FileInfo fileInfo) {
        fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
    }

    public void updateFileName2(String id,String key) {
        FileInfo fileInfo = fileInfoMapper.selectByPrimaryKey(id);
        fileInfo.setPlatCode(key);
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
    }

    public List<FileInfo> findByFreignAndType(String key, String type) {
        return fileInfoMapper.findByFreignAndType(key,type);
    }

    public List<FileInfo> findByFreignAndTypeDesginCount(String key, String type) {
        return fileInfoMapper.findByFreignAndTypeDesginCount(key,type);
    }

    public List<FileInfo> findByFreignAndType2(String key, String type) {

        return fileInfoMapper.findByFreignAndType2(key,type);
    }

    public List<FileInfo> findByPlatCode(String id) {
        return fileInfoMapper.findByPlatCode(id);
    }

    public void deleteOldFileList(String key) {
        fileInfoMapper.deleteOldFileList(key);
    }

    public void insert(FileInfo fileInfo) {
        try {
            fileInfoMapper.insert(fileInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reductionFile(String id) {
        List<FileInfo> fileInfoList = fileInfoMapper.reductionFileF(id);
        if(fileInfoList.size() > 0){
            for (FileInfo fileInfo : fileInfoList) {
                fileInfo.setStatus("0");
                fileInfo.setUpdateTime(DateUtil.getDateTime());
                fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
            }
        }
    }

    public FileInfo findByKey(String id) {
        return fileInfoMapper.selectByPrimaryKey(id);
    }

    public List<FileInfo> findCostFile(String key, String type,String id) {
        BaseProject budgetStatus = null;
        BaseProject proStatus = null;
        BaseProject visaChangeStatus = null;
        BaseProject trackStatus = null;
        //预算
        Budgeting budgeting = budgetingDao.selectByPrimaryKey(key);
        if (budgeting !=null){
            budgetStatus = baseProjectDao.selectByPrimaryKey(budgeting.getBaseProjectId());
        }
        //进度款
        ProgressPaymentInformation paymentInformation = progressPaymentInformationDao.selectByPrimaryKey(key);
        if (paymentInformation != null){
            proStatus = baseProjectDao.selectByPrimaryKey(paymentInformation.getBaseProjectId());
        }
        //签证变更
        VisaChange visaChange = visaChangeMapper.selectByPrimaryKey(key);
        if (visaChange !=null){
            visaChangeStatus = baseProjectDao.selectByPrimaryKey(visaChange.getBaseProjectId());
        }
        //结算

        LastSettlementReview oneUp = UpSetDao.findOneUp(key);
        SettlementAuditInformation oneDown = downSetDao.findOneDown(key);


        BaseProject settAuditStatus = baseProjectDao.selectByPrimaryKey(key);
        //检维修
         MaintenanceProjectInformation mainStatus = maintenanceProjectInformationMapper.selectByPrimaryKey(key);
        //跟踪审计
        TrackAuditInfo trackAuditInfo = trackAuditInfoDao.selectByPrimaryKey(key);
        if (trackAuditInfo != null){
            trackStatus = baseProjectDao.selectByPrimaryKey(trackAuditInfo.getBaseProjectId());
        }
        // 造价文件
        List<FileInfo> costFile = fileInfoMapper.findCostFile(key, type);
        ArrayList<FileInfo> fileInfos = new ArrayList<>();

        // 未审批一审、变更一审 审核信息
        List<AuditInfo> auditInfos = auditInfoDao.selectAuditInfoList(key);
        MkyUser auditUser = null;
        if (auditInfos.size() > 0) {
            for (AuditInfo thisAudit : auditInfos) {
                String auditorId = thisAudit.getAuditorId();
                auditUser = mkyUserMapper.selectByPrimaryKey(auditorId);
            }
        }else {
            if (oneUp != null){
                List<AuditInfo> auditInfos1 = auditInfoDao.selectAuditInfoList(oneUp.getId());
                for (AuditInfo auditInfo : auditInfos1) {
                    String auditorId = auditInfo.getAuditorId();
                    auditUser = mkyUserMapper.selectByPrimaryKey(auditorId);
                }
            }
            if (oneDown != null){
                List<AuditInfo> auditInfos1 = auditInfoDao.selectAuditInfoList(oneDown.getId());
                for (AuditInfo auditInfo : auditInfos1) {
                    String auditorId = auditInfo.getAuditorId();
                    auditUser = mkyUserMapper.selectByPrimaryKey(auditorId);
                }
            }
        }
        if (costFile.size() > 0) {
            for (FileInfo thisFile : costFile) {
                // 当前登录人信息
                MkyUser createUser = mkyUserMapper.selectOneUser(thisFile.getUserName());
                // 芜湖
                if ("1".equals(createUser.getJobId())) {
                    //只有创建人和领导可见
                    if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm)) {
                         fileInfos.add(thisFile);
                        // 如果不是已完成
                    } else if (budgetStatus != null) {
                        if (!"4".equals(budgetStatus.getBudgetStatus())){
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (trackStatus != null) {
                        if (!"5".equals(trackStatus.getTrackStatus())){
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (visaChangeStatus != null) {
                        if (!"6".equals(visaChangeStatus.getVisaStatus())){
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (settAuditStatus != null) {
                        if (!"5".equals(settAuditStatus.getSettleAccountsStatus())){
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (proStatus !=null) {
                        if (!"6".equals(proStatus.getProgressPaymentStatus())){
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (mainStatus != null) {
                        if (!"5".equals(mainStatus.getType())) {
                            if (id.equals(createUser.getId()) || id.equals(whzjh) || id.equals(whzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }
                    // 吴江
                } else if ("2".equals(createUser.getJobId())) {
                    //只有创建人和领导可见
                    if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm)) {
                        fileInfos.add(thisFile);
                        // 如果是待确认状态下，创建人、互审人、领导可以查看附件
                    }else if (budgetStatus != null ) {
                        if (!"4".equals(budgetStatus.getBudgetStatus()) ) {
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (trackStatus != null) {
                        if (!"5".equals(trackStatus.getTrackStatus())){
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (visaChangeStatus != null) {
                        if (!"6".equals(visaChangeStatus.getVisaStatus())){
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (settAuditStatus != null) {
                        if (!"5".equals(settAuditStatus.getSettleAccountsStatus())){
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (proStatus != null) {
                        if (!"6".equals(proStatus.getProgressPaymentStatus())){
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }else if (mainStatus != null) {
                        if (!"5".equals(mainStatus.getType())) {
                            if (id.equals(createUser.getId()) || id.equals(wjzjh) || id.equals(wjzjm) || id.equals(auditUser.getId())) {
                                fileInfos.add(thisFile);
                            }
                        }
                    }
                }
            }
        }
        return fileInfos;
    }
}
