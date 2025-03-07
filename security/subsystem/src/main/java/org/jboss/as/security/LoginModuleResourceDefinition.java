/*
 *
 *  JBoss, Home of Professional Open Source.
 *  Copyright 2013, Red Hat, Inc., and individual contributors
 *  as indicated by the @author tags. See the copyright.txt file in the
 *  distribution for a full listing of individual contributors.
 *
 *  This is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Lesser General Public License as
 *  published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This software is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this software; if not, write to the Free
 *  Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 *  02110-1301 USA, or see the FSF site: http://www.fsf.org.
 * /
 */

package org.jboss.as.security;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.ModelOnlyAddStepHandler;
import org.jboss.as.controller.ModelOnlyRemoveStepHandler;
import org.jboss.as.controller.ModelOnlyWriteAttributeHandler;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.PropertiesAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.validation.EnumValidator;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelType;

/**
 * @author <a href="mailto:tomaz.cerar@redhat.com">Tomaz Cerar</a> (c) 2012 Red Hat Inc.
 */
public class LoginModuleResourceDefinition extends SimpleResourceDefinition {
    static final SimpleAttributeDefinition CODE = new SimpleAttributeDefinitionBuilder(Constants.CODE, ModelType.STRING)
            .setRequired(true)
            .setMinSize(1)
            .build();

    static final SimpleAttributeDefinition FLAG = new SimpleAttributeDefinitionBuilder(Constants.FLAG, ModelType.STRING)
            .setRequired(true)
            .setAllowExpression(true)
            .setValidator(new EnumValidator<ModuleFlag>(ModuleFlag.class, false, true))
            .build();

    static final SimpleAttributeDefinition MODULE = new SimpleAttributeDefinitionBuilder(Constants.MODULE, ModelType.STRING)
            .setRequired(false)
            .setAllowExpression(false)
            .setMinSize(1)
            .build();
    static final PropertiesAttributeDefinition MODULE_OPTIONS = new PropertiesAttributeDefinition.Builder(Constants.MODULE_OPTIONS, true)
            .setAllowExpression(true)
            .build();

    static final AttributeDefinition[] ATTRIBUTES = {CODE, FLAG, MODULE, MODULE_OPTIONS};

    LoginModuleResourceDefinition(final String key) {
        super(PathElement.pathElement(key),
                SecurityExtension.getResourceDescriptionResolver(Constants.LOGIN_MODULE_STACK, Constants.LOGIN_MODULES),
                new ModelOnlyAddStepHandler(ATTRIBUTES),
                ModelOnlyRemoveStepHandler.INSTANCE
        );
    }

    @Override
    public void registerAttributes(ManagementResourceRegistration resourceRegistration) {
        super.registerAttributes(resourceRegistration);
        OperationStepHandler writeHandler = new ModelOnlyWriteAttributeHandler(ATTRIBUTES);
        for (AttributeDefinition attribute : ATTRIBUTES) {
            resourceRegistration.registerReadWriteAttribute(attribute, null, writeHandler);
        }
    }
}
