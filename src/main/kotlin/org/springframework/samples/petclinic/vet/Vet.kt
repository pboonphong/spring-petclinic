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
package org.springframework.samples.petclinic.vet

import java.util.ArrayList
import java.util.Collections
import java.util.HashSet

import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.xml.bind.annotation.XmlElement

import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PropertyComparator
import org.springframework.samples.petclinic.model.Person

/**
 * Simple JavaBean domain object representing a veterinarian.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Arjen Poutsma
 * @author Putthiphong Boonphong
 */
@Entity
@Table(name = "vets")
open class Vet : Person() {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vet_specialties", joinColumns = arrayOf(JoinColumn(name = "vet_id")),
            inverseJoinColumns = arrayOf(JoinColumn(name = "specialty_id")))
    protected val specialtiesInternal: MutableSet<Specialty> = HashSet()

    val specialties: List<Specialty>
        @XmlElement
        get() {
            val sortedSpecs = ArrayList(specialtiesInternal)
            PropertyComparator.sort(sortedSpecs, MutableSortDefinition("name", true, true))
            return Collections.unmodifiableList(sortedSpecs)
        }

    val nrOfSpecialties: Int
        get() = specialtiesInternal.size

    fun addSpecialty(specialty: Specialty) {
        specialtiesInternal.add(specialty)
    }

}
