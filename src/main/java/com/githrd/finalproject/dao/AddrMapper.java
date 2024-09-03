package com.githrd.finalproject.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.githrd.finalproject.vo.AddrVo;

@Mapper
public interface AddrMapper {

    List<AddrVo> selectList(int member_id);

    AddrVo selectOneFromIdx(int addr_id); // 주소 일련번호 조회

    int insert(AddrVo vo);

    int update(AddrVo vo);

    int delete(AddrVo vo);
}
