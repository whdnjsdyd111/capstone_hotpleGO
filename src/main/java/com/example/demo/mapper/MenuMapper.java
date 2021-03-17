package com.example.demo.mapper;

import com.example.demo.domain.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    public int insert(MenuVO vo);

    public List<MenuVO> selectList(String str);

    public int delete(String code);

    public int update(MenuVO vo);
}
