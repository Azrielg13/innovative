<!-- 

// ONLY USE lowercase FOR ALL OPTIONS
var small_icon 			= "no"		// SHOW THE SMALL POP UP ICON
var popup_text 			= "yes"		// HAVE TEXT IN THE POP-UPS
var picture_only		= "no"		// "yes" FOR ONLY THE PICTURE TO SHOW IN THE POPUPS
var viewer	 			= "no"		// yes/FULL SCREEN OR no/POPUP MODE

var close_button 		= "no"		// CLOSE BUTTON ON/OFF
var slideshow_button 	= "no"		// SLIDESHOW BUTTON ON/OFF
right_click_on			= "no"		// RIGHT CLICK PROTECTION ON

var watermark 			= "no"		// WATERMARK ON/OFF
var watermark_horz		= "340"		// WATERMARK POSITION
var watermark_vert		= "200"		// WATERMARK POSITION
var watermark_opacity	= "35"		// DOES NOT WORK IN NETSCAPE


// OTHER OPTIONS YOU CAN CHANGE

text_width				= "200"		// WIDTH OF THE TEXT TABLE
border_color			= "000000"	// PICTURE OUTLINE COLOR

var slideshow_width 	= 750		// SLIDESHOW POPUP WINDOW WIDTH
var slideshow_height 	= 525		// SLIDESHOW POPUP WINDOW HEIGHT

var view_width 			= 400		// GALLERY POPUP WIDTH
var view_height 		= 500		// GALLERY POPUP HEIGHT

music_width				= "200"		// MUSIC POPUP WIDTH
music_height			= "200"		// MUSIC POPUP HEIGHT

var scrollbarS 			= "1"		// TURN ON POPUP SCROLLBARS "1" FOR ON "0" FOR OFF
var hide_status			= "yes"		// HIDE LINK STATUS
var status_text 		= ""		// TEXT IN THE STATUS BAR
var zoom	 			= "yes"		// SHOW POPUP ZOOM LINKS


function popUpSlideshow(URL) {
	day = new Date();
	id = day.getTime();

	if (viewer == "no") {
		eval("page" + id + " = window.open(URL, '" + id + "', 'toolbar=0,scrollbars=' + scrollbarS + ',location=0,statusbar=0,menubar=0,resizable=1,width='+slideshow_width+',height='+slideshow_height+'');");
	}else if (viewer == "yes") {
		eval("page" + id + " = window.open(URL);");
	}
}


// START GALLERY POPUP CODE

