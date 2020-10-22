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

    @Select("SELECT " +
            " fi.* , " +
            " mm.member_name userName " +
            "FROM " +
            " file_info fi " +
            " left join member_manage mm on mm.id = fi.user_id " +
            "WHERE " +
            " fi.STATUS = '0'  " +
            " AND fi.type = #{type} and fi.plat_code = #{key}")
    List<FileInfo> findByFreignAndType(@Param("key") String key,@Param("type") String type);

    @Select("SELECT " +
            " fi.* " +
            "FROM " +
            " file_info fi " +
            "WHERE " +
            " fi.STATUS = '0'  " +
            " AND fi.type = #{type} and fi.plat_code = #{key}")
    List<FileInfo> findByFreignAndType2(@Param("key") String key,@Param("type") String type);

    @Select("SELECT " +
            " fi.* " +
            "FROM " +
            " file_info fi " +
            "WHERE " +
            " fi.STATUS = '0'  " +
            " AND fi.plat_code = #{id}")
    List<FileInfo> findByPlatCode(@Param("id")String id);
}
