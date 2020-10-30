package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.vo.LoginUser;
import net.tec.cloud.common.web.MediaTypes;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.snsEmailFile.mapper.SysCompanyMapper;
import net.zlw.cloud.snsEmailFile.model.SysCompany;
import net.zlw.cloud.snsEmailFile.service.MemberService;
import net.zlw.cloud.snsEmailFile.util.AESUtil;
import net.zlw.cloud.snsEmailFile.util.AdUtils;
import net.zlw.cloud.snsEmailFile.util.AesEncryptUtil;
import net.zlw.cloud.snsEmailFile.util.Common;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * @Author Armyman
 * @Description 登陆
 * @Date 2020/10/27 17:22
 **/
@RestController
public class LoginController  extends BaseController {

    Logger log = Logger.getLogger(this.getClass());

    private static final String test_userAccount = Common.getValueByProperty("verification_flag","/application.yml");

    @Autowired
    private MemberService sysUserService;

    @Autowired
    private SysCompanyMapper companyMapper;
    /**
     * @Author Armyman
     * @Description //域账号登陆接口
     * @Date 17:50 2020/10/27
     **/
    @RequestMapping(value = "/login/userLogin", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> userLogin(String userName, String spCode, HttpSession session, String rember){
        try{
            String result = "";
            if("1".equals(test_userAccount)){
                log.info("域账号校验开始："+System.currentTimeMillis());
                result = AdUtils.connect(userName, spCode);
                log.info("域账号校验结束："+System.currentTimeMillis());
            }else{
                result="OK";
            }
            log.info("域账号校验结果result："+result);
            String message="";
            if("OK".equals(result)){
                message = sysUserService.userLogin(userName,request);
                String key = UUID.randomUUID().toString();
                if("ok".equals(message)){

                    //记住我
                    if (rember != null && "1".equals(rember)) {
                        Cookie c1 = new Cookie("username", userName);
                        Cookie c2 = new Cookie("password", spCode);
                        Cookie c3 = new Cookie("rember", rember);

                        c1.setMaxAge(7 * 60 * 60 * 24);
                        c2.setMaxAge(7 * 60 * 60 * 24);
                        c3.setMaxAge(7 * 60 * 60 * 24);
                        response.addCookie(c1);
                        response.addCookie(c2);
                        response.addCookie(c3);
                    }
                    log.info("用户保存session开始："+System.currentTimeMillis());
                    this.saveLoginUserToSession(session);
                    log.info("用户保存session结束，保存登录ip开始："+System.currentTimeMillis());

                    return RestUtil.success();
                }else{
                    return RestUtil.error( message);
                }
            }else{
                return RestUtil.error("域账号校验失败");
            }
        }catch(Exception ex){
            log.info("新域账号登录接口错误");
            ex.printStackTrace();
            return RestUtil.error("域账号登录错误");
        }
    }
    @RequestMapping(value = "/login/userLogin2", method = {RequestMethod.GET, RequestMethod.POST}, produces = MediaTypes.JSON_UTF_8)
    public Map<String, Object> userLogin(String userName, String password){
        return RestUtil.success("success"); 
    }

    protected void saveLoginUserToSession(HttpSession session) {
        session.setAttribute("loginUser", getLoginUser());
    }

}
