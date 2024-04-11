package com.spring.dao;

import com.base.MapperBase;
import com.spring.entity.Text;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TextMapper extends MapperBase<Text> {
    public void insertText(Text text);
}
