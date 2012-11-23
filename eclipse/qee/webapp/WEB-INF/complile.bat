cd classes
for %%j in (com/digitald4/order/*.java) do javac -d ../../WEB-INF/classes/ com/digitald4/order/%%j
for %%j in (com/digitald4/order/servlet/*.java) do javac -d ../../WEB-INF/classes/ com/digitald4/order/servlet/%%j
pause