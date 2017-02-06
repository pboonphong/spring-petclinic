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
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.ModelAndView

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 * @author Putthiphong Boonphong
 */
@Controller
internal class OwnerController
@Autowired
constructor(private val owners: OwnerRepository) {

    @InitBinder
    fun setAllowedFields(dataBinder: WebDataBinder) {
        dataBinder.setDisallowedFields("id")
    }

    @RequestMapping(value = "/owners/new", method = arrayOf(RequestMethod.GET))
    fun initCreationForm(model: MutableMap<String, Any>): String {
        val owner = Owner()
        model.put("owner", owner)
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @RequestMapping(value = "/owners/new", method = arrayOf(RequestMethod.POST))
    fun processCreationForm(@Valid owner: Owner, result: BindingResult): String {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            this.owners.save(owner)
            return "redirect:/owners/" + owner.id
        }
    }

    @RequestMapping(value = "/owners/find", method = arrayOf(RequestMethod.GET))
    fun initFindForm(model: MutableMap<String, Any>): String {
        model.put("owner", Owner())
        return "owners/findOwners"
    }

    @RequestMapping(value = "/owners", method = arrayOf(RequestMethod.GET))
    fun processFindForm(owner: Owner, result: BindingResult, model: MutableMap<String, Any>): String {
        var owner = owner

        // allow parameterless GET request for /owners to return all records
        if (owner.lastName == null) {
            owner.lastName = "" // empty string signifies broadest possible search
        }

        // find owners by last name
        val results = this.owners.findByLastName(owner.lastName)
        if (results.isEmpty()) {
            // no owners found
            result.rejectValue("lastName", "notFound", "not found")
            return "owners/findOwners"
        } else if (results.size == 1) {
            // 1 owner found
            owner = results.iterator().next()
            return "redirect:/owners/" + owner.id!!
        } else {
            // multiple owners found
            model.put("selections", results)
            return "owners/ownersList"
        }
    }

    @RequestMapping(value = "/owners/{ownerId}/edit", method = arrayOf(RequestMethod.GET))
    fun initUpdateOwnerForm(@PathVariable("ownerId") ownerId: Int, model: Model): String {
        val owner = this.owners.findById(ownerId)
        model.addAttribute(owner)
        return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
    }

    @RequestMapping(value = "/owners/{ownerId}/edit", method = arrayOf(RequestMethod.POST))
    fun processUpdateOwnerForm(@Valid owner: Owner, result: BindingResult, @PathVariable("ownerId") ownerId: Int): String {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_OR_UPDATE_FORM
        } else {
            owner.id = ownerId
            this.owners.save(owner)
            return "redirect:/owners/{ownerId}"
        }
    }

    /**
     * Custom handler for displaying an owner.

     * @param ownerId the ID of the owner to display
     * *
     * @return a ModelMap with the model attributes for the view
     */
    @RequestMapping("/owners/{ownerId}")
    fun showOwner(@PathVariable("ownerId") ownerId: Int): ModelAndView {
        val mav = ModelAndView("owners/ownerDetails")
        mav.addObject(this.owners.findById(ownerId))
        return mav
    }

    companion object {

        private val VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm"
    }

}
