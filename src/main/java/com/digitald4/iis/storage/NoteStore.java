package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;
import static java.lang.Long.parseLong;

import com.digitald4.common.model.ModelObject;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.storage.UserStore;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Note;
import com.digitald4.iis.model.User;
import com.google.common.collect.ImmutableMap;
import java.util.Objects;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Provider;

public class NoteStore extends GenericLongStore<Note> {
  private final UserStore<User> userStore;
  private final Provider<User> userProvider;
  private final EntityStore entityStore;

  @Inject
  public NoteStore(
      Provider<DAO> daoProvider, UserStore<User> userStore, Provider<User> userProvider, EntityStore entityStore) {
    super(Note.class, daoProvider);
    this.userStore = userStore;
    this.userProvider = userProvider;
    this.entityStore = entityStore;
  }

  @Override
  protected Iterable<Note> preprocess(Iterable<Note> notes, boolean isCreate) {
    ImmutableMap<Long, String> userNames = userStore
        .get(stream(notes)
            .peek(note -> {
              if (note.getCreationUserId() == null) {
                note.setCreationUsername(userProvider.get().getUsername()).setCreationUserId(userProvider.get().getId());
              }
            })
            .filter(note -> note.getCreationUsername() == null)
            .map(Note::getCreationUserId)
            .filter(Objects::nonNull)
            .collect(toImmutableSet()))
        .getItems().stream()
        .filter(user -> Objects.nonNull(user.getUsername()))
        .collect(toImmutableMap(User::getId, User::getUsername));

    ImmutableMap<String, String> entities = entityStore
        .getEntities(
            stream(notes)
                .map(n -> Pair.of(n.getEntityType(), Long.parseLong(n.getEntityId()))).collect(toImmutableSet()))
        .stream().collect(toImmutableMap(EntityStore::getEntityTypeId, ModelObject::toString));

    stream(notes).forEach(note -> {
      if (note.getCreationUsername() == null) {
        note.setCreationUsername(userNames.get(note.getCreationUserId()));
      }
      note.setEntityName(entities.get(note.getEntityType() + "-" + note.getEntityId()));
    });

    return notes;
  }
}
