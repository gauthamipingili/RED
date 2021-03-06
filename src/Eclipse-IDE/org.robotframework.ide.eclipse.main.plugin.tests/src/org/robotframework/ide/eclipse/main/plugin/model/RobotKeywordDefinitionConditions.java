package org.robotframework.ide.eclipse.main.plugin.model;

import org.assertj.core.api.Condition;

public class RobotKeywordDefinitionConditions {

    public static Condition<RobotKeywordDefinition> properlySetParent() {
        return new Condition<RobotKeywordDefinition>() {

            @Override
            public boolean matches(final RobotKeywordDefinition keyword) {
                return keyword.getParent() != null && keyword.getParent().getChildren().contains(keyword)
                        && keyword.getLinkedElement().getParent() != null
                        && keyword.getLinkedElement().getParent().getKeywords().contains(keyword.getLinkedElement())
                        && keyword.getParent().getLinkedElement() == keyword.getLinkedElement().getParent();
            }
        };
    }
}
