// number formatting function
// copyright Stephen Chapman 24th March 2006
// permission to use this function is granted provided
// that this copyright notice is retained intact

function formatCurrency(s) {
	num = s.value;
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
			num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));
	s.value = (((sign)?'':'-') + '$' + num);
	//s.value = num;
	
}

function formatCurrency2(s) {
	num = s.value;
	num = num.toString().replace(/\$|\,/g,'');
	if(isNaN(num))
		num = "0";
	sign = (num == (num = Math.abs(num)));
	num = Math.floor(num*100+0.50000000001);
	cents = num%100;
	num = Math.floor(num/100).toString();
	if(cents<10)
		cents = "0" + cents;
	for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)
		num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));
	//return (((sign)?'':'-') + '$' + num + '.' + cents);
	s.value= (((sign)?'':'-') + '$' + num + '.' + cents);
}

// (e.g.)formatNumber(mynum,2,' ','.','','','-','') 

function formatSSN(s){	
	q=s.value	
	if(q.length==3){		
		qq=q;		
		e5=q.indexOf('-')		
		if(e5==-1){
			qq=qq+"-";
		}		
		s.value="";
		s.value=qq;
	}
	if(q.length>3){
		
		e2=q.indexOf('-')
		if (e2==-1){
			w30=q.length;
			q30=q.substring(0,3);
		
			q30=q30+"-"
			q31=q.substring(3,w30);
			qq=q30+q31;
		
			s.value="";
			s.value=qq;
		}
	}
	if(q.length>5){
		q11=q.substring(0,e2);
		
		if(q11.length>3){
			q12=q11;
			w12=q12.length;
			w15=q.length
		
			q13=q11.substring(0,3);
			q14=q11.substring(3,w12);
			
			q15=q.substring(e2+1,w15);
			s.value="";
			qq=q13+"-"+q14+q15;
			s.value=qq;
		
		}
		
		w16=q.length;
		q16=q.substring(e2+1,w16);
		w17=q16.length;
		
		if(w17>1&&q16.indexOf('-')==-1){
			
			q17=q.substring(e2+1,e2+3);
			q18=q.substring(e2+3,w16);
			q19=q.substring(0,e2+1);
		
			qq=q19+q17+"-"+q18;
			s.value="";
			s.value=qq;
		
		}
	}
	
	setTimeout(formatSSN,100)
}

function formatPhone(r){
	
	p=r.value
	if(p.length==3){
		
		pp=p;
		d4=p.indexOf('(')
		d5=p.indexOf(')')
		if(d4==-1){
			pp="("+pp;
		}
		if(d5==-1){
			pp=pp+") ";
		}
		
		r.value="";
		r.value=pp;
	}
	if(p.length>3){
		d1=p.indexOf('(')
		d2=p.indexOf(')')
		if (d2==-1){
			l30=p.length;
			p30=p.substring(0,4);
			
			p30=p30+") "
			p31=p.substring(5,l30);
			pp=p30+p31;
			
			r.value="";
			r.value=pp;
		}
	}
	if(p.length>6){
		p11=p.substring(d1+1,d2);
		if(p11.length>3){
			p12=p11;
			l12=p12.length;
			l15=p.length
			
			p13=p11.substring(0,3);
			p14=p11.substring(4,l12);
			p15=p.substring(d2+1,l15);
			r.value="";
			pp="("+p13+") "+p14+p15;
			r.value=pp;			
		}
		l16=p.length;
		p16=p.substring(d2+2,l16);
		l17=p16.length;
		if(l17>2&&p16.indexOf('-')==-1){
			p17=p.substring(d2+2,d2+5);
			p18=p.substring(d2+5,l16);
			p19=p.substring(0,d2+2);
			
			pp=p19+p17+"-"+p18;
			r.value="";
			r.value=pp;			
			
		}
	}
	
	setTimeout(formatPhone,100)
}