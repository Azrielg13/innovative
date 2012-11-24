/* 
 * Copyright Notice. 
 * This code was originally created by yCode: http://www.yCode.com  
 * Any modifications to this code can only be made upon purchase from
 * yCode provided that this notice remains in the source code. 
 *
 */

var cSortOrder = "";

function initComboBox() {
	var iBrowserVersion = checkBrowser();
	if ( iBrowserVersion == 0 ) {
		alert("This page may not be displayed properly:\n\
		This product requires Microsoft Internet Explorer 5 or later browser only.");
	}

	innerHTML = "<SPAN><INPUT /><SELECT>" + innerHTML + "</SELECT></SPAN>";
	var oSpan = children( 0 );

	oTextBox = oSpan.children( 0 );
	oTextBox.id = uniqueID + "_text";
	oTextBox.name = name + "_text";

	oTextBox.attachEvent("onchange",onChangeEditText);
	oTextBox.attachEvent("onkeyup",onEditKeyUp);
	oTextBox.attachEvent("onkeydown",onEditKeyDown);
	oTextBox.attachEvent("onkeypress",onEditKeyPress);
	oTextBox.attachEvent("onblur",onEditBlur);

	oSelectBox = oSpan.children( 1 );
	oSelectBox.id	= uniqueID + "_select";
	oSelectBox.name = name + "_select";
	oSelectBox.tabIndex = -1;

	oSelectBox.calculate_absolute_X=calculate_absolute_X;
	oSelectBox.calculate_absolute_Y=calculate_absolute_Y;

	oSelectBox.style.position	=	"absolute";
	oSelectBox.style.setExpression("pixelLeft", "calculate_absolute_X(" + oTextBox.id + ")");
	oSelectBox.style.setExpression("pixelTop", "calculate_absolute_Y(" + oTextBox.id + ")");
	//oSelectBox.style.setExpression("pixelWidth", id + ".offsetWidth" );
	//oSelectBox.style.setExpression("pixelHeight", id + ".offsetHeight");

	if ( iBrowserVersion == 6 ) {
		oSelectBox.style.setExpression("clip","'rect(auto auto auto '+(offsetWidth - 18)+')'");
	}
	else {
		oSelectBox.style.setExpression("clip","'rect(auto auto auto '+(offsetWidth - 20)+')'");
	}

	var iWidth = getAttribute( "width" );
	if ( ( iWidth != null ) && ( iWidth != "" ) ) {
		oSelectBox.style.pixelWidth = iWidth;
	}

	oTextBox.style.setExpression("pixelWidth", oSelectBox.id + ".offsetWidth - 18" );
	oSpan.style.setExpression("pixelWidth", oSelectBox.id + ".offsetWidth" );

	oSelectBox.attachEvent("onchange", onChangeListSelection);
	
	/*
	 * Supported styles: background color, color, font, letter-spacing, margin, text-align
	 */
	var sProperty;
	var sPropertyValue;
		
	sPropertyValue = style.getAttribute("backgroundColor");
	if ( ( sPropertyValue != null ) && ( sPropertyValue != "" ) ) {
		oTextBox.style.setAttribute("backgroundColor", sPropertyValue );
		oSelectBox.style.setAttribute("backgroundColor", sPropertyValue );
	}
	sPropertyValue = style.getAttribute("color");
	if ( ( sPropertyValue != null ) && ( sPropertyValue != "" ) ) {
		oTextBox.style.setAttribute("color", sPropertyValue );
		oSelectBox.style.setAttribute("color", sPropertyValue );
	}

	sPropertyValue = style.getAttribute("font");
	if ( ( sPropertyValue != null ) && ( sPropertyValue != "" ) ) {
		oTextBox.style.setAttribute("font", sPropertyValue );
		oSelectBox.style.setAttribute("font", sPropertyValue );
	}

	sPropertyValue = style.getAttribute("letterSpacing");
	if ( ( sPropertyValue != null ) && ( sPropertyValue != "" ) ) {
		oTextBox.style.setAttribute("letterSpacing", sPropertyValue );
		oSelectBox.style.setAttribute("letterSpacing", sPropertyValue );
	}

	sPropertyValue = style.getAttribute("textAlign");
	if ( ( sPropertyValue != null ) && ( sPropertyValue != "" ) ) {
		oTextBox.style.setAttribute("textAlign", sPropertyValue );
		oSelectBox.style.setAttribute("textAlign", sPropertyValue );
	}
	
	attachEvent( "onpropertychange", changeStyle );
	/*
	if ( currentStyle.width != "auto") {
	}
	*/
	
	/*
	 * Do not sort list for performance reasons; options should be provided in appropriate order
	 *
	if ( element.sort_order != "" ) {
		sortList();
	}
	*/

	// If element is intialized with options, insert appropriate text into edit box
	var iSelected = oSelectBox.selectedIndex;
	if ( iSelected > -1 ) {
		oTextBox.value = oSelectBox.options[ iSelected ].text;
		fireTextChange();
	}

	if ( ( currentStyle.visibility == "visible" ) && ( currentSTyle.display != "none" ) ) {		// if control is visible
		oTextBox.select();
		oTextBox.focus();
	}

	lookup_options		= true;
	case_sensitive		= false;
	options_only		= false;
	sort_order			= "";
	max_text_length		= "";
	tables				= 0;
}
/*
 *	iDefaultMode: 0 - text, 1 - index, 2 - value; default = text
 */
function initOptions( asInitialOptions, asInitialValues, sDefaultText, iDefaultMode )
{
	if ( asInitialOptions == null ) {
		return;
	}

	// sort options array appropriately
	if ( sort_order != "" ) {
		asInitialOptions.sort();		
		if ( sort_order != "a" ) {						
			asInitialOptions.reverse();
		}
	}

	var iLen = asInitialOptions.length;
	var iInd;
		
	if ( iDefaultMode == null ) {
		iDefaultMode = 0;
	}
	if ( ( iDefaultMode == 1 ) && !isInteger( sDefaultText ) ) {
		iDefaultMode = 0;
	}

	var bSelectOption = ( sDefaultText != null );

	for ( iInd = 0; iInd < iLen; iInd++ ) {
		var oOption		= document.createElement("OPTION");
		var sOptionText = asInitialOptions[ iInd ];
		var sOptionValue = ( asInitialValues != null ) ? asInitialValues[ iInd ] : "";

		oOption.text	= sOptionText;
		oOption.value	= sOptionValue;
		oSelectBox.add(oOption);

		if ( bSelectOption ) {
			if ( ( iDefaultMode == 0 ) && ( sOptionText == sDefaultText ) ) {
				oSelectBox.selectedIndex = iInd;
				oTextBox.value = sOptionText;
			}
			if ( ( iDefaultMode == 2 ) && ( sOptionValue == sDefaultText ) ) {
				oSelectBox.selectedIndex = iInd;
				oTextBox.value = sOptionText;
			}
		}
	}

	if ( bSelectOption && ( iDefaultMode == 1 ) ) {
		var iIndex = parseInt( sDefaultText, 10 );

		if ( ( iIndex <= iLen ) || ( iIndex < 0 ) ) {
			oSelectBox.selectedIndex = -1;
			oTextBox.value = "";
		}
		else {
			oSelectBox.selectedIndex = iIndex;
			oTextBox.value = oSelectBox.options[ iIndex ].text;
		}
	}

	if ( !bSelectOption ) {
		oSelectBox.selectedIndex = -1;
		oTextBox.value = "";
	}
	oTextBox.focus();
	oTextBox.select();

	if ( bSelectOption ) {
		fireTextChange();
		fireSelectionChange( oSelectBox.selectedIndex );
	}
}

/********************************************************************
 **************************METHODS***********************************
 ********************************************************************/

function getOptionCount()
{
	return oSelectBox.options.length;
}
 
function getSelectedIndex()
{
	return oSelectBox.selectedIndex;
}

