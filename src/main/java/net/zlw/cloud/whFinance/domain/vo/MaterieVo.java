package net.zlw.cloud.whFinance.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import net.zlw.cloud.whFinance.domain.Materie;

import java.util.List;

@Data
public class MaterieVo {

    @JsonProperty("Materiel")
    private List<MateriesVo> Materiel;
}
