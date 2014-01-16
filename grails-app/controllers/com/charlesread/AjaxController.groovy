package com.charlesread

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
        def clazz = grailsApplication.getArtefact("Domain",params.clazz)?.getClazz()?.findById(params.id)

        //def instance = Demo.get(params.id)
        clazz[params.field] = params.value
        if (clazz.save(flush: true)) {
            println "worked!"
            //response.setStatus(200)
        } else {
            //response.setStatus(500)
        }
    }
}
