import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PMConstants } from './common/PMConstants';
import { NotFoundComponent } from './components/not-found/not-found.component';


const routes: Routes = [
  { 
    path: '',
    loadChildren: () => import('./authentication-module/authentication.module').then(m => m.AuthenticationModuleModule)
  },
  {
    path: PMConstants.AUTHENTICATION_MODULE_BASE_URI,
    loadChildren: () => import('./authentication-module/authentication.module').then(m => m.AuthenticationModuleModule)
  },
  {
    path: PMConstants.PROJECT_MODULE_BASE_URI,
    loadChildren: () => import('./project/project.module').then(m => m.ProjectModule)
  },
  {
    path: 'notFound', component: NotFoundComponent
  },
  {
    path: '**',
    redirectTo: '/notFound'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
