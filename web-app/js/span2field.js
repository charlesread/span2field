function hideSpan(e) {
    $('#' + e + '_span').css('display','none');
    $('#' + e + '_input').css('display','inline');
    $('#' + e + '_input').focus();
}

function hideInput(e) {
    $('#' + e + '_input').css('display','none');
    $('#' + e + '_span').html($('#' + e + '_input').val() || '...');
    $('#' + e + '_span').css('display','inline');
}

function hideSpanSelectSingle(e) {
    $('#' + e + '_span').css('display','none');
    $('#' + e + '_input').css('display','inline');
    $('#' + e + '_input').focus();
}

function hideInputSelectSingle(e) {
    $('#' + e + '_input').css('display','none');
    $('#' + e + '_span').html($('#' + e + '_input option:selected').text()  || '...');
    $('#' + e + '_span').css('display','inline');
}

function hideInputSelectMultiple(e) {
    $('#' + e + '_input').css('display','none');
    if ($('#' + e + '_input option:selected').length>0) {
	    $('#' + e + '_span').html('');
	    $('#' + e + '_span').append('<ul>')
	    $.each($('#' + e + '_input option:selected'), function(i,v){
	        $('#' + e + '_span ul').append('<li>' + v.textContent + '</li>');
	    });
	    $('#' + e + '_span').append('</ul>')
	}
    $('#' + e + '_span').css('display','inline');
}

function changeCheckBoxSpan(e,checked,unchecked) {
    var changeTo;
    if($('#' + e + '_input').is(':checked')) {
        changeTo = unchecked;
        $('#' + e + '_span').removeClass("editableCheckBoxChecked").addClass("editableCheckBoxUnchecked");
    } else {
        changeTo = checked;
        $('#' + e + '_span').removeClass("editableCheckBoxUnchecked").addClass("editableCheckBoxChecked");
    }
    $('#' + e + '_input').trigger('click');
    $('#' + e + '_span').css({width:'auto'});
    $('#' + e + '_span').html(changeTo);
}

function colorConf(e,c) {
    var originalColor = e.css('color');
    e.animate({
        'color': c || 'green'
    },100,function(){
        e.animate({
            'color': originalColor
        },2000);
    });
}
