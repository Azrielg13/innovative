package com.digitald4.iis.server;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.Streams.stream;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.common.storage.Store;
import com.digitald4.common.storage.UserStore;
import com.digitald4.iis.model.Note;
import com.digitald4.iis.model.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@Api(
    name = "notes",
    version = "v1",
    namespace = @ApiNamespace(
        ownerDomain = "iis.digitald4.com",
        ownerName = "iis.digitald4.com"
    )
)
public class NoteService extends AdminService<Note> {
  private final UserStore<User> userStore;
  @Inject
  NoteService(Store<Note, Long> store, LoginResolver loginResolver, UserStore<User> userStore) {
    super(store, loginResolver);
    this.userStore = userStore;
  }

  @Override
  protected Iterable<Note> transform(Iterable<Note> entities) {
    Map<Long, String> userNamesById = new HashMap<>();
    return stream(super.transform(entities))
        .map(note ->
            note.setCreationUser(
                userNamesById.computeIfAbsent(
                    note.getCreationUserId(), userId -> userStore.get(userId).fullName())))
        .collect(toImmutableList());
  }
}
