package com.jcloud.orm.domain;


import com.jcloud.common.domain.CommonPage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataBasePage<T> extends CommonPage<T> {

    public DataBasePage() {

    }

    public DataBasePage(Page page) {
        super();
        this.setTotalCount(page.getTotalElements());
        this.setPageCount(page.getTotalPages());
        this.setPageSize(page.getSize());
        this.setCurrentPage(page.getNumber() + 1);
        this.setList(page.getContent());
    }

    public DataBasePage(com.baomidou.mybatisplus.extension.plugins.pagination.Page page, List<T> list) {
        this.setTotalCount(page.getTotal());
        this.setPageCount(Long.valueOf(page.getPages()).intValue());
        this.setPageSize(Long.valueOf(page.getSize()).intValue());
        this.setCurrentPage(Long.valueOf(page.getCurrent()).intValue());
        this.setList(list);
    }

    public void update(Page page, Function mapper) {
        this.setTotalCount(page.getTotalElements());
        this.setPageCount(page.getTotalPages());
        this.setList((List<T>) page.getContent().stream().map(mapper).collect(Collectors.toList()));
    }


    public DataBasePage(Page page, List<T> list) {
        super();
        this.setTotalCount(page.getTotalElements());
        this.setPageCount(Long.valueOf(page.getTotalPages()).intValue());
        this.setPageSize(Long.valueOf(page.getSize()).intValue());
        this.setCurrentPage(Long.valueOf(page.getNumber()).intValue());
        this.setList(list);
    }

    public DataBasePage(long totalCount, int pageCount, int pageSize, int currentPage, List<T> list) {
        super(totalCount, pageCount, pageSize, currentPage, list);
    }

    public DataBasePage(long totalCount, int currentPage, int pageSize, List<T> list) {
        super(totalCount, currentPage, pageSize, list);

    }
}
