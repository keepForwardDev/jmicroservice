package com.jcloud.elasticsearch.domain;

import com.jcloud.common.bean.EsPageBaseEntityBean;
import com.jcloud.common.domain.CommonPage;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author jiaxm
 * @date 2021/3/23
 */
public class EsPage<T> extends CommonPage<T> {

    public EsPage () {

    }


    public static EsPage of(EsPageBaseEntityBean bean) {
        EsPage commonPage = new EsPage();
        // es from 从0开始
        commonPage.setPageSize(bean.getPageSize());
        commonPage.setCurrentPage(bean.getCurrentPage());
        return commonPage;
    }


    public EsPage(Page page , List<T> list) {
        this.setList(list);
        // es 查询出来的page需要+1
        this.setCurrentPage(page.getNumber() + 1);
        this.setPageSize(page.getSize());
        this.setTotalCount(page.getTotalElements());
        this.setPageCount(page.getTotalPages());
    }

}
