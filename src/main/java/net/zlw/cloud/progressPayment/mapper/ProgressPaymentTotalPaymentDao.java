package net.zlw.cloud.progressPayment.mapper;

import net.zlw.cloud.progressPayment.model.ProgressPaymentTotalPayment;
import net.zlw.cloud.progressPayment.model.vo.ProgressPaymentTotalPaymentVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface ProgressPaymentTotalPaymentDao extends Mapper<ProgressPaymentTotalPayment> {

    @Select("SELECT\n" +
            "\tpp.id id,\n" +
            "\tp.id progressPaymentId,\n" +
            "\tpp.total_payment_amount totalPaymentAmount,\n" +
            "\tpp.cumulative_number_payment cumulativeNumberPayment,\n" +
            "\tpp.accumulative_payment_proportion accumulativePaymentProportion,\n" +
            "\tp.project_type projectType,\n" +
            "\tp.receiving_time receivingTime,\n" +
            "\tp.compile_time compileTime \n" +
            "FROM\n" +
            "\tprogress_payment_total_payment pp\n" +
            "\tLEFT JOIN progress_payment_information p ON pp.progress_payment_id = p.id\n" +
            "where \n" +
            "  pp.del_flag = '0'\n" +
            "\tand \n" +
            "\tp.del_flag = '0'\n" +
            "\tand \n" +
            "\tp.id = #{id} or #{id} = ''")
    List<ProgressPaymentTotalPaymentVo> findByProgressPaymentId(@Param("id") String id);
}
