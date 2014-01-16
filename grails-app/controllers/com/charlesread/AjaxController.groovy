package com.charlesread

import grails.converters.JSON

class AjaxController {

    static allowedMethods = [update: ['POST']]

    def index() {
        render "INDEX!"
    }

    def update() {
        println "updateData called..."
        println "value - > ${params.value}"
        println "field - > ${params.field}"
        println "id - > ${params.id}"
        println "clazz -> ${params.clazz}"
        def domainInstanceClassName = org.hibernate.Hibernate.getClass(params.clazz).getName()
        def domainInstance = grailsApplication.getArtefact("Domain",params.clazz)?.getClazz()?.findById(params.id)

        //def instance = Demo.get(params.id)
        domainInstance[params.field] = params.value
        if (domainInstance.save(flush: true)) {
            println "worked!"
            response.setStatus(200)
        } else {
            response.setStatus(500)
        }
        render ""

    }
}
