span2field
===============
A simple Grails plugin providing a tablig that allows you to make `span`s editable.  It is very easy to implement, as easy as any other of the delivered Grails input taglib tags (like `g:textField` and `g:select`).  

The plugin's taglib tags create `span`s that display data, when the `span` is clicked the `span` is hidden and an `input` element appears, allowing you to edit the data, when you click out of the `input` element (or select an entry, in the case of the `select` input element) the `input` element is hidden and the `span` reappears, containing the new value that was just input.  

The plugin effectively combines the *show* and *edit* controller actions that `grails generate-controller` might produce, the result is a single view that both displays data **and** allows you to edit it inline, without having to navigate to another page.

## Usage
You first need to include the plugin resources (just some Javascript) in your view with the `<sf:resources>` tag
```grails
<head>
...
<sf:resources>
...
</head>
```
If you were to use Grails to generate controllers and views it would make a template for the form (`_form.gsp`) and views for editing and showing entries (`edit.gsp` and `show.gap`, respectively).  The form template (`_form.gsp`) would contain taglib tags like `g:textfield` for inputing data, while the view for showing the data (`show.gsp`) would contain taglib tags for displaying data (like `g:fieldValue`).  The idea is that we want to replace these tags that only display data with plugin tags that show the data **and** allow you to edit inline and add a `form` that will submit the data on the page to a controller action that will save the data (like the *update* action that might be created with `grails create-controller`).

Let's say you have used Grails to create the controller and views for a class called `User`, your `_form.gsp` might look like this:

```grails
...
<g:textField name="firstName" value="${userInstance?.firstName}"/>
...
<g:textField name="lastName" value="${userInstance?.lastName}"/>
...
```

Your `show.gsp` might look like this:
```grails
...
<g:fieldValue bean="${userInstance}" field="firstName"/>
...
<g:fieldValue bean="${userInstance}" field="lastName"/>
...
```
And your `edit.gsp` might look something like this:
```grails
<g:form method="post">
    <g:hiddenField name="id" value="${userInstance?.id}" />
    <g:hiddenField name="version" value="${userInstance?.version}" />
    <g:render template="form"/>
    <fieldset class="buttons">
        <g:actionSubmit class="save" action="update" ... />
        <g:actionSubmit class="delete" action="delete" ... />
	</fieldset>
</g:form>
```
We basically want to kind combine all three of these and rewrite `show.gsp` as something like this:
```grails
<g:form method="post">
    <g:hiddenField name="id" value="${userInstance?.id}" />
    <g:hiddenField name="version" value="${userInstance?.version}" />
    <sf:textField name="firstName" value="${userInstance?.firstName}"/>
    <sf:textField name="lastName" value="${userInstance?.lastName}"/>
    <fieldset class="buttons">
        <g:actionSubmit class="save" action="update" ... />
        <g:actionSubmit class="delete" action="delete" ... />
    </fieldset>
</g:form>
```
The only thing that's really different about this form, as compared to any other Grails form, is the namespace of the taglib tags (notice that `g:textField` is replaced by `sf:textField`)
## Notes


### Syntax
The tag syntax of each tag is **exactly** that of its delivered counterpart, that's because the plugin is using the delivered taglibs to actually produce the input element.  So see the Grails documentation for the delivered taglib syntax.


### Available Tags
All tags are in the namespace `sf`.
Currently, the available tags are:
* `sf:textField`
* `sf:textArea`
* `sf:select` (only works for single selection dropdown boxes right now)
* `sf:checkBox`

**A few notes about the checkBox:**

1. The `sf:checkBox` tag responds to two additional attributes: `checkedText` and `uncheckedText`, these represent the text to be displayed in the `span` that takes the place of the checkbox.  If they are not specified `sf:checkBox` relies on the Grails `formatBoolean` taglib to determine what text to display in the span.
2. `sf:checkBox` works slightly differently than the other tags. Like all others it displays a `span` that when clicked allows you to edit the value of the underlying input field (a text field, a select list, etc.), but unlike the other tags, the `span` created by `sf:checkBox` does not display an actual checkbox input element when it is clicked, rather the text in the `span` toggles between the values of the `checkedText` and `uncheckedText` attributes (or values returned by `formatBoolean`).

### Styling
Each span created will respond the `.editableSpan` CSS class, this is useful if you want to change the styling of *every* `span` that is created.  The `span`s also respond to classes named to represent what kind of field is being represented.  For example, the `span` created by the `sf:textArea` tag will respond to the `.editableTextAreaSpan` CSS class, this is useful if you only want to style the `span`s of a certain type.

Each input element responds to similarly named CSS classes.  Each input element responds to the  `.editableInput` and `.editable<INPUT_TYPE>Input` classes.  For example, the input element created by the `sf:select` tag will respond to the `.editableSelectInput` CSS class. 