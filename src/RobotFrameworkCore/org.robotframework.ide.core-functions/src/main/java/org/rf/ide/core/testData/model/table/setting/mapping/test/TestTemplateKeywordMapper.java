/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testData.model.table.setting.mapping.test;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testData.model.FilePosition;
import org.rf.ide.core.testData.model.RobotFileOutput;
import org.rf.ide.core.testData.model.table.SettingTable;
import org.rf.ide.core.testData.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testData.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testData.model.table.setting.TestTemplate;
import org.rf.ide.core.testData.text.read.ParsingState;
import org.rf.ide.core.testData.text.read.RobotLine;
import org.rf.ide.core.testData.text.read.recognizer.RobotToken;
import org.rf.ide.core.testData.text.read.recognizer.RobotTokenType;

import com.google.common.annotations.VisibleForTesting;


public class TestTemplateKeywordMapper implements IParsingMapper {

    private final ParsingStateHelper stateHelper;


    public TestTemplateKeywordMapper() {
        this.stateHelper = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        rt.getTypes().add(0, RobotTokenType.SETTING_TEST_TEMPLATE_KEYWORD_NAME);
        rt.setText(text);
        rt.setRaw(text);

        final SettingTable settings = robotFileOutput.getFileModel()
                .getSettingTable();
        final List<TestTemplate> templates = settings.getTestTemplates();
        if (!templates.isEmpty()) {
            templates.get(templates.size() - 1).setKeywordName(rt);
        } else {
            // FIXME: some internal error
        }
        processingState.push(ParsingState.SETTING_TEST_TEMPLATE_KEYWORD);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final ParsingState state = stateHelper.getCurrentStatus(processingState);

        if (state == ParsingState.SETTING_TEST_TEMPLATE) {
            final List<TestTemplate> testTemplates = robotFileOutput.getFileModel()
                    .getSettingTable().getTestTemplates();
            result = !checkIfHasAlreadyKeywordName(testTemplates);
        }
        return result;
    }


    @VisibleForTesting
    protected boolean checkIfHasAlreadyKeywordName(
            final List<TestTemplate> testTemplates) {
        boolean result = false;
        for (final TestTemplate setting : testTemplates) {
            result = (setting.getKeywordName() != null);
            result = result || !setting.getUnexpectedTrashArguments().isEmpty();
            if (result) {
                break;
            }
        }

        return result;
    }
}