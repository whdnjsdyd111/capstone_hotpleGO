package com.example.demo.service.web.implement;

import com.example.demo.domain.web.ChatLogVO;
import com.example.demo.mapper.web.ChatLogMapper;
import com.example.demo.service.web.ChatLogService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatLogImpl implements ChatLogService {
    @Setter(onMethod_ = @Autowired)
    ChatLogMapper mapper;

    @Override
    public List<ChatLogVO> getListChatLog() {
        return mapper.getListByStr("C");
    }

    @Override
    public List<ChatLogVO> getListMemo() {
        return mapper.getListByStr("M");
    }

    @Override
    public boolean registerChat(ChatLogVO vo) {
        return mapper.insertChat(vo) == 1;
    }

    @Override
    public boolean registerMemo(ChatLogVO vo) {
        return mapper.insertMemo(vo) == 1;
    }
}
