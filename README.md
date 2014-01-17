span2field
===============
A simple Grails plugin providing a tablig that allows you to make `span`s editable.  It is very easy to implement, as easy as any other of the delivered Grails input taglib tags (like `g:textField` and `g:select`).  

The plugin's taglib tags create `span`s that display data, when the `span` is clicked the `span` is hidden and an `input` element appears, allowing you to edit the data, when you click out of (blur) the `input` element (or select an entry, in the case of the single `select` input element) the `input` element is hidden and the `span` reappears, containing the new value that was just input.

The plugin effectively combines the *show* and *edit* controller actions that `grails generate-controller` might produce, the result is a single view that both displays data **and** allows you to edit it inline, without having to navigate to another page.

## Usage

You first need to include the plugin resources (just some Javascript) in your view with the `<sf:resources>` tag
```html
<head>
...
<sf:resources />
...
</head>
```
If you were to use Grails to generate controllers and views it would make a template for the form (`_form.gsp`) and views for editing and showing entries (`edit.gsp` and `show.gap`, respectively).  The form template (`_form.gsp`) would contain taglib tags like `g:textfield` for inputing data, while the view for showing the data (`show.gsp`) would contain taglib tags for displaying data (like `g:fieldValue`).  The idea is that we want to replace these tags that only display data with plugin tags that show the data **and** allow you to edit inline and add a `form` that will submit the data on the page to a controller action that will save the data (like the *update* action that might be created with `grails create-controller`).

Let's say you have used Grails to create the controller and views for a class called `User`, your `_form.gsp` might look like this:

```html
...
<g:textField name="firstName" value="${userInstance?.firstName}"/>
...
<g:textField name="lastName" value="${userInstance?.lastName}"/>
...
```

Your `show.gsp` might look like this:
```html
...
<g:fieldValue bean="${userInstance}" field="firstName"/>
...
<g:fieldValue bean="${userInstance}" field="lastName"/>
...
```
And your `edit.gsp` might look something like this:
```html
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
```html
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

### AJAX
The usage as detailed above clearly involves creating an actual form and submitting that form in order to persist changes to the database.  The plugin allows you to, with very little effort, have data persisted via AJAX as soon as the element is changed.
#### AJAX Usage
The process for AJAXifying a *span2field* field is virtually identical to the process for creating a non-AJAX *span2field* field.  You create the field exactly as you would as detailed above, you only have to add two additional attributes to the taglib tags:

* `ajax="true"`, just a toggle to tell the plugin to use AJAX
* `domainInstance="${domainInstance}"`, the instance of the domain class that you wish to update

For example:

```html
<sf:textField ajax="true" domainInstance="${userInstance}" name="firstName" value="${userInstance?.firstName}"/>
<sf:textField ajax="true" domainInstance="${userInstance}" name="lastName" value="${userInstance?.lastName}"/>
```

By default, when a *span2field* input field is blurred an AJAX call will be made to the `update` action in the `ajax` controller that is included in the plugin.  The `update` action is pretty good at doing what it needs to do in order to persist the data, but you may want to post the data to a different controller/action, which is made possible with two additional taglib attributes:

* `controller`, used for specifying the conroller that contains the action that will persist the data
* `action` used for specifying the action in the controller that will persist the data

For example:

```html
<sf:textField ajax="true" domainInstance="${userInstance}" controller="user" action="updateFirstName" name="firstName" value="${userInstance?.firstName}"/>

<sf:textField ajax="true" domainInstance="${userInstance}" controller="user" action="updateLastName" name="lastName" value="${userInstance?.lastName}"/>
```

The advantage to using the controller/action included with the plugin is that you don't have to worry about making actions for each element to be persisted, but it clearly lacks the flexibility that would be afforded by making your own controller/action to handle persistence.

#### Data Passed to the Controller/Action (by the AJAX call)

Several parameters are passed to the receiving controller during an AJAX call:

* `id`, the ID of the domain instance that is being persisted
* `value`, the value that needs to be persisted, with `<select multiple>` this comes in the form of a comma-delimited list of values
* `field`, the name of the field that needs to be persisted - *firstName*, for example
* `clazz`, the name of the class that has data that needs to be persisted - *com.charlesread.User*, for example (this is why the `domainInstance` attribute is used)
* `selectClazz`, the name of the class that is used as the data source with `select` tags - *com.charlesread.State*, for example - this will be the same class that is used in the `from` attribute of the `sf:select` tag
* `type`, indicates what type of element is being changed - like *textField*, *textArea*, *selectSingle* , *selectMultiple*, or *checkBox* 

