package net.zlw.cloud.whFinance.service;


import net.zlw.cloud.whFinance.dao.MaterialDao;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.transaction.Transactional;
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

    public List<Materie> selectAll(String key) {
        Example example = new Example(Materie.class);
        example.createCriteria().andLike("materialCode","%"+key+"%")
                                .andLike("specificationsModels","%"+key+"%")
                                .andEqualTo("status","0");
        List<Materie> materieList = materialDao.selectByExample(example);
        return materieList;
    }

    public void addMaterie(Materie materie) {
        materialDao.insertSelective(materie);
    }

    public Materie findOneMaterie(String id) {
        return materialDao.selectByPrimaryKey(id);
    }

    public void updateMaterie(Materie materie) {
        materialDao.updateByPrimaryKeySelective(materie);
    }

    public void deleteMaterie(String id) {
        Materie materie = materialDao.selectByPrimaryKey(id);
        materie.setStatus("1");
        materialDao.updateByPrimaryKeySelective(materie);
    }
}
