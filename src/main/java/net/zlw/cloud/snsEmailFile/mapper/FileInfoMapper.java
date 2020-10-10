package net.zlw.cloud.snsEmailFile.mapper;

import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author Armyman
 * @Description 文件mapper
 * @Date 2020/10/9 16:38
 **/
public interface FileInfoMapper extends Mapper<FileInfo> {

    @Select("SELECT * FROM file_info where status = '0' and =#{type} and plat_code = #{key}")
    List<FileInfo> findByFreignAndType(@Param("type") String type, @Param("key") String key);
}
