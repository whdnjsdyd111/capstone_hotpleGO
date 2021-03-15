package com.example.demo.service.web;

import com.example.demo.domain.web.EventVO;

import java.util.List;

public interface EventService {
    /*
     * @param id 기본키
     * @return EventVO
     *
     */
    public EventVO get(String id);

    /*
     *
     * @return List EventVO
     * 관리자 이벤트 페이지에서 조회
     * 진행중인 이벤트
     */
    public List<EventVO> getList();

    /**
     *
     * @param vo
     * @return 등록 여부
     */
    public boolean register(EventVO vo);

}
