package com.charlesread

import grails.converters.JSON

class AjaxController {

    static allowedMethods = [update: ['POST']]

    def index() {
        render "INDEX!"
    }

    def update() {
        
        def error

        try {

            def domainInstanceClassName = org.hibernate.Hibernate.getClass(params.clazz).getName()
            def domainInstance = grailsApplication.getArtefact("Domain",params.clazz)?.getClazz()?.findById(params.id)

            switch(params.type) {
                case ['textField','textArea']:
                    domainInstance[params.field] = params.value    
                    break
                case 'selectSingle':
                    domainInstance[params.field.tokenize('.')[0]] = grailsApplication.getArtefact("Domain",params.selectClazz)?.getClazz()?.findById(params.value.toLong()) 
                    break
                case 'selectMultiple':
                    def selections = params.value.tokenize(',')
                    domainInstance[params.field].clear()
                    selections.eachWithIndex {v,i ->
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
