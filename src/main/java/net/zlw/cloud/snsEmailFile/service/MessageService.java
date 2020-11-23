package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.index.mapper.MessageNotificationDao;
import net.zlw.cloud.index.model.MessageNotification;
import net.zlw.cloud.progressPayment.mapper.MemberManageDao;
import net.zlw.cloud.remindSet.mapper.RemindSetMapper;
import net.zlw.cloud.remindSet.model.RemindSet;
import net.zlw.cloud.snsEmailFile.controller.EmailInfoController;
import net.zlw.cloud.snsEmailFile.controller.SnsInfoController;
import net.zlw.cloud.snsEmailFile.mapper.EmailInfoMapper;
import net.zlw.cloud.snsEmailFile.mapper.MkyUserMapper;
import net.zlw.cloud.snsEmailFile.mapper.SnsInfoMapper;
import net.zlw.cloud.snsEmailFile.model.EmailInfo;
import net.zlw.cloud.snsEmailFile.model.SnsInfo;
import net.zlw.cloud.snsEmailFile.model.vo.MessageVo;
import net.zlw.cloud.warningDetails.model.MemberManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Classname 公用接口（消息通知）
 * @Description TODO
 * @Date 2020/11/19 14:08
 * @Created by sjf
 */

@Service
public class MessageService {

    @Autowired
    private MessageNotificationDao messageNotificationDao;

    @Autowired
    private RemindSetMapper remindSetMapper;

    @Autowired
    private SnsInfoService snsInfoService;

    @Autowired
    private EmailInfoMapper emailInfoMapper;

    @Autowired
    private SnsInfoMapper snsInfoMapper;

    @Autowired
    private EmailInfoController emailInfoController;

    @Autowired
    private MkyUserMapper mkyUserMapper;

    @Autowired
    private SnsInfoController snsInfoController;
    @Autowired
    private MemberManageDao memberManageDao;

    public void sendOrClose(MessageVo messageVo) {
        String data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());


        RemindSet remindSet = remindSetMapper.selectByPrimaryKey(messageVo.getId());
        //根据审核id查找用户信息（邮箱、手机号）
        Example example = new Example(MemberManage.class);
        example.createCriteria().andEqualTo("id", messageVo.getUserId())
                .andEqualTo("status", "0");
        MemberManage memberManage = memberManageDao.selectOneByExample(example);
        //邮件信息表
        EmailInfo emailInfo = new EmailInfo();
        //获得提醒表的userid进行切割
                    //如果邮箱状态是通知的话就调用邮箱接口存数据
                    if ("1".equals(remindSet.getEmailMessage())) {
                        emailInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        emailInfo.setReceiver(memberManage.getEmail());
                        emailInfo.setContent(messageVo.getContent());
                        emailInfo.setUserId(memberManage.getId());
                        emailInfo.setSubject(messageVo.getTitle());
                        emailInfo.setCreateTime(data);
                        emailInfoController.sendEmailInterface("2364797787@qq.com", messageVo.getTitle(), messageVo.getContent());
                    }
                    //如果短信状态是通知
                    if ("1".equals(remindSet.getNoteMessage())) {
                        SnsInfo snsInfo = new SnsInfo();
                        snsInfo.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        snsInfo.setPhone(memberManage.getPhone());
                        snsInfo.setContent(messageVo.getSnsContent());
                        snsInfo.setUserId(memberManage.getId());
                        snsInfo.setCreateTime(data);
                        snsInfoController.sendCode("18255747151", messageVo.getSnsContent());
                    }
                    //如果站内状态是通知
                    if ("1".equals(remindSet.getMessage())) {
                        MessageNotification messageNotification = new MessageNotification();
                        messageNotification.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                        messageNotification.setInform(messageVo.getTitle());
                        messageNotification.setTitle(messageVo.getTitle());
                        messageNotification.setDetails(messageVo.getDetails());
                        messageNotification.setAcceptId(memberManage.getId());
//                    messageNotification.setFounderId(loginUser.getUsername()); //TODO 待改
                        messageNotification.setCreateTime(data);
                        messageNotification.setSubmitTime(data);
                        messageNotification.setInformStatus("0");
                        messageNotification.setStatus("0");
                        messageNotificationDao.insertSelective(messageNotification);
                    }
                }
            }

