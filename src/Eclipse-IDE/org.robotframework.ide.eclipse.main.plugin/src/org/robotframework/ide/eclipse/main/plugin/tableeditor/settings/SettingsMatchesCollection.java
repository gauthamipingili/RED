/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see licence.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.tableeditor.settings;

import org.robotframework.ide.eclipse.main.plugin.model.RobotElement;
import org.robotframework.ide.eclipse.main.plugin.model.RobotKeywordCall;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.ISectionFormFragment.MatchesCollection;

public class SettingsMatchesCollection extends MatchesCollection {

    @Override
    public void collect(final RobotElement element, final String filter) {
        if (element instanceof RobotKeywordCall) {
            collectMatches((RobotKeywordCall) element, filter);
        }
    }

    private void collectMatches(final RobotKeywordCall setting, final String filter) {
        boolean isMatching = false; 

        isMatching |= collectMatches(filter, setting.getName());
        for (final String argument : setting.getArguments()) {
            isMatching |= collectMatches(filter, argument);
        }
        isMatching |= collectMatches(filter, setting.getComment());
        if (isMatching) {
            rowsMatching++;
        }
    }
}
