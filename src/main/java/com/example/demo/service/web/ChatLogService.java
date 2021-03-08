package com.example.demo.service.web;

import com.example.demo.domain.web.ChatLogVO;

import java.util.List;

public interface ChatLogService {
    /**
     *
     * @return 채팅 로그
     * str 에 'C' 을 주입함으로써 채팅 로그
     */
    public List<ChatLogVO> getListChatLog();

    /**
     *
     * @return 메모
     * str 에 'M' 을 주입함으로써 채팅 로그
     */
    public List<ChatLogVO> getListMemo();

    /**
     *
     * @param vo
     * @return 인서트 여부
     * 채팅 인서트
     */
    public boolean registerChat(ChatLogVO vo);

    /**
     *
     * @param vo
     * @return 인서트 여부
     * 메모 인서트
     */
    public boolean registerMemo(ChatLogVO vo);
}
