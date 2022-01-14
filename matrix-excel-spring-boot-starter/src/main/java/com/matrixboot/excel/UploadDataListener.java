package com.matrixboot.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 * <p>
 * create in 2021/2/19 5:41 下午
 *
 * @author shishaodong
 * @version 0.0.1
 */

public class UploadDataListener extends AnalysisEventListener<Object> {

    @Getter
    private final List<Object> arrayList;

    public UploadDataListener() {
        this.arrayList = new ArrayList<>();
    }

    @Override
    public void invoke(Object data, AnalysisContext context) {
        arrayList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // Do nothing
    }
}
