package com.matrixboot.excel.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * TODO
 * <p>
 * create in 2022/4/12 15:06
 *
 * @author shishaodong
 * @version 0.0.1
 */
@Data
public class ExcelDataDTO {

    @ExcelProperty("标题")
    private String title;

    @ExcelProperty("数据")
    private String data;

    @ExcelProperty("序号")
    private Integer index;

}
