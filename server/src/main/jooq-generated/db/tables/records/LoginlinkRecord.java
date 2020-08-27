/*
 * This file is generated by jOOQ.
 */
package db.tables.records;


import db.tables.Loginlink;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LoginlinkRecord extends UpdatableRecordImpl<LoginlinkRecord> implements Record5<String, LocalDateTime, LocalDateTime, String, Integer> {

    private static final long serialVersionUID = 165461073;

    /**
     * Setter for <code>public.loginlink.code</code>.
     */
    public LoginlinkRecord setCode(String value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.code</code>.
     */
    public String getCode() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.loginlink.created_at</code>.
     */
    public LoginlinkRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>public.loginlink.expires_at</code>.
     */
    public LoginlinkRecord setExpiresAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.expires_at</code>.
     */
    public LocalDateTime getExpiresAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>public.loginlink.requestor_ip</code>.
     */
    public LoginlinkRecord setRequestorIp(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.requestor_ip</code>.
     */
    public String getRequestorIp() {
        return (String) get(3);
    }

    /**
     * Setter for <code>public.loginlink.account_id</code>.
     */
    public LoginlinkRecord setAccountId(Integer value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.account_id</code>.
     */
    public Integer getAccountId() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<String, LocalDateTime, LocalDateTime, String, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<String, LocalDateTime, LocalDateTime, String, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Loginlink.LOGINLINK.CODE;
    }

    @Override
    public Field<LocalDateTime> field2() {
        return Loginlink.LOGINLINK.CREATED_AT;
    }

    @Override
    public Field<LocalDateTime> field3() {
        return Loginlink.LOGINLINK.EXPIRES_AT;
    }

    @Override
    public Field<String> field4() {
        return Loginlink.LOGINLINK.REQUESTOR_IP;
    }

    @Override
    public Field<Integer> field5() {
        return Loginlink.LOGINLINK.ACCOUNT_ID;
    }

    @Override
    public String component1() {
        return getCode();
    }

    @Override
    public LocalDateTime component2() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime component3() {
        return getExpiresAt();
    }

    @Override
    public String component4() {
        return getRequestorIp();
    }

    @Override
    public Integer component5() {
        return getAccountId();
    }

    @Override
    public String value1() {
        return getCode();
    }

    @Override
    public LocalDateTime value2() {
        return getCreatedAt();
    }

    @Override
    public LocalDateTime value3() {
        return getExpiresAt();
    }

    @Override
    public String value4() {
        return getRequestorIp();
    }

    @Override
    public Integer value5() {
        return getAccountId();
    }

    @Override
    public LoginlinkRecord value1(String value) {
        setCode(value);
        return this;
    }

    @Override
    public LoginlinkRecord value2(LocalDateTime value) {
        setCreatedAt(value);
        return this;
    }

    @Override
    public LoginlinkRecord value3(LocalDateTime value) {
        setExpiresAt(value);
        return this;
    }

    @Override
    public LoginlinkRecord value4(String value) {
        setRequestorIp(value);
        return this;
    }

    @Override
    public LoginlinkRecord value5(Integer value) {
        setAccountId(value);
        return this;
    }

    @Override
    public LoginlinkRecord values(String value1, LocalDateTime value2, LocalDateTime value3, String value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached LoginlinkRecord
     */
    public LoginlinkRecord() {
        super(Loginlink.LOGINLINK);
    }

    /**
     * Create a detached, initialised LoginlinkRecord
     */
    public LoginlinkRecord(String code, LocalDateTime createdAt, LocalDateTime expiresAt, String requestorIp, Integer accountId) {
        super(Loginlink.LOGINLINK);

        set(0, code);
        set(1, createdAt);
        set(2, expiresAt);
        set(3, requestorIp);
        set(4, accountId);
    }
}
