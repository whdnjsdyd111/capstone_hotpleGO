package com.example.demo.mapper;

import com.example.demo.domain.HotpleVO;
import com.example.demo.domain.ImageAttachVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface HotpleMapper {
    public HotpleVO readId(String htId);

    public HotpleVO readAddr(String str);

    public List<HotpleVO> readHotples(Set<Long> htIds);

    public HotpleVO selectByManager(String uCode);

    public List<HotpleVO> searchHotple(String keyword);

    public List<HotpleVO> searchHotpleByGeo(String keyword, double lat, double lng);

    public List<HotpleVO> searchHotpleByGeoAndArea(double lat, double lng, int area);

    public int insertBusn(HotpleVO vo);

    public int insertBusnGo(HotpleVO vo);

    public void updateWithImage(@Param("hotple") HotpleVO hotple, @Param("image") ImageAttachVO image);

    public int update(HotpleVO vo);

    public int delete(HotpleVO vo);

    public int insertGoogle(List<HotpleVO> vo);

    List<HotpleVO> getAllHotples();
}
