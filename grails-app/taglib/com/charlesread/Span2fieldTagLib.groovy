package com.charlesread

class Span2fieldTagLib {
    def grailsApplication

    static namespace = "sf"

    def resources = {attr,body ->
        out << g.javascript([src: 'jquery-1.10.2.min.js'])
        out << g.javascript([src: 'span2field.js'])
    }

    def textField = {attr, body ->
        out << "<span class='editableSpan editableTextFieldSpan' ${attr.style}' id='${attr.name}_span' onclick='hideSpan(\"${attr.name}\")'>${attr.value}</span>"
        attr.id = "${attr.name}_input"
        attr.onblur = "hideInput('${attr.name}')"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextFieldInput'
        out << g.textField(attr)
    }

    def textArea = {attr, body ->
        out << "<span class='editableSpan editableTextAreaSpan' id='${attr.name}_span' onclick='hideSpan(\"${attr.name}\")'>${attr.value}</span>"
        attr.id = "${attr.name}_input"
        attr.onblur = "hideInput('${attr.name}')"
        attr.style = 'display:none;'
        attr.class = attr.class + ' editableInput editableTextAreaInput'
        out << g.textArea(attr)
    }

    def selectSingle = {attr, body ->
        println attr
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
        def domainInstance = grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.findById(attr.value).toString()
        out << "<span class='editableSpan editableSelectSingleSpan' id='${attr.id}_span' onclick='hideSpan(\"${attr.id}\")'>${domainInstance}</span>"
        attr.onchange = "hideInputSelectSingle(\"${attr.id}\")"
        attr.style = 'display:none;'
        attr.id = "${attr.id}_input"
        attr.class = attr.class + ' editableInput editableSingleSelectInput'
        out << g.select(attr)
    }

    def selectMultiple = {attr, body ->
        println attr
        def firstDomainInstanceClassName = org.hibernate.Hibernate.getClass(attr.from.get(0)).getName()
//        def domainInstance = grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.findById(attr.value).toString()
        out << "<span class='editableSpan editableSelectMultipleSpan' id='${attr.id ?: attr.name}_span' onclick='hideSpan(\"${attr.id ?: attr.name}\")'>"
        out << "<ul>"
        attr.value.each {
            out << "<li>${grailsApplication.getArtefact("Domain",firstDomainInstanceClassName)?.getClazz()?.get(it).toString()}</li>"
        }
        out << "</ul>"
        out << "</span>"
        attr.onblur = "hideInputSelectMultiple(\"${attr.id ?: attr.name}\")"
        attr.style = 'display:none;'
        attr.id = "${attr.id ?: attr.name}_input"
        attr.class = attr.class + ' editableInput editableMultipleSelectInput'
//        out << "<div id='${attr.id ?: attr.name}_wrapper' onmouseout='hideInputSelectMultiple(\"${attr.id ?: attr.name}\")' >"
        out << g.select(attr)
//        out << "</div>"
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
//        out << "<label>"
        out << "<span onclick='changeCheckBoxSpan(\"${attr.id}\",\"${attr.checkedText ?: g.formatBoolean(boolean: true)}\",\"${attr.uncheckedText ?: g.formatBoolean(boolean: false)}\")' class='editableSpan editableCheckBoxSpan' id='${attr.id}_span'>${attr.checkedText && attr.uncheckedText ? (attr.value ? attr.checkedText : attr.uncheckedText) : g.formatBoolean(boolean: attr.value)}</span>"
        attr.class = attr.class + ' editableInput editableCheckBoxInput'
        out << g.checkBox(attr)
//        out << "</label>"
    }

}
