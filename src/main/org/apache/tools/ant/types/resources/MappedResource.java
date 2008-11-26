/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.tools.ant.types.resources;

import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.util.FileNameMapper;

/**
 * A decorator around a different resource that uses a mapper to
 * dynamically remap the resource's name.
 *
 * <p>Strips the FileProvider interface from decorated resources since
 * it may be used to circumvent name mapping.</p>
 *
 * @since Ant 1.8.0
 */
public class MappedResource extends ResourceDecorator {
    private final FileNameMapper mapper;

    /**
     * Wraps an existing resource.
     */
    public MappedResource(Resource r, FileNameMapper m) {
        super(r);
        mapper = m;
    }

    /**
     * Maps the name.
     */
    public String getName() {
        String name = getResource().getName();
        if (isReference()) {
            return name;
        } else {
            String[] mapped = mapper.mapFileName(name);
            return mapped != null && mapped.length > 0 ? mapped[0] : null;
        }
    }

    /**
     * Not really supported since mapper is never null.
     */
    public void setRefid(Reference r) {
        if (mapper != null) {
            throw noChildrenAllowed();
        }
        super.setRefid(r);
    }

    /**
     * Suppress FileProvider
     */
    public Object as(Class clazz) {
        return FileProvider.class.isAssignableFrom(clazz) 
            ? null : getResource().as(clazz);
    }

}