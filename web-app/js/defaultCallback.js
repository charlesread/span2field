function callback(e) {
    e.append('<i class="fa fa-pencil"></i>');
    e.animate(
        {
            width: '+=30'
        },
        {
            duration: 300,
            complete: function(){
                $(this).addClass('successAfter');
            }
        }
    ).delay(1500).animate(
        {
            width: '-=30'
        },
        {
            duration: 300,
            start: function(){
                e.removeClass('successAfter');
            }
        }
    );
}

$(document).ready(function(){
    $('span.editableSpan').append('<i class="fa fa-pencil"></i>');
});