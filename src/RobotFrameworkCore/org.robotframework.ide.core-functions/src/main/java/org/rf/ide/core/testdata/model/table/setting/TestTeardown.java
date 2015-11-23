/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.setting;

import org.rf.ide.core.testdata.model.AKeywordBaseSetting;
import org.rf.ide.core.testdata.model.ModelType;
import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;


public class TestTeardown extends AKeywordBaseSetting<SettingTable> {

    public TestTeardown(final RobotToken declaration) {
        super(declaration);
    }


    @Override
    public ModelType getModelType() {
        return ModelType.SUITE_TEST_TEARDOWN;
    }
}