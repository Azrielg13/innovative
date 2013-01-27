package com.digitald4.iis.tools;

import com.digitald4.common.model.GeneralData;
import com.digitald4.iis.model.GenData;

public class DataInsert {
	public static void insertAssCats() throws Exception {
		GeneralData gd = GenData.ASS_CAT.get();
		if (gd.getGeneralDatas().size() > 0) {
			System.out.println("Assessement Catogories Already inserted. (Skipping)");
			return;
		}
		int c = 1;
		int g = 1;
		int i = 1;
		gd.addGeneralData(new GeneralData().setName("Behavioral Status").setDescription("Behavioral Status").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName("Status").setDescription("Behavioral Status").setData("MULTI_CHECK").setInGroupId(g++).setRank(g)
						.addGeneralData(new GeneralData().setName("NPA").setDescription("NPA").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Alert").setDescription("Alert").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Anxious").setDescription("Anxious").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Lethargic").setDescription("Lethargic").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Depressed").setDescription("Depressed").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Restless").setDescription("Restless").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Flat Effect").setDescription("Flat Effect").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Confused").setDescription("Confused").setInGroupId(i++).setRank(i))
						.addGeneralData(new GeneralData().setName("Unresponsive").setDescription("Unresponsive").setInGroupId(i++).setRank(i))
				)
				.addGeneralData(new GeneralData().setName("Memory").setDescription("Memory").setData("RADIO").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Orientation").setDescription("Orientation").setData("RADIO").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Speech").setDescription("Speech").setData("RADIO").setInGroupId(g++).setRank(g))
				.addGeneralData(new GeneralData().setName("Comments").setDescription("Comments").setData("TEXT").setInGroupId(g++).setRank(g))
		);
		g = 1;
		i = 1;
		gd.addGeneralData(new GeneralData().setName("Hearing").setDescription("Hearing").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData()));
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Cardiovascular").setDescription("Cardiovascular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Respiratory").setDescription("Respiratory").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("G.I.").setDescription("G.I.").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Endocrine").setDescription("Endocrine").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("GU").setDescription("GU").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Neuromuscular").setDescription("Neuromuscular").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Integumetary").setDescription("Integumetary").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Pain").setDescription("Pain").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
		g = 1;
		gd.addGeneralData(new GeneralData().setName("Teaching").setDescription("Teaching").setInGroupId(c++).setRank(c)
				.addGeneralData(new GeneralData().setName(name).setDescription("").setInGroupId(g++).setRank(g).setData());
	}
}