function setSelectedIndex( iSelectedIndex )
{
	if ( !isInteger( iSelectedIndex ) ) {
		return -1;
	}

	var iLen = oSelectBox.options.length;
	if ( ( iSelectedIndex < 0 ) || ( iSelectedIndex >= iLen ) ) {
		return -1;
	}
	oSelectBox.selectedIndex = iSelectedIndex;

	oTextBox.value = oSelectBox.options[ iSelectedIndex ].text;
	oTextBox.focus();
	oTextBox.select();

	fireTextChange();
	fireSelectionChange( iSelectedIndex );
	return iSelectedIndex;
}

function getOptionValue( iIndex ) 
{
	if ( !isInteger( iIndex ) ) {
		return "Error";
	}

	var iLen = oSelectBox.options.length;
	if ( ( iIndex < 0 ) || ( iIndex >= iLen ) ) {
		return "Error";
	}
	return oSelectBox.options[ iIndex ].value;
}

function setOptionValue( iIndex, sValue )
{
	if ( !isInteger( iIndex ) ) {
		return -1;
	}

	var iLen = oSelectBox.options.length;
	if ( ( iIndex < 0 ) || ( iIndex >= iLen ) ) {
		return -1;
	}
	oSelectBox.options[ iIndex ].value = sValue;
	return iIndex;
}

function clearText()
{
	oSelectBox.selectedIndex = -1;
	oTextBox.value = "";
	oTextBox.focus();
	oTextBox.select();

	fireTextChange();
	fireSelectionChange( -1 );
}

function getText()
{
	return oTextBox.value;
}

function setText( sText )
{
	var sSavedText = oTextBox.value;		// remember the previous value

	oTextBox.value = sText;
	onChangeEditText();			// select matching option

	// If an option is selected, select text in text control
	if ( oSelectBox.selectedIndex > -1 ) {
		oTextBox.focus();
		oTextBox.select();
	}
	else if ( options_only == true ) 	{
		oTextBox.value = sSavedText;			// if only options are allowed, don't change text control
		oTextBox.focus();
		oTextBox.select();
	}
}

function getOptionText( iIndex )
{
	if ( !isInteger( iIndex ) ) {
		return "";
	}

	var iLen = oSelectBox.options.length;
	if ( ( iIndex < 0 ) || ( iIndex >= iLen ) ) {
		return "";
	}
	return oSelectBox.options[ iIndex ].text;
}

//============================================================
//addValue() -- adds a value to the combo box; if the argument
// is not present or is empty, adds the current combo box value.
// iInsertIndex - insertion index; if none is given:
//   if list is not sorted, places the option to the end of options array.
//   if list is sorted, places into appropriate position
//------------------------------------------------------------
function addOption( sText, sValue, iInsertIndex )
{
	if ( (sText == null) || (sText == "") ) {
		if ( oTextBox.value == "" ) {
			return -1;
		}
		sText = oTextBox.value;
	}

	if ( !isInteger( iInsertIndex ) ) {
		iInsertIndex = null;
	}

	var iLen = oSelectBox.options.length;
	if ( ( iInsertIndex < 0 ) || ( iInsertIndex > iLen ) ) {
		iInsertIndex = null;
	}

	var bSort = false;

	// Ignore insertion index if list is sorted
	if ( cSortOrder != "" ) {
		iInsertIndex = null;
		bSort = true;
	}

	var iIndex;
	var bExists			= false;
	var sOptionText, sOptionTextUpper;
	var sTextUpper		= sText;

	if (case_sensitive == false) {
		sTextUpper = sTextUpper.toUpperCase();
	}

	//check for duplicates and return if a duplicate exists
	for (iIndex=0; iIndex<iLen; iIndex++)
	{
		sOptionTextUpper = sOptionText = oSelectBox.options(iIndex).text;

		if (case_sensitive == false) {
			sOptionTextUpper = sOptionTextUpper.toUpperCase();
		}

		if ( sOptionTextUpper == sTextUpper )
		{
			oSelectBox.selectedIndex = iIndex;
			sText = sOptionText;
			bExists = true;
			break;
		}

		// if list is sorted, figure out insertion point
		if ( bSort ) {
			if ( iInsertIndex != null ) {
				continue;			// insertion index already found
			}
			if ( cSortOrder == "a" ) {
				if ( sText < sOptionText ) {
					iInsertIndex = iIndex;
				}
			}
			else {
				if ( sText > sOptionText ) {
					iInsertIndex = iIndex;
				}
			}
		}
	}

	if ( !bExists ) {
		var oOption		= document.createElement("OPTION");
		oOption.text	= sText;
		oOption.value	= sValue;

		if ( iInsertIndex == null ) {
			oSelectBox.add(oOption );			// place at the end
			oSelectBox.selectedIndex = iLen;
		}
		else {
			oSelectBox.add(oOption, iInsertIndex);
			oSelectBox.selectedIndex = iInsertIndex;
		}
	}
	oTextBox.value = sText;
	oTextBox.select();
	oTextBox.focus();

	fireTextChange();
	fireSelectionChange( oSelectBox.selectedIndex );
	return oSelectBox.selectedIndex;
}


//============================================================
//deleteValue() -- deletes the value from the combo box; if the 
// argument is not present or is empty, deletes the current combo 
// box value.
//------------------------------------------------------------
function deleteOption( sText )
{
	if ( (sText == null) || (sText == "") ) {
		if ( oTextBox.value == "" ) {
			return false;
		}
		sText = oTextBox.value;
	}

	var iLen = oSelectBox.options.length;
	var iIndex;
	var iFound = -1;
	var sOptionText;

	if (case_sensitive == false) {
		sText = sText.toUpperCase();
	}

	for (iIndex=0; iIndex<iLen; iIndex++)
	{
		sOptionText = oSelectBox.options(iIndex).text;

		if (case_sensitive == false) {
			sOptionText = sOptionText.toUpperCase();
		}
		if ( sOptionText == sText)
		{
			iFound = iIndex;
			break;
		}
	}

	if (iFound == -1)
	{
		return false;
	}
	oSelectBox.remove(iFound);
	oSelectBox.selectedIndex = -1;
	oTextBox.value = "";

	fireTextChange();
	fireSelectionChange( -1 );
	return true;
}

function deleteOptionIndex( iOptionIndex )
{
	var iLen = oSelectBox.options.length;

	if ( !isInteger( iOptionIndex ) ) {
		return false;
	}
	if ( ( iOptionIndex < 0 ) || ( iOptionIndex >= iLen ) ) {
		return false;
	}
	oSelectBox.remove(iOptionIndex);
	oSelectBox.selectedIndex = -1;
	oTextBox.value = "";

	fireTextChange();
	fireSelectionChange( -1 );
	return true;
}

function resetList()
{
	var bWasSelected = ( oSelectBox.selectedIndex > -1 );
	var bWasText = ( oTextBox.value != "" );
	
	var iLen = oSelectBox.options.length;
	var iIndex;

	oSelectBox.options.length = 0;
	oTextBox.value = "";
	oSelectBox.selectedIndex = -1;

	if ( bWasText ) {
		fireTextChange();
	}
	if ( bWasSelected ) {
		fireSelectionChange( -1 );
	}
}

function findText( sText, bExact )
{
	if ( (sText == null) || (sText == "") ) {
		if ( oTextBox.value == "" ) {
			return false;
		}
		sText = oTextBox.value;
	}
	if ( bExact == null ) {
		bExact = false;
	}
	if (case_sensitive == false) {
		sText = sText.toUpperCase();
	}
	var iLen = oSelectBox.options.length;
	var iIndex;
	var sCurrentText;
	var bFound;

	for (iIndex=0; iIndex<iLen; iIndex++)
	{
		sCurrentText = oSelectBox.options[ iIndex ].text;
		if (case_sensitive == false) {
			sCurrentText = sCurrentText.toUpperCase();
		}
		bFound = bExact ? ( sText == sCurrentText ) : (sCurrentText.indexOf(sText) == 0);
		
		if ( bFound ) {
			return iIndex;
		}
	}
	return -1;
}

function selectText( sText, bExact )
{
	return setSelectedIndex( findText( sText, bExact ) );
}
 
/********************************************************************
 **************************EXPOSED EVENTS****************************
 ********************************************************************/

function onChangeListSelection()
{
	var iSelected = oSelectBox.selectedIndex;
	var oSelected = oSelectBox.options[iSelected];

	oTextBox.value  = oSelected.text;
	oTextBox.focus();
	oTextBox.select();

	fireSelectionChange( iSelected );
}

function onChangeEditText()
{
	if (oTextBox.value == "") {
		return;
	}

	var iIndex;
	var iLen		= oSelectBox.options.length;
	var sEditText	= oTextBox.value;
	var sListText;
	var bFound		= false;

	// Select list item which text is equal to text box value
	for (iIndex=0; iIndex<iLen; iIndex++)
	{
		sListText = oSelectBox.options(iIndex).text;

		if (case_sensitive == false)
		{
			sListText = sListText.toUpperCase();
			sEditText = sEditText.toUpperCase();
		}
		
		if (sListText == sEditText)
		{
			oSelectBox.selectedIndex = iIndex;
			bFound = true;
			break;
		}
	}

	// warn user if only list entries are allowed
	if ( !bFound ) {
		if ( options_only == true ) 	{
			error("'" + oTextBox.value + "' is not allowed");
			oTextBox.focus();	
			oTextBox.select();
			return;
		}
		oSelectBox.selectedIndex = -1;
	}

	fireTextChange();
	fireSelectionChange( oSelectBox.selectedIndex );
}

function onEditKeyUp()
{
	// Alpha-numeric keys only
	if ( ( event.keyCode >= 48 ) && ( event.keyCode <= 57 ) || ( event.keyCode >= 65 ) && ( event.keyCode <= 90 ) ) {
		if ( lookup_options == true ) {
			event.returnValue = !lookupSelectBox();
		}
	}
    oEvent = createEventObject();

    oEvent.altKey = event.altKey;
	oEvent.altLeft = event.altLeft;
    oEvent.ctrlKey = event.ctrlKey;
	oEvent.ctrlLeft = event.ctrlLeft;
    oEvent.shiftKey = event.shiftKey;
	oEvent.shiftLeft = event.shiftLeft;
    oEvent.clientX = event.clientX;
	oEvent.clientY = event.clientY;
    oEvent.offsetX = event.offsetX;
	oEvent.offsetX = event.offsetY;
    oEvent.screenX = event.screenX;
	oEvent.screenX = event.screenY;
    oEvent.x = event.x;
	oEvent.y = event.y;
	oEvent.keyCode = event.keyCode;
	event_textkeyup.fire( oEvent );
}

function onEditKeyDown()
{
    oEvent = createEventObject();

    oEvent.altKey = event.altKey;
	oEvent.altLeft = event.altLeft;
    oEvent.ctrlKey = event.ctrlKey;
	oEvent.ctrlLeft = event.ctrlLeft;
    oEvent.shiftKey = event.shiftKey;
	oEvent.shiftLeft = event.shiftLeft;
    oEvent.clientX = event.clientX;
	oEvent.clientY = event.clientY;
    oEvent.offsetX = event.offsetX;
	oEvent.offsetX = event.offsetY;
    oEvent.screenX = event.screenX;
	oEvent.screenX = event.screenY;
    oEvent.x = event.x;
	oEvent.y = event.y;
	oEvent.keyCode = event.keyCode;
	event_textkeydown.fire( oEvent );
}

function onEditKeyPress()
{
    oEvent = createEventObject();

    oEvent.altKey = event.altKey;
	oEvent.altLeft = event.altLeft;
    oEvent.ctrlKey = event.ctrlKey;
	oEvent.ctrlLeft = event.ctrlLeft;
    oEvent.shiftKey = event.shiftKey;
	oEvent.shiftLeft = event.shiftLeft;
    oEvent.clientX = event.clientX;
	oEvent.clientY = event.clientY;
    oEvent.offsetX = event.offsetX;
	oEvent.offsetX = event.offsetY;
    oEvent.screenX = event.screenX;
	oEvent.screenX = event.screenY;
    oEvent.x = event.x;
	oEvent.y = event.y;
	oEvent.keyCode = event.keyCode;
	event_textkeypress.fire( oEvent );
}

