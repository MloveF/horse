package com.kaishengit.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.cache.RedisCacheHelper;
import com.kaishengit.entity.Movie;
import com.kaishengit.mapper.MovieMapper;
import com.kaishengit.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieMapper movieMapper;

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    @Override
    public Movie findById(Integer id) {
        String moviekey = "movie:" + id;
        Movie movie = (Movie) redisCacheHelper.get(moviekey,Movie.class);
        if(movie == null) {
            movie = movieMapper.findById(id);
            redisCacheHelper.set(moviekey,movie,10);
        }
        return movie;
    }

    @Override
    public PageInfo<Movie> findAllByPageNo(Integer pageNo) {
        PageHelper.startPage(pageNo,10);
        return new PageInfo<>(movieMapper.findAll());
    }
}
