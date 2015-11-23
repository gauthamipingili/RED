/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testData.model.table.setting.mapping;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testData.model.FilePosition;
import org.rf.ide.core.testData.model.RobotFileOutput;
import org.rf.ide.core.testData.model.table.SettingTable;
import org.rf.ide.core.testData.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testData.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testData.model.table.setting.DefaultTags;
import org.rf.ide.core.testData.text.read.ParsingState;
import org.rf.ide.core.testData.text.read.RobotLine;
import org.rf.ide.core.testData.text.read.recognizer.RobotToken;
import org.rf.ide.core.testData.text.read.recognizer.RobotTokenType;


public class DefaultTagsTagNameMapper implements IParsingMapper {

    private final ParsingStateHelper utility;


    public DefaultTagsTagNameMapper() {
        this.utility = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        rt.getTypes().add(0, RobotTokenType.SETTING_DEFAULT_TAG);
        rt.setText(text);
        rt.setRaw(text);

        final SettingTable settings = robotFileOutput.getFileModel()
                .getSettingTable();
        final List<DefaultTags> suiteDefaultTags = settings.getDefaultTags();
        if (!suiteDefaultTags.isEmpty()) {
            suiteDefaultTags.get(suiteDefaultTags.size() - 1).addTag(rt);
        } else {
            // FIXME: some error
        }
        processingState.push(ParsingState.SETTING_DEFAULT_TAGS_TAG_NAME);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final ParsingState state = utility.getCurrentStatus(processingState);
        result = (state == ParsingState.SETTING_DEFAULT_TAGS || state == ParsingState.SETTING_DEFAULT_TAGS_TAG_NAME);

        return result;
    }

}