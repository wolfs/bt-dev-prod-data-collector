/*
 * This file is generated by jOOQ.
 */
package org.gradle.devprod.enterprise.export.generated.jooq.tables.records;


import org.gradle.devprod.enterprise.export.generated.jooq.tables.Tags;
import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TagsRecord extends UpdatableRecordImpl<TagsRecord> implements Record2<String, String> {

    private static final long serialVersionUID = -917467245;

    /**
     * Setter for <code>public.tags.build_id</code>.
     */
    public void setBuildId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.tags.build_id</code>.
     */
    public String getBuildId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>public.tags.tag_name</code>.
     */
    public void setTagName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.tags.tag_name</code>.
     */
    public String getTagName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<String, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row2<String, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<String, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return Tags.TAGS.BUILD_ID;
    }

    @Override
    public Field<String> field2() {
        return Tags.TAGS.TAG_NAME;
    }

    @Override
    public String component1() {
        return getBuildId();
    }

    @Override
    public String component2() {
        return getTagName();
    }

    @Override
    public String value1() {
        return getBuildId();
    }

    @Override
    public String value2() {
        return getTagName();
    }

    @Override
    public TagsRecord value1(String value) {
        setBuildId(value);
        return this;
    }

    @Override
    public TagsRecord value2(String value) {
        setTagName(value);
        return this;
    }

    @Override
    public TagsRecord values(String value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TagsRecord
     */
    public TagsRecord() {
        super(Tags.TAGS);
    }

    /**
     * Create a detached, initialised TagsRecord
     */
    public TagsRecord(String buildId, String tagName) {
        super(Tags.TAGS);

        set(0, buildId);
        set(1, tagName);
    }
}
