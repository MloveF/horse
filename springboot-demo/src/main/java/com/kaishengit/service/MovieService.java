package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Movie;

public interface MovieService {

    Movie findById(Integer id);

    PageInfo<Movie> findAllByPageNo(Integer pageNo);
}
