package net.zlw.cloud.snsEmailFile.mapper;

import net.zlw.cloud.snsEmailFile.model.FileInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
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
            " fi.* , " +
            " mm.member_name userName " +
            "FROM " +
            " file_info fi " +
            " left join member_manage mm on mm.id = fi.user_id " +
            "WHERE " +
            "fi.type = #{type} and fi.plat_code = #{key}")
    List<FileInfo> findByFreignAndTypeDesginCount(@Param("key") String key,@Param("type") String type);

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

    @Select("SELECT * FROM file_info WHERE plat_code = #{id}")
    FileInfo findIdByStatus(@Param("id") String id);

    @Update(
            "UPDATE\n" +
                    "file_info\n" +
                    "SET\n" +
                    "status = '1'\n" +
                    "WHERE\n" +
                    "plat_code = #{key}"
    )
    void deleteOldFileList(@Param("key") String key);

    @Update("UPDATE file_info SET `status` = '1' WHERE user_id = #{foundId}")
    void updateStatus(@Param("foundId") String foundId);

    @Select("SELECT " +
            " fi.* " +
            "FROM " +
            " file_info fi " +
            "WHERE " +
            " fi.STATUS = '0'  " +
            " AND fi.type = #{type} and fi.plat_code = #{key}")
    FileInfo findByCodeAndType(@Param("type") String type,@Param("key") String key);

}
