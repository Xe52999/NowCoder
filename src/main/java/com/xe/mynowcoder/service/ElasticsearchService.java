package com.xe.mynowcoder.service;

/**
 * @author xjx
 * @DATE 2022/8/22
 */

import com.xe.mynowcoder.dao.elasticsearch.DiscussPostRepository;
import com.xe.mynowcoder.entity.DiscussPost;
import jdk.nashorn.internal.objects.NativeNumber;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Handler;

/**
 * 搜索服务
 */
@Service
public class ElasticsearchService {
    @Autowired
    private DiscussPostRepository discussRepository;
    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    public void saveDiscussPost(DiscussPost post) {
        discussRepository.save(post);
    }
    public void deleteDiscussPost(int id) {
        discussRepository.deleteById(id);
    }
    public Map<String,Object> searchDiscussPost(String keyword, int current, int limit) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        SearchHits<DiscussPost> search = restTemplate.search(searchQuery, DiscussPost.class);

        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : searchHits) {
            DiscussPost post = hit.getContent();

            List<String> title = hit.getHighlightField("title");
            if(!title.isEmpty()){
                System.out.println(title);
                post.setTitle(title.get(0));
            }

            List<String> content = hit.getHighlightField("content");
            if(!content.isEmpty()){
                System.out.println(content);
                post.setContent(content.get(0));
            }
            list.add(post);

        }
        Map<String ,Object> map = new HashMap<>();
        map.put("list",list);
        map.put("total",search.getTotalHits());
        return map;
    }


}
