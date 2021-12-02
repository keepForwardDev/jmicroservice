package com.jcloud.admin.mapper;

import com.jcloud.admin.entity.AppLog;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author jiaxm
 * @date 2021/11/24
 */
@Repository
public interface AppLogDao extends ElasticsearchRepository<AppLog, Long> {
}
