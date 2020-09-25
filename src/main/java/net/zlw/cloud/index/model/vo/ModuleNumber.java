package net.zlw.cloud.index.model.vo;

import lombok.Data;
import net.zlw.cloud.progressPayment.model.BaseProject;

@Data
public class ModuleNumber {
    private BaseProject baseProject;
    private String projectFlow;
    private String designFlow;
}
