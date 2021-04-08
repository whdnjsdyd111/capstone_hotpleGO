package com.example.demo.service;

import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.MenuVO;
import com.example.demo.mapper.MenuMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Setter(onMethod_ = @Autowired)
    MenuMapper mapper;

    public boolean register(MenuVO vo) {
        return mapper.insert(vo) == 1;
    }

    public List<MenuVO> getList(String htId) {
        return mapper.selectList(htId);
    }

    public List<MenuVO> getListByUser(String uCode) {
        return mapper.selectListByUser(uCode);
    }

    public boolean remove(String code, boolean uuid) {
        return mapper.delete(code, uuid) == 1;
    }

    public boolean update(MenuVO vo) {
        return mapper.update(vo) == 1;
    }

    public boolean updateOnlyImage(MenuVO vo) {
        return mapper.updateOnlyImage(vo) == 1;
    }

    public void updateWithImage(MenuVO vo, ImageAttachVO imageAttachVO) {
        mapper.updateWithImage(vo, imageAttachVO);
    }

    public boolean updateCategory(String htId, String before, String category) {
        return mapper.updateCate(htId, before, category) > 0;
    }

    public boolean removeCategory(String htId, String category) {
        return mapper.deleteCate(htId, category) > 0;
    }
}
