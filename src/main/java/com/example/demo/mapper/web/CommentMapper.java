package com.example.demo.mapper.web;

import com.example.demo.domain.web.CommentVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    public List<CommentVO> commentOrderByReco(String bdCode);

    public List<CommentVO> commentOrderByRecent(String bdCode);

    public List<CommentVO> commentOrderByWritenReply(String bdCode);

    public int commentInsert(CommentVO commentVO);

    public int commentUpdate(CommentVO commentVO);

    public int commentDelete(String comCode);
}
