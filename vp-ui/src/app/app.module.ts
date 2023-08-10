import {LOCALE_ID, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ServiceWorkerModule} from '@angular/service-worker';
import {environment} from '../environments/environment';
import {DiscoverComponent} from './components/discover/discover.component';
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {LogoComponent} from './components/logo/logo.component';
import {MatIconModule} from "@angular/material/icon";
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {HeaderComponent} from './components/discover/header/header.component';
import {DiscoverMainComponent} from './components/discover/discover-main/discover-main.component';
import {NavbarComponent} from './components/discover/navbar/navbar.component';
import {LoginComponent} from './components/auth/login/login.component';
import {RegisterComponent} from './components/auth/register/register.component';

// Google auth
import {
  GoogleLoginProvider,
  GoogleSigninButtonModule,
  SocialAuthServiceConfig,
  SocialLoginModule
} from '@abacritt/angularx-social-login';
import {InputComponent} from './components/common/input/input.component';
import {CheckComponent} from './components/common/check/check.component';
import {ButtonComponent} from './components/common/button/button.component';
import {ReactiveFormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {CookieSetterInterceptor} from "./interceptor/cookie-setter.interceptor";
import {CookieService} from "ngx-cookie-service";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {ConfirmComponent} from './components/auth/confirm/confirm.component';
import {ApplicationComponent} from './components/application/application.component';
import {ProjectComponent} from './components/application/project/project.component';
import {DashboardComponent} from './components/application/dashboard/dashboard.component';
import {SettingsComponent} from './components/application/settings/settings.component';
import {CreateProjectComponent} from './components/application/create-project/create-project.component';

import {DatePipe, NgOptimizedImage, registerLocaleData} from '@angular/common';
import localeFr from '@angular/common/locales/fr';
import {
  ProjectDashboardComponent
} from './components/application/project/project-dashboard/project-dashboard.component';
import {ProjectTasksComponent} from './components/application/project/project-tasks/project-tasks.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import {
  AddUserDialogComponent
} from './components/application/project/project-dashboard/add-user-dialog/add-user-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import {StatusPipe} from './pipes/status.pipe';
import {
  AddGoalDialogComponent
} from './components/application/project/project-dashboard/add-goal-dialog/add-goal-dialog.component';
import {SelectComponent} from './components/common/select/select.component';
import {EditGoalComponent} from './components/application/project/project-dashboard/edit-goal/edit-goal.component';
import {
  AddTaskDialogComponent
} from './components/application/project/project-dashboard/edit-goal/add-task-dialog/add-task-dialog.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {TaskPreviewComponent} from './components/application/project/common/task-preview/task-preview.component';
import {LabelListComponent} from './components/application/project/common/label-list/label-list.component';
import {LabelComponent} from './components/application/project/common/label/label.component';
import {MatMenuModule} from "@angular/material/menu";
import {ProjectSettingsComponent} from './components/application/project/project-settings/project-settings.component';
import {
  AddLabelDialogComponent
} from './components/application/project/project-settings/add-label-dialog/add-label-dialog.component';
import {ConfirmDialogComponent} from './components/common/confirm-dialog/confirm-dialog.component';
import {FirstLettersPipe} from './pipes/first-letters.pipe';
import {MyAccountComponent} from './components/application/my-account/my-account.component';
import {EmailPrivacyPipe} from './pipes/email-privacy.pipe';
import {FileUploadComponent} from './components/common/file-upload/file-upload.component';
import {FileUploadDirective} from './directives/file-upload.directive';
import {CalendarComponent} from './components/application/calendar/calendar.component';
import {TodoComponent} from './components/application/todo/todo.component';
import {SwitcherComponent} from './components/common/switcher/switcher.component';
import {WeekViewComponent} from './components/application/calendar/week-view/week-view.component';
import {DayViewComponent} from './components/application/calendar/day-view/day-view.component';
import {MonthViewComponent} from './components/application/calendar/month-view/month-view.component';
import {
  CreateEventDialogComponent
} from './components/application/calendar/week-view/create-event-dialog/create-event-dialog.component';
import {ApiResponseInterceptor} from "./interceptor/api-response.interceptor";

registerLocaleData(localeFr);

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
    ConfirmComponent,
    ApplicationComponent,
    ProjectComponent,
    DashboardComponent,
    SettingsComponent,
    CreateProjectComponent,
    ProjectDashboardComponent,
    ProjectTasksComponent,
    AddUserDialogComponent,
    StatusPipe,
    AddGoalDialogComponent,
    SelectComponent,
    EditGoalComponent,
    EditGoalComponent,
    AddTaskDialogComponent,
    TaskPreviewComponent,
    LabelListComponent,
    LabelComponent,
    ProjectSettingsComponent,
    AddLabelDialogComponent,
    ConfirmDialogComponent,
    FirstLettersPipe,
    MyAccountComponent,
    EmailPrivacyPipe,
    FileUploadComponent,
    FileUploadDirective,
    CalendarComponent,
    TodoComponent,
    SwitcherComponent,
    WeekViewComponent,
    DayViewComponent,
    MonthViewComponent,
    CreateEventDialogComponent,
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
    MatProgressSpinnerModule,
    MatTooltipModule,
    MatDialogModule,
    DragDropModule,
    MatMenuModule,
    NgOptimizedImage,
    GoogleSigninButtonModule
  ],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(environment.googleClientId)
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
    { provide: LOCALE_ID, useValue: 'fr-FR'},
    {
      provide: HTTP_INTERCEPTORS,
      useClass: CookieSetterInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ApiResponseInterceptor,
      multi: true
    },
    CookieService,
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
