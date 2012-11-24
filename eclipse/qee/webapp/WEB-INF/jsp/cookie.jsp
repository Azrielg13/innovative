<link rel="stylesheet" type="text/css" href="report.css" />
<p>Joseph K. Myers<br />
Thursday, January 5, 2006</p>

<head>

<script type="text/javascript" src="cookie.js"></script>

</head>

<%
String username = request.getParameter("username");
if(username == null || username.length() == 0)
	username = "";

String remember = request.getParameter("remember");
if(remember == null || remember.length() == 0)
	remember = "0";

%>


<body onload="readCookieSetInput('login','username');<%=(remember.equals("1")?"makeCookie('login', '"+username+"', { domain: 'mdi.sce.com', expires: 10 });":"")%>">

<form name="login" action="/ajax/cookie.jsp" method="post">
	Login <input type="textbox" name="username" value="" /> (Request: <%=username%>)
	<br/><input type="submit" value="submit"/> 
	<br/><input type="checkbox" value="1" name="remember" checked/> Remember Me (Request: <%=remember%>)
	<br/>
	<br/><input type="button" value="Create Cookie" onclick="idField = document.getElementById('username');idField.value=makeCookie('login', '<%=username%>', { expires: 10 })"/> 
	<br/><input type="button" value="Get Cookie" onclick="readCookieSetInput('login','username')"/> 
	<br/><input type="button" value="Get Cookie 2" onclick="idField = document.getElementById('username');idField.value=readCookie('login');"/> 
	<br/><input type="button" value="Get Time" onclick="idField = document.getElementById('username');idField.value=reldate('100');"/> 
	
</form>
</body>





<h1>JavaScript Cookies</h1>

<p>The JavaScript Cookies provide a complete set of cookie functions. Designed for usability, they make all the details, like setting expiration dates, easy and fast.</p>

<p>JavaScript functions makeCookie(), readCookie(), and rmCookie() provide the best solution for writing, reading, and removing cookies. Any JavaScript application now has the capability to access cookies and utilize all of their magical properties. The cookie functions provide high performance, and each one provides return values precisely indicating the success or failure of the requested operation. They allow you to use relative expiration dates, to easily let a cookie expire in a few seconds, hours, days, or years from the present time, or to create a "session cookie" that safely vanishes when the user quits the browser. The cookie functions also allow you to choose the domain and path for cookies, or to create secure cookies that are accessible only through encrypted pages.</p>

<div class="article">
<h2>Introduction</h2>

<p>Ordinarily, we could make up our own new attributes of window or document objects and use them to store information, but nothing would happen.</p>

<p>If we set the property of window.IggaBigga = 71, then this imaginary property will stay defined until the browser loads some other document and destroys our script. (It should stay defined because the window is a JavaScript object, and objects can be assigned new properties.)</p>

<p>However, since a window also corresponds to an actual object that the user is interacting with, some properties have special effects. When we assign to them, a magical command will occur, and not just an invisible assignment of memory.</p>

<p>We wouldn't expect to have access to HTTP cookies like a webserver does. How could a pure JavaScript, without a fake addition of some ActiveX control, be able to store something on a user's computer?</p>

<p>But with the magical command capability in mind, there is some hope that the document or the window object will give us a special property. Values given to this property will store cookies, and values read from this property will return cookie values to us.</p>

<p>This property is there, and it is called document.cookie.</p>

<p>Since it's a magical thing, after we put a value into document.cookie, document.cookie will not be equal to that value. Putting something into document.cookie actually sends it to the browser for storage along with any other HTTP cookies. When it is created, an HTTP cookie has to have a special format that indicates the name and value, along with the expiration date and other parameters.</p>

<p>Reading document.cookie won't give us back this formatted string, but a list of all the HTTP* cookies stored for the current document.</p>

<div class="comment"><p>*Since HTTP was the method which defined these cookies, cookies for HTTP are those a browser normally stores. In concept, there could be FTP cookies, or cookies reserved only for the use of JavaScript. In fact, such things would be very useful and helpful. Cookies stored for JavaScript would not need to slow down the web requests anymore. There is already a mechanism for restricting cookies to a certain protocol, HTTPS. Unfortunately, the present way of doing this is not good, as we briefly mention in the source code. The keyword used for this is should be 'protocol=secure.' Simply by adding one more keyword, 'protocol=javascript,' we could improve efficiency for many web applications.)</p>
</div>

<p>Before it is magically stored in document.cookie, a cookie string might be formatted like this:</p>

<pre>cookie=hello; expires=Thu, 05 Jan 2006 19:36:27 GMT
</pre>

<p>Someone who knows the format of HTTP cookie strings sent to a browser, and how to read the string of cookies received from a browser, would be able to write JavaScript functions to make cookies and read cookies. They would be able to offer a more friendly interface, even though the magical properties of document.cookie would still be accessed behind the scenes.</p>


<h2>JavaScript interface to cookies</h2>

<p>The interface would be this:</p>

<div class="cutting"><pre>makeCookie('name', 'value', { properties });
</pre>
<p>name, value are strings, properties are an optional object. The return value is true if storing the cookie was successful, otherwise false.</p>
<pre>readCookie('name');
</pre>
<p>name is a string. The return value is a string or null.</p>
<pre>rmCookie('name');
</pre>
<p>name is a string. The return value is true if the cookie was deleted, otherwise false.</p>
</div>

<h2>Examples of using cookies</h2>

<p>For examples:</p>

<pre>makeCookie('color', 'silver');
</pre>

<p>This saves a cookie indicating that the color is silver. The cookie would expire after the current session (as soon as the user quits the browser).</p>

<pre>makeCookie('color', 'green', { domain: 'gardens.home.com' });
</pre>

