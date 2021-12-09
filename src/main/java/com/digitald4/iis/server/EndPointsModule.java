package com.digitald4.iis.server;

import com.digitald4.common.server.service.Echo;
import com.digitald4.common.server.service.GeneralDataService;
import com.digitald4.common.storage.*;
import com.digitald4.common.util.ProviderThreadLocalImpl;
import com.digitald4.iis.model.*;
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

    bind(Duration.class).annotatedWith(Annotations.SessionDuration.class).toInstance(Duration.ofHours(8));
    bind(Boolean.class).annotatedWith(Annotations.SessionCacheEnabled.class).toInstance(false);

    ProviderThreadLocalImpl<User> userProvider = new ProviderThreadLocalImpl<>();
    bind(User.class).toProvider(userProvider);
    bind(new TypeLiteral<ProviderThreadLocalImpl<User>>(){}).toInstance(userProvider);
    bind(new TypeLiteral<UserStore<User>>(){}).toInstance(new GenericUserStore<>(User.class, getProvider(DAO.class)));

    bind(new TypeLiteral<Store<License>>(){}).to(new TypeLiteral<GenericStore<License>>(){});
    bind(new TypeLiteral<Store<Patient>>(){}).to(new TypeLiteral<GenericStore<Patient>>(){});
    bind(new TypeLiteral<Store<Paystub>>(){}).to(new TypeLiteral<GenericStore<Paystub>>(){});
    bind(new TypeLiteral<Store<Vendor>>(){}).to(new TypeLiteral<GenericStore<Vendor>>(){});

    bind(GeneralDataService.class).to(new TypeLiteral<GeneralDataService<User>>(){});

    configureEndpoints(
        getApiUrlPattern(),
        ImmutableList.of(
            AppointmentService.class,
            Echo.class,
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
