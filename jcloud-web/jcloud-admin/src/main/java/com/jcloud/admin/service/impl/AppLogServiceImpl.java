package com.jcloud.admin.service.impl;

import com.jcloud.admin.bean.LogFilter;
import com.jcloud.admin.entity.AppLog;
import com.jcloud.admin.service.AppLogSearchWay;
import com.jcloud.admin.service.AppLogService;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.util.DateUtil;
import com.jcloud.common.util.NumberUtil;
import com.jcloud.elasticsearch.domain.EsPage;
import com.jcloud.elasticsearch.util.SearchResultHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志查看
 *
 * @author jiaxm
 * @date 2021/11/24
 */
@Slf4j
@Service
public class AppLogServiceImpl implements AppLogService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public ResponseData<AppLog> pageList(EsPage pager, LogFilter appLog) {
        AppLogSearchWay appLogSearchWay = null;
        if (appLog.getShowTable()) {
            appLogSearchWay = new TableSearch(appLog, pager);
        } else {
            appLogSearchWay = new FileSearch(appLog, pager);
        }
        return appLogSearchWay.getResponseData();
    }


    public NativeSearchQuery getNativeSearchQuery(EsPage pager, LogFilter appLog) {
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
        nativeSearchQuery.addSort(Sort.by("createTime").descending()); // 注意在集群情况下这里的排序不准确，_doc就是Lucene索引文档时本地的先后顺序，在集群环境中各个节点的顺序并不是最终排序。这里为单节点，可以使用。
        return nativeSearchQuery;
    }


    /**
     * 获取项目 和服务器地址分组
     *
     * @return
     */
    public ResponseData getEnum() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(new BoolQueryBuilder());
        nativeSearchQuery.setPageable(PageRequest.of(0, 1));
        String projectName = "projectName";
        String sourceFrom = "sourceFrom";
        nativeSearchQuery.addAggregation(AggregationBuilders.terms(projectName).field(projectName).size(20));
        nativeSearchQuery.addAggregation(AggregationBuilders.terms(sourceFrom).field(sourceFrom).size(20));
        SearchHits<AppLog> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, AppLog.class);
        List<LabelNode> projectNameLabelNodes = SearchResultHelper.stringAggregation(projectName, searchHits.getAggregations());
        List<LabelNode> sourceFromLabelNodes = SearchResultHelper.stringAggregation(sourceFrom, searchHits.getAggregations());
        Map<String, Object> result = new HashMap<>();
        result.put("projectName", projectNameLabelNodes);
        result.put("sourceFrom", sourceFromLabelNodes);
        return ResponseData.getSuccessInstance(result);
    }

    public ResponseData autoRefresh(Long totalCount, String projectName) {
        LogFilter logFilter = new LogFilter();
        logFilter.setProjectName(projectName);
        EsPage esPage = new EsPage();
        NativeSearchQuery nativeSearchQuery = getNativeSearchQuery(esPage, logFilter);
        long total = elasticsearchRestTemplate.count(nativeSearchQuery, AppLog.class);
        if (total > totalCount) {
            Integer size = Long.valueOf(total - totalCount).intValue();
            Pageable pageable = PageRequest.of(0, size);
            nativeSearchQuery.setPageable(pageable);
            SearchHits<AppLog> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, AppLog.class);
            FileSearch fileSearch = new FileSearch(searchHits, pageable);
            return fileSearch.getResponseData();
        }
        return ResponseData.getSuccessInstance();
    }


    /**
     * 表格查询
     */
    class TableSearch implements AppLogSearchWay {

        LogFilter appLog;

        EsPage esPage;

        public TableSearch(LogFilter appLog, EsPage esPage) {
            this.appLog = appLog;
            this.esPage = esPage;
        }

        @Override
        public ResponseData getResponseData() {
            NativeSearchQuery nativeSearchQuery = getNativeSearchQuery(esPage, appLog);
            SearchHits<AppLog> searchHits = elasticsearchRestTemplate.search(nativeSearchQuery, AppLog.class);
            return ResponseData.getSuccessInstance(SearchResultHelper.toPage(searchHits, nativeSearchQuery.getPageable()));
        }

    }

    /**
     * 文件查询式
     */
    class FileSearch implements AppLogSearchWay {

        private boolean cursorExpired = false;

        LogFilter appLog;

        EsPage esPage;

        Pageable pageable;

        SearchHits<AppLog> searchHits;

        public FileSearch(LogFilter appLog, EsPage esPage) {
            this.appLog = appLog;
            this.esPage = esPage;
        }

        public FileSearch(SearchHits<AppLog> searchHits, Pageable pageable) {
            this.searchHits = searchHits;
            this.pageable = pageable;
        }


        private final SearchHits<AppLog> getSearchHits() {
            NativeSearchQuery nativeSearchQuery = getNativeSearchQuery(esPage, appLog);
            this.pageable = nativeSearchQuery.getPageable();
            SearchHits<AppLog> searchHits = null;
            if (nativeSearchQuery.getPageable().getPageNumber() == 0) { // 游标滚动查询
                searchHits = elasticsearchRestTemplate.searchScrollStart(60000, nativeSearchQuery, AppLog.class, IndexCoordinates.of("datacenterlog"));
            } else { // 否则必须传送游标id
                try {
                    searchHits = elasticsearchRestTemplate.searchScrollContinue(appLog.getId(), 60000, AppLog.class, IndexCoordinates.of("datacenterlog"));
                } catch (Exception e) {
                    cursorExpired = true;
                    log.warn("当前游标已失效,继续从0开始:{}", appLog.getId());
                    nativeSearchQuery.setPageable(PageRequest.of(0, pageable.getPageSize()));
                    searchHits = elasticsearchRestTemplate.searchScrollStart(60000, nativeSearchQuery, AppLog.class, IndexCoordinates.of("datacenterlog"));
                }
            }
            this.searchHits = searchHits;
            return searchHits;
        }

        public ResponseData getResponseData() {
            if (this.searchHits == null) {
                getSearchHits();
            }
            SearchHitsImpl<AppLog> searchHitsImpl = (SearchHitsImpl<AppLog>) searchHits;
            CommonPage<AppLog> commonPage = SearchResultHelper.toPage(searchHits, pageable);
            StringBuilder stringBuilder = new StringBuilder();
            String format = "%s %s %s  %s [%s] %s:%s";
            List<AppLog> appLogList = commonPage.getList();
            Calendar calendar = Calendar.getInstance();
            for (AppLog log : appLogList) {
                calendar.setTime(log.getCreateTime());
                calendar.add(Calendar.HOUR, -8);
                stringBuilder.append(String.format(format, log.getSourceFrom(), DateUtil.formatDate(calendar.getTime()), log.getLevel(), log.getPid(), log.getThreadName(), log.getClazz(), log.getMessage()));
                stringBuilder.append("\r");
            }
            ResponseData responseData = ResponseData.getSuccessInstance();
            commonPage.setList(null); // 日志展示无需输出格式化数据
            responseData.setData(commonPage);
            LabelNode labelNode = new LabelNode();
            labelNode.setLabel(stringBuilder.toString());
            labelNode.setName(searchHitsImpl.getScrollId());
            labelNode.setValue(NumberUtil.booleanToInteger(cursorExpired).longValue());
            responseData.setReserveData(labelNode);
            return responseData;
        }
    }
}
