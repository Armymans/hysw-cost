package net.zlw.cloud.whFinance.service;


import com.github.pagehelper.PageHelper;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import net.zlw.cloud.whFinance.domain.vo.MateriesVo;
import net.zlw.cloud.whFinance.mapper.MaterialMapper;
import org.aspectj.weaver.ast.Var;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");



    public void getMaterialservice(MaterieVo materieVo){
        //物料信息
        Materie materie = new Materie();
        List<MateriesVo> materies = materieVo.getMateriel();
        for (MateriesVo thisMateriers : materies) {
            if (thisMateriers.getMaterial_code() != null){
                Materie materie1 = materialMapper.findByCode(thisMateriers.getMaterial_code());
                if(materie1 != null){
                    int s = Integer.parseInt(thisMateriers.getStatus()) - 1;
                    materie1.setDelFlag(s+"");
                    materie1.setUpdateTime(simpleDateFormat.format(new Date()));
                    materialMapper.updateByPrimaryKeySelective(materie1);
                }else {
                    materie.setId(UUID.randomUUID().toString().replace("-", ""));
                    materie.setArea("1");
                    materie.setCreateTime(simpleDateFormat.format(new Date()));
                    materie.setItemName(thisMateriers.getItem_name());
                    materie.setMaterialCode(thisMateriers.getMaterial_code());
                    materie.setRemark(thisMateriers.getRemark());
                    materie.setSpecificationsModels(thisMateriers.getSpecifications_models());
                    materie.setUnit(thisMateriers.getUnit());
                    int s = Integer.parseInt(thisMateriers.getStatus()) - 1;
                    materie.setDelFlag(s + "");
                    materialMapper.insertSelective(materie);
                }
            }
        }

    }

    public void getMaterialserviceOfWj(MaterieVo materieVo){


        //物料信息
        Materie materie = new Materie();
        List<MateriesVo> materies = materieVo.getMateriel();
        for (MateriesVo thisMateriers : materies) {
            if (thisMateriers.getMaterial_code() != null){
                Materie materie1 = materialMapper.findByCode(thisMateriers.getMaterial_code());
                if(materie1 != null){
                    int s = Integer.parseInt(thisMateriers.getStatus()) - 1;
                    materie1.setDelFlag(s+"");
                    materie1.setUpdateTime(simpleDateFormat.format(new Date()));
                    materialMapper.updateByPrimaryKeySelective(materie1);
                }else{
                    materie.setId(UUID.randomUUID().toString().replace("-",""));
                    materie.setArea("2");
                    materie.setCreateTime(simpleDateFormat.format(new Date()));
                    materie.setItemName(thisMateriers.getItem_name());
                    materie.setMaterialCode(thisMateriers.getMaterial_code());
                    materie.setRemark(thisMateriers.getRemark());
                    materie.setSpecificationsModels(thisMateriers.getSpecifications_models());
                    materie.setUnit(thisMateriers.getUnit());
                    int s = Integer.parseInt(thisMateriers.getStatus()) - 1;
                    materie.setDelFlag(s+"");
                    materialMapper.insertSelective(materie);
                }
            }
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
