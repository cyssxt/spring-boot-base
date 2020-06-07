package com.cyssxt.common.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AuditEntity extends BaseEntity {
    public AuditEntity(){

    }

    private Byte audit;
    private String auditMsg;
    @Basic
    @Column(name = "audit")
    public Byte getAudit() {
        return audit;
    }

    public void setAudit(Byte audit) {
        this.audit = audit;
    }


    @Basic
    @Column(name = "audit_msg")
    public String getAuditMsg() {
        return auditMsg;
    }

    public void setAuditMsg(String auditMsg) {
        this.auditMsg = auditMsg;
    }
}
