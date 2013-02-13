package com.digitald4.iis.tools;

import java.io.BufferedReader;
import java.io.FileReader;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.GenData;

public class DataInsert {
	public static void insertAssCats() throws Exception {
		GeneralData gd = GenData.ASS_CAT.get();
		if (gd == null) {
			gd = new GeneralData().setName("ASS_CAT").setDescription("Assessment Categories").setInGroupId(GenData.ASS_CAT.getInGroupId());
		}
		else if (gd.getGeneralDatas().size() > 0) {
			System.out.println("Assessement Categories Already inserted. (Skipping)");
			return;
		}
		int c = 1;
		int g = 1;
		int i = 1;
		gd.addGeneralData(new GeneralData().setName("Vital Signs").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Blood Pressure").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Temp").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("RR").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("HR").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Height").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Weight").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("SOC/Recertification/FU").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Diagnosis").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Physician").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("MD Phone Number").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Last Visit").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Order Change").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Yes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("No").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Allergies").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		gd.addGeneralData(new GeneralData().setName("Behavioral Status").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Behavioral Status").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Alert").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Anxious").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Lethargic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Depressed").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Restless").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Flat Effect").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Confused").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Unresponsive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Memory").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Good").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Fair").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Poor").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Orientation").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Person").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Place").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Time").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Speech").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Slurred").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Garbled").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Aphasic").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		i = 1;
		gd.addGeneralData(new GeneralData().setName("Senses").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Hearing").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("R").setDescription("Right").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L").setDescription("Left").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Assistive Device").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Vision").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Glasses/Contacts").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Legally Blind").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Blurred/Double Vision").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Primary Language").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Cardiovascular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Cardiovascular").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Palpitations").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Angina").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Peripheral Pulse").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Present").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Absent").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Edema (Location/Amount)").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Respiratory").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Shortness of Breath").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Exertion").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("At Rest").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Orthopnea").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Cough").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Non-Productive").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Productive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Sputum Character").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Lung Sounds").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Wheezes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Crackies").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Oxygen Therapy").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("NC").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L/PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Cont").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Trach").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("w/Cuff").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("TX").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("GI").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("GI").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Nausea").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Vomiting").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Anorexia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Dysphagia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Bleeding").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Pain").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Ascites").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Constipation").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diarrhea").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Distention").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Ostomy Type").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Bowel Sounds X4").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Feeding Tube Type").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Feeding Tube Site").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Endocrine").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Endocrine").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diabetic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Thyroid Disease").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Indicate Treatment").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Finger Stick Glocose Precribed diet").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("GU").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("GU").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Frequency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Urgency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Burning").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Pain").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Retention").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Incontinence").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Vaginal Bleeding").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Catheter").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("External").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Indwelling").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("S/P").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Urine").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Cloudy").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Odor").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Hematuria").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Urine Color").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Neuromuscular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Neuromusular").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("ROM Loss").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("R").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Site").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Mobility Problems").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Amputation").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Contractures").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Sensation/Numbness/Tingling Location(s)").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Describe").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("?").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Headache").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Tremors").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Seizures").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Vertigo").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Ataxia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Episodes of Unconsciousness").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Integumetary").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Integumetary").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Rash").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Jaundice").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Poor Turgor").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Pruritis").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Brusing").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Lesions").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Staples").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Sutures").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Incision").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Unusual Color").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Wound Location(s)").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Description").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Dressing Change (Indicate type/drain)").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Amount").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Pain").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Pain").setData("ACK_TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating at Rest").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating w/Activity").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Describe Pain Quality").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Other Relief Measures").setData("TEXT").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Teaching").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Teaching").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Patient").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Primary Care Giver").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Subject").setData("TEXTAREA").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Response/Verification").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Verbal").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Demo").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Level of Understanding").setData("RADIO").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Partial").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Complete").setInGroupId(i++).setRank(i))
				)
		);
		gd.insert();
	}
	public static void insertLookUpData(GenData genD) throws Exception {
		GeneralData gd = genD.get();
		if (gd == null) {
			gd = new GeneralData().setName(""+genD).setDescription(""+genD).setInGroupId(genD.getInGroupId());
		}
		else if (gd.getGeneralDatas().size() > 0) {
			System.out.println(genD+" already inserted. (Skipping)");
			return;
		}
		int c = 1;
		BufferedReader br = new BufferedReader(new FileReader("data/"+genD.toString().toLowerCase()+".txt"));
		String line = br.readLine();
		while(line != null) {
			line = line.trim();
			if (line.length() > 0 && !line.startsWith("#")) {
				gd.addGeneralData(new GeneralData().setName(line).setDescription(line).setInGroupId(c++));
			}
			line = br.readLine();
		}
		br.close();
		gd.insert();
	}
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver", "jdbc:mysql://142.129.252.255/iis?autoReconnect=true", "iis", "webpass");
		insertAssCats();
		insertLookUpData(GenData.DIANOSIS);
		insertLookUpData(GenData.VENDORS);
		insertLookUpData(GenData.RX);
		insertLookUpData(GenData.IV_ACCESS);
		insertLookUpData(GenData.THERAPY_TYPE);
		insertLookUpData(GenData.PATIENT_STATE);
	}
}
