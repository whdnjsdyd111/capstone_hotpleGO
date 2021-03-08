package com.example.demo.mapper.web;

import com.example.demo.domain.web.ChatLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatLogMapper {
    public List<ChatLogVO> getListByStr(String str);

    public int insertChat(ChatLogVO vo);

    public int insertMemo(ChatLogVO vo);
}
