import {forwardRef, NgModule} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { DiscoverComponent } from './components/discover/discover.component';
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import { LogoComponent } from './components/logo/logo.component';
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import { HeaderComponent } from './components/discover/header/header.component';
import { DiscoverMainComponent } from './components/discover/discover-main/discover-main.component';
import { NavbarComponent } from './components/discover/navbar/navbar.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';

// Google auth
import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider
} from '@abacritt/angularx-social-login';
import { InputComponent } from './components/common/input/input.component';
import { CheckComponent } from './components/common/check/check.component';
import { ButtonComponent } from './components/common/button/button.component';
import {NG_VALUE_ACCESSOR, ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {CookieSetterInterceptor} from "./interceptor/cookie-setter.interceptor";
import {CookieService} from "ngx-cookie-service";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import { ConfirmComponent } from './components/auth/confirm/confirm.component';

@NgModule({
  declarations: [
    AppComponent,
    DiscoverComponent,
    LogoComponent,
    HeaderComponent,
    DiscoverMainComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    InputComponent,
    CheckComponent,
    ButtonComponent,
    ConfirmComponent
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        BrowserAnimationsModule,
        ServiceWorkerModule.register('ngsw-worker.js', {
            enabled: environment.production,
            // Register the ServiceWorker as soon as the application is stable
            // or after 30 seconds (whichever comes first).
            registrationStrategy: 'registerWhenStable:30000'
        }),
        MatCardModule,
        MatButtonModule,
        MatIconModule,
        MatToolbarModule,
        MatSidenavModule,
        MatListModule,
        SocialLoginModule,
        ReactiveFormsModule,
        HttpClientModule,
        MatProgressSpinnerModule
    ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('1052954350067-sersub6hdqgbl01nq0fhohnsodrhelcc.apps.googleusercontent.com')
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    { provide: HTTP_INTERCEPTORS, useClass: CookieSetterInterceptor, multi: true },
    CookieService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
