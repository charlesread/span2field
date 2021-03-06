class Span2fieldGrailsPlugin {
    // the plugin version
    def version = "0.1.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Span2field Plugin" // Headline display name of the plugin
    def author = "Charles Read"
    def authorEmail = "charles@charlesread.com"
    def description = '''\
An easy way to make Grails views act like forms, allows you to click on a text element and edit the value in place, without navigating to an 'edit' page.
'''

    // URL to the plugin's documentation
    //def documentation = "http://grails.org/plugin/span2field"
    def documentation = "https://github.com/charlesread/span2field"
    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
    def developers = [ [ name: "Charles Read", email: "charles@charlesread.com" ]]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "github", url: "https://github.com/charlesread/span2field/issues" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "https://github.com/charlesread/span2field" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
