package com.example.demo.service;

import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.ImageAttachVO;
import com.example.demo.mapper.HotpleMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Log4j2
public class HotpleService {
    @Setter(onMethod_ = @Autowired)
    HotpleMapper mapper;

    public HotpleVO getId(String id) {
        return mapper.readId(id);
    }

    public HotpleVO getIdByAddr(String addr) {
        return mapper.readAddr(addr);
    }

    public List<HotpleVO> getHotples(Set<Long> htIds) {
        return mapper.readHotples(htIds);
    }
    public List<HotpleVO> getAllHotples(){
        return mapper.getAllHotples();
    }

    public HotpleVO getByUCode(String uCode) {
        return mapper.selectByManager(uCode);
    }

    public List<HotpleVO> getByKeyword(String keyword) {
        return mapper.searchHotple(keyword);
    }

    public List<HotpleVO> getByKeywordGeo(String keyword, double lat, double lng) {
        return mapper.searchHotpleByGeo(keyword, lat, lng);
    }

    public List<HotpleVO> getByGeoAndArea(double lat, double lng, int area) {
        return mapper.searchHotpleByGeoAndArea(lat, lng, area);
    }

    public boolean registerBusn(HotpleVO vo) {
        return mapper.insertBusn(vo) == 1;
    }

    public boolean registerBusnGo(HotpleVO vo) {
        return mapper.insertBusnGo(vo) == 1;
    }

    public void updateWithImage(HotpleVO hotple, ImageAttachVO image) {
        mapper.updateWithImage(hotple, image);
    }

    public boolean update(HotpleVO vo) {
        return mapper.update(vo) == 1;
    }

    public boolean remove(HotpleVO vo) {
        return mapper.delete(vo) == 1;
    }

    public boolean insertGoogle(List<HotpleVO> vo) {
        return mapper.insertGoogle(vo) == 1;
    }

    public int searchGoogle(String keyword) {
        return mapper.searchGoogle(keyword);
    }

    public String readGoId(String goId) {
        return mapper.readGoId(goId);
    }

    public long getHtId() {
        return mapper.getHtId();
    }
}
