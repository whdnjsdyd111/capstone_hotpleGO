package com.example.demo.service.web;

import com.example.demo.domain.web.ChatLogVO;
import com.example.demo.mapper.web.ChatLogMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLogService {
    @Setter(onMethod_ = @Autowired)
    ChatLogMapper mapper;

    public List<ChatLogVO> getListChatLog() {
        return mapper.getListByStr("C");
    }

    public List<ChatLogVO> getListMemo() {
        return mapper.getListByStr("M");
    }

    public boolean registerChat(ChatLogVO vo) {
        return mapper.insertChat(vo) == 1;
    }

    public boolean registerMemo(ChatLogVO vo) {
        return mapper.insertMemo(vo) == 1;
    }
}
