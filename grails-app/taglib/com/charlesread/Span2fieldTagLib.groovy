package com.charlesread

import org.apache.commons.lang.StringEscapeUtils

class Span2fieldTagLib {
    def grailsApplication

    static namespace = "sf"

    def noData = '...'

    def resources = {attr,body ->
        out << g.javascript([src: 'jquery-1.10.2.min.js'])
        out << g.javascript([src: 'span2field.js'])
    }

    def textField = {attr, body ->
        out << "<span class='editableSpan editableTextFieldSpan' id='${attr.name}_span' onclick='hideSpan(\"${attr.name}\")'>${attr.value ?: noData}</span>"
        attr.id = "${attr.name}_input"
        attr.onblur = "hideInput('${attr.name}');"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextFieldInput'
        if (attr.ajax == "true" && attr.domainInstance) {
            def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onblur = "${attr.onblur} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${domainInstanceClassName}&type=textField'" )

        }
        out << g.textField(attr)
    }

    def textArea = {attr, body ->
        out << "<span class='editableSpan editableTextAreaSpan' id='${attr.name}_span' onclick='hideSpan(\"${attr.name}\")'>${attr.value ?: noData}</span>"
        attr.id = "${attr.name}_input"
        attr.onblur = "hideInput('${attr.name}');"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextAreaInput'
        if (attr.ajax == "true" && attr.domainInstance) {
            def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onblur = "${attr.onblur} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${domainInstanceClassName}&type=textArea'" )

        }
        out << g.textArea(attr)
    }

    def selectSingle = {attr, body ->
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
        def domainInstance = grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.findById(attr.value).toString()
        out << "<span class='editableSpan editableSelectSingleSpan' id='${attr.id}_span' onclick='hideSpan(\"${attr.id}\")'>${domainInstance ?: noData}</span>"
        attr.onchange = "hideInputSelectSingle(\"${attr.id}\");"
        attr.style = 'display:none;'
        attr.id = "${attr.id}_input"
        attr.class = attr.class + ' editableInput editableSingleSelectInput'
        if (attr.ajax == "true" && attr.domainInstance) {
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            attr.onchange = "${attr.onchange} " + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: '\'value=\' + this.value + \'&field=' + "${attr.name}&id=" + "${attr.domainInstance.id}&clazz=" + "${attr.domainInstance.getClass().getName()}&type=selectSingle&selectClazz=" + "${firstDomainInstanceClassName}'" )

        }
        out << g.select(attr)
    }

    def selectMultiple = {attr, body ->
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
        out << "<span class='editableSpan editableSelectMultipleSpan' id='${attr.id ?: attr.name}_span' onclick='hideSpan(\"${attr.id ?: attr.name}\")'>"
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
        attr.onblur = "hideInputSelectMultiple(\"${attr.id ?: attr.name}\");"
        attr.style = 'display:none;'
        attr.id = "${attr.id ?: attr.name}_input"
        attr.class = attr.class + ' editableInput editableMultipleSelectInput'
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
        attr.id = attr.id ?: attr.name
        attr.style = 'display:none;'
        attr.class = attr.class + " editableInput editableCheckBoxInput"
        def onclickString = "changeCheckBoxSpan('${attr.id}','${StringEscapeUtils.escapeJavaScript(attr.checkedText) ?: StringEscapeUtils.escapeJavaScript(g.formatBoolean(boolean: true))}','${StringEscapeUtils.escapeJavaScript(attr.uncheckedText) ?: StringEscapeUtils.escapeJavaScript(g.formatBoolean(boolean: false))}');"
        if (attr.ajax == "true" && attr.domainInstance) {
        	def domainInstanceClassName = org.hibernate.Hibernate.getClass(attr.domainInstance).getName()
            def controller = attr.controller ?: 'ajax'
            def action = attr.action ?: 'update'
            onclickString = "${onclickString}" + g.remoteFunction(onSuccess: attr?.onSuccess, onFailure: attr?.onFailure, method: 'POST', action: action, controller: controller, params: "'field=${attr.name}&clazz=${domainInstanceClassName}&type=checkBox&id=${attr.domainInstance.id}'" )
        }
        out << """
        	<span onclick="${onclickString}" class='editableSpan editableCheckBoxSpan ${attr.value ? 'editableCheckBoxChecked' : 'editableCheckBoxUnchecked'}' id='${attr.id}_span'>
        		${attr.checkedText && attr.uncheckedText ? (attr.value ? attr.checkedText : attr.uncheckedText) : g.formatBoolean(boolean: attr.value)}
        	</span>
        	"""
        out << g.checkBox(attr)
    }

}
