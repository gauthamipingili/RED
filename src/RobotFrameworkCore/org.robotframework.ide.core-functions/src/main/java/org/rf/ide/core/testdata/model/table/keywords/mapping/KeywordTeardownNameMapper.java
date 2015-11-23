/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.keywords.mapping;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.RobotFileOutput;
import org.rf.ide.core.testdata.model.table.keywords.KeywordTeardown;
import org.rf.ide.core.testdata.model.table.keywords.UserKeyword;
import org.rf.ide.core.testdata.model.table.mapping.ElementsUtility;
import org.rf.ide.core.testdata.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testdata.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testdata.text.read.IRobotTokenType;
import org.rf.ide.core.testdata.text.read.ParsingState;
import org.rf.ide.core.testdata.text.read.RobotLine;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;


public class KeywordTeardownNameMapper implements IParsingMapper {

    private final ElementsUtility utility;
    private final ParsingStateHelper stateHelper;


    public KeywordTeardownNameMapper() {
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
        types.add(0, RobotTokenType.KEYWORD_SETTING_TEARDOWN_KEYWORD_NAME);
        rt.setText(text);
        rt.setRaw(text);
        final List<UserKeyword> keywords = robotFileOutput.getFileModel()
                .getKeywordTable().getKeywords();
        final UserKeyword keyword = keywords.get(keywords.size() - 1);
        final List<KeywordTeardown> teardowns = keyword.getTeardowns();

        final KeywordTeardown keywordTeardown = teardowns.get(teardowns.size() - 1);
        keywordTeardown.setKeywordName(rt);

        processingState.push(ParsingState.KEYWORD_SETTING_TEARDOWN_KEYWORD);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final ParsingState state = stateHelper.getCurrentStatus(processingState);
        if (state == ParsingState.KEYWORD_SETTING_TEARDOWN) {
            final List<UserKeyword> keywords = robotFileOutput.getFileModel()
                    .getKeywordTable().getKeywords();
            final UserKeyword keyword = keywords.get(keywords.size() - 1);
            final List<KeywordTeardown> teardowns = keyword.getTeardowns();

            result = !utility.checkIfHasAlreadyKeywordName(teardowns);
        }

        return result;
    }

}