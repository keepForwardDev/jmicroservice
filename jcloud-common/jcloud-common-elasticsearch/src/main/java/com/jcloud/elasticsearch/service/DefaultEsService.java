package com.jcloud.elasticsearch.service;

import com.jcloud.common.consts.Const;
import com.jcloud.common.domain.CommonPage;
import com.jcloud.common.domain.ResponseData;
import com.jcloud.common.service.Transformer;
import com.jcloud.common.util.ReflectUtil;
import com.jcloud.elasticsearch.domain.EsPage;
import com.jcloud.elasticsearch.model.BaseMapping;
import com.jcloud.elasticsearch.util.SearchResultHelper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 子类需要实现 fillData 当索引需要重建的时候需要用此方法，需要先删除索引{@link #removeIndex()}，然后{@link #createIndex()}，
 * convert 将mysql模型 转为es 模型
 * 默认 pageList将整个 实体输出，需要替换的可以覆盖{@link #pageList(EsPage, Object)},使用{@link #pageList(Query, Transformer)}
 *
 * @param <E> es repository
 * @param <T> es enitty
 * @param <B> mysql entity
 */
public abstract class   DefaultEsService<E extends PagingAndSortingRepository<T, Long>, T extends BaseMapping, B> implements Transformer<B, T>, EsCrudListService<T, B> {

    @Autowired
    protected E baseDao;

    @Autowired
    protected ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 保存或者更新实体
     *
     * @param bean
     * @return
     */
    public T saveEntity(B bean) {
        T entity = convert(bean);
        beforeSave(entity, bean);
        baseDao.save(entity);
        afterSave(entity, bean);
        return entity;
    }

    /**
     * before save the entity hook
     *
     * @param entity
     */
    protected void beforeSave(T entity, B bean) {

    }

    /**
     * after save the entity hook
     *
     * @param entity
     */
    protected void afterSave(T entity, B bean) {

    }

    /**
     * 自定义列表查询
     *
     * @param searchQuery
     * @param transformer 可以自定义输出
     * @return
     */
    public ResponseData pageList(Query searchQuery, Transformer<T, ?> transformer) {
        Class<?> clazz = ReflectUtil.getSuperClassGenericType(this.getClass(), 1);
        SearchHits<?> searchHits = elasticsearchRestTemplate.search(searchQuery, clazz);
        CommonPage commonPage = SearchResultHelper.toPage(searchHits, searchQuery.getPageable(), transformer);
        ResponseData result = ResponseData.getSuccessInstance();
        result.setData(commonPage);
        return result;
    }

    /**
     * 默认id倒序列表查询
     *
     * @param page
     * @param bean
     * @return
     */
    public ResponseData pageList(EsPage page, Object bean) {
        NativeSearchQuery nativeSearchQuery = getSearchQuery(page, bean, defaultOrderBy(bean));
        return pageList(nativeSearchQuery, null);
    }

    /**
     * 自定义排序列表查询
     *
     * @param page
     * @param bean
     * @param sortBuilder
     * @return
     */
    public ResponseData pageList(EsPage page, B bean, SortBuilder sortBuilder) {
        NativeSearchQuery nativeSearchQuery = getSearchQuery(page, bean, sortBuilder);
        return pageList(nativeSearchQuery, null);
    }

    /**
     * 自定义query
     *
     * @param page
     * @param bean
     * @param sortBuilder
     * @return
     */
    protected NativeSearchQuery getSearchQuery(EsPage page, Object bean, SortBuilder sortBuilder) {
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withQuery(queryCondition(bean));
        searchQueryBuilder.withPageable(PageRequest.of(page.getCurrentPage() - 1, page.getPageSize()));
        // 是否使用默认排序
        Boolean useDefaultOrder = (Boolean) ReflectUtil.getFieldValue("useDefaultOrder", bean.getClass(), bean);
        // maybe null
        useDefaultOrder = useDefaultOrder == null ? true : useDefaultOrder;
        if (useDefaultOrder && sortBuilder != null) {
            searchQueryBuilder.withSort(sortBuilder);
        }
        NativeSearchQuery searchQuery = searchQueryBuilder.build();
        searchQuery.setTrackTotalHits(true);
        return searchQuery;
    }

    /**
     * 为支持前端排序，定义 前端传入 orderField，orderWay 公共字段
     *
     * @param params
     */
    protected SortBuilder defaultOrderBy(Object params) {
        Object orderField = ReflectUtil.getFieldValue("orderField", params.getClass(), params);
        Object orderWay = ReflectUtil.getFieldValue("orderWay", params.getClass(), params);
        if (orderField != null && !StringUtils.isEmpty(orderField.toString()) && !StringUtils.isEmpty(orderWay.toString())) {
            if (orderWay.toString().equals(Const.ORDER_ASC)) {
                return SortBuilders.fieldSort(orderField.toString()).order(SortOrder.ASC);
            } else {
                return SortBuilders.fieldSort(orderField.toString()).order(SortOrder.DESC);
            }
        } else {
            return SortBuilders.fieldSort("id").order(SortOrder.DESC);
        }
    }


    /**
     * 列表查询条件，真正使用需覆盖
     *
     * @param bean
     * @return
     */
    public QueryBuilder queryCondition(Object bean) {
        BoolQueryBuilder condition = new BoolQueryBuilder();
        condition.must(QueryBuilders.termQuery("deleted", 0));
        return condition;
    }

    /**
     * 物理删除
     *
     * @param idList
     */
    public void physicsDelete(List<Long> idList) {
        idList.forEach(id -> {
            physicsDelete(id);
        });
    }

    /**
     * 物理删除
     *
     * @param id
     */
    public void physicsDelete(Long id) {
        baseDao.deleteById(id);
    }

    @Override
    public boolean createIndex() {
        Class<?> clazz = ReflectUtil.getSuperClassGenericType(this.getClass(), 1);
        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(clazz);
        boolean flag = indexOperations.create();
        if (flag) {
            indexOperations.putMapping(indexOperations.createMapping());
        }
        return flag;
    }

    @Override
    public boolean removeIndex() {
        Class<?> clazz = ReflectUtil.getSuperClassGenericType(this.getClass(), 1);
        return elasticsearchRestTemplate.indexOps(clazz).delete();
    }
}
