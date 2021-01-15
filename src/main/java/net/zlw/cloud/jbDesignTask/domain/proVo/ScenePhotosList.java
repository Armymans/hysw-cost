package net.zlw.cloud.jbDesignTask.domain.proVo;

import lombok.Data;

/***
 * 项目踏勘-现场照片
 */
@Data
public class ScenePhotosList {

    private String id;
    private String scene_photos_file_name;
    private String scene_photos_type;
    private String scene_photos_size;
    private String scene_photos_up_time;
    private String scene_photos_up_by;
    private String scene_photos_link;
}
