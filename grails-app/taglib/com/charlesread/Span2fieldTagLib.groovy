package com.charlesread

import org.apache.commons.lang.StringEscapeUtils

class Span2fieldTagLib {

    def grailsApplication

    static namespace = "sf"

    private String getNoData() {
        return grailsApplication.config.span2field.noData ?: '...'
    }

    private String onSuccessDefault(String e) {
    	if (grailsApplication.config.span2field.onSuccess.defaultAction == false || !grailsApplication.config.span2field.onSuccess.defaultAction) {
    		return	
    	} else {
    		return "callback(\$('#${e}_span'),'${grailsApplication.config.span2field.onSuccess.color ?: 'green'}')"
    	}
    }

    private String onFailureDefault(String e) {
    	if (grailsApplication.config.span2field.onFailure.defaultAction == false || !grailsApplication.config.span2field.onFailure.defaultAction) {
    		return	
    	} else {
    		return "callback(\$('#${e}_span'),'${grailsApplication.config.span2field.onFailure.color ?: 'red'}')"
    	}
    }

    private String genToken() {
        String str = new Date().getTime().toString() + new Random().nextInt(9999999).toString()
        return str.encodeAsMD5()[0..5]
    }

    def resources = {attr,body ->
        out << g.javascript([src: 'jquery-1.10.2.min.js'])
        out << g.javascript([src: 'jquery-ui.min.js'])
        out << g.javascript([src: 'span2field.js'])
    }

    def defaultCallback = {attr, body ->
        out << g.javascript([src: 'defaultCallback.js'])
        out << "<link rel=\"stylesheet\" href=\"${resource(dir: 'css', file: 'defaultCallback.css')}\" type=\"text/css\">"
        out << "<link href=\"//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css\" rel=\"stylesheet\">"
    }

    def textField = {attr, body ->
        def token = genToken()
        out << "<span class='editableSpan editableTextFieldSpan' id='${attr.name}_${token}_span' onclick='hideSpan(\"${attr.name}_${token}\")'>${attr.value ?: noData}</span>"
        attr.id = "${attr.name}_${token}_input"
        attr.onblur = "hideInput('${attr.name}_${token}');"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextFieldInput'
        attr.onSuccess = attr?.onSuccess ?: onSuccessDefault("${attr.name}_${token}")
        attr.onFailure = attr?.onFailure ?: onFailureDefault("${attr.name}_${token}")
        if (attr.ajax == "true" && attr.domainInstance) {
            def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onblur = "${attr.onblur} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${domainInstanceClassName}&type=textField'" )

        }
        out << g.textField(attr)
    }

    def textArea = {attr, body ->
        def token = genToken()
        out << "<span class='editableSpan editableTextAreaSpan' id='${attr.name}_${token}_span' onclick='hideSpan(\"${attr.name}_${token}\")'>${attr.value ?: noData}</span>"
        attr.id = "${attr.name}_${token}_input"
        attr.onblur = "hideInput('${attr.name}_${token}');"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextAreaInput'
        attr.onSuccess = attr?.onSuccess ?: onSuccessDefault("${attr.name}_${token}")
        attr.onFailure = attr?.onFailure ?: onFailureDefault("${attr.name}_${token}")
        if (attr.ajax == "true" && attr.domainInstance) {
            def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onblur = "${attr.onblur} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${domainInstanceClassName}&type=textArea'" )

        }
        out << g.textArea(attr)
    }

    def selectSingle = {attr, body ->
        def token = genToken()
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
        def domainInstance = grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.findById(attr.value).toString()
        out << "<span class='editableSpan editableSelectSingleSpan' id='${attr.id}_${token}_span' onclick='hideSpan(\"${attr.id}_${token}\")'>${domainInstance ?: noData}</span>"
        attr.onchange = "hideInputSelectSingle(\"${attr.id}_${token}\");"
        attr.style = 'display:none;'
        attr.id = "${attr.id}_${token}_input"
        attr.class = attr.class + ' editableInput editableSingleSelectInput'
        attr.onSuccess = attr?.onSuccess ?: onSuccessDefault(attr.name.tokenize('.')[0] + "_${token}")
        attr.onFailure = attr?.onFailure ?: onFailureDefault(attr.name.tokenize('.')[0] + "_${token}")
        if (attr.ajax == "true" && attr.domainInstance) {
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onchange = "${attr.onchange} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${attr.domainInstance.getClass().getName()}&type=selectSingle&selectClazz=" + "${firstDomainInstanceClassName}'" )

        }
        out << g.select(attr)
    }

    def selectMultiple = {attr, body ->
        def token = genToken()
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
        out << "<span class='editableSpan editableSelectMultipleSpan' id='${attr.id ?: attr.name}_${token}_span' onclick='hideSpan(\"${attr.id ?: attr.name}_${token}\")'>"
        if (!attr.value) {
        	out << noData
        } else {
        	out << "<ul>"
        	attr.value.each {
            	out << "<li>${grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.get(it).toString()}</li>"
        	}
        	out << "</ul>"
        }
        out << "</span>"
        attr.onblur = "hideInputSelectMultiple(\"${attr.id ?: attr.name}_${token}\");"
        attr.style = 'display:none;'
        attr.id = "${attr.id ?: attr.name}_${token}_input"
        attr.class = attr.class + ' editableInput editableMultipleSelectInput'
        attr.onSuccess = attr?.onSuccess ?: onSuccessDefault(attr.name.tokenize('.')[0] + "_${token}")
        attr.onFailure = attr?.onFailure ?: onFailureDefault(attr.name.tokenize('.')[0] + "_${token}")
        if (attr.ajax == "true" && attr.domainInstance) {
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onblur = "${attr.onblur} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + $(this).val().join(",") + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${attr.domainInstance.getClass().getName()}&type=selectMultiple&selectClazz=" + "${firstDomainInstanceClassName}'" )

        }
        out << g.select(attr)
    }

    def select = {attr,body ->
        if (attr.multiple) {
            out << selectMultiple(attr,body)
        } else {
            out << selectSingle(attr,body)
        }
    }

    def checkBox = {attr,body ->
        def token = genToken()
        attr.id = (attr.id ?: attr.name) + "_${token}_input"
        attr.style = 'display:none;'
        attr.class = attr.class + " editableInput editableCheckBoxInput"
        attr.onSuccess = attr?.onSuccess ?: onSuccessDefault("${attr.name}_${token}")
        attr.onFailure = attr?.onFailure ?: onFailureDefault("${attr.name}_${token}")
        def onclickString = "changeCheckBoxSpan('${attr.name}_${token}','${StringEscapeUtils.escapeJavaScript(attr.checkedText) ?: StringEscapeUtils.escapeJavaScript(g.formatBoolean(boolean: true))}','${StringEscapeUtils.escapeJavaScript(attr.uncheckedText) ?: StringEscapeUtils.escapeJavaScript(g.formatBoolean(boolean: false))}');"
        if (attr.ajax == "true" && attr.domainInstance) {
        	def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            onclickString = "${onclickString}" + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: "'field=${attr.name}&clazz=${domainInstanceClassName}&type=checkBox&id=${attr.domainInstance.id}'" )
        }
        out << """
        	<span onclick="${onclickString}" class='editableSpan editableCheckBoxSpan ${attr.value ? 'editableCheckBoxChecked' : 'editableCheckBoxUnchecked'}' id='${attr.name}_${token}_span'>
        		${attr.checkedText && attr.uncheckedText ? (attr.value ? attr.checkedText : attr.uncheckedText) : g.formatBoolean(boolean: attr.value)}
        	</span>
        	"""
        out << g.checkBox(attr)
    }

}
