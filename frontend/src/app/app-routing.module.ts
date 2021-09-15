import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { 
    path: '',
    loadChildren: () => import('./authentication-module/authentication-module.module').then(m => m.AuthenticationModuleModule)
  },
  {
    path: 'auth',
    loadChildren: () => import('./authentication-module/authentication-module.module').then(m => m.AuthenticationModuleModule)
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
