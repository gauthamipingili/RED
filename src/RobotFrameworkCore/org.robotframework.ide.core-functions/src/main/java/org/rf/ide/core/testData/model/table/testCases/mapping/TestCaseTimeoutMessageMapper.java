/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testData.model.table.testCases.mapping;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testData.model.FilePosition;
import org.rf.ide.core.testData.model.RobotFileOutput;
import org.rf.ide.core.testData.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testData.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testData.model.table.testCases.TestCase;
import org.rf.ide.core.testData.model.table.testCases.TestCaseTimeout;
import org.rf.ide.core.testData.text.read.IRobotTokenType;
import org.rf.ide.core.testData.text.read.ParsingState;
import org.rf.ide.core.testData.text.read.RobotLine;
import org.rf.ide.core.testData.text.read.recognizer.RobotToken;
import org.rf.ide.core.testData.text.read.recognizer.RobotTokenType;

import com.google.common.annotations.VisibleForTesting;


public class TestCaseTimeoutMessageMapper implements IParsingMapper {

    private final ParsingStateHelper stateHelper;


    public TestCaseTimeoutMessageMapper() {
        this.stateHelper = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        final List<IRobotTokenType> types = rt.getTypes();
        types.remove(RobotTokenType.UNKNOWN);
        types.add(0, RobotTokenType.TEST_CASE_SETTING_TIMEOUT_MESSAGE);

        rt.setText(text);
        rt.setRaw(text);

        final List<TestCase> testCases = robotFileOutput.getFileModel()
                .getTestCaseTable().getTestCases();
        final TestCase testCase = testCases.get(testCases.size() - 1);
        final List<TestCaseTimeout> timeouts = testCase.getTimeouts();
        final TestCaseTimeout testCaseTimeout = timeouts.get(timeouts.size() - 1);
        testCaseTimeout.addMessagePart(rt);

        processingState
                .push(ParsingState.TEST_CASE_SETTING_TEST_TIMEOUT_MESSAGE_ARGUMENTS);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result;
        if (!processingState.isEmpty()) {
            final ParsingState currentState = stateHelper
                    .getCurrentStatus(processingState);
            if (currentState == ParsingState.TEST_CASE_SETTING_TEST_TIMEOUT_VALUE
                    || currentState == ParsingState.TEST_CASE_SETTING_TEST_TIMEOUT_MESSAGE_ARGUMENTS) {
                result = true;
            } else if (currentState == ParsingState.TEST_CASE_SETTING_TEST_TIMEOUT) {
                final List<TestCase> testCases = robotFileOutput.getFileModel()
                        .getTestCaseTable().getTestCases();
                final List<TestCaseTimeout> timeouts = testCases.get(
                        testCases.size() - 1).getTimeouts();
                result = checkIfHasAlreadyValue(timeouts);
            } else {
                result = false;
            }
        } else {
            result = false;
        }
        return result;
    }


    @VisibleForTesting
    protected boolean checkIfHasAlreadyValue(
            final List<TestCaseTimeout> testCaseTimeouts) {
        boolean result = false;
        for (final TestCaseTimeout setting : testCaseTimeouts) {
            result = (setting.getTimeout() != null);
            result = result || !setting.getMessage().isEmpty();
            if (result) {
                break;
            }
        }

        return result;
    }

}