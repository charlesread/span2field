function callback(e) {
    e.css({width: 'auto'});
    e.append('<i class="fa fa-pencil"></i>');
    $(e).animate(
        {
            width: '+=30',
            borderColor: '#E6F2FF',
            borderWidth: 1
        },
        {
            duration: 300,
            complete: function(){
                $(e).addClass('successAfter');
            }
        }
    ).delay(1500).animate(
        {
            width: '-=30',
            borderColor: 'rgba(0,0,0,0)'
        },
        {
            duration: 300,
            start: function(){
                $(e).removeClass('successAfter');
            }
        }
    );
}

$(document).ready(function(){
    $('span.editableSpan').append('<i class="fa fa-pencil"></i>');
});