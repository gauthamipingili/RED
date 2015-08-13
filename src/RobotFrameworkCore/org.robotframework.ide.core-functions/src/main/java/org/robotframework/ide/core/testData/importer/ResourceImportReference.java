package org.robotframework.ide.core.testData.importer;

import org.robotframework.ide.core.testData.model.RobotFileOutput;
import org.robotframework.ide.core.testData.model.table.setting.ResourceImport;


public class ResourceImportReference {

    private ResourceImport importDeclaration;
    private RobotFileOutput reference;


    public ResourceImportReference(final ResourceImport importDeclaration,
            final RobotFileOutput reference) {
        this.importDeclaration = importDeclaration;
        this.reference = reference;
    }


    public ResourceImport getImportDeclaration() {
        return importDeclaration;
    }


    public RobotFileOutput getReference() {
        return reference;
    }
}