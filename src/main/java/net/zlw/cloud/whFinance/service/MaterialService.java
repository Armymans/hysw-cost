package net.zlw.cloud.whFinance.service;


import com.github.pagehelper.PageHelper;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import net.zlw.cloud.whFinance.mapper.MaterialMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/***
 * 芜湖物料
 */
@Service
@Transactional
public class MaterialService {

    @Resource
    private MaterialMapper materialMapper;




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
                materie.setDelFlag("0");
            }
            materialMapper.insertSelective(materie);
        }

    }

    public List<Materie> selectAll(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(),pageRequest.getPageSize());
        List<Materie> materieList = materialMapper.findAllMaterie(pageRequest.getKeyWord());
        return materieList;
    }

    public void addMaterie(Materie materie) {
        materie.setId(UUID.randomUUID().toString().replaceAll("-",""));
        materie.setDelFlag("0");
        materialMapper.insertSelective(materie);
    }

    public Materie findOneMaterie(String id) {
        Materie materie = materialMapper.selectByPrimaryKey(id);
        return materie;
    }

    public void updateMaterie(Materie materie) {
        materialMapper.updateByPrimaryKeySelective(materie);
    }

    public void deleteMaterie(String id) {
        Materie materie = materialMapper.selectByPrimaryKey(id);
            materie.setDelFlag("1");
            materialMapper.updateByPrimaryKeySelective(materie);
    }
}
