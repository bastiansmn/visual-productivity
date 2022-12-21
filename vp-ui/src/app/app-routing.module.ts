import {NgModule} from '@angular/core';
import {ExtraOptions, RouterModule, Routes} from '@angular/router';
import {DiscoverComponent} from "./components/discover/discover.component";
import {CommonModule} from '@angular/common';
import {LoginComponent} from "./components/auth/login/login.component";
import {RegisterComponent} from "./components/auth/register/register.component";
import {ConfirmComponent} from "./components/auth/confirm/confirm.component";
import {ApplicationComponent} from "./components/application/application.component";
import {ProjectComponent} from "./components/application/project/project.component";
import {DashboardComponent} from "./components/application/dashboard/dashboard.component";
import {SettingsComponent} from "./components/application/settings/settings.component";
import {CreateProjectComponent} from "./components/application/create-project/create-project.component";
import {IsLoggedInGuard} from "./guards/is-logged-in.guard";
import {
  ProjectDashboardComponent
} from "./components/application/project/project-dashboard/project-dashboard.component";
import {ProjectTasksComponent} from "./components/application/project/project-tasks/project-tasks.component";

const routerOptions: ExtraOptions = {
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
  paramsInheritanceStrategy: 'always',
};

const routes: Routes = [
  {
    path: 'login',
    component: LoginComponent
  },
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
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'confirm',
    component: ConfirmComponent
  },
  {
    path: 'app',
    component: ApplicationComponent,
    // TOOD: Si pas login, rediriger vers /discover, sinon continuer
    canActivate: [IsLoggedInGuard],
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
        component: ProjectComponent,
        children: [
          {
            path: '',
            redirectTo: 'dashboard',
            pathMatch: 'full'
          },
          {
            path: 'dashboard',
            component: ProjectDashboardComponent
          },
          {
            path: 'tasks',
            component: ProjectTasksComponent
          }
        ]
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
