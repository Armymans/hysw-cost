package net.zlw.cloud.snsEmailFile.service;

import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    public void updateFileName(FileInfo fileInfo) {
        fileInfoMapper.updateByPrimaryKeySelective(fileInfo);
    }

    public List<FileInfo> findByFreignAndType(String key, String type) {
        return fileInfoMapper.findByFreignAndType(key,type);
    }
}
