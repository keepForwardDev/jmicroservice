package com.jcloud.elasticsearch.util;

import com.jcloud.common.consts.Const;
import com.jcloud.common.util.DateUtil;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.time.ZoneOffset;

/**
 * 便于生成聚合统计AggregationBuilder
 * @author jiaxm
 * @date 2021/5/20
 */
public class AggregationBuilderUtil {

    /**
     * 普通分组聚合统计
     * @param field
     * @param pageSize
     * @param orderWay
     * @return
     */
    public static TermsAggregationBuilder termsAggregationBuilder(String field, int pageSize, String orderWay) {
        return AggregationBuilders.terms(field).field(field).size(pageSize).order(BucketOrder.count(Const.ORDER_ASC.equals(orderWay)));
    }

    /**
     * 日期分组
     * @param field
     * @return
     */
    public static DateHistogramAggregationBuilder dateHistogramAggregationBuilder(String field) {
        DateHistogramAggregationBuilder dateHistogramAggregationBuilder = AggregationBuilders.dateHistogram(field).field(field)
                .dateHistogramInterval(new DateHistogramInterval("year"))
                .format(DateUtil.PATTERN_YEAR).timeZone(ZoneOffset.ofHours(8))
                .order(BucketOrder.key(false));
        return dateHistogramAggregationBuilder;
    }

}
