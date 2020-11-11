package net.zlw.cloud.snsEmailFile.service;

import net.tec.cloud.common.util.DateUtil;
import net.zlw.cloud.followAuditing.mapper.TrackMonthlyDao;
import net.zlw.cloud.followAuditing.model.TrackMonthly;
import net.zlw.cloud.snsEmailFile.mapper.FileInfoMapper;
import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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

    @Resource
    private TrackMonthlyDao trackMonthlyDao;


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
    public List<FileInfo> findByFreignAndType2(String key, String type) {

        return fileInfoMapper.findByFreignAndType2(key,type);
    }

    public List<FileInfo> findByPlatCode(String id) {
        return fileInfoMapper.findByPlatCode(id);
    }

    public void deleteOldFileList(String key) {
        fileInfoMapper.deleteOldFileList(key);
    }
}
