package com.example.demo.service.web;

import com.example.demo.domain.web.CommentVO;
import com.example.demo.mapper.web.CommentMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    CommentMapper commentMapper;

    public List<CommentVO> commentList(CommentVO commentVO){
        return commentMapper.commentList();
    }

    public int commentInsert(CommentVO commentVO){
        return commentMapper.commentInsert(commentVO);
    }

    public int commentUpdate(CommentVO commentVO){
        return commentMapper.commentUpdate(commentVO);
    }

    public int commentDelete(String comCode){
        return commentMapper.commentDelete(comCode);
    }
}