<p>This saves the color green for gardens.home.com.</p>

<pre>makeCookie('color', 'white', { domain: '.home.com', path: '/invoices' });
makeCookie('invoiceno', '0259876', { path: '/invoices', secure: 1 });
</pre>

<p>saves the color white for invoices viewed anywhere at home.com. The second cookie is a secure cookie, and records an invoice number. This cookie will be sent only to pages that are viewed through secure HTTPS connections, and scripts within secure pages are the only scripts allowed to access the cookie.</p>

<p>One HTTP host is not allowed to store or read cookies for another HTTP host. Thus, a cookie domain must be stored with at least two periods. By default, the domain is the same as the domain of the web address which created the cookie.</p>

<p>The path of an HTTP cookie restricts it to certain files on the HTTP host. Some browsers use a default path of /, so the cookie will be available on the whole host. Other browsers use the whole filename. In this case, if /invoices/overdue.cgi creates a cookie, only /invoices/overdue.cgi is going to get the cookie back.</p>

<div class="comment"><p>When setting paths and other parameters, they are usually based on data obtained from variables like location.href, etc. These strings are already escaped, so when the cookie is created, the cookie function does not escape these values again. Only the name and value of the cookie are escaped, so we can conveniently use arbitrary names or values. Some browsers limit the total size of a cookie, or the total number of cookies which one domain is allowed to keep.</p>
</div>

<pre>makeCookie('rememberemail', 'yes', { expires: 7 });
makeCookie('rememberlogin', 'yes', { expires: 1 });
makeCookie('allowentergrades', 'yes', { expires: 1/24 });
</pre>

<p>these cookies would remember the user's email for 7 days, the user's login for 1 day, and allow the user to enter grades without a password for 1 hour (a twenty-fourth of a day). These time limits are obeyed even if they quit the browser, and even if they don't quit the browser. Users are free to use a different browser program, or to delete cookies. If they do this, the cookies will have no effect, regardless of the expiration date.</p>

<pre>makeCookie('rememberlogin', 'yes', { expires: -1 });
</pre>

<p>deletes the cookie. The cookie value is superfluous, and the return value false means that deletion was successful. (A expiration of -1 is used instead of 0. If we had used 0, the cookie might be undeleted until one second past the current time. In this case we would think that deletion was unsuccessful.)</p>

<div class="comment"><p>Obviously, since a cookie can be deleted in this way, a new cookie will also overwrite any value of an old cookie which has the same name, including the expiration date, etc. However, cookies for completely non-overlapping paths or domains are stored separately, and the same names do not interfere with each other. But in general, any path or domain which has access to a cookie can overwrite the cookie, no matter whether or not it changes the path or domain of the new cookie.</p>
</div>

<pre>rmCookie('rememberlogin');
</pre>

<p>also deletes the cookie, by doing makeCookie('rememberlogin', '', { expires: -1 }). This makes the cookie code longer, but saves code for people who use it, which one might think saves more code in the long run.</p>

<h2>JavaScript cookie source code</h2>

<p>The code that does this is short, with a few more comments and notes.</p>

<pre class="cutting">/* JKM, cookies, cookies-jkm.js, 1-5-06 */

function reldate(days) {
var d;
d = new Date();

/* We need to add a relative amount of time to
the current date. The basic unit of JavaScript
time is milliseconds, so we need to convert the
days value to ms. Thus we have
ms/day
= 1000 ms/sec *  60 sec/min * 60 min/hr * 24 hrs/day
= 86,400,000. */

d.setTime(d.getTime() + days*86400000);
return d.toGMTString();
}

function readCookie(name) {
var s = document.cookie, i;
if (s)
for (i=0, s=s.split('; '); i<s.length; i++) {
s[i] = s[i].split('=', 2);
if (unescape(s[i][0]) == name)
return unescape(s[i][1]);
}
return null;
}

function makeCookie(name, value, p) {
var s, k;
s = escape(name) + '=' + escape(value);
if (p) for (k in p) {

/* convert a numeric expires value to a relative date */

if (k == 'expires')
p[k] = isNaN(p[k]) ? p[k] : reldate(p[k]);

/* The secure property is the only special case
here, and it causes two problems. Rather than
being '; protocol=secure' like all other
properties, the secure property is set by
appending '; secure', so we have to use a
ternary statement to format the string.

The second problem is that secure doesn't have
any value associated with it, so whatever value
people use doesn't matter. However, we don't
want to surprise people who set { secure: false }.
For this reason, we actually do have to check
the value of the secure property so that someone
won't end up with a secure cookie when
they didn't want one. */

if (p[k])
s += '; ' + (k != 'secure' ? k + '=' + p[k] : k);
}
document.cookie = s;
return readCookie(name) == value;
}

function rmCookie(name) {
return !makeCookie(name, '', { expires: -1 });
}

</pre>

<h2>How to put the cookie functions in your page</h2>

<p>To use the functions in your page, you'll also need to have a copy of cookies-jkm.js inside of your page. You do this by uploading the cookies-jkm.js to your website, and then putting this HTML into your page.</p>

<pre class="cutting"><script type="text/javascript" src="cookies-jkm.js">
</script>
</pre>

<p>If you'd like, it would be nice to let people know where to find the script, by including a link to the place where you found the script. If you don't know where this was, the original address of the script is at the end of this page.</p>

<h2>Download</h2>

<p><a href="cookies.zip">cookies.zip</a> (10.2 KB)</p>

<h2>Contact</h2>

<p>Email Joseph Myers, jkm-2006.jan @ codelib.net, or jkm-year.month @ codelib.net in other months, or cookies @ users.sourceforge.net.</p>

</div>

<p>http://www.codelib.net/javascript/cookies.html</p>


