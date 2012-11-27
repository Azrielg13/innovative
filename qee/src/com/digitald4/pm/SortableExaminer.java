package com.digitald4.pm;
import java.util.Hashtable;
public class SortableExaminer implements Comparable<Object>{
	private User examiner;
	private double pMatch;
	public SortableExaminer(User examiner, Hashtable<String,Exam> reqExams)throws Exception{
		this.examiner = examiner;
		calcPercentMatch(reqExams);
	}
	public User getExaminer(){
		return examiner;
	}
	public void setPercentMatch(double pMatch){
		this.pMatch = pMatch;
	}
	public double getPercentMatch(){
		return pMatch;
	}
	public void calcPercentMatch(Hashtable<String,Exam> reqExams)throws Exception{
		if(reqExams.size() > 0){
			int matchCount=0;
			for(int ee=0; ee<examiner.getExams().size(); ee++){
				if(reqExams.containsKey(""+examiner.getExams().get(ee).getId()))
					matchCount++;
			}
			pMatch = matchCount*100.0/reqExams.size();
		}
	}
	public int compareTo(Object o){
		if(o instanceof SortableExaminer){
			SortableExaminer se = (SortableExaminer)o;
			if(getPercentMatch() > se.getPercentMatch())
				return -1;
			else if(getPercentMatch() < se.getPercentMatch())
				return 1;
			else
				return getExaminer().compareTo(se.getExaminer());
		}
		return 0;
	}
	public String toString(){
		return examiner+" "+pMatch;
	}
}