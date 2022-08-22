package com.xe.mynowcoder;


import com.xe.mynowcoder.dao.DiscussPostMapper;
import com.xe.mynowcoder.dao.elasticsearch.DiscussPostRepository;
import com.xe.mynowcoder.entity.DiscussPost;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MyNowCoderApplication.class)
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussMapper;

    @Autowired
    private DiscussPostRepository discussRepository;
//
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testInsert() {
        discussRepository.save(discussMapper.selectDiscussPostById(241));
        discussRepository.save(discussMapper.selectDiscussPostById(242));
        discussRepository.save(discussMapper.selectDiscussPostById(243));

    }

    @Test
    public void testInsertList() {
        discussRepository.saveAll(discussMapper.selectDiscussPosts(101, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(102, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(103, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(111, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(112, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(131, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(132, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(133, 0, 100,0));
        discussRepository.saveAll(discussMapper.selectDiscussPosts(134, 0, 100,0));
    }

    @Test
    public void testUpdate() {
        DiscussPost post = discussMapper.selectDiscussPostById(231);
        post.setContent("我是新人,使劲灌水.");
        discussRepository.save(post);
    }

    @Test
    public void testDelete() {
//         discussRepository.deleteById(231);
        discussRepository.deleteAll();
    }
//
    @Test
    public void testSearchByRepository() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                //多字段搜索
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();



//         底层获取得到了高亮显示的值, 但是没有返回.

        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);

//        search.stream().iterator().forEachRemaining(System.out::println);

        //总条数
        System.out.println(search.getTotalHits());

        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        for (SearchHit<DiscussPost> searchHit : searchHits) {
            System.out.println("----------------------");
            System.out.println(searchHit.getIndex());
            System.out.println(searchHit.getId());
            System.out.println(searchHit.getScore());
            DiscussPost content = searchHit.getContent();
            System.out.println(content);

        }
    }

    @Test
    public void testSearchByTemplate() {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        //高亮显示
        SearchHits<DiscussPost> search = elasticsearchRestTemplate.search(searchQuery, DiscussPost.class);
        List<SearchHit<DiscussPost>> searchHits = search.getSearchHits();
        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : searchHits) {
            System.out.println("----------------------");
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
        for (DiscussPost post : list) {
            System.out.println(post);
        }

    }


}
