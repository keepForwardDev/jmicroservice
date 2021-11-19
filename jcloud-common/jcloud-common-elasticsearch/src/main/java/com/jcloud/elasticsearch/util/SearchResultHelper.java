package com.jcloud.elasticsearch.util;

import com.jcloud.common.bean.ItemNode;
import com.jcloud.common.bean.LabelNode;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.service.Transformer;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * es 查询结果帮助
 * @author jiaxm
 * @date 2021/4/28
 */
public class SearchResultHelper {


    /**
     * 分页输出
     * @param searchHits 查询结果
     * @param pageable 分页
     * @return
     */
    public static CommonPage toPage(SearchHits searchHits, Pageable pageable) {
        return toPage(searchHits, pageable, null);
    }

    /**
     * 分页输出
     * @param searchHits 查询结果
     * @param pageable 分页
     * @param transformer 转换数据接口
     * @return
     */
   public static CommonPage toPage(SearchHits searchHits, Pageable pageable, Transformer transformer) {
       List<Object> dataList = null;
       CommonPage commonPage = new CommonPage();
       commonPage.setCurrentPage(pageable.getPageNumber() + 1);
       commonPage.setPageSize(pageable.getPageSize());
       List<Object> list= (List<Object>) searchHits.stream().map(r -> {
           SearchHit searchHit = (SearchHit)r;
           return searchHit.getContent();
       }).collect(Collectors.toList());
       if (transformer == null) {
           dataList = list;
       } else {
           dataList = transformer.convert(list);
       }
       commonPage.setTotalCount(searchHits.getTotalHits());
       commonPage.setList(dataList);
       return commonPage;
    }

    /**
     * terms转换为string item
     * @param name
     * @param aggregations
     * @return
     */
    public static List<LabelNode> stringAggregation(String name, Aggregations aggregations) {
        List<LabelNode> list = new ArrayList<>();
        Terms terms = aggregations.get(name);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            LabelNode labelNode = new LabelNode();
            labelNode.setLabel(bucket.getKey().toString());
            labelNode.setValue(bucket.getDocCount());
            list.add(labelNode);
        }
        return list;
    }

    /**
     * terms 转换为数字item
     * @param name
     * @param aggregations
     * @return id为分组key
     */
    public static List<ItemNode> numberAggregation(String name, Aggregations aggregations) {
        List<ItemNode> list = new ArrayList<>();
        Terms terms = aggregations.get(name);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            ItemNode itemNode = new ItemNode();
            Long id = bucket.getKeyAsNumber().longValue();
            itemNode.setId(id);
            itemNode.setValue(bucket.getDocCount());
            list.add(itemNode);
        }
        return list;
    }


    /**
     * 日期分组转换，例如我们要获取19年 前面三年的数据，endYear 2019 size 3
     * @param name
     * @param aggregations
     * @param endYear 末尾年份，结果包含该年份
     * @param size
     * @return
     */
    public static List<LabelNode> dateAggregation(String name, Aggregations aggregations, int endYear, int size) {
        List<LabelNode> list = new ArrayList<>();
        ParsedDateHistogram parsedDateHistogram = aggregations.get(name);
        List<ParsedDateHistogram.ParsedBucket> parsedBuckets = (List<ParsedDateHistogram.ParsedBucket>) parsedDateHistogram.getBuckets();
        Map<String, ParsedDateHistogram.ParsedBucket> bucketMap = parsedBuckets.stream().collect(Collectors.toMap(r -> r.getKeyAsString(), r -> {return r;}));
        for (int i = 0; i < size ; i++) {
            LabelNode labelNode = new LabelNode();
            String key = Integer.valueOf(endYear - i).toString();
            labelNode.setLabel(key);
            ParsedDateHistogram.ParsedBucket bucket = bucketMap.get(key);
            if (bucket != null) {
                labelNode.setValue(bucket.getDocCount());
            } else {
                labelNode.setValue(0l);
            }
            list.add(labelNode);
        }
        return list;
    }

    /**
     * 统计分组
     * @param name
     * @param aggregations
     * @return
     */
    public static Double sumAggregation(String name, Aggregations aggregations) {
        ParsedSum parsedSum = aggregations.get(name);
        return parsedSum.getValue();
    }


}
