package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.progressPayment.model.ProgressPaymentTotalPayment;
import net.zlw.cloud.progressPayment.model.vo.ProgressPaymentTotalPaymentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProgressPaymentTotalPaymentDao extends Mapper<ProgressPaymentTotalPayment> {

    @Select("SELECT " +
            " pp.id id, " +
            " p.id progressPaymentId, " +
            " pp.total_payment_amount totalPaymentAmount, " +
            " pp.cumulative_number_payment cumulativeNumberPayment, " +
            " pp.accumulative_payment_proportion accumulativePaymentProportion, " +
            " p.project_type projectType, " +
            " p.receiving_time receivingTime, " +
            " p.compile_time compileTime  " +
            "FROM " +
            " progress_payment_total_payment pp " +
            " LEFT JOIN progress_payment_information p ON pp.progress_payment_id = p.id " +
            "where  " +
            "  pp.del_flag = '0' " +
            " and  " +
            " p.del_flag = '0' " +
            " and  " +
            " p.id = #{id} or #{id} = ''")
    List<ProgressPaymentTotalPaymentVo> findByProgressPaymentId(@Param("id") String id);



}