The included controller/action (ajax/update) makes good use of these parameters to persist data, but you are free to use them in your own controllers and actions.

#### Callbacks

The Javascript function that does the AJAX call currently has two callbacks that can be specified in the `onSuccess` and `onFailure` tag attributes, for example:

```html
<sf:checkBox onSuccess="if (data.status == 200) { alert('nice!'); }" ajax="true" domainInstance="${demoInstance}" name="checkBox" value="${demoInstance?.checkBox}" />
```

This snippet of code will produce the following AJAX call:
```javascript
jQuery.ajax({
    type:'POST',
    data:'field=checkBox&clazz=com.charlesread.Demo&type=checkBox&id=1',
    url:'/span2field-demo/ajax/update',
    success:function(data,textStatus){if (data.status == 200) { alert('nice!');};},
    error:function(XMLHttpRequest,textStatus,errorThrown){}
});
```

As you can see, the `onSuccess` attribute manifests itself as the assignment of an anonymous function `function(data,textStatus){}`, here the `data` parameter is a JSON object containing the status code and a few other attributes, it will look something like this:

```json
{
    "status":200,
    "classUpdated":"com.charlesread.Demo",
    "fieldUpdated":"checkBox",
    "error":"no error"
}
```

The `onError` attribute becomes the assignment of a function `function(XMLHttpRequest,textStatus,errorThrown){}`.

More information on these functions is available in the [jQuery AJAX documentation](http://api.jquery.com/jquery.ajax/).

#### Default `onSuccess` and `onFailure` Callbacks

By default, these events trigger the color of the text in the `span` element to flash a color upon the success or failure of saving the domain object (green or red, respectively).  You can override this by setting your own callbacks (as described above), or by setting the relevant configuration option in `Config.groovy`, as shown below:

```
span2field {
    onSuccess {
        defaultAction = true //set this to false to disable the "text color flash"
        color = 'green' //change the color of the "text color flash", use any color understood by CSS
    }
    onFailure {
        defaultAction = true
        color = 'red'
    }
}
```

## Notes


### Syntax
The tag syntax of each tag is **exactly** that of its delivered counterpart, that's because the plugin is using the delivered taglibs to actually produce the input element.  So see the Grails documentation for the delivered taglib tag syntax.


### Available Tags
All tags are in the namespace `sf`.
Currently, the available tags are:

* `sf:textField`
* `sf:textArea`
* `sf:select` (single and multiple, the span for a multiple select input element will contain a `ul`)
* `sf:checkBox`

**A few notes about the checkBox:**

1. The `sf:checkBox` tag responds to two additional attributes: `checkedText` and `uncheckedText`, these represent the text to be displayed in the `span` that takes the place of the checkbox.  If they are not specified `sf:checkBox` relies on the Grails `formatBoolean` taglib to determine what text to display in the span.
2. `sf:checkBox` works slightly differently than the other tags. Like all others it displays a `span` that when clicked allows you to edit the value of the underlying input field (a text field, a select list, etc.), but unlike the other tags, the `span` created by `sf:checkBox` does not display an actual checkbox input element when it is clicked, rather the text in the `span` toggles between the values of the `checkedText` and `uncheckedText` attributes (or values returned by `formatBoolean`).
3.  The spans corresponding to checked and unchecked checkboxes will respond to the `editableCheckBoxChecked` and `editableCheckBoxUnchecked` CSS classes, respectively.

### Styling
Each span created will respond the `.editableSpan` CSS class, this is useful if you want to change the styling of *every* `span` that is created.  The `span`s also respond to classes named to represent what kind of field is being represented.  For example, the `span` created by the `sf:textArea` tag will respond to the `.editableTextAreaSpan` CSS class, this is useful if you only want to style the `span`s of a certain type.

Each input element responds to similarly named CSS classes.  Each input element responds to the  `.editableInput` and `.editable<INPUT_TYPE>Input` classes.  For example, the input element created by the `<sf:select multiple>` tag will respond to the `.editableSelectMultipleInput` CSS class. 

