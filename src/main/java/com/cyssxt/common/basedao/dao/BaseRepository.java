package com.cyssxt.common.basedao.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.transaction.Transactional;

@NoRepositoryBean
public interface BaseRepository<T> extends CrudRepository<T,String>, JpaRepository<T,String> {

    @Transactional
    @Modifying
    @Query("update #{#entityName} set delFlag=true,updateTime=CURRENT_TIMESTAMP where rowId=?1")
    int del(String rowId);
}
