package com.digitald4.iis.server;

import com.digitald4.common.model.Company;
import com.digitald4.common.model.Flag;
import com.digitald4.common.server.service.ChangeHistoryService;
import com.digitald4.common.server.service.Echo;
import com.digitald4.common.server.service.FlagService;
import com.digitald4.common.server.service.GeneralDataService;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.ProviderThreadLocalImpl;
import com.digitald4.iis.model.*;
import com.digitald4.iis.storage.FlagStore;
import com.google.common.collect.ImmutableList;
import com.google.inject.TypeLiteral;
import java.time.Duration;

public class EndPointsModule extends com.digitald4.common.server.EndPointsModule {

  public EndPointsModule() {
    super("ip360-179401");
  }

  @Override
  public void configureServlets() {
    super.configureServlets();

    bind(Company.class).toInstance(new Company().setName("Infusion Partners 360, LLC")
        .setAddress("7056 Archibald 102-375\nCorona, CA 92880").setEmail("info@infusionpartners360.com")
        .setPhone("(866) 714-3955").setWebsite("www.infusionpartners360.com"));

    bind(Duration.class).annotatedWith(Annotations.SessionDuration.class).toInstance(Duration.ofHours(8));
    bind(Boolean.class).annotatedWith(Annotations.SessionCacheEnabled.class).toInstance(false);

    ProviderThreadLocalImpl<User> userProvider = new ProviderThreadLocalImpl<>();
    bind(com.digitald4.common.model.User.class).toProvider(userProvider);
    bind(User.class).toProvider(userProvider);
    bind(new TypeLiteral<ProviderThreadLocalImpl<User>>(){}).toInstance(userProvider);
    UserStore<User> userStore = new GenericUserStore<>(User.class, getProvider(DAO.class));
    bind(new TypeLiteral<UserStore<User>>(){}).toInstance(userStore);
    bind(new TypeLiteral<UserStore<? extends com.digitald4.common.model.User>>(){}).toInstance(userStore);
    bind(LoginResolver.class).to(new TypeLiteral<SessionStore<User>>(){});

    bind(new TypeLiteral<Store<Flag, String>>(){}).to(FlagStore.class);

    bind(new TypeLiteral<Store<Note, Long>>(){}).to(new TypeLiteral<GenericLongStore<Note>>(){});

    bind(SearchIndexer.class).to(SearchIndexerAppEngineImpl.class);

    configureEndpoints(
        getApiUrlPattern(),
        ImmutableList.of(
            AppointmentService.class,
            ChangeHistoryService.class,
            Echo.class,
            ExportService.class,
            FlagService.class,
            FileService.class,
            GeneralDataService.class,
            InvoiceService.class,
            LicenseService.class,
            NoteService.class,
            NotificationService.class,
            NurseService.class,
            PatientService.class,
            PaystubService.class,
            QuickBooksExportService.class,
            SearchService.class,
            ServiceCodeService.class,
            UserService.class,
            VendorService.class));
  }
}
