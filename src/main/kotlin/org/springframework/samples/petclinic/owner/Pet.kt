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
package org.springframework.samples.petclinic.owner

import java.util.ArrayList
import java.util.Collections
import java.util.Date
import java.util.HashSet
import java.util.LinkedHashSet

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PropertyComparator
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.samples.petclinic.model.NamedEntity
import org.springframework.samples.petclinic.visit.Visit

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Putthiphong Boonphong
 */
@Entity
@Table(name = "pets")
open class Pet : NamedEntity() {

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    var birthDate: Date? = null

    @ManyToOne
    @JoinColumn(name = "type_id")
    var type: PetType? = null

    @ManyToOne
    @JoinColumn(name = "owner_id")
    var owner: Owner? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL), mappedBy = "petId", fetch = FetchType.EAGER)
    protected var visitsInternal: MutableSet<Visit>? = LinkedHashSet()
        get() {
            if (field == null) {
                this.visitsInternal = HashSet<Visit>()
            }
            return field
        }

    val visits: List<Visit>
        get() {
            val sortedVisits = ArrayList(visitsInternal)
            PropertyComparator.sort(sortedVisits, MutableSortDefinition("date", false, false))
            return Collections.unmodifiableList(sortedVisits)
        }

    fun addVisit(visit: Visit) {
        visitsInternal!!.add(visit)
        visit.petId = this.id
    }

}
