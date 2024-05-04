package com.digitald4.iis.server;

import com.digitald4.common.storage.LoginResolver;
import com.digitald4.iis.model.Note;
import com.digitald4.iis.storage.NoteStore;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;
import javax.inject.Inject;

@Api(
    name = "notes",
    version = "v1",
    namespace = @ApiNamespace(ownerDomain = "iis.digitald4.com", ownerName = "iis.digitald4.com")
)
public class NoteService extends AdminService<Note> {
  @Inject
  NoteService(NoteStore store, LoginResolver loginResolver) {
    super(store, loginResolver);
  }
}
