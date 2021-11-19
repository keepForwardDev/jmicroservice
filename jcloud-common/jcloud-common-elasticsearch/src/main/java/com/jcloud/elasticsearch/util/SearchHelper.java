package com.jcloud.elasticsearch.util;

import cn.hutool.extra.spring.SpringUtil;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.service.Transformer;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 原生公共方法查询
 * @author jiaxm
 * @date 2021/6/4
 */
public class SearchHelper {

    private static RestHighLevelClient restHighLevelClient;

    static {
        restHighLevelClient = SpringUtil.getBean(RestHighLevelClient.class);
    }

    /**
     *
     * 分页查询
     *
     *  searchForPage(indexName, SearchSourceBuilder, r -> {
     *  T t = new T();
     *  return t;
     *  },pager);
     *
     * @param indexName 索引名称
     * @param builder
     * @param transformer 转换对象方法
     * @param pager
     * @param <T>
     * @return
     */
    public static <T> CommonPage<T> searchForPage(String indexName, SearchSourceBuilder builder, Transformer<SearchHit, T> transformer, CommonPage<T> pager) {
        builder.trackTotalHits(true);
        SearchRequest request = new SearchRequest(indexName);
        request.source(builder);
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            pager.setTotalCount(response.getHits().getTotalHits().value);
            SearchHit[] hits = response.getHits().getHits();
            List<T> res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(transformer.convert(hit));
            }
            pager.setList(res);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return pager;
    }

    /**
     * list查询
     * @param indexName
     * @param builder
     * @param transformer
     * @param <T>
     * @return
     */
    public static <T> List<T> searchForList(String indexName, SearchSourceBuilder builder, Transformer<SearchHit, T> transformer) {
        CommonPage<T> commonPage = searchForPage(indexName, builder, transformer, new CommonPage<>());
        return commonPage.getList();
    }

}
