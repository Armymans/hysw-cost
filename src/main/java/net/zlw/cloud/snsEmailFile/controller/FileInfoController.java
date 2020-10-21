package net.zlw.cloud.snsEmailFile.controller;

import net.tec.cloud.common.controller.BaseController;
import net.tec.cloud.common.util.DateUtil;
import net.tec.cloud.common.util.FileUtil;
import net.tec.cloud.common.util.IdUtil;
import net.tec.cloud.common.vo.LoginUser;
import net.zlw.cloud.common.RestUtil;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import net.zlw.cloud.snsEmailFile.service.FileInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.rpc.ServiceException;
import javax.xml.ws.soap.Addressing;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Armyman
 * @Description 文件接口
 * @Date 2020/10/9 16:48
 **/
@RestController
@RequestMapping(value = "/common/file")
public class FileInfoController extends BaseController {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
    private static final transient Logger log = LoggerFactory.getLogger(FileInfoController.class);

    @Value("${app.attachPath}")
    private String LixAttachDir;
    @Value("${app.testPath}")
    private String WinAttachDir;

    @Autowired
    private FileInfoService fileInfoService;


    /**
     * @Author Armyman
     * @Description //上传文件
     * @Date 11:28 2020/10/10
     **/
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Map<String, Object> applyUpload1(@RequestParam("file") MultipartFile file, String type) {
        log.info(getLogInfo("upload", file));
        FileInfo attachInfo = new FileInfo();
        try {
            String fileName = new String(FileUtil.getFileName(file).getBytes(), "UTF-8");
            String fileType = FileUtil.getFileExtName(file);

            String fileDir = "/" + sdf2.format(new Date());

            String tmpFileName = IdUtil.uuid2().substring(0, 15) + sdf.format(new Date()) + "." + fileType;
            String filePath = fileDir + "/" + tmpFileName;
            //上传路径
            String path = "";
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) {
                path = WinAttachDir;
            } else {
                path = LixAttachDir;
            }

            File outDir = new File(path + fileDir);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            log.info(outDir.getAbsolutePath());
            File targetFile = new File(outDir.getAbsolutePath(), tmpFileName);
            try {
                file.transferTo(targetFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
            attachInfo.setFileName(fileName);
            attachInfo.setFilePath(filePath);
            attachInfo.setFileType(fileType);
            attachInfo.setUserId(getLoginUser().getId());
            attachInfo.setType(type);
            attachInfo.setCreateTime(DateUtil.getDateTime());
            attachInfo.setStatus("1");
            //todo
//            attachInfo.setUserId(getLoginUser().getId());
            attachInfo.setStatus("0");
//            attachInfo.setCompanyId(getLoginUser().getCompanyId());

            //将文件与企业材料管理进行关联
            fileInfoService.uploadFile(attachInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return RestUtil.error("操作异常,请联系管理员!");
        }
        return RestUtil.success(attachInfo);
    }

    /**
     * @Author Armyman
     * @Description //修改文件信息 名字和备注
     * @Date 11:31 2020/10/10
     **/
    @RequestMapping(value = "/updateFileName", method = RequestMethod.POST)
    public Map<String, Object> updateFileName(String code, String id, String name, String remark) {
        FileInfo fileInfo = fileInfoService.getByKey(id);
        fileInfo.setName(name);
        fileInfo.setRemark(remark);
        fileInfo.setPlatCode(code);
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoService.updateFileName(fileInfo);
        return RestUtil.success("修改成功");
    }

    /**
     * @Author Armyman
     * @Description //修改文件信息 外键
     * @Date 11:31 2020/10/10
     **/
    @RequestMapping(value = "/updateFileFreign", method = RequestMethod.POST)
    public Map<String, Object> updateFileFreign(String id, String key) {
        FileInfo fileInfo = fileInfoService.getByKey(id);
        fileInfo.setPlatCode(key);
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoService.updateFileName(fileInfo);
        return RestUtil.success("修改成功");
    }

    /**
     * @Author Armyman
     * @Description //删除文件
     * @Date 13:51 2020/10/10
     **/
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public Map<String, Object> deleteFile(String id) {
        FileInfo fileInfo = fileInfoService.getByKey(id);
        fileInfo.setStatus("1");
        fileInfo.setUpdateTime(DateUtil.getDateTime());
        fileInfoService.updateFileName(fileInfo);
        return RestUtil.success("删除成功");
    }

    /**
     * @Author Armyman
     * @Description //查询文件
     * @Date 14:00 2020/10/10
     **/
    @RequestMapping(value = "/findByFreignAndType", method = RequestMethod.POST)
    public Map<String, Object> findByFreignAndType(String key, String type) {
        List<FileInfo> fileInfoList = fileInfoService.findByFreignAndType(key, type);
        return RestUtil.success(fileInfoList);
    }

    /**
     * @Author Armyman
     * @Description //查询文件
     * @Date 14:00 2020/10/10
     **/
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    public Map<String, Object> findById(String id) {
        FileInfo fileInfoList = fileInfoService.getByKey(id);
        return RestUtil.success(fileInfoList);
    }

    /**
     * @Author Armyman
     * @Description //获取唯一标识
     * @Date 14:00 2020/10/10
     **/
    @RequestMapping(value = "/getPlatCode", method = RequestMethod.GET)
    public Map<String, Object> getPlatCode() {
        Map<String, String> map = new HashMap<>();
        map.put("platCode", UUID.randomUUID().toString().replace("-",""));
        return RestUtil.success(map);
    }

    /**
     * @Author Armyman
     * @Description //下载文件
     * @Date 14:11 2020/10/10
     **/
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "id", required = false) String id) {
        try {
            FileInfo docInfo = fileInfoService.getByKey(id);
            if (docInfo != null) {
                String attachName = docInfo.getFileName() + "." + docInfo.getFileType();//文件名+文件类型  要不下载的无法查看
                String filePath = docInfo.getFilePath();
                String os = System.getProperty("os.name");
                if (os.toLowerCase().startsWith("win")) {
                    filePath = WinAttachDir + filePath;
                } else {
                    filePath = LixAttachDir + filePath;
                }
                String fileName = encodeFileName(attachName);
                response.setHeader("content-type", "application/octet-stream");//服务器告诉浏览器数据类型
                response.setContentType("application/octet-stream");//二进制流 不知道下载文件类型
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);//告诉浏览器这个文件的名称和内容
                BufferedInputStream bis = null;
                OutputStream outputStream = response.getOutputStream();
                try {
                    bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
                    byte[] buff = new byte[bis.available()];
                    bis.read(buff);
                    outputStream.write(buff);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Author Armyman
     * @Description //浏览器兼容
     * @Date 14:20 2020/10/10
     **/
    private String encodeFileName(String fileName) throws UnsupportedEncodingException {
        String agent = request.getHeader("USER-AGENT");//获取浏览器请求头中的user-agent
        System.out.println("agent:" + agent);
        if (null != agent && agent.contains("MSIE") || null != agent && agent.contains("Trident")) {// ie
            fileName = URLEncoder.encode(fileName, "iso-8859-1");
        } else if (null != agent && agent.contains("Mozilla")) {// 火狐,chrome等
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        } else {
            fileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
        }
        return fileName;
    }



}