/********************************************************************
 **************************INTERNAL EVENTS***************************
 ********************************************************************/

function onEditBlur()
{
	var sCurrentText = oTextBox.value;		// remove selection from text control
	oTextBox.value = sCurrentText;
}

/********************************************************************
 **************************INTERNAL FUNCTIONS************************
 ********************************************************************/

function calculate_absolute_X( theElement )
{
	var xPosition = 0;

	//while ( theElement != null )
	while ( ( theElement != null ) && ( theElement.id != id ) )
	{
		xPosition += theElement.offsetLeft;
		theElement = theElement.offsetParent;
	}

	return xPosition + tables;
}

function calculate_absolute_Y( theElement )
{
	var yPosition = 0;

//	while ( theElement != null )
	while ( ( theElement != null ) && ( theElement.id != id ) )
	{
		yPosition += theElement.offsetTop;
		theElement = theElement.offsetParent;
	}

	return yPosition + tables;
}

function isInteger( iNumber )
{
	if ( isNaN( iNumber ) ) {
		return false;
	}
	return ( iNumber == parseInt( iNumber, 10 ) );
}

function error( sErrorMessage )
{
	alert( sErrorMessage );
}

function getSortOrder()
{
	return cSortOrder;
}

function sortList( cNewSortOrder )
{
	if ( cSortOrder == cNewSortOrder ) {
		return;
	}

	cSortOrder = cNewSortOrder; 
	if ( cSortOrder == "" ) {
		return;
	}
	var bAscending = ( cSortOrder == "a" );		// "a" for accending, "d" for decsending

	var iLen = oSelectBox.options.length;
	var iInd;
	var asOptions = new Array( iLen );
	var asSortedOptions = new Array( iLen );
	var asValues = new Array( iLen );
	var sText;

	var iSelectedIndex = oSelectBox.selectedIndex;
	var sSelectedText = "";

	if ( iSelectedIndex > -1 ) {
		sSelectedText = oSelectBox.options[ iSelectedIndex ].text;
	}

	// Get values from existing options
	for ( iInd = 0; iInd < iLen; iInd++ ) {
		sText = oSelectBox.options[ iInd ].text;
		asSortedOptions[ iInd ] = asOptions[ iInd ] = sText;
		asValues[ sText ] = oSelectBox.options[ iInd ].value;
	}

	asSortedOptions.sort();
	if ( !bAscending ) {					// reverse array
		asSortedOptions.reverse();
	}

	if ( asSortedOptions == asOptions ) {
		return;		// already sorted
	}

	resetList();

	for ( iInd = 0; iInd < iLen; iInd++ ) {
		var oOption		= document.createElement("OPTION");

		sText = asSortedOptions[ iInd ];
		oOption.text	= sText;
		oOption.value	= asValues[ sText ];
		oSelectBox.add(oOption);

		if ( sSelectedText == sText ) {
			oOption.selected = true;
			oTextBox.value = sText;

			oTextBox.focus();
			oTextBox.select();
		}
	}
	propid_sort.fireChange();
}

function limitTextLength( iNumChars )
{
	if ( iNumChars == "" ) {
		oTextBox.removeAttribute( "maxLength", 0 );		// "" indicates no limit to text length
	}
	else {
		if ( !isInteger( iNumChars ) ) {
			alert( "Invalid function parameter" );
			return;
		}
		else {
			oTextBox.maxLength = iNumChars;
		}
	}
	propid_maxtextlen.fireChange();
}

