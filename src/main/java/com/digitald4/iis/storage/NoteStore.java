package com.digitald4.iis.storage;

import static com.google.common.collect.ImmutableMap.toImmutableMap;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.google.common.collect.Streams.stream;
import static java.lang.Long.parseLong;

import com.digitald4.common.model.ModelObject;
import com.digitald4.common.storage.DAO;
import com.digitald4.common.storage.GenericLongStore;
import com.digitald4.common.util.Pair;
import com.digitald4.iis.model.Note;
import com.google.common.collect.ImmutableMap;
import javax.inject.Inject;
import javax.inject.Provider;

public class NoteStore extends GenericLongStore<Note> {
  private final EntityStore entityStore;

  @Inject
  public NoteStore(Provider<DAO> daoProvider, EntityStore entityStore) {
    super(Note.class, daoProvider);
    this.entityStore = entityStore;
  }

  @Override
  protected Iterable<Note> preprocess(Iterable<Note> notes, boolean isCreate) {
    super.preprocess(notes, isCreate);
    ImmutableMap<String, String> entityNames = entityStore
        .getEntities(
            stream(notes).map(n -> Pair.of(n.getEntityType(), parseLong(n.getEntityId()))).collect(toImmutableSet()))
        .stream().collect(toImmutableMap(EntityStore::getEntityTypeId, ModelObject::toString));

    stream(notes).forEach(note -> note.setEntityName(entityNames.get(note.getEntityType() + "-" + note.getEntityId())));

    return notes;
  }
}
