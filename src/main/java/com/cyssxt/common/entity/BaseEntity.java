package com.cyssxt.common.entity;

import com.cyssxt.common.annotations.CopyFilter;
import com.cyssxt.common.reflect.Copy;
import com.cyssxt.common.util.CommonUtil;
import com.cyssxt.common.util.DateUtil;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Objects;

@MappedSuperclass
public class BaseEntity extends Copy implements Serializable,Cloneable {
    protected String rowId;
    protected Timestamp createTime;
    protected Timestamp updateTime;
    protected Boolean delFlag;

    @CopyFilter
    Method[] methods = null;

    public BaseEntity() {
        this.rowId = CommonUtil.generatorKey();
        this.createTime = DateUtil.getCurrentTimestamp();
        this.updateTime = this.createTime;
        this.delFlag = false;
    }

    @Transient
    public boolean delStatus(){
        return CommonUtil.isTrue(this.getDelFlag());
    }

    @Id
    @Column(name = "row_id")
    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }


    @Basic
    @Column(name = "del_flag")
    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    public void insert(JpaRepository baseRepository){
        Timestamp createTime = DateUtil.getCurrentTimestamp();
        this.setUpdateTime(createTime);
        this.setCreateTime(createTime);
        this.setDelFlag(false);
        this.setRowId(CommonUtil.generatorKey());
        baseRepository.save(this);
    }

    public void update(JpaRepository baseRepository){
        this.setUpdateTime(DateUtil.getCurrentTimestamp());
        baseRepository.save(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return Objects.equals(rowId, that.rowId) &&
         Objects.equals(delFlag, that.delFlag) &&
         Objects.equals(createTime, that.createTime) &&
                Objects.equals(updateTime, that.updateTime);
    }

    public void del(JpaRepository baseRepository){
        this.setDelFlag(true);
        this.setUpdateTime(DateUtil.getCurrentTimestamp());
        this.update(baseRepository);
    }

    @Override
    public int hashCode() {
        Class clazz = this.getClass();
        Field[] fields = clazz.getFields();
        int length = methods==null?methods.length:fields.length;
        Object[] values = new Object[length];
        if(methods==null) {
            for (int i = 0; i < length; i++) {
                Field field = fields[i];
                String name = field.getName();
                try {
                    PropertyDescriptor pd = new PropertyDescriptor(name, clazz);
                    Method read = pd.getReadMethod();
                    methods[i] = read;
                    values[i] = read.invoke(this);
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }else{
            for(int i=0;i<length;i++){
                Method method = methods[i];
                try {
                    values[i] = method.invoke(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return Objects.hash(values);
    }

    @Override
    public Object clone() {
        Object o = null;
        try{
            o = super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return o;
    }

}