function lookupSelectBox()
{
	var sCurrentText		= oTextBox.value;
	var iIndex;

	var iLen = oSelectBox.options.length;
	var sOptionText, sOptionTextUC;
	
	for (iIndex=0; iIndex<iLen; iIndex++)
	{
		sOptionText = sOptionTextUC = oSelectBox.options(iIndex).text;

		if (case_sensitive == false)
		{
			sOptionTextUC = sOptionTextUC.toUpperCase();
			sCurrentText = sCurrentText.toUpperCase();
		}

		if (sOptionTextUC.indexOf(sCurrentText) == 0)
		{
			oSelectBox.selectedIndex = iIndex;
			oTextBox.value = sOptionText;
		
			var oTextRange = oTextBox.createTextRange();
			oTextRange.moveStart("character", sCurrentText.length);
			oTextRange.select();

			fireTextChange();
			fireSelectionChange( iIndex );
			return true;
		}
	}
	return false;
}

function changeStyle()
{
	// Changing width
	if ( event.propertyName == "width" ) {
		oSelectBox.style.pixelWidth = getAttribute( "width" );
		return;
	}

	if (event.propertyName.substring(0, 5) != 'style') {
		return;
	}
		
	var sValue;
	var oSpan = oTextBox.parentElement;

	switch ( event.propertyName ) {
		case "style.backgroundColor":
			sValue = style.getAttribute( "backgroundColor" );
			oTextBox.style.setAttribute("backgroundColor", sValue );
			oSelectBox.style.setAttribute("backgroundColor", sValue );
			break;

		case "style.color":
			sValue = style.getAttribute( "color" );
			oTextBox.style.setAttribute("color", sValue );
			oSelectBox.style.setAttribute("color", sValue );
			break;

		case "style.font":
			sValue = style.getAttribute( "font" );
			oTextBox.style.setAttribute("font", sValue );
			oSelectBox.style.setAttribute("font", sValue );
			break;

		case "style.letterSpacing":
			sValue = style.getAttribute( "letterSpacing" );
			oTextBox.style.setAttribute("letterSpacing", sValue );
			oSelectBox.style.setAttribute("letterSpacing", sValue );
			break;

		case "style.textAlign":
			sValue = style.getAttribute( "textAlign" );
			oTextBox.style.setAttribute("textAlign", sValue );
			oSelectBox.style.setAttribute("textAlign", sValue );
			break;
	}
}

function fireSelectionChange( iNewSelection )
{
    oEvent = createEventObject();
    oEvent.altKey = event.altKey;
	oEvent.altLeft = event.altLeft;
    oEvent.ctrlKey = event.ctrlKey;
	oEvent.ctrlLeft = event.ctrlLeft;
    oEvent.shiftKey = event.shiftKey;
	oEvent.shiftLeft = event.shiftLeft;
    oEvent.clientX = event.clientX;
	oEvent.clientY = event.clientY;
    oEvent.offsetX = event.offsetX;
	oEvent.offsetX = event.offsetY;
    oEvent.x = event.x;
	oEvent.y = event.y;
	oEvent.selectedIndex = iNewSelection;
	event_selectionchanged.fire( oEvent );
}

function fireTextChange()
{
	oEvent = createEventObject();
    oEvent.altKey = event.altKey;
	oEvent.altLeft = event.altLeft;
    oEvent.ctrlKey = event.ctrlKey;
	oEvent.ctrlLeft = event.ctrlLeft;
    oEvent.shiftKey = event.shiftKey;
	oEvent.shiftLeft = event.shiftLeft;
    oEvent.clientX = event.clientX;
	oEvent.clientY = event.clientY;
    oEvent.offsetX = event.offsetX;
	oEvent.offsetX = event.offsetY;
    oEvent.x = event.x;
	oEvent.y = event.y;
	event_textchanged.fire( oEvent );
}

function checkBrowser()
{
	var av=navigator.appVersion;

	// Check if IE and if so, check version
	var iMSIE=parseInt(av.indexOf("MSIE"));

	if (iMSIE>=1)
	{
		// Get major version and write appropriate style
		var iVer=parseInt(av.charAt(iMSIE+5));
		if ( iVer >= 6 ) {
			return 6;
		}
		if (iVer>=5)
		{
			return 5;
		}
	} 
	return 0;
}
 
