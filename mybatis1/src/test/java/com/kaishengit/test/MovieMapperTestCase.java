package com.kaishengit.test;


import com.kaishengit.entity.Movie;
import com.kaishengit.mapper.MovieMapper;
import com.kaishengit.util.SqlSessionFactoryUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.sql.Array;
import java.util.*;

public class MovieMapperTestCase {

    private SqlSession sqlSession;
    private MovieMapper movieMapper;
    @Before
    public void init() {
        sqlSession = SqlSessionFactoryUtil.getSqlSession(true);
        // 动态代理 动态生成
        movieMapper = sqlSession.getMapper(MovieMapper.class);
    }

    @After
    public void destroy() {
        sqlSession.close();
    }
    @Test
    public void testFindList() {
        String title = "";
        List<Movie> movieList = movieMapper.findList(title);
        for(Movie movie : movieList) {
            System.out.println(movie);
        }
    }

    @Test
    public void testFindByParams() {
        String title = "%大圣归来%";
        String director = "%周星驰%";

        Map<String, Object> params = new HashMap<>();
        params.put("title", title);
        params.put("director",director);

        List<Movie> movieList = movieMapper.findByParams(params);
        for(Movie movie : movieList) {
            System.out.println(movie);
        }
    }

    @Test
    public void testFindByIdList() {

        List<Integer> idList = Arrays.asList(1,2);
        List<Movie> movieList = movieMapper.findByIdList(idList);
        for(Movie movie : movieList) {
            System.out.println(movie);
        }
    }

    @Test
    public void insertBatch() {
        List<Movie> movieList = new ArrayList<>();
        Movie movie = new Movie();
        movie.setTitle("千");
        movie.setDirector("宫崎骏");
        movie.setRate(8.9F);

        movieList.add(movie);
        movieMapper.insertBatch(movieList);

    }
}
