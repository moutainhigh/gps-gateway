package com.zkkj.gps.gateway.ccs.dto.page;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * author : cyc
 * Date : 2019/9/6
 */

@Data
@ApiModel(value = "分页模型", description = "分页模型")
public class PageDto<T> {

    /**
     * 数据
     */
    private List<T> data;

    /**
     * 上一页
     */
    private int lastPage;

    /**
     * 当前页
     */
    private int pageIndex;

    /**
     * 下一页
     */
    private int nextPage;

    /**
     * 每页条数
     */
    private int pageSize;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 总数据条数
     */
    private int totalCount;

    public PageDto(List<T> data, int pageIndex, int pageSize) {
        this.data = data;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalCount = data.size();
        this.totalPage = (totalCount + pageSize - 1) / pageSize;
        this.lastPage = pageIndex - 1 > 1 ? pageIndex - 1 : 1;
        this.nextPage = pageIndex >= totalPage ? totalPage : pageIndex + 1;
    }

    /**
     * 得到分页后的数据
     *
     * @return 分页后结果
     */
    public List<T> getData() {
        int fromIndex = (pageIndex - 1) * pageSize;
        if (fromIndex >= data.size()) {
            return Collections.emptyList();//空数组
        }
        if (fromIndex < 0) {
            return Collections.emptyList();//空数组
        }
        int toIndex = pageIndex * pageSize;
        if (toIndex >= data.size()) {
            toIndex = data.size();
        }
        return data.subList(fromIndex, toIndex);
    }
}
