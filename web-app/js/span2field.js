function hideSpan(e) {
    $('#' + e + '_span').css('display','none');
    $('#' + e + '_input').css('display','inline');
    $('#' + e + '_input').focus();
}

function hideInput(e) {
    $('#' + e + '_input').css('display','none');
    $('#' + e + '_span').html($('#' + e + '_input').val());
    $('#' + e + '_span').css('display','inline');
}

function hideSpanSelectSingle(e) {
    $('#' + e + '_span').css('display','none');
    $('#' + e + '_input').css('display','inline');
    $('#' + e + '_input').focus();
}

function hideInputSelectSingle(e) {
    $('#' + e + '_input').css('display','none');
    $('#' + e + '_span').html($('#' + e + '_input option:selected').text());
    $('#' + e + '_span').css('display','inline');
}

function changeCheckBoxSpan(e,checked,unchecked) {
    var changeTo;
//    alert($('#' + e).is(':checked'));
    if($('#' + e).is(':checked')) {
        changeTo = unchecked;
    } else {
        changeTo = checked;
    }
    $('#' + e).trigger('click');
    $('#' + e + '_span').html(changeTo);
}