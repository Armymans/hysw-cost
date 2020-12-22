package net.zlw.cloud.snsEmailFile.service;

import com.github.pagehelper.util.StringUtil;
import net.zlw.cloud.depManage.domain.DepManage;
import net.zlw.cloud.depManage.mapper.DepManageMapper;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.snsEmailFile.mapper.PlatInformactionLogDao;
import net.zlw.cloud.snsEmailFile.mapper.SysCompanyMapper;
import net.zlw.cloud.snsEmailFile.model.InformationLog;
import net.zlw.cloud.snsEmailFile.model.SysCompany;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description TODO
 * @Date 2020/10/27 17:41
 **/
@Service
@Transactional
public class MemberService {

    Logger log = Logger.getLogger(this.getClass());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MemberManageDao userDao;

    @Autowired
    private SysCompanyMapper companyDao;

    @Autowired
    private PlatInformactionLogDao platInformactionLogDao;

    @Autowired
    private DepManageMapper depManageMapper;

    public String userLogin(String userAccount,HttpServletRequest request) {
        try {
            if(userAccount.contains("\\")){
                String[] split = userAccount.split("\\\\");
                userAccount = split[0] +"\\\\"+split[1];
            }
            MemberManage user = userDao.selectByAccount(userAccount);
            if (user != null) {
                InformationLog login = new InformationLog();
                SysCompany company = companyDao.selectByPrimaryKey(user.getCompanyId());
                DepManage depManage = depManageMapper.selectByPrimaryKey(user.getCompanyId());
                if (company == null && depManage == null) {
                    return "用户信息未同步，请联系管理员。";

                } else {
                    String ip = getIp(request);
                    if(company != null){
                        login.setBusinessType(company.getBusinessType());
                        login.setCompanyId(company.getId());
                        login.setCompanyName(company.getName());
                    }else{
                        login.setCompanyId(depManage.getId());
                        login.setCompanyName(depManage.getDepName());
                    }
                    login.setUserId(user.getId());
                    login.setUserName(user.getMemberName());
                    login.setLoginIpAddress(ip);
                    login.setLogType("100");
                    login.setId(UUID.randomUUID().toString());
                    String date = sdf.format(new Date());
                    login.setLoginTime(date);
                    login.setCreateTime(date);
                    login.setStatus("1");
                    platInformactionLogDao.insertSelective(login);
                    log.info("保存登录ip结束：" + System.currentTimeMillis());
                    log.info("==============================用户登录日志保存成功，返回成功信息===================");

                    return "ok";
                }
            } else {
                return "用户未同步，请联系管理员。";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("X-Requested-For");
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtil.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if("0:0:0:0:0:0:0:1".equals(ip)){
            return "127.0.0.1";
        }
        return ip;
    }

    /*****
     *  用户域账户校验成功后，验证用户信息是否保存在采购平台数据库内（是否同步过来）
     * @param
     * @return
     */
    public MemberManage checkUserAccount(String userAccount) {
        try {
            return userDao.selectByAccount(userAccount);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("SysUserService - checkUserAccount -error");
        }
        return null;
    }


}
