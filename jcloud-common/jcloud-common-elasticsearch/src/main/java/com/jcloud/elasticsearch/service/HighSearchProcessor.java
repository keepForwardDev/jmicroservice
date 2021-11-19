package com.jcloud.elasticsearch.service;


import com.jcloud.elasticsearch.domain.HighSearchBean;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jcloud.elasticsearch.consts.HighSearchChar.*;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * 高级查询的基础功能,可以重写拓展
 * 作为条件的T 是基础蓝底类
 *
 * @author laiguowei
 * @date 2021/2/24 16:7
 */
public interface HighSearchProcessor<T> {

    String SHOULD = "should";
    String MUST = "must";


    /**
     * 默认的高阶查询 实现方法
     * 暂时只兼容  多条件并且 和多条件或者
     *
     * @param baseSearchArg
     * @param map
     * @return
     */
    default NativeSearchQuery buildEsHighQuery(T baseSearchArg, Map<String, List<HighSearchBean>> map) {
        //如果没有高级查询条件 就直接基于基础查询条件返回
        if (map == null || map.keySet().size() == 0) {
            return this.buildBaseQuery(baseSearchArg);
        }
        List<HighSearchBean> must = new ArrayList<>();
        List<HighSearchBean> should = new ArrayList<>();
        map.forEach((condition, conditions) -> {
            if (condition.equals(MUST)) {
                must.addAll(conditions);
            }
            if (condition.equals(SHOULD)) {
                should.addAll(conditions);
            }
        });
        NativeSearchQuery nativeSearchQuery = this.buildBaseQuery(baseSearchArg);
        BoolQueryBuilder searchBuilder = boolQuery();
        BoolQueryBuilder rootQueryBuilder = (BoolQueryBuilder) nativeSearchQuery.getQuery();

        //1.封装好所有的 并且关系
        BoolQueryBuilder mustBuilder = boolQuery();
        for (HighSearchBean mustHighSearchBean : must) {
            if (mustHighSearchBean.getSymbol().equals(EQUAL.getName())) {
                mustBuilder.must(QueryBuilders.matchPhraseQuery(mustHighSearchBean.getField(), mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(NOT_EQUAL.getName())) {
                mustBuilder.mustNot(QueryBuilders.matchQuery(mustHighSearchBean.getField(), mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(RANGE_GTE.getName())) {
                mustBuilder.must(QueryBuilders.rangeQuery(mustHighSearchBean.getField()).gte(mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(RANGE_GT.getName())) {
                mustBuilder.must(QueryBuilders.rangeQuery(mustHighSearchBean.getField()).gt(mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(RANGE_LTE.getName())) {
                mustBuilder.must(QueryBuilders.rangeQuery(mustHighSearchBean.getField()).lte(mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(RANGE_LT.getName())) {
                mustBuilder.must(QueryBuilders.rangeQuery(mustHighSearchBean.getField()).lt(mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(INCLUDE.getName())) {
                mustBuilder.must(QueryBuilders.matchQuery(mustHighSearchBean.getField(), mustHighSearchBean.getValue()));
            } else if (mustHighSearchBean.getSymbol().equals(NOT_INCLUDE.getName())) {
                mustBuilder.mustNot(QueryBuilders.matchQuery(mustHighSearchBean.getField(), mustHighSearchBean.getValue()));
            }
        }

        searchBuilder.should(mustBuilder);

        //2.遍历所有的should 把它和 上面的所有的并且关系都组装起来 逐个组装
        if (should.size() > 0) {
            BoolQueryBuilder shouldBuilder = boolQuery();
            for (HighSearchBean shouldHighSearchBean : should) {
                //每个或者条件都要带上 抒基础的条件拼接上
                if (shouldHighSearchBean.getSymbol().equals(EQUAL.getName())) {
                    shouldBuilder.should(boolQuery().must(QueryBuilders.matchPhraseQuery(shouldHighSearchBean.getField(), shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(NOT_EQUAL.getName())) {
                    shouldBuilder.should(boolQuery().mustNot(QueryBuilders.matchQuery(shouldHighSearchBean.getField(), shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(RANGE_GTE.getName())) {
                    shouldBuilder.should(boolQuery().must(QueryBuilders.rangeQuery(shouldHighSearchBean.getField()).gte(shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(RANGE_GT.getName())) {
                    shouldBuilder.should(boolQuery().must(QueryBuilders.rangeQuery(shouldHighSearchBean.getField()).gt(shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(RANGE_LTE.getName())) {
                    shouldBuilder.should(boolQuery().must(QueryBuilders.rangeQuery(shouldHighSearchBean.getField()).lte(shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(RANGE_LT.getName())) {
                    shouldBuilder.should(boolQuery().must(QueryBuilders.rangeQuery(shouldHighSearchBean.getField()).lt(shouldHighSearchBean.getValue())));
                } else if (shouldHighSearchBean.getSymbol().equals(INCLUDE.getName())) {
                    shouldBuilder.must(QueryBuilders.matchQuery(shouldHighSearchBean.getField(), shouldHighSearchBean.getValue()));
                } else if (shouldHighSearchBean.getSymbol().equals(NOT_INCLUDE.getName())) {
                    shouldBuilder.mustNot(QueryBuilders.matchQuery(shouldHighSearchBean.getField(), shouldHighSearchBean.getValue()));
                }
            }
            searchBuilder.should(shouldBuilder);
        }

        rootQueryBuilder.must(searchBuilder);
        return nativeSearchQuery;
    }

    /**
     * 基础查询条件 需要子类实现
     *
     * @param t
     * @return
     */
    NativeSearchQuery buildBaseQuery(T t);


}
