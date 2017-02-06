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

import javax.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Putthiphong Boonphong
 */
@Controller
@RequestMapping("/owners/{ownerId}")
internal class PetController
@Autowired
constructor(private val pets: PetRepository, private val owners: OwnerRepository) {

    @ModelAttribute("types")
    fun populatePetTypes(): Collection<PetType> {
        return this.pets.findPetTypes()
    }

    @ModelAttribute("owner")
    fun findOwner(@PathVariable("ownerId") ownerId: Int): Owner {
        return this.owners.findById(ownerId)
    }

    @InitBinder("owner")
    fun initOwnerBinder(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @InitBinder("pet")
    fun initPetBinder(dataBinder: WebDataBinder) {
        dataBinder.validator = PetValidator()
    }

    @RequestMapping(value = "/pets/new", method = arrayOf(RequestMethod.GET))
    fun initCreationForm(owner: Owner, model: ModelMap): String {
        val pet = Pet()
        owner.addPet(pet)
        model.put("pet", pet)
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @RequestMapping(value = "/pets/new", method = arrayOf(RequestMethod.POST))
    fun processCreationForm(owner: Owner, @Valid pet: Pet, result: BindingResult, model: ModelMap): String {
        if (StringUtils.hasLength(pet.name) && pet.isNew() && owner.getPet(pet.name, true) != null) {
            result.rejectValue("name", "duplicate", "already exists")
        }
        if (result.hasErrors()) {
            model.put("pet", pet)
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            owner.addPet(pet)
            this.pets.save(pet)
            return "redirect:/owners/{ownerId}"
        }
    }

    @RequestMapping(value = "/pets/{petId}/edit", method = arrayOf(RequestMethod.GET))
    fun initUpdateForm(@PathVariable("petId") petId: Int, model: ModelMap): String {
        val pet = this.pets.findById(petId)
        model.put("pet", pet)
        return VIEWS_PETS_CREATE_OR_UPDATE_FORM
    }

    @RequestMapping(value = "/pets/{petId}/edit", method = arrayOf(RequestMethod.POST))
    fun processUpdateForm(@Valid pet: Pet, result: BindingResult, owner: Owner, model: ModelMap): String {
        if (result.hasErrors()) {
            pet.owner = owner
            model.put("pet", pet)
            return VIEWS_PETS_CREATE_OR_UPDATE_FORM
        } else {
            owner.addPet(pet)
            this.pets.save(pet)
            return "redirect:/owners/{ownerId}"
        }
    }

    companion object {

        private val VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm"
    }

}
