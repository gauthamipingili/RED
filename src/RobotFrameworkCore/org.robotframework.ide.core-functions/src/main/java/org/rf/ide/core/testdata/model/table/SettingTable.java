/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rf.ide.core.testdata.model.RobotFile;
import org.rf.ide.core.testdata.model.presenter.DataDrivenKeywordName;
import org.rf.ide.core.testdata.model.table.setting.AImported;
import org.rf.ide.core.testdata.model.table.setting.DefaultTags;
import org.rf.ide.core.testdata.model.table.setting.ForceTags;
import org.rf.ide.core.testdata.model.table.setting.Metadata;
import org.rf.ide.core.testdata.model.table.setting.SuiteDocumentation;
import org.rf.ide.core.testdata.model.table.setting.SuiteSetup;
import org.rf.ide.core.testdata.model.table.setting.SuiteTeardown;
import org.rf.ide.core.testdata.model.table.setting.TestSetup;
import org.rf.ide.core.testdata.model.table.setting.TestTeardown;
import org.rf.ide.core.testdata.model.table.setting.TestTemplate;
import org.rf.ide.core.testdata.model.table.setting.TestTimeout;
import org.rf.ide.core.testdata.model.table.setting.UnknownSetting;


public class SettingTable extends ARobotSectionTable {

    private final List<AImported> imports = new ArrayList<>();
    private final List<SuiteDocumentation> documentations = new ArrayList<>();
    private final List<Metadata> metadatas = new ArrayList<>();
    private final List<SuiteSetup> suiteSetups = new ArrayList<>();
    private final List<SuiteTeardown> suiteTeardowns = new ArrayList<>();
    private final List<ForceTags> forceTags = new ArrayList<>();
    private final List<DefaultTags> defaultTags = new ArrayList<>();
    private final List<TestSetup> testSetups = new ArrayList<>();
    private final List<TestTeardown> testTeardowns = new ArrayList<>();
    private final List<TestTemplate> testTemplates = new ArrayList<>();
    private final List<TestTimeout> testTimeouts = new ArrayList<>();
    private final List<UnknownSetting> unknownSettings = new ArrayList<>();

    private final DataDrivenKeywordName<TestTemplate> templateKeywordGenerator = new DataDrivenKeywordName<>();


    public SettingTable(final RobotFile parent) {
        super(parent);
    }


    public boolean isEmpty() {
        return imports.isEmpty() && documentations.isEmpty()
                && metadatas.isEmpty() && suiteSetups.isEmpty()
                && suiteTeardowns.isEmpty() && forceTags.isEmpty()
                && defaultTags.isEmpty() && testSetups.isEmpty()
                && testTeardowns.isEmpty() && testTemplates.isEmpty()
                && testTimeouts.isEmpty() && unknownSettings.isEmpty();
    }


    public List<AImported> getImports() {
        return Collections.unmodifiableList(imports);
    }


    public void addImported(final AImported imported) {
        imported.setParent(this);
        imports.add(imported);
    }


    public List<SuiteDocumentation> getDocumentation() {
        return Collections.unmodifiableList(documentations);
    }


    public void addDocumentation(final SuiteDocumentation doc) {
        doc.setParent(this);
        documentations.add(doc);
    }


    public List<Metadata> getMetadatas() {
        return Collections.unmodifiableList(metadatas);
    }


    public void addMetadata(final Metadata metadata) {
        metadata.setParent(this);
        metadatas.add(metadata);
    }


    public List<SuiteSetup> getSuiteSetups() {
        return Collections.unmodifiableList(suiteSetups);
    }


    public void addSuiteSetup(final SuiteSetup suiteSetup) {
        suiteSetup.setParent(this);
        suiteSetups.add(suiteSetup);
    }


    public List<SuiteTeardown> getSuiteTeardowns() {
        return Collections.unmodifiableList(suiteTeardowns);
    }


    public void addSuiteTeardown(final SuiteTeardown suiteTeardown) {
        suiteTeardown.setParent(this);
        suiteTeardowns.add(suiteTeardown);
    }


    public List<ForceTags> getForceTags() {
        return Collections.unmodifiableList(forceTags);
    }


    public void addForceTags(final ForceTags tags) {
        tags.setParent(this);
        forceTags.add(tags);
    }


    public List<DefaultTags> getDefaultTags() {
        return Collections.unmodifiableList(defaultTags);
    }


    public void addDefaultTags(final DefaultTags tags) {
        tags.setParent(this);
        defaultTags.add(tags);
    }


    public List<TestSetup> getTestSetups() {
        return Collections.unmodifiableList(testSetups);
    }


    public void addTestSetup(final TestSetup testSetup) {
        testSetup.setParent(this);
        testSetups.add(testSetup);
    }


    public List<TestTeardown> getTestTeardowns() {
        return Collections.unmodifiableList(testTeardowns);
    }


    public void addTestTeardown(final TestTeardown testTeardown) {
        testTeardown.setParent(this);
        testTeardowns.add(testTeardown);
    }


    public List<TestTemplate> getTestTemplates() {
        return Collections.unmodifiableList(testTemplates);
    }


    public String getRobotViewAboutTestTemplate() {
        return templateKeywordGenerator.createRepresentation(testTemplates);
    }


    public void addTestTemplate(final TestTemplate testTemplate) {
        testTemplate.setParent(this);
        testTemplates.add(testTemplate);
    }


    public List<TestTimeout> getTestTimeouts() {
        return Collections.unmodifiableList(testTimeouts);
    }


    public void addTestTimeout(final TestTimeout testTimeout) {
        testTimeout.setParent(this);
        testTimeouts.add(testTimeout);
    }


    public List<UnknownSetting> getUnknownSettings() {
        return Collections.unmodifiableList(unknownSettings);
    }


    public void addUnknownSetting(final UnknownSetting unknownSetting) {
        unknownSetting.setParent(this);
        unknownSettings.add(unknownSetting);
    }
}