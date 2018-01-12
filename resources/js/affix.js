/*
$('#xfoldertree').affix({
    offset: {
      top: $('#foldertree').offset().top,
      bottom: function() {
          console.log(".md-footer-bg="+$('.md-footer-bg').outerHeight(true));
          console.log(".md-footer"+$('.md-footer').outerHeight(true));
          console.log(".md-footer offset"+$('.md-footer').offset().bottom);
          return ($('.md-footer').outerHeight(true) + $('.md-footer-bg').outerHeight(true)) + 240;
      }
    }
});
*/

var scrollTimer = null;
$(window).scroll(function() {
    var top = $(document).scrollTop();
    clearTimeout(scrollTimer);
    scrollTimer = setTimeout(
        function()
        {
            //console.log("scrolltimer:"+top);
            var posMax = ($('.md-footer-container').offset().top - $('#foldertree').outerHeight(true)-100); //Position (an der der Baum den Footer trifft)

            if (top<posMax) {            
                //console.log("top:"+top);
                //console.log(".md-footer-bg: "+$('.md-footer-bg').offset().top);
                //console.log("#foldertree.height: "+$('#foldertree').outerHeight(true));
                //console.log(".md-footer-container: "+$('.md-footer-container').offset().top);
                //console.log("pos ballooning: "+($('.md-footer-container').offset().top - $('#foldertree').outerHeight(true)-100));
                $("#balloon").animate({height:top+'px'});
                //$("#balloon").css("height",top+'px');
            }
        }, 3000);

});