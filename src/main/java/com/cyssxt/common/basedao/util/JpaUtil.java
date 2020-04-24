package com.cyssxt.common.basedao.util;


import com.cyssxt.common.basedao.dao.BaseRepository;
import com.cyssxt.common.entity.BaseEntity;
import com.cyssxt.common.exception.ValidException;
import com.cyssxt.common.response.CoreErrorMessage;
import com.cyssxt.common.response.ErrorMessage;

import java.util.Optional;

public class JpaUtil {
    public static <T extends BaseEntity> T get(String rowId, BaseRepository baseRepository) throws ValidException {
        if(rowId==null){
            throw new ValidException(CoreErrorMessage.ITEM_ID_NOT_NULL);
        }
        Optional<T> optional = baseRepository.findById(rowId);
        if(optional.isPresent()){
            return optional.get();
        }
        return null;
    }

    public static <T extends BaseEntity> T get(String rowId, BaseRepository baseRepository,boolean flag) throws ValidException {
        if(flag){
            return get(rowId,baseRepository,CoreErrorMessage.ITEM_NOT_FOUND);
        }
        return get(rowId,baseRepository);
    }

    public static <T extends BaseEntity> T get(String rowId, BaseRepository baseRepository, ErrorMessage errorMessage) throws ValidException {
        T t = get(rowId,baseRepository);
        if(t==null && errorMessage!=null){
            throw new ValidException(errorMessage);
        }
        return t;
    }
}