function popUp(URL,TITLE,TEXT,ITEM_NO) {

	if (viewer == "no") {
		var look='toolbar=0,scrollbars=' + scrollbarS + ',location=0,statusbar=1,menubar=0,resizable=1,width='+view_width+',height='+view_height+','
		popwin=window.open("","",look,"TITLE","TEXT")
	}else if (viewer == "yes") {
		popwin=window.open()
	}
	
	popwin.document.open()	
	popwin.document.write('<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"><html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"><head><title>Angel Exotic Wear</title>')
	
	if (right_click_on == "yes") {
		
		popwin.document.write('<link rel="stylesheet" type="text/css" href="../formatting.css" media="screen" title="default"/>')
		popwin.document.write('<link rel="stylesheet" type="text/css" href="../design.css" media="screen" title="default"/>')
		popwin.document.write('<META HTTP-EQUIV="imagetoolbar" CONTENT="no">')
		popwin.document.write('<script language="JavaScript">')
		popwin.document.write('function noRightClick() {')
		popwin.document.write('if (event.button==2) {')
		popwin.document.write('alert(\'You may not right mouse click this page.\')')
		popwin.document.write('}')
		popwin.document.write('}')
		popwin.document.write('document.onmousedown=noRightClick')
		popwin.document.write('</script>')
	}
	popwin.document.write('</head>')
	popwin.document.write('<body onLoad="show.style.zoom=1.0" style="background:url(img/bg_fade_pink.gif) left bottom fixed repeat-x;"> ')
	popwin.document.write('<div id="content" style="width:100%;height;100%">')
	
	if (picture_only == "no") {
		popwin.document.write('</div>')
		popwin.document.write('<div style="width:'+text_width+';">')
		
		if (small_icon == "yes") {
			popwin.document.write('<img src="img/icon-popup.gif"><br/>')
		}
		
		if (popup_text == "yes") {
			popwin.document.write('<h3 style="background:url(img/h3.gif) left no-repeat;">'+TITLE+'</h3><p>'+TEXT+'</p>')			
		}
		
		popwin.document.write('</div>')
		popwin.document.write('<div>')
   	}
	// ZOOM CODE
	if (zoom == "yes") {
		browser_version= parseInt(navigator.appVersion);
		browser_type = navigator.appName;
		if (browser_type == "Microsoft Internet Explorer" && (browser_version >= 4) && (navigator.userAgent.indexOf("Windows") != -1)) {
			popwin.document.write("<div style=\"position:absolute;top:20px;right:20px;\">");
			popwin.document.write("<a href=\"#\" onClick=\"show.style.zoom='1.0'\"><img src=\"img/zoom-0.gif\" border=\"0\"></a>");
			popwin.document.write("<a href=\"#\" onClick=\"show.style.zoom='1.5'\"><img src=\"img/zoom-1.gif\" border=\"0\"></a>");
			popwin.document.write("<a href=\"#\" onClick=\"show.style.zoom='2.0'\"><img src=\"img/zoom-2.gif\" border=\"0\"></a>");
			popwin.document.write("<a href=\"#\" onClick=\"show.style.zoom='2.5'\"><img src=\"img/zoom-3.gif\" border=\"0\"></a>");			
			if (close_button == "yes") {
				popwin.document.write('<form style="margin:0px"><input type="button" value="Close" onmouseover="this.className=\'buttonon-popups\'" onmouseout="this.className=\'button-popups\'" class="button-popups" onClick=\'self.close()\'>&nbsp;</form>')
			}
			popwin.document.write("</div>");
		}
	}
	popwin.document.write('<div style="text-align:center;">')
	popwin.document.write('<img src="'+URL+'" name="show"/><br/>')
	//popwin.document.write('<a href="order.jsp?Item_No='+ITEM_NO+'" target="parent" onClick="self.close()"><img src="img/add_to_cart.gif" border="0" alt=""/></a><br/>')
	popwin.document.write('</div>')
	popwin.document.write('<br/>')
	popwin.document.write('<div style="text-align:center;">')
	popwin.document.write('<a onClick="self.close()"><img src="img/finished.gif" border="0" alt=""/></a><br/>')

	if (slideshow_button == "yes") {
		popwin.document.write('</td><td>')
		popwin.document.write('<form action="slideshow.htm" style="margin: 0px"><input type="submit" value="Slideshow" onmouseover="this.className=\'buttonon-popups\'" onmouseout="this.className=\'button-popups\'" class="button-popups"></form>')
	}
		popwin.document.write('</td></tr></table>')

	// START WATERMARK CODE
	if (watermark == "yes") {
		popwin.document.write('<div id="watermark" style=" LEFT: ' + watermark_horz + 'px; POSITION: absolute; TOP: ' + watermark_vert + 'px; Filter: Alpha(Opacity=' + watermark_opacity + ')">')
		popwin.document.write('<TABLE cellpadding="0" cellspacing="0" border="0"><tr><td>')
		popwin.document.write('<img src="picts/watermark.gif"><br>')
		popwin.document.write('</td></tr></table>')
		popwin.document.write('</div>')
	}
	popwin.document.write('</div>')
	popwin.document.write('</body></html>')
	popwin.document.close()
}


// MUSIC POPUP CODE
function PLAYMUSIC() {
	msg=open('music/music-1.htm','frameWindow','scrollbars=no,toolbar=no,resizable=yes,directories=no,menubar=no,location=no,status=no,left=0,top=250,width=' + music_width + ',height=' + music_height + '');
}



// FULL SCREEN MODE
function fullScreen() {
	window.open(location.href,'fullscreen','fullscreen,scrollbars')
}


// MOUSEOVER STATUS
if (hide_status == "yes") {
	function hidestatus(){
		window.status=status_text
		return true
	}

	if (document.layers)
		document.captureEvents(Event.MOUSEOVER | Event.MOUSEOUT)

	document.onmouseover=hidestatus
	document.onmouseout=hidestatus
}

// -->
