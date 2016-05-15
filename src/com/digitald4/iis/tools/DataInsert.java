package com.digitald4.iis.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.digitald4.common.jpa.EntityManagerHelper;
import com.digitald4.common.model.GeneralData;
import com.digitald4.common.model.User;
import com.digitald4.iis.model.GenData;
import com.digitald4.iis.model.Vendor;

public class DataInsert {
	
	public static void insertEnumed(EntityManager entityManager) throws Exception {
		for (GenData genData : GenData.values()) {
			genData.get(entityManager);
		}
	}
	
	public static void insertAssCats(EntityManager entityManager) throws Exception {
		GeneralData gd = GenData.ASS_CAT.get(entityManager);
		if (gd == null) {
			gd = new GeneralData(entityManager).setName("ASS_CAT").setDescription("Assessment Categories").setInGroupId(GenData.ASS_CAT.getInGroupId());
		}
		else if (gd.getGeneralDatas().size() > 0) {
			System.out.println("Assessement Categories Already inserted. (Skipping)");
			return;
		}
		int c = 1;
		int g = 1;
		int i = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Vital Signs").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("B/P").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Temp").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("RR").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("HR").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Height").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Weight").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("SOC/Recertification/FU").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Diagnosis").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Physician").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("MD Phone Number").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Last Visit").setData("{'type': 'TEXT', 'copies': false}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Order Change").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Yes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("No").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Allergies").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		gd.addGeneralData(new GeneralData(entityManager).setName("Behavioral Status").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Behavioral Status").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Alert").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Anxious").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Lethargic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Depressed").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Restless").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Flat Effect").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Confused").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Unresponsive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Memory").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Good").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Fair").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Poor").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Orientation").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Person").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Place").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Time").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Speech").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Slurred").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Garbled").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Aphasic").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		i = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Senses").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Hearing").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("R").setDescription("Right").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("L").setDescription("Left").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Assistive Device").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Vision").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Glasses/Contacts").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Legally Blind").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Blurred/Double Vision").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Primary Language").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Cardiovascular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Cardiovascular").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Palpitations").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Angina").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Peripheral Pulse").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Present").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Absent").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Edema (Location/Amount)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Respiratory").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Shortness of Breath").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Exertion").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("At Rest").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Orthopnea").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Cough").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Non-Productive").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Productive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Sputum Character").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Lung Sounds").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Diminished").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Wheezes").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Crackies").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Oxygen Therapy").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("NC").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("L/PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Cont").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("PM").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Trach").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("w/Cuff").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("TX").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("GI").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("GI").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Nausea").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Vomiting").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Anorexia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Dysphagia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Bleeding").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Pain").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Ascites").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Constipation").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Diarrhea").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Distention").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Ostomy Type").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Bowel Sounds X4").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Feeding Tube Type").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Feeding Tube Site").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Endocrine").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Endocrine").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Diabetic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Thyroid Disease").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Other").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Indicate Treatment").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Finger Stick Glocose Precribed diet").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("GU").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("GU").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Frequency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Urgency").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Burning").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Pain").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Retention").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Incontinence").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Vaginal Bleeding").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Catheter").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("External").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Indwelling").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("S/P").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Urine").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Clear").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Cloudy").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Odor").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Hematuria").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Urine Color").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Neuromuscular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Neuromusular").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("ROM Loss").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("R").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("L").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Site").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Mobility Problems").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Amputation").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Contractures").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Sensation/Numbness/Tingling Location(s)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Describe").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("?").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("Headache").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Tremors").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Seizures").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Vertigo").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Ataxia").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Episodes of Unconsciousness").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Integumetary").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Integumetary").setData("{'type': 'MULTI_CHECK'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Rash").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Jaundice").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Poor Turgor").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Pruritis").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Brusing").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Lesions").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Staples").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Sutures").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Incision").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Unusual Color").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Wound Location(s)").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Description").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Dressing Change (Indicate type/drain)").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Amount").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Pain").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Pain").setData("{'type': 'ACK_TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Pain Rating").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Pain Rating at Rest").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Pain Rating w/Activity").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Describe Pain Quality").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Other Relief Measures").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Comments").setData("{'type': 'TEXT'}").setInGroupId(g++).setRank(g))
		);		
		g = 1;
		gd.addGeneralData(new GeneralData(entityManager).setName("Teaching").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData(entityManager).setName("Teaching").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Patient").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Primary Care Giver").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Subject").setData("{'type': 'TEXTAREA'}").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData(entityManager).setName("Response/Verification").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Verbal").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Demo").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData(entityManager).setName("Level of Understanding").setData("{'type': 'RADIO'}").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData(entityManager).setName("None").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Partial").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData(entityManager).setName("Complete").setInGroupId(i++).setRank(i))
				)
		);
		gd.insert();
	}
	
	public static void insertLookUpData(EntityManager entityManager, GenData genD) throws Exception {
		insertLookUpData(entityManager, genD, false);
	}
	public static void insertLookUpData(EntityManager entityManager, GenData genD, boolean perserveOrder) throws Exception {
		GeneralData gd = genD.get(entityManager);
		if (gd == null) {
			gd = new GeneralData(entityManager).setName(""+genD).setDescription(""+genD).setInGroupId(genD.getInGroupId());
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
				GeneralData child = new GeneralData(entityManager).setName(name).setDescription(name).setInGroupId(currentParent.getGeneralDatas().size() + 1).setData(data);
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
	
	@SuppressWarnings("unused")
	private static void insertFirstUser(EntityManager entityManager) throws Exception {
		if (User.getAll(entityManager).size() == 0) {
			new User(entityManager)
					.setFirstName("Eddie")
					.setLastName("Mayfield")
					.setEmail("eddiemay@gmail.com")
					.setPassword("vxae11")
					.setType(GenData.UserType_Admin.get(entityManager))
					.save();
		}
	}
	
	@SuppressWarnings("unused")
	private static void insertVendors(EntityManager entityManager) throws Exception {
		if (Vendor.getAll(Vendor.class, entityManager).size() > 0) {
			System.out.println("Vendors alreay inserted. (Skipping)");
			return;
		}
		BufferedReader br = new BufferedReader(new FileReader("data/vendors.txt"));
		String line = br.readLine();
		while (line != null) {
			if (!line.startsWith("#")) {
				StringTokenizer st = new StringTokenizer(line, ",");
				new Vendor(entityManager).setName(st.nextToken().trim()).setBillingRate(Double.parseDouble(st.nextToken().trim())).insert();
			}
			line = br.readLine();
		}
		br.close();
	}
	
	public static void resetData(EntityManager entityManager, GenData gd) throws Exception {
		for (GeneralData generalData : new ArrayList<GeneralData>(gd.get(entityManager).getGeneralDatas())) {
			generalData.delete();
		}
	}
	
	public static void outputData(EntityManager entityManager, GenData gd) throws Exception {
		System.out.println(getOutputData(gd.get(entityManager), ""));
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
		EntityManager entityManager = EntityManagerHelper.getEntityManagerFactory("org.gjt.mm.mysql.Driver",
	//			"jdbc:mysql://198.38.82.101/iisosnet_main?autoReconnect=true",
				"jdbc:mysql://localhost/iisosnet_main?autoReconnect=true",
				"iisosnet_user", "getSchooled85").createEntityManager();
		User.setActiveUser(User.getInstance(entityManager, 1));
		//resetData(GenData.LICENSE);
		//resetData(GenData.ASS_CAT);
		/*insertLookUpData(GenData.DIANOSIS);
		insertLookUpData(GenData.IV_ACCESS);
		insertLookUpData(GenData.THERAPY_TYPE);
		insertLookUpData(GenData.PATIENT_STATE);
		insertLookUpData(GenData.LICENSE, true);*/
		insertAssCats(entityManager);
		/*insertFirstUser();
		insertVendors();
		insertEnumed();*/
		//outputData(GenData.LICENSE);
		//outputData(GenData.ASS_CAT);
	}
}
