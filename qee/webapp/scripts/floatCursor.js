
/*
Roaming Cursor script (By Kurt at kurt.grigg@virgin.net)
Modified and permission granted to Dynamic Drive to feature script in archive
For full source, usage terms, and 100's more DHTML scripts, visit http://dynamicdrive.com
*/

//specify path to cursor image
var cursorpath="img/cursor.gif"

if (document.layers)
{document.write("<LAYER NAME='PoInTeRs' LEFT=10 TOP=10><img src='"+cursorpath+"' width=17 height=22></LAYER>")}
else if (document.all){document.write("<div id='pOiNtErS' style='position:absolute;top:10px;left:10px;width:17px;height:22px;z-index:50'><img src='"+cursorpath+"' width=17 height=22></div>")}

count=-1;                                                     
move=1;

function Curve(){
abc=new Array(0,1,1,1,2,3,4,0,6,-1,-1,-1,-2,-3,-4,0,-6)
for (i=0; i < abc.length; i++)
{var C=Math.round(Math.random()*[i])}
howbend=abc[C];
setTimeout('Curve()',1900);
return howbend;
}
ypos=10;
xpos=10;

degree = 60;
function MoveRandom(){
PathBend=degree+=howbend;//ok!
y = 4*Math.sin(PathBend*Math.PI/180);
x = 6*Math.cos(PathBend*Math.PI/180);
if (document.layers){
ypos+=y;
xpos+=x;
document.PoInTeRs.top=ypos+window.pageYOffset;
document.PoInTeRs.left=xpos+window.pageXOffset;
}
else if (document.all){
ypos+=y;
xpos+=x;
document.all.pOiNtErS.style.top=ypos+document.body.scrollTop;
document.all.pOiNtErS.style.left=xpos+document.body.scrollLeft;
}
T=setTimeout('MoveRandom()',50);
}
function edges(){
if (document.layers){
if (document.PoInTeRs.left >= window.innerWidth-40+window.pageXOffset)degree=Math.round(Math.random()*45+157.5);
if (document.PoInTeRs.top >= window.innerHeight-30+window.pageYOffset)degree=Math.round(Math.random()*45-112.5);
if (document.PoInTeRs.top <= 2+window.pageYOffset) degree = Math.round(Math.random()*45+67.5);//OK!
if (document.PoInTeRs.left <= 2+window.pageXOffset) degree = Math.round(Math.random()*45-22.5);//OK!
}
else if (document.all)
{
if (document.all.pOiNtErS.style.pixelLeft >= document.body.offsetWidth-45+document.body.scrollLeft)degree=Math.round(Math.random()*45+157.5);
if (document.all.pOiNtErS.style.pixelTop >= document.body.offsetHeight-35+document.body.scrollTop)degree=Math.round(Math.random()*45-112.5);
if (document.all.pOiNtErS.style.pixelTop <= 2+document.body.scrollTop) degree = Math.round(Math.random()*45+67.5);//OK!
if (document.all.pOiNtErS.style.pixelLeft <= 2+document.body.scrollLeft) degree = Math.round(Math.random()*45-22.5);//OK!
}
setTimeout('edges()',100);
}
function starteffect(){
Curve();
MoveRandom();// onUnload="opener.gO()"
edges();
}

if (document.all||document.layers)
window.onload=starteffect
