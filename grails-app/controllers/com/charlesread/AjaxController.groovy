package com.charlesread

import grails.converters.JSON

class AjaxController {

    static allowedMethods = [update: ['POST']]

    def index() {
        render "INDEX!"
    }

    def update() {
        println "updateData called..."
        println "type - > ${params.type}"
        println "value - > ${params.value}"
        println "field - > ${params.field}"
        println "id - > ${params.id}"
        println "clazz -> ${params.clazz}"
        
        def error

        try {

            def domainInstanceClassName = org.hibernate.Hibernate.getClass(params.clazz).getName()
            def domainInstance = grailsApplication.getArtefact("Domain",params.clazz)?.getClazz()?.findById(params.id)

            switch(params.type) {
                case ['textField','textArea']:
                    println "tF or tA found..."
                    domainInstance[params.field] = params.value    
                    break
                case 'selectSingle':
                    println "selectClazz -> ${params.selectClazz}"
                    domainInstance[params.field.tokenize('.')[0]] = grailsApplication.getArtefact("Domain",params.selectClazz)?.getClazz()?.findById(params.value.toLong()) 
                    println "select found...  ${params.field.tokenize('.').getAt(0)}"
                    break
                case 'selectMultiple':
                    def selections = params.value.tokenize(',')
                    domainInstance[params.field].clear()
                    selections.eachWithIndex {v,i ->
                        println "${i} --> ${v}"
                        domainInstance[params.field].add(grailsApplication.getArtefact("Domain",params.selectClazz)?.getClazz()?.findById(v.toLong()))
                    }
                    break
                case 'checkBox':
                    domainInstance[params.field] = !domainInstance[params.field]
                    break
                default:
                    return 
            }
            
            if (domainInstance.save(flush: true)) {
                response.setStatus(200)
            } else {
                response.setStatus(500)
            }

        } catch(e) {
            error = e
            response.setStatus(400)
            render ([status: response.status, error: e])
        }

        render ([status: response.status, classUpdated: params.clazz, fieldUpdated: params.field, error: error ?: 'no error'] as JSON)

    }
}
