/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testdata.model.table.setting.mapping;

import java.util.List;
import java.util.Stack;

import org.rf.ide.core.testdata.model.FilePosition;
import org.rf.ide.core.testdata.model.RobotFileOutput;
import org.rf.ide.core.testdata.model.table.SettingTable;
import org.rf.ide.core.testdata.model.table.mapping.ElementPositionResolver;
import org.rf.ide.core.testdata.model.table.mapping.IParsingMapper;
import org.rf.ide.core.testdata.model.table.mapping.ParsingStateHelper;
import org.rf.ide.core.testdata.model.table.mapping.ElementPositionResolver.PositionExpected;
import org.rf.ide.core.testdata.model.table.setting.UnknownSetting;
import org.rf.ide.core.testdata.text.read.IRobotTokenType;
import org.rf.ide.core.testdata.text.read.ParsingState;
import org.rf.ide.core.testdata.text.read.RobotLine;
import org.rf.ide.core.testdata.text.read.recognizer.RobotToken;
import org.rf.ide.core.testdata.text.read.recognizer.RobotTokenType;


public class UnknownSettingMapper implements IParsingMapper {

    private final ElementPositionResolver positionResolver;
    private final ParsingStateHelper utility;


    public UnknownSettingMapper() {
        this.positionResolver = new ElementPositionResolver();
        this.utility = new ParsingStateHelper();
    }


    @Override
    public RobotToken map(final RobotLine currentLine,
            final Stack<ParsingState> processingState,
            final RobotFileOutput robotFileOutput, final RobotToken rt, final FilePosition fp,
            final String text) {
        final List<IRobotTokenType> types = rt.getTypes();
        types.remove(RobotTokenType.UNKNOWN);
        types.add(0, RobotTokenType.SETTING_UNKNOWN);
        rt.setStartColumn(fp.getColumn());
        rt.setText(text);
        rt.setRaw(text);

        final SettingTable setting = robotFileOutput.getFileModel().getSettingTable();
        final UnknownSetting unknownSetting = new UnknownSetting(rt);
        setting.addUnknownSetting(unknownSetting);

        processingState.push(ParsingState.SETTING_UNKNOWN);

        return rt;
    }


    @Override
    public boolean checkIfCanBeMapped(final RobotFileOutput robotFileOutput,
            final RobotLine currentLine, final RobotToken rt, final String text,
            final Stack<ParsingState> processingState) {
        boolean result = false;
        final ParsingState currentState = utility.getCurrentStatus(processingState);

        if (currentState == ParsingState.SETTING_TABLE_INSIDE) {
            if (text != null) {
                result = positionResolver.isCorrectPosition(
                        PositionExpected.SETTING_TABLE_ELEMENT_DECLARATION,
                        robotFileOutput.getFileModel(), currentLine, rt);
            }
        }

        return result;
    }
}