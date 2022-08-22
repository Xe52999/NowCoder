package com.xe.mynowcoder.dao.elasticsearch;

import com.xe.mynowcoder.entity.DiscussPost;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author xjx
 * @DATE 2022/8/19
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer>{

}
