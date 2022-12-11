import { NgModule } from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import { DiscoverComponent } from "./components/discover/discover.component";
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./components/auth/login/login.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {ConfirmComponent} from "./components/auth/confirm/confirm.component";
import {CanConfirmAccount} from "./guards/can-confirm-account.service";
import {ApplicationComponent} from "./components/application/application.component";
import {IsLoggedInGuard} from "./guards/is-logged-in.guard";
import {LoginResolverService} from "./services/resolvers/login-resolver.service";
import {ProjectComponent} from "./components/application/project/project.component";
import {DashboardComponent} from "./components/application/dashboard/dashboard.component";
import {SettingsComponent} from "./components/application/settings/settings.component";
import {CreateProjectComponent} from "./components/application/create-project/create-project.component";

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled'
};

const routes: Routes = [
  {
    path: '',
    redirectTo: 'app',
    pathMatch: 'full'
  },
  {
    path: 'discover',
    component: DiscoverComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'confirm',
    component: ConfirmComponent,
    canActivate: [CanConfirmAccount],
    canLoad: [CanConfirmAccount]
  },
  {
    path: 'app',
    component: ApplicationComponent,
    resolve: {
      loggedIn: LoginResolverService
    },
    canLoad: [IsLoggedInGuard],
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'create-project',
        component: CreateProjectComponent
      },
      {
        path: 'projects/:id',
        component: ProjectComponent
        // TODO: Add project resolver and guard
      },
      {
        path: 'settings',
        component: SettingsComponent
      }
    ]
  }
];

@NgModule({
  imports: [CommonModule, RouterModule.forRoot(routes, routerOptions)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
