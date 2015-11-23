/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.rf.ide.core.testData.robotImported;

import java.util.Map;

import org.rf.ide.core.testData.model.table.variables.AVariable.VariableType;


public class DictionaryRobotInternalVariable extends
        ARobotInternalVariable<Map<String, ?>> {

    public DictionaryRobotInternalVariable(final String name, final Map<String, ?> value) {
        super(name, value, VariableType.DICTIONARY);
    }
}