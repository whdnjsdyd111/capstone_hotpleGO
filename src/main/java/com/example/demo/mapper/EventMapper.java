package com.example.demo.mapper;

import com.example.demo.domain.Criteria;
import com.example.demo.domain.EventVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventMapper {
    public EventVO read(String code);

    public List<EventVO> getList(String str, Criteria cri);

    public int getTotalCount(String str, Criteria cri);

    public List<EventVO> getEventCurrentList();

    public List<EventVO> getCurrentFive();

    public int insertAnnounce(EventVO vo);

    public int insertEvent(EventVO vo);

    public int updateOther(@Param("vo") EventVO vo, @Param("str") String str);

    public int update(EventVO vo);

    public int delete(String code);
}
