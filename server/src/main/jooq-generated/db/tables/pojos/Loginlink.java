/*
 * This file is generated by jOOQ.
 */
package db.tables.pojos;


import java.io.Serializable;
import java.sql.Timestamp;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Loginlink implements Serializable {

    private static final long serialVersionUID = 924124240;

    private String    code;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private String    requestorIp;
    private Integer   accountId;

    public Loginlink() {}

    public Loginlink(Loginlink value) {
        this.code = value.code;
        this.createdAt = value.createdAt;
        this.expiresAt = value.expiresAt;
        this.requestorIp = value.requestorIp;
        this.accountId = value.accountId;
    }

    public Loginlink(
        String    code,
        Timestamp createdAt,
        Timestamp expiresAt,
        String    requestorIp,
        Integer   accountId
    ) {
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.requestorIp = requestorIp;
        this.accountId = accountId;
    }

    public String getCode() {
        return this.code;
    }

    public Loginlink setCode(String code) {
        this.code = code;
        return this;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }

    public Loginlink setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Timestamp getExpiresAt() {
        return this.expiresAt;
    }

    public Loginlink setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public String getRequestorIp() {
        return this.requestorIp;
    }

    public Loginlink setRequestorIp(String requestorIp) {
        this.requestorIp = requestorIp;
        return this;
    }

    public Integer getAccountId() {
        return this.accountId;
    }

    public Loginlink setAccountId(Integer accountId) {
        this.accountId = accountId;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Loginlink (");

        sb.append(code);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(expiresAt);
        sb.append(", ").append(requestorIp);
        sb.append(", ").append(accountId);

        sb.append(")");
        return sb.toString();
    }
}
