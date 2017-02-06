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

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import org.springframework.transaction.annotation.Transactional

/**
 * Repository class for `Pet` domain objects All method names are compliant with Spring Data naming
 * conventions so this interface can easily be extended for Spring Data See here: http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Putthiphong Boonphong
 */
interface PetRepository : Repository<Pet, Int> {

    /**
     * Retrieve all [PetType]s from the data store.
     * @return a Collection of [PetType]s.
     */
    @Query("SELECT ptype FROM PetType ptype ORDER BY ptype.name")
    @Transactional(readOnly = true)
    fun findPetTypes(): List<PetType>

    /**
     * Retrieve a [Pet] from the data store by id.
     * @param id the id to search for
     * *
     * @return the [Pet] if found
     */
    @Transactional(readOnly = true)
    fun findById(id: Int?): Pet

    /**
     * Save a [Pet] to the data store, either inserting or updating it.
     * @param pet the [Pet] to save
     */
    fun save(pet: Pet)

}

