package com.digitald4.iis.tools;

import com.digitald4.common.model.GeneralData;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.storage.GeneralDataStore;
import com.digitald4.common.storage.Query;
import com.digitald4.common.storage.Query.OrderBy;
import com.digitald4.common.storage.QueryResult;
import java.util.regex.Pattern;

public class FixGenDataNames {
  private static final Pattern CONST_CASE = Pattern.compile("([A-Z_]+)");
  public static void main(String[] args) throws Exception {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1"));
    GeneralDataStore generalDataStore = new GeneralDataStore(() -> dao);
    QueryResult<GeneralData> result =
        generalDataStore.list(Query.forList().setOrderBys(OrderBy.of("name")).setLimit(500));
    System.out.println(result.getItems().size() + " of " + result.getTotalSize() + " fetched");
    result.getItems().forEach(generalData -> {
      System.out.printf("%s%d - %s%s\n",
          generalData.getGroupId() < 1000 ? "0" : "",
          generalData.getGroupId(),
          generalData.getName(),
          CONST_CASE.matcher(generalData.getName()).matches() ? "  ----> needs to be renamed" : "");
    });
  }
}
