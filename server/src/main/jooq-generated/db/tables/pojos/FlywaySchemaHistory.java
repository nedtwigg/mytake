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
public class FlywaySchemaHistory implements Serializable {

    private static final long serialVersionUID = 1070484060;

    private Integer   installedRank;
    private String    version;
    private String    description;
    private String    type;
    private String    script;
    private Integer   checksum;
    private String    installedBy;
    private Timestamp installedOn;
    private Integer   executionTime;
    private Boolean   success;

    public FlywaySchemaHistory() {}

    public FlywaySchemaHistory(FlywaySchemaHistory value) {
        this.installedRank = value.installedRank;
        this.version = value.version;
        this.description = value.description;
        this.type = value.type;
        this.script = value.script;
        this.checksum = value.checksum;
        this.installedBy = value.installedBy;
        this.installedOn = value.installedOn;
        this.executionTime = value.executionTime;
        this.success = value.success;
    }

    public FlywaySchemaHistory(
        Integer   installedRank,
        String    version,
        String    description,
        String    type,
        String    script,
        Integer   checksum,
        String    installedBy,
        Timestamp installedOn,
        Integer   executionTime,
        Boolean   success
    ) {
        this.installedRank = installedRank;
        this.version = version;
        this.description = description;
        this.type = type;
        this.script = script;
        this.checksum = checksum;
        this.installedBy = installedBy;
        this.installedOn = installedOn;
        this.executionTime = executionTime;
        this.success = success;
    }

    public Integer getInstalledRank() {
        return this.installedRank;
    }

    public FlywaySchemaHistory setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
        return this;
    }

    public String getVersion() {
        return this.version;
    }

    public FlywaySchemaHistory setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getDescription() {
        return this.description;
    }

    public FlywaySchemaHistory setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public FlywaySchemaHistory setType(String type) {
        this.type = type;
        return this;
    }

    public String getScript() {
        return this.script;
    }

    public FlywaySchemaHistory setScript(String script) {
        this.script = script;
        return this;
    }

    public Integer getChecksum() {
        return this.checksum;
    }

    public FlywaySchemaHistory setChecksum(Integer checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getInstalledBy() {
        return this.installedBy;
    }

    public FlywaySchemaHistory setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
        return this;
    }

    public Timestamp getInstalledOn() {
        return this.installedOn;
    }

    public FlywaySchemaHistory setInstalledOn(Timestamp installedOn) {
        this.installedOn = installedOn;
        return this;
    }

    public Integer getExecutionTime() {
        return this.executionTime;
    }

    public FlywaySchemaHistory setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
        return this;
    }

    public Boolean getSuccess() {
        return this.success;
    }

    public FlywaySchemaHistory setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("FlywaySchemaHistory (");

        sb.append(installedRank);
        sb.append(", ").append(version);
        sb.append(", ").append(description);
        sb.append(", ").append(type);
        sb.append(", ").append(script);
        sb.append(", ").append(checksum);
        sb.append(", ").append(installedBy);
        sb.append(", ").append(installedOn);
        sb.append(", ").append(executionTime);
        sb.append(", ").append(success);

        sb.append(")");
        return sb.toString();
    }
}
