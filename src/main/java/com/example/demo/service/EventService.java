package com.example.demo.service;

import com.example.demo.domain.EventVO;
import com.example.demo.mapper.EventMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Setter(onMethod_ = @Autowired)
    EventMapper mapper;

    public boolean registerEvent(EventVO vo) {
        return mapper.insertEvent(vo) == 1;
    }

    public boolean registerAnnounce(EventVO vo) {
        return mapper.insertAnnounce(vo) == 1;
    }

    public List<EventVO> getEventList() {
        return mapper.getList("E");
    }

    public List<EventVO> getAnnounceList() {
        return mapper.getList("A");
    }

    public EventVO getEvent(String code) {
        return mapper.read(code);
    }

    public boolean remove(String code) {
        return mapper.delete(code) == 1;
    }

    public boolean changeEtoA(EventVO vo) {
        return mapper.updateOther(vo, "A") == 1;
    }

    public boolean changeAtoE(EventVO vo) {
        return mapper.updateOther(vo, "E") == 1;
    }

    public boolean change(EventVO vo) {
        return mapper.update(vo) == 1;
    }
}
