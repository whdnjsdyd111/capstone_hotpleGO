package com.example.demo.mapper.web;

import com.example.demo.domain.web.EventVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    public EventVO read(String id);

    public List<EventVO> getList(String str);

    public int insert(EventVO vo);

    public int delete(String id);
}
