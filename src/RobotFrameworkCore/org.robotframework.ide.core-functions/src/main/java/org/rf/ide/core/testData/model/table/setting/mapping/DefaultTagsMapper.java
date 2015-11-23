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
import org.rf.ide.core.testData.model.table.mapping.ElementPositionResolver;
import org.rf.ide.core.testData.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testData.model.table.mapping.ElementPositionResolver.PositionExpected;
import org.rf.ide.core.testData.model.table.setting.DefaultTags;
import org.rf.ide.core.testData.text.read.IRobotTokenType;
import org.rf.ide.core.testData.text.read.ParsingState;
import org.rf.ide.core.testData.text.read.RobotLine;
import org.rf.ide.core.testData.text.read.recognizer.RobotToken;
import org.rf.ide.core.testData.text.read.recognizer.RobotTokenType;

import com.google.common.annotations.VisibleForTesting;


public class DefaultTagsMapper implements IParsingMapper {

    private final ElementPositionResolver positionResolver;


    public DefaultTagsMapper() {
        this.positionResolver = new ElementPositionResolver();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        final List<IRobotTokenType> types = rt.getTypes();
        types.add(0, RobotTokenType.SETTING_DEFAULT_TAGS_DECLARATION);
        rt.setText(text);
        rt.setRaw(text);

        final SettingTable setting = robotFileOutput.getFileModel().getSettingTable();
        if (setting.getDefaultTags().isEmpty()) {
            final DefaultTags defaultTags = new DefaultTags(rt);
            setting.addDefaultTags(defaultTags);
        }
        processingState.push(ParsingState.SETTING_DEFAULT_TAGS);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final List<IRobotTokenType> types = rt.getTypes();
        if (types.size() == 1
                && types.get(0) == RobotTokenType.SETTING_DEFAULT_TAGS_DECLARATION) {
            if (positionResolver.isCorrectPosition(
                    PositionExpected.SETTING_TABLE_ELEMENT_DECLARATION,
                    robotFileOutput.getFileModel(), currentLine, rt)) {
                if (isIncludedInSettingTable(currentLine, processingState)) {
                    result = true;
                } else {
                    // FIXME: it is in wrong place means no settings table
                    // declaration
                }
            } else {
                // FIXME: wrong place | | Library or | Library | Library X |
                // case.
            }
        }
        return result;
    }


    @VisibleForTesting
    protected boolean isIncludedInSettingTable(final RobotLine line,
            final Stack<ParsingState> processingState) {
        boolean result;
        if (!processingState.isEmpty()) {
            result = (processingState.get(processingState.size() - 1) == ParsingState.SETTING_TABLE_INSIDE);
        } else {
            result = false;
        }

        return result;
    }
}