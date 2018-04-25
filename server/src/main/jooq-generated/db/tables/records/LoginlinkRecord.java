/*
 * This file is generated by jOOQ.
*/
package db.tables.records;


import db.tables.Loginlink;

import java.sql.Timestamp;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.10.2"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class LoginlinkRecord extends UpdatableRecordImpl<LoginlinkRecord> implements Record5<String, Timestamp, Timestamp, String, Integer> {

    private static final long serialVersionUID = 1887079809;

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
    public LoginlinkRecord setCreatedAt(Timestamp value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.created_at</code>.
     */
    public Timestamp getCreatedAt() {
        return (Timestamp) get(1);
    }

    /**
     * Setter for <code>public.loginlink.expires_at</code>.
     */
    public LoginlinkRecord setExpiresAt(Timestamp value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.loginlink.expires_at</code>.
     */
    public Timestamp getExpiresAt() {
        return (Timestamp) get(2);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<String, Timestamp, Timestamp, String, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row5<String, Timestamp, Timestamp, String, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field1() {
        return Loginlink.LOGINLINK.CODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field2() {
        return Loginlink.LOGINLINK.CREATED_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Timestamp> field3() {
        return Loginlink.LOGINLINK.EXPIRES_AT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Loginlink.LOGINLINK.REQUESTOR_IP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field5() {
        return Loginlink.LOGINLINK.ACCOUNT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component1() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp component3() {
        return getExpiresAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getRequestorIp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component5() {
        return getAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value1() {
        return getCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value2() {
        return getCreatedAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Timestamp value3() {
        return getExpiresAt();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getRequestorIp();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value5() {
        return getAccountId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord value1(String value) {
        setCode(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord value2(Timestamp value) {
        setCreatedAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord value3(Timestamp value) {
        setExpiresAt(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord value4(String value) {
        setRequestorIp(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord value5(Integer value) {
        setAccountId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LoginlinkRecord values(String value1, Timestamp value2, Timestamp value3, String value4, Integer value5) {
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
    public LoginlinkRecord(String code, Timestamp createdAt, Timestamp expiresAt, String requestorIp, Integer accountId) {
        super(Loginlink.LOGINLINK);

        set(0, code);
        set(1, createdAt);
        set(2, expiresAt);
        set(3, requestorIp);
        set(4, accountId);
    }
}
