/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.core.testData.model.table;

import java.util.Comparator;
import java.util.Map;

import org.robotframework.ide.core.testData.model.AModelElement;
import org.robotframework.ide.core.testData.model.FilePosition;
import org.robotframework.ide.core.testData.model.ModelType;


public abstract class AModelTypeComparator<T extends AModelElement<?>>
        implements Comparator<T> {

    private final Map<ModelType, Integer> position;


    public AModelTypeComparator(final Map<ModelType, Integer> position) {
        this.position = position;
    }


    @Override
    public int compare(T modelElement1, T modelElement2) {
        int result = ECompareResult.EQUAL.getValue();
        FilePosition filePos1 = modelElement1.getBeginPosition();
        FilePosition filePos2 = modelElement2.getBeginPosition();

        if (filePos1.isNotSet() && filePos1.isNotSet()) {
            result = Integer.compare(
                    position.get(modelElement1.getModelType()),
                    position.get(modelElement2.getModelType()));
        } else if (filePos1.isNotSet()) {
            result = ECompareResult.LESS.getValue();
        } else if (filePos2.isNotSet()) {
            result = ECompareResult.GREATER.getValue();
        } else {
            result = filePos1.compare(filePos2);
        }

        return result;
    }
}