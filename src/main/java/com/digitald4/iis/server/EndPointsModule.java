package com.digitald4.iis.server;

import com.digitald4.common.server.service.ChangeHistoryService;
import com.digitald4.common.server.service.Echo;
import com.digitald4.common.server.service.FileService;
import com.digitald4.common.server.service.GeneralDataService;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.ProviderThreadLocalImpl;
import com.digitald4.iis.model.*;
import com.digitald4.iis.storage.SearchIndexerImpl;
import com.google.common.collect.ImmutableList;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import java.time.Duration;

public class EndPointsModule extends com.digitald4.common.server.EndPointsModule {

  public EndPointsModule() {
    super("ip360-179401");
  }

  @Override
  public void configureServlets() {
    super.configureServlets();

    bind(Duration.class).annotatedWith(Annotations.SessionDuration.class)
        .toInstance(Duration.ofHours(8));
    bind(Boolean.class).annotatedWith(Annotations.SessionCacheEnabled.class).toInstance(false);

    ProviderThreadLocalImpl<User> userProvider = new ProviderThreadLocalImpl<>();
    bind(com.digitald4.common.model.User.class).toProvider(userProvider);
    bind(User.class).toProvider(userProvider);
    bind(new TypeLiteral<ProviderThreadLocalImpl<User>>(){}).toInstance(userProvider);
    bind(new TypeLiteral<UserStore<User>>(){}).toInstance(
        new GenericUserStore<>(User.class, getProvider(DAO.class)));
    bind(LoginResolver.class).to(new TypeLiteral<SessionStore<User>>(){});

    bind(new TypeLiteral<Store<Paystub, Long>>(){})
        .to(new TypeLiteral<GenericLongStore<Paystub>>(){});
    bind(new TypeLiteral<Store<Vendor, Long>>(){})
        .to(new TypeLiteral<GenericLongStore<Vendor>>(){});

    bind(SearchIndexer.class).to(SearchIndexerImpl.class);
    // bind(UserService.class).to(new TypeLiteral<UserService<User>>(){});

    configureEndpoints(
        getApiUrlPattern(),
        ImmutableList.of(
            AppointmentService.class,
            ChangeHistoryService.class,
            Echo.class,
            FileService.class,
            GeneralDataService.class,
            InvoiceService.class,
            LicenseService.class,
            NotificationService.class,
            NurseService.class,
            PatientService.class,
            PaystubService.class,
            UserService.class,
            VendorService.class));
  }
}
