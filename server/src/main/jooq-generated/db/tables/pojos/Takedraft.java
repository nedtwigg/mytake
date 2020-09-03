/*
 * This file is generated by jOOQ.
 */
package db.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Takedraft implements Serializable {

    private static final long serialVersionUID = 784033527;

    private Integer id;
    private Integer userId;
    private Integer lastRevision;

    public Takedraft() {}

    public Takedraft(Takedraft value) {
        this.id = value.id;
        this.userId = value.userId;
        this.lastRevision = value.lastRevision;
    }

    public Takedraft(
        Integer id,
        Integer userId,
        Integer lastRevision
    ) {
        this.id = id;
        this.userId = userId;
        this.lastRevision = lastRevision;
    }

    public Integer getId() {
        return this.id;
    }

    public Takedraft setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public Takedraft setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Integer getLastRevision() {
        return this.lastRevision;
    }

    public Takedraft setLastRevision(Integer lastRevision) {
        this.lastRevision = lastRevision;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Takedraft (");

        sb.append(id);
        sb.append(", ").append(userId);
        sb.append(", ").append(lastRevision);

        sb.append(")");
        return sb.toString();
    }
}