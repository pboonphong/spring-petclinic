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

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.Digits

import org.hibernate.validator.constraints.NotEmpty
import org.springframework.beans.support.MutableSortDefinition
import org.springframework.beans.support.PropertyComparator
import org.springframework.core.style.ToStringCreator
import org.springframework.samples.petclinic.model.Person

/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Putthiphong Boonphong
 */
@Entity
@Table(name = "owners")
open class Owner : Person() {

    @Column(name = "address")
    @NotEmpty
    var address: String? = null

    @Column(name = "city")
    @NotEmpty
    var city: String? = null

    @Column(name = "telephone")
    @NotEmpty
    @Digits(fraction = 0, integer = 10)
    var telephone: String? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL), mappedBy = "owner")
    private var pets: MutableSet<Pet>? = null

    fun getPets(): List<Pet> {
        val sortedPets = ArrayList(this.getPetsInternal())
        PropertyComparator.sort(sortedPets, MutableSortDefinition("name", true, true))
        return Collections.unmodifiableList(sortedPets)
    }

    fun getPetsInternal(): MutableSet<Pet> {
        if (pets == null) {
            this.pets = mutableSetOf()
        }
        return this.pets as MutableSet<Pet>
    }



    fun addPet(pet: Pet) {
        if (pet.isNew()) {
            getPetsInternal().add(pet)
        }
        pet.owner = this
    }

    /**
     * Return the Pet with the given name, or null if none found for this Owner.
     *
     * @param name to test
     * @return true if pet name is already in use
     */
    @JvmOverloads fun getPet(name: String, ignoreNew: Boolean = false): Pet? {
        return getPetsInternal().firstOrNull { (!ignoreNew || !it.isNew()) && it.name.equals(name, ignoreCase = true) }
    }

    override fun toString(): String {
        return ToStringCreator(this)
                .append("id", this.id)
                .append("new", this.isNew())
                .append("lastName", this.lastName)
                .append("firstName", this.firstName)
                .append("address", this.address)
                .append("city", this.city)
                .append("telephone", this.telephone)
                .toString()
    }
}
