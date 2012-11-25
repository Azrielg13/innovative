	var req = false;
	var idField;
	var idFieldOGColor;

	function oupdate(object,field,element,key1,key2,key3) {
		
		idField = document.getElementById(element);		
		idFieldOGColor = Fat.get_bgcolor(idField.name);	
		
		var url = "oupdate?object=" + escape(object)+"&field=" + escape(field)+"&value=" + escape(idField.value)+"&key=" + escape(key1)+"&key=" + escape(key2)+"&key=" + escape(key3);	
	
		if (window.XMLHttpRequest) {
			// Create XMLHttpRequest object in non-Microsoft browsers
			req = new XMLHttpRequest();

		} else if (window.ActiveXObject){

			try {
				// Try to create XMLHttpRequest in later versions of Internet Explorer
				req = new ActiveXObject("Msxml2.XMLHTTP");

			} catch (e1) {
				// Failed to create required ActiveXObject

				try {
					// Try version supported by older versions of Internet Explorer
					req = new ActiveXObject("Microsoft.XMLHTTP");

				} catch (e2) {
					// Unable to create an XMLHttpRequest with ActiveX
				}
			}
		}
	   req.open("GET", url, true);
	   req.onreadystatechange = callback;
	   req.send(null);
	   Fat.set_bgcolor(idField.name,'#ffff99')
	}

	function callback() { 			   
	   if (req.readyState == 4) {
			if (req.status == 200) {
				var message = req.responseXML.getElementsByTagName("error")[0];
				setMessage(message.childNodes[0].nodeValue);   					
			}
			else{
				setMessage("Error in transaction.");
			}
		}
	}

	function setMessage(message) {				
		mdiv = document.getElementById("errorMessage");
		if (message == "Valid") {			             					
			Fat.fade_element(idField.name,'30','1500','#ffff33',idFieldOGColor);
		} else {
			mdiv.innerHTML = "<div style=\"color:red\">"+message+"</ div>";
			Fat.fade_element(idField.name,'30','1500','#ffff33','#ff0000');
		}			    
	}
			