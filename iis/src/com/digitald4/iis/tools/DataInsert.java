package com.digitald4.iis.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Vendor;

public class DataInsert {
	
	public static void insertEnumed() throws Exception {
		for (GenData genData : GenData.values()) {
			genData.get();
		}
	}
	
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
				.addGeneralData(new GeneralData().setName("B/P").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Temp").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("RR").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("HR").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Height").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Weight").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("SOC/Recertification/FU").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Diagnosis").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Physician").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("MD Phone Number").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Last Visit").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Order Change").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Yes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("No").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Allergies").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		gd.addGeneralData(new GeneralData().setName("Behavioral Status").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Behavioral Status").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
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
				.addGeneralData(new GeneralData().setName("Memory").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Good").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Fair").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Poor").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Orientation").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Person").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Place").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Time").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Speech").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Slurred").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Garbled").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Aphasic").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		i = 1;
		gd.addGeneralData(new GeneralData().setName("Senses").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Hearing").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("R").setDescription("Right").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L").setDescription("Left").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Assistive Device").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Vision").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Glasses/Contacts").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Legally Blind").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Blurred/Double Vision").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Primary Language").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Cardiovascular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Cardiovascular").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Palpitations").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Angina").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Peripheral Pulse").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Present").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Absent").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Edema (Location/Amount)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Respiratory").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Shortness of Breath").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Exertion").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("At Rest").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Orthopnea").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Cough").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Non-Productive").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Productive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Sputum Character").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Lung Sounds").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Wheezes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Crackies").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Oxygen Therapy").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("NC").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L/PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Cont").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Trach").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("w/Cuff").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("TX").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("GI").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("GI").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
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
				.addGeneralData(new GeneralData().setName("Ostomy Type").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Bowel Sounds X4").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Feeding Tube Type").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Feeding Tube Site").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Endocrine").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Endocrine").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Diabetic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Thyroid Disease").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Indicate Treatment").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Finger Stick Glocose Precribed diet").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("GU").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("GU").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Frequency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Urgency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Burning").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Pain").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Retention").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Incontinence").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Vaginal Bleeding").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Catheter").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("External").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Indwelling").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("S/P").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Urine").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Cloudy").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Odor").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Hematuria").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Urine Color").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Neuromuscular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Neuromusular").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("ROM Loss").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("R").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("L").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Site").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Mobility Problems").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Amputation").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Contractures").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Sensation/Numbness/Tingling Location(s)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Describe").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("?").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("Headache").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Tremors").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Seizures").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Vertigo").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Ataxia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Episodes of Unconsciousness").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Integumetary").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Integumetary").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
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
				.addGeneralData(new GeneralData().setName("Unusual Color").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Wound Location(s)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Description").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Dressing Change (Indicate type/drain)").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Amount").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Pain").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Pain").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating at Rest").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Pain Rating w/Activity").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Describe Pain Quality").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Other Relief Measures").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Teaching").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Teaching").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Patient").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Primary Care Giver").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Subject").setData("{'type': 'TEXTAREA'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Response/Verification").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Verbal").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Demo").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Level of Understanding").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Partial").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Complete").setInGroupId(i++).setRank(i))
				)
		);
		gd.insert();
	}
	
	public static void insertLookUpData(GenData genD) throws Exception {
		insertLookUpData(genD, false);
	}
	public static void insertLookUpData(GenData genD, boolean perserveOrder) throws Exception {
		GeneralData gd = genD.get();
		if (gd == null) {
			gd = new GeneralData().setName(""+genD).setDescription(""+genD).setInGroupId(genD.getInGroupId());
		}
		else if (gd.getGeneralDatas().size() > 0) {
			System.out.println(genD+" already inserted. (Skipping)");
			return;
		}
		GeneralData currentParent = gd;
		BufferedReader br = new BufferedReader(new FileReader("data/"+genD.toString().toLowerCase()+".txt"));
		String line = br.readLine();
		while (line != null) {
			boolean setCurrent = false;
			line = line.trim();
			if (line.startsWith("]")) {
				currentParent = currentParent.getGroup();
				line = line.substring(1).trim();
			}
			if (line.endsWith("[")) {
				setCurrent = true;
				line = line.substring(0, line.length() - 1).trim();
			}
			if (line.length() > 0 && !line.startsWith("#")) {
				String name = null;
				String data = null;
				if (line.contains(",")) {
					name = line.substring(0, line.indexOf(',')).trim();
					data = line.substring(line.indexOf(',') + 1).trim();
				} else {
					name = line.trim();
				}
				GeneralData child = new GeneralData().setName(name).setDescription(name).setInGroupId(currentParent.getGeneralDatas().size() + 1).setData(data);
				if (perserveOrder) {
					child.setRank(child.getInGroupId());
				}
				currentParent.addGeneralData(child);
				if (setCurrent) {
					currentParent = child;
				}
			}
			line = br.readLine();
		}
		br.close();
		gd.insert();
	}
	
	private static void insertFirstUser() throws Exception {
		if (User.getAll().size() == 0) {
			new User()
					.setFirstName("Eddie")
					.setLastName("Mayfield")
					.setEmail("eddiemay@gmail.com")
					.setPassword("vxae11")
					.setType(GenData.UserType_Admin.get())
					.save();
		}
	}
	
	private static void insertVendors() throws Exception {
		if (Vendor.getAll().size() > 0) {
			System.out.println("Vendors alreay inserted. (Skipping)");
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader("data/vendors.txt"));
		String line = br.readLine();
		while (line != null) {
			if (!line.startsWith("#")) {
				StringTokenizer st = new StringTokenizer(line, ",");
				new Vendor().setName(st.nextToken().trim()).setBillingRate(Double.parseDouble(st.nextToken().trim())).insert();
			}
			line = br.readLine();
		}
		br.close();
	}
	
	public static void resetData(GenData gd) throws Exception {
		for (GeneralData generalData : new ArrayList<GeneralData>(gd.get().getGeneralDatas())) {
			generalData.delete();
		}
	}
	
	public static void outputData(GenData gd) throws JSONException, Exception {
		System.out.println(getOutputData(gd.get(), ""));
	}
	
	public static String getOutputData(GeneralData gd, String indent) {
		StringBuffer buffer = new StringBuffer(indent + gd);
		String data = gd.getData();
		if (data != null && data.length() > 0) {
			buffer.append(", " + data);
		}
		List<GeneralData> generalDatas = gd.getGeneralDatas();
		if (generalDatas.size() > 0) {
			buffer.append(" [\n");
			for (GeneralData generalData : generalDatas) {
				buffer.append(getOutputData(generalData, indent + "\t"));
			}
			buffer.append(indent + "]");
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
	public static JSONObject getJSON(GeneralData gd) throws JSONException, Exception {
		JSONArray jArray = new JSONArray();
		for (GeneralData generalData : gd.getGeneralDatas()) {
			jArray.put(getJSON(generalData));
		}
		JSONObject json = new JSONObject()
			.put("id", gd.getId())
			.put("name", gd.getName())
			.put("description", gd.getDescription())
			.put("data", gd.getData());
		if (jArray.length() > 0) {
			json.put("generalData", jArray);
		}
		return json;
	}
	
	public static void main(String[] args) throws Exception {
		EntityManagerHelper.init("DD4JPA", "org.gjt.mm.mysql.Driver",
	//			"jdbc:mysql://198.38.82.101/iisosnet_main?autoReconnect=true",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"iisosnet_user", "getSchooled85");
		User.setActiveUser(User.getInstance(1));
		//resetData(GenData.LICENSE);
		//resetData(GenData.ASS_CAT);
		/*insertLookUpData(GenData.DIANOSIS);
		insertLookUpData(GenData.IV_ACCESS);
		insertLookUpData(GenData.THERAPY_TYPE);
		insertLookUpData(GenData.PATIENT_STATE);
		insertLookUpData(GenData.LICENSE, true);*/
		insertAssCats();
		/*insertFirstUser();
		insertVendors();
		insertEnumed();*/
		//outputData(GenData.LICENSE);
		//outputData(GenData.ASS_CAT);
	}
}
