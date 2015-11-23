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
import org.rf.ide.core.testData.model.table.mapping.ElementsUtility;
import org.rf.ide.core.testData.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testData.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testData.model.table.testCases.TestCase;
import org.rf.ide.core.testData.model.table.testCases.TestCaseTeardown;
import org.rf.ide.core.testData.text.read.IRobotTokenType;
import org.rf.ide.core.testData.text.read.ParsingState;
import org.rf.ide.core.testData.text.read.RobotLine;
import org.rf.ide.core.testData.text.read.recognizer.RobotToken;
import org.rf.ide.core.testData.text.read.recognizer.RobotTokenType;


public class TestCaseTeardownKeywordArgumentMapper implements IParsingMapper {

    private final ElementsUtility utility;
    private final ParsingStateHelper stateHelper;


    public TestCaseTeardownKeywordArgumentMapper() {
        this.utility = new ElementsUtility();
        this.stateHelper = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        final List<IRobotTokenType> types = rt.getTypes();
        types.remove(RobotTokenType.UNKNOWN);
        types.add(0, RobotTokenType.TEST_CASE_SETTING_TEARDOWN_KEYWORD_ARGUMENT);

        rt.setText(text);
        rt.setRaw(text);
        final List<TestCase> testCases = robotFileOutput.getFileModel()
                .getTestCaseTable().getTestCases();
        final TestCase testCase = testCases.get(testCases.size() - 1);
        final List<TestCaseTeardown> teardowns = testCase.getTeardowns();
        final TestCaseTeardown teardown = teardowns.get(teardowns.size() - 1);
        teardown.addArgument(rt);

        processingState
                .push(ParsingState.TEST_CASE_SETTING_TEARDOWN_KEYWORD_ARGUMENT);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final ParsingState state = stateHelper.getCurrentStatus(processingState);
        if (state == ParsingState.TEST_CASE_SETTING_TEARDOWN) {
            final List<TestCase> tests = robotFileOutput.getFileModel()
                    .getTestCaseTable().getTestCases();
            final List<TestCaseTeardown> teardowns = tests.get(tests.size() - 1)
                    .getTeardowns();
            result = utility.checkIfHasAlreadyKeywordName(teardowns);
        } else if (state == ParsingState.TEST_CASE_SETTING_TEARDOWN_KEYWORD
                || state == ParsingState.TEST_CASE_SETTING_TEARDOWN_KEYWORD_ARGUMENT) {
            result = true;
        }

        return result;
    }

}