package net.zlw.cloud.excelLook.service;

import net.zlw.cloud.excelLook.dao.QuantitiesPartialWorksDao;
import net.zlw.cloud.excelLook.domain.QuantitiesPartialWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/***
 * 分部分项工程量清单计价查看
 */
@Service
@Transactional
public class QuantitiesPartialWorksService {

    @Autowired
    private QuantitiesPartialWorksDao quantitiesPartialWorksDao;

    /***
     * 分部分项工程量清单计价查看
     */
    public List<QuantitiesPartialWorks> quantitiesPartialWorksList(String id){
       return quantitiesPartialWorksDao.getList(id);
    }

    /***
     * 分部分项工程量上家结算查看
     * @param id
     * @return
     */
    public List<QuantitiesPartialWorks> quantitiesPartialWorksLists(String id){
        return quantitiesPartialWorksDao.selectQuantitiespartialWorksById(id);
    }
}
