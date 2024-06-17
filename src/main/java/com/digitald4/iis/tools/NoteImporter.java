package com.digitald4.iis.tools;

import static com.digitald4.iis.tools.DataImporter.parseDate;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static java.util.Arrays.stream;
import static java.util.function.Function.identity;

import com.digitald4.common.exception.DD4StorageException;
import com.digitald4.common.server.APIConnector;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.DAOApiImpl;
import com.digitald4.common.util.Calculate;
import com.digitald4.common.util.FormatText;
import com.digitald4.iis.model.Employee;
import com.digitald4.iis.model.Note;
import com.digitald4.iis.model.Nurse;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class NoteImporter implements DataImporter<Note> {
  private final ImmutableMap<Long, Employee> employeesMap;

  public NoteImporter(EmployeeImporter employeeImporter) {
    this.employeesMap = employeeImporter.process().stream().collect(toImmutableMap(Employee::getId, identity()));
  }

  @Override
  public NoteImporter setColumnNames(String line) {
    throw new UnsupportedOperationException("Unsupported");
  }

  @Override
  public ImmutableList<Note> process() {
    return process(stream(new File("data/").list()).filter(f -> f.startsWith("Note - All Time")).map(f -> "data/" + f)
        .max(Comparator.comparing(Objects::toString)).orElseThrow());
  }

  @Override
  public ImmutableList<Note> process(String filePath) {
    AtomicInteger lineNum = new AtomicInteger(1);
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      StringBuilder content = new StringBuilder();
      String line;
      while ((line = br.readLine()) != null) {
        content.append(line).append("\n");
        lineNum.incrementAndGet();
      }

      lineNum.set(1);
      AtomicLong idSeq = new AtomicLong();
      return Calculate.jsonFromCSV(content.toString()).stream()
          .peek(n -> lineNum.incrementAndGet())
          .map(this::parse)
          .sorted(Comparator.comparing(Note::getCreationTime))
          .peek(note -> note.setId(idSeq.incrementAndGet()))
          .collect(toImmutableList());
    } catch (Exception e) {
      throw new DD4StorageException("Error reading file: " + filePath + " line: #" + lineNum, e);
    }
  }

  @Override
  public Note parse(String line) throws ParseException {
    throw new UnsupportedOperationException("Unsupported");
  }

  public Note parse(JSONObject json) {
    String noteType = json.getString("Client/Employee Note");
    long id = Long.parseLong(
        "Client Note".equals(noteType) ? json.getString("Internal Id_1") : json.getString("Internal Id_2"));
    String entityType = switch (noteType) {
      case "Client Note" -> "Patient";
      case "Employee Note" -> employeesMap.get(id) instanceof Nurse ? "Nurse" : "User";
      default -> throw new IllegalArgumentException("Unknown Entity Type: " + noteType);
    };
    try {
      return (Note) new Note()
          .setEntityType(entityType)
          .setEntityId(String.valueOf(id))
          .setStatus(Note.Status.valueOf(FormatText.toCapitalized(json.getString("Status"))))
          .setType(Note.Type.valueOf(FormatText.toCapitalized(json.getString("Type"))))
          .setNote(json.getString("Content"))
          .setCreationUsername(json.getString("Email").substring(0, json.getString("Email").indexOf("@")))
          .setCreationTime(parseDate(json.getString("Creation Time")));
    } catch (Exception e) {
      throw new DD4StorageException("Error parsing json: " + json, e);
    }
  }

  public static void main(String[] args) {
    DAO dao = new DAOApiImpl(new APIConnector("https://ip360-179401.appspot.com/_api", "v1").loadIdToken());
    ImmutableList<Note> notes = new NoteImporter(new EmployeeImporter()).process();
    notes.forEach(System.out::println);
    System.out.printf("Total size: %d\n", notes.size());
    System.out.println(notes.stream().collect(Collectors.groupingBy(Note::getEntityType, Collectors.counting())));
    notes.stream().filter(n -> "User".equals(n.getEntityType()))
        .peek(l -> System.out.println("Inserting Note: " + l.getId())).forEach(dao::create);
  }
}
