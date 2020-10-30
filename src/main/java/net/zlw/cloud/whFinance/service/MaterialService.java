package net.zlw.cloud.whFinance.service;


import javafx.scene.paint.Material;
import net.zlw.cloud.whFinance.dao.MaterialDao;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/***
 * 芜湖物料
 */
@Service
@Transactional
public class MaterialService {

    @Autowired
    private MaterialDao materialDao;


    public void getMaterialservice(MaterieVo materieVo, String Account){


        //物料信息
        Materie materie = new Materie();
        List<Materie> materies = materieVo.getMateries();
        for (Materie thisMateriers : materies) {
            if (thisMateriers.getMaterialCode() != null){
                materie.setId(thisMateriers.getId());
                materie.setCreateTime(thisMateriers.getCreateTime());
                materie.setItemName(thisMateriers.getItemName());
                materie.setMaterialCode(thisMateriers.getMaterialCode());
                materie.setRemark(thisMateriers.getRemark());
                materie.setSpecificationsModels(thisMateriers.getSpecificationsModels());
                materie.setUnit(thisMateriers.getUnit());
                materie.setUpdateTime(thisMateriers.getUpdateTime());
                materie.setStatus("0");
            }
            materialDao.insertSelective(materie);
        }

    }
}
