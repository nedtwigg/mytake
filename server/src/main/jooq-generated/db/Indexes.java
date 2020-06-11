/*
 * This file is generated by jOOQ.
 */
package db;


import db.tables.FlywaySchemaHistory;
import db.tables.Takepublished;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables of the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index FLYWAY_SCHEMA_HISTORY_S_IDX = Indexes0.FLYWAY_SCHEMA_HISTORY_S_IDX;
    public static final Index TAKEPUBLISHED_TITLE_USER = Indexes0.TAKEPUBLISHED_TITLE_USER;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index FLYWAY_SCHEMA_HISTORY_S_IDX = Internal.createIndex("flyway_schema_history_s_idx", FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, new OrderField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.SUCCESS }, false);
        public static Index TAKEPUBLISHED_TITLE_USER = Internal.createIndex("takepublished_title_user", Takepublished.TAKEPUBLISHED, new OrderField[] { Takepublished.TAKEPUBLISHED.TITLE_SLUG, Takepublished.TAKEPUBLISHED.USER_ID }, true);
    }
}
