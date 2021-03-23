package com.example.demo.mapper;

import com.example.demo.domain.ImageAttachVO;
import com.example.demo.domain.MenuVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MenuMapper {
    public int insert(MenuVO vo);

    public List<MenuVO> selectList(String htId);

    public int delete(@Param("code") String code, @Param("uuid") boolean uuid);

    public int update(MenuVO vo);

    public int updateOnlyImage(MenuVO vo);

    public void updateWithImage(@Param("menu") MenuVO menu, @Param("image")ImageAttachVO image);

    public int updateCate(@Param("htId") String htId, @Param("before") String before, @Param("category") String category);

    public int deleteCate(@Param("htId") String htId, @Param("category") String category);
}
