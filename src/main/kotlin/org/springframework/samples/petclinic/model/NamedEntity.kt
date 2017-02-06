/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model

import javax.persistence.Column
import javax.persistence.MappedSuperclass


/**
 * Simple JavaBean domain object adds a name property to `BaseEntity`. Used as a base class for objects
 * needing these properties.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Putthiphong Boonphong
 */
@MappedSuperclass
open class NamedEntity : BaseEntity() {

    @Column(name = "name")
    var name: String = ""

    override fun toString(): String {
        return this.name
    }

}
