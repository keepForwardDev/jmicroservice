package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.LogFilter;
import com.jcloud.admin.entity.AppLog;
import com.jcloud.admin.service.AppLogService;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.DateUtil;
import com.jcloud.elasticsearch.domain.EsPage;
import com.jcloud.elasticsearch.util.SearchResultHelper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志查看
 * @author jiaxm
 * @date 2021/11/24
 */
@Service
public class AppLogServiceImpl implements AppLogService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public ResponseData<AppLog> pageList(EsPage pager, LogFilter appLog) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (StringUtils.isNotBlank(appLog.getClazz())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("clazz", appLog.getClazz()));
        }
        if (StringUtils.isNotBlank(appLog.getLevel())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("level", appLog.getLevel()));
        }
        if (StringUtils.isNotBlank(appLog.getMessage())) {
            boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("message", appLog.getMessage()));
        }
        if (appLog.getLCreateTime() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").gte(appLog.getLCreateTime()));
        }
        if (appLog.getRCreateTime() != null) {
            boolQueryBuilder.must(QueryBuilders.rangeQuery("createTime").lte(appLog.getRCreateTime()));
        }
        if (StringUtils.isNotBlank(appLog.getProjectName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("projectName", appLog.getProjectName()));
        }
        if (StringUtils.isNotBlank(appLog.getSourceFrom())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("sourceFrom", appLog.getSourceFrom()));
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        nativeSearchQuery.setTrackTotalHits(true);
        nativeSearchQuery.setPageable(PageRequest.of(pager.getCurrentPage() - 1, pager.getPageSize()));
        SearchHits<AppLog> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, AppLog.class);
        CommonPage<AppLog> commonPage = SearchResultHelper.toPage(searchHits, nativeSearchQuery.getPageable(), null);
        StringBuilder stringBuilder = new StringBuilder();
        if (!appLog.getShowTable()) {
            String format = "%s %s %s %s  %s [%s] %s:%s";
            List<AppLog> appLogList = commonPage.getList();
            for (AppLog log : appLogList) {
                stringBuilder.append(String.format(format, log.getProjectName(), log.getSourceFrom(), DateUtil.formatDate(log.getCreateTime()), log.getLevel(), log.getPid(), log.getThreadName(), log.getClazz(), log.getMessage()));
                stringBuilder.append("\r");
            }
            ResponseData responseData = ResponseData.getSuccessInstance();
            commonPage.setList(null);
            responseData.setData(commonPage);
            responseData.setReserveData(stringBuilder.toString());
            return responseData;
        } else {
            return ResponseData.getSuccessInstance(commonPage);
        }
    }
}
