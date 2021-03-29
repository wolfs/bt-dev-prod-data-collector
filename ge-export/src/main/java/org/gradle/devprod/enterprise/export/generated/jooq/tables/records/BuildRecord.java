/*
 * This file is generated by jOOQ.
 */
package org.gradle.devprod.enterprise.export.generated.jooq.tables.records;


import java.time.OffsetDateTime;

import org.gradle.devprod.enterprise.export.generated.jooq.tables.Build;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record8;
import org.jooq.Row8;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BuildRecord extends UpdatableRecordImpl<BuildRecord> implements Record8<String, String, String, Long, OffsetDateTime, OffsetDateTime, String, String> {

    private static final long serialVersionUID = -918053620;

    /**
     * Setter for <code>public.build.build_id</code>.
     */
    public void setBuildId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.build.build_id</code>.
     */
    public String getBuildId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.build.root_project</code>.
     */
    public void setRootProject(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.build.root_project</code>.
     */
    public String getRootProject() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.build.path_to_first_test_task</code>.
     */
    public void setPathToFirstTestTask(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.build.path_to_first_test_task</code>.
     */
    public String getPathToFirstTestTask() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.build.time_to_first_test_task</code>.
     */
    public void setTimeToFirstTestTask(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.build.time_to_first_test_task</code>.
     */
    public Long getTimeToFirstTestTask() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>public.build.build_start</code>.
     */
    public void setBuildStart(OffsetDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.build.build_start</code>.
     */
    public OffsetDateTime getBuildStart() {
        return (OffsetDateTime) get(4);
    }

    /**
     * Setter for <code>public.build.build_finish</code>.
     */
    public void setBuildFinish(OffsetDateTime value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.build.build_finish</code>.
     */
    public OffsetDateTime getBuildFinish() {
        return (OffsetDateTime) get(5);
    }

    /**
     * Setter for <code>public.build.username</code>.
     */
    public void setUsername(String value) {
        set(6, value);
    }

    /**
     * Getter for <code>public.build.username</code>.
     */
    public String getUsername() {
        return (String) get(6);
    }

    /**
     * Setter for <code>public.build.host</code>.
     */
    public void setHost(String value) {
        set(7, value);
    }

    /**
     * Getter for <code>public.build.host</code>.
     */
    public String getHost() {
        return (String) get(7);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record8 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row8<String, String, String, Long, OffsetDateTime, OffsetDateTime, String, String> fieldsRow() {
        return (Row8) super.fieldsRow();
    }

    @Override
    public Row8<String, String, String, Long, OffsetDateTime, OffsetDateTime, String, String> valuesRow() {
        return (Row8) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Build.BUILD.BUILD_ID;
    }

    @Override
    public Field<String> field2() {
        return Build.BUILD.ROOT_PROJECT;
    }

    @Override
    public Field<String> field3() {
        return Build.BUILD.PATH_TO_FIRST_TEST_TASK;
    }

    @Override
    public Field<Long> field4() {
        return Build.BUILD.TIME_TO_FIRST_TEST_TASK;
    }

    @Override
    public Field<OffsetDateTime> field5() {
        return Build.BUILD.BUILD_START;
    }

    @Override
    public Field<OffsetDateTime> field6() {
        return Build.BUILD.BUILD_FINISH;
    }

    @Override
    public Field<String> field7() {
        return Build.BUILD.USERNAME;
    }

    @Override
    public Field<String> field8() {
        return Build.BUILD.HOST;
    }

    @Override
    public String component1() {
        return getBuildId();
    }

    @Override
    public String component2() {
        return getRootProject();
    }

    @Override
    public String component3() {
        return getPathToFirstTestTask();
    }

    @Override
    public Long component4() {
        return getTimeToFirstTestTask();
    }

    @Override
    public OffsetDateTime component5() {
        return getBuildStart();
    }

    @Override
    public OffsetDateTime component6() {
        return getBuildFinish();
    }

    @Override
    public String component7() {
        return getUsername();
    }

    @Override
    public String component8() {
        return getHost();
    }

    @Override
    public String value1() {
        return getBuildId();
    }

    @Override
    public String value2() {
        return getRootProject();
    }

    @Override
    public String value3() {
        return getPathToFirstTestTask();
    }

    @Override
    public Long value4() {
        return getTimeToFirstTestTask();
    }

    @Override
    public OffsetDateTime value5() {
        return getBuildStart();
    }

    @Override
    public OffsetDateTime value6() {
        return getBuildFinish();
    }

    @Override
    public String value7() {
        return getUsername();
    }

    @Override
    public String value8() {
        return getHost();
    }

    @Override
    public BuildRecord value1(String value) {
        setBuildId(value);
        return this;
    }

    @Override
    public BuildRecord value2(String value) {
        setRootProject(value);
        return this;
    }

    @Override
    public BuildRecord value3(String value) {
        setPathToFirstTestTask(value);
        return this;
    }

    @Override
    public BuildRecord value4(Long value) {
        setTimeToFirstTestTask(value);
        return this;
    }

    @Override
    public BuildRecord value5(OffsetDateTime value) {
        setBuildStart(value);
        return this;
    }

    @Override
    public BuildRecord value6(OffsetDateTime value) {
        setBuildFinish(value);
        return this;
    }

    @Override
    public BuildRecord value7(String value) {
        setUsername(value);
        return this;
    }

    @Override
    public BuildRecord value8(String value) {
        setHost(value);
        return this;
    }

    @Override
    public BuildRecord values(String value1, String value2, String value3, Long value4, OffsetDateTime value5, OffsetDateTime value6, String value7, String value8) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached BuildRecord
     */
    public BuildRecord() {
        super(Build.BUILD);
    }

    /**
     * Create a detached, initialised BuildRecord
     */
    public BuildRecord(String buildId, String rootProject, String pathToFirstTestTask, Long timeToFirstTestTask, OffsetDateTime buildStart, OffsetDateTime buildFinish, String username, String host) {
        super(Build.BUILD);

        set(0, buildId);
        set(1, rootProject);
        set(2, pathToFirstTestTask);
        set(3, timeToFirstTestTask);
        set(4, buildStart);
        set(5, buildFinish);
        set(6, username);
        set(7, host);
    }
}
