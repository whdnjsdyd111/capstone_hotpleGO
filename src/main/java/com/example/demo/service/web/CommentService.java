package com.example.demo.service.web;

import com.example.demo.domain.web.BoardVO;
import com.example.demo.domain.web.CommentVO;
import com.example.demo.mapper.web.CommentMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class CommentService {
    @Setter(onMethod_ = @Autowired)
    CommentMapper commentMapper;

    public List<CommentVO> commOdByReco(String bdCode){
        return commentMapper.commentOrderByReco(bdCode);
    }

    public List<CommentVO> commOdByRecent(String bdCode){
        return commentMapper.commentOrderByRecent(bdCode);
    }

    public List<CommentVO> commOdByWritenReply(String comCode){
        return commentMapper.commentOrderByWritenReply(comCode);
    }

    public boolean commentInsert(CommentVO commentVO){
        return commentMapper.commentInsert(commentVO) == 1;
    }

    public boolean commentUpdate(CommentVO commentVO){
        return commentMapper.commentUpdate(commentVO) == 1;
    }

    public boolean commentDelete(String comCode){
        return commentMapper.commentDelete(comCode) == 1;
    }
}
