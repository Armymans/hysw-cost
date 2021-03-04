package net.zlw.cloud.whFinance.service;


import com.github.pagehelper.PageHelper;
import net.zlw.cloud.designProject.mapper.OperationLogDao;
import net.zlw.cloud.designProject.model.OperationLog;
import net.zlw.cloud.maintenanceProjectInformation.model.vo.PageRequest;
import net.zlw.cloud.whFinance.domain.Materie;
import net.zlw.cloud.whFinance.domain.vo.MaterieVo;
import net.zlw.cloud.whFinance.domain.vo.MateriesVo;
import net.zlw.cloud.whFinance.mapper.MaterialMapper;
import org.apache.commons.lang3.StringUtils;
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

    @Resource
    private OperationLogDao operationLogDao;

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
                    materie1.setItemName(thisMateriers.getItem_name());
                    materie1.setRemark(thisMateriers.getRemark());
                    materie1.setSpecificationsModels(thisMateriers.getSpecifications_models());
                    materie1.setUnit(thisMateriers.getUnit());
                    materialMapper.updateByPrimaryKeySelective(materie1);

                    //日志
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                    operationLog.setType("16");
                    operationLog.setDoTime(simpleDateFormat.format(new Date()));
                    operationLog.setStatus("0");
                    operationLog.setName("芜湖财务物料库存");
                    operationLog.setDoObject(thisMateriers.getMaterial_code());
                    operationLog.setContent("芜湖财务修改了物料编码为【"+thisMateriers.getMaterial_code()+"】" + "的物料库存");
                    operationLogDao.insertSelective(operationLog);
                }else {
                    if (StringUtils.isEmpty(thisMateriers.getPk_invbasdoc())){
                        thisMateriers.setPk_invbasdoc(UUID.randomUUID().toString().replace("-", ""));
                    }
                    materie.setId(thisMateriers.getPk_invbasdoc());
                    materie.setArea("1");
                    materie.setCreateTime(simpleDateFormat.format(new Date()));
                    materie.setItemName(thisMateriers.getItem_name());
                    materie.setMaterialCode(thisMateriers.getMaterial_code());
                    materie.setRemark(thisMateriers.getRemark());
                    materie.setSpecificationsModels(thisMateriers.getSpecifications_models());
                    materie.setUnit(thisMateriers.getUnit());
                    int s = Integer.parseInt(thisMateriers.getStatus()) - 1;
                    materie.setDelFlag(s + "");
                    materialMapper.insert(materie);

                    //日志
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                    operationLog.setType("16");
                    operationLog.setDoTime(simpleDateFormat.format(new Date()));
                    operationLog.setStatus("0");
                    operationLog.setName("芜湖财务物料库存");
                    operationLog.setDoObject(thisMateriers.getMaterial_code());
                    operationLog.setContent("芜湖财务对接过来一条物料编码为【"+thisMateriers.getMaterial_code()+"】" + "的物料库存");
                    operationLogDao.insertSelective(operationLog);
                }
            }
        }
        // 日志
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
                    materie1.setItemName(thisMateriers.getItem_name());
                    materie1.setRemark(thisMateriers.getRemark());
                    materie1.setSpecificationsModels(thisMateriers.getSpecifications_models());
                    materie1.setUnit(thisMateriers.getUnit());
                    materialMapper.updateByPrimaryKeySelective(materie1);

                    //日志
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                    operationLog.setType("17");
                    operationLog.setDoTime(simpleDateFormat.format(new Date()));
                    operationLog.setStatus("0");
                    operationLog.setName("吴江财务物料库存");
                    operationLog.setDoObject(thisMateriers.getMaterial_code());
                    operationLog.setContent("吴江财务修改了物料编码为【"+thisMateriers.getMaterial_code()+"】" + "的物料库存");
                    operationLogDao.insertSelective(operationLog);
                }else{
                    if (StringUtils.isEmpty(thisMateriers.getPk_invbasdoc())){
                        thisMateriers.setPk_invbasdoc(UUID.randomUUID().toString().replace("-", ""));
                    }
                    materie.setId(thisMateriers.getPk_invbasdoc());
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

                    //日志
                    OperationLog operationLog = new OperationLog();
                    operationLog.setId(UUID.randomUUID().toString().replace("-",""));
                    operationLog.setType("17");
                    operationLog.setDoTime(simpleDateFormat.format(new Date()));
                    operationLog.setStatus("0");
                    operationLog.setName("吴江财务物料库存");
                    operationLog.setDoObject(thisMateriers.getMaterial_code());
                    operationLog.setContent("吴江财务对接过来一条物料编码为【"+thisMateriers.getMaterial_code()+"】" + "的物料库存");
                    operationLogDao.insertSelective(operationLog);
                }
            }
            // 日志
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
