/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.setting.mapping.test;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.RobotFileOutput;
import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testdata.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testdata.model.table.setting.TestTimeout;
import org.rf.ide.core.testdata.text.read.ParsingState;
import org.rf.ide.core.testdata.text.read.RobotLine;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;

import com.google.common.annotations.VisibleForTesting;


public class TestTimeoutMessageMapper implements IParsingMapper {

    private final ParsingStateHelper utility;


    public TestTimeoutMessageMapper() {
        this.utility = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        rt.getTypes().add(0, RobotTokenType.SETTING_TEST_TIMEOUT_MESSAGE);
        rt.setText(text);
        rt.setRaw(text);

        final SettingTable settings = robotFileOutput.getFileModel()
                .getSettingTable();
        final List<TestTimeout> timeouts = settings.getTestTimeouts();
        if (!timeouts.isEmpty()) {
            timeouts.get(timeouts.size() - 1).addMessageArgument(rt);
        } else {
            // FIXME: some error
        }
        processingState
                .push(ParsingState.SETTING_TEST_TIMEOUT_MESSAGE_ARGUMENTS);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result;
        if (!processingState.isEmpty()) {
            final ParsingState currentState = utility
                    .getCurrentStatus(processingState);
            if (currentState == ParsingState.SETTING_TEST_TIMEOUT_VALUE
                    || currentState == ParsingState.SETTING_TEST_TIMEOUT_MESSAGE_ARGUMENTS) {
                result = true;
            } else if (currentState == ParsingState.SETTING_TEST_TIMEOUT) {
                final List<TestTimeout> testTimeouts = robotFileOutput.getFileModel()
                        .getSettingTable().getTestTimeouts();
                result = checkIfHasAlreadyValue(testTimeouts);
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }


    @VisibleForTesting
    protected boolean checkIfHasAlreadyValue(final List<TestTimeout> testTimeouts) {
        boolean result = false;
        for (final TestTimeout setting : testTimeouts) {
            result = (setting.getTimeout() != null);
            result = result || !setting.getMessageArguments().isEmpty();
            if (result) {
                break;
            }
        }

        return result;
    }

}
