import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MessageComponent } from './components/message/message.component';
import { API } from './services/Api';
import { MessageService } from './services/message.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { RouteSecureService } from './services/routeSecure.service';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { sessionManagerService } from './services/sessionManager.service';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { ProjectApiService } from './PMApi/project.api';
import { MenubarModule } from 'primeng/menubar';
import { MenuBarComponent } from './components/menu-bar/menu-bar.component';
import { RoutingService } from './services/routing.service';
import { SharedModule } from 'primeng/api';
import {SlideMenuModule} from 'primeng/slidemenu';
import { UserApiService } from './PMApi/user-api.service';


const SERVICES = [API, 
  MessageService,
  RouteSecureService,
  sessionManagerService,
  ProjectApiService,
  RoutingService,
  UserApiService
];

@NgModule({
  declarations: [
    AppComponent,
    MessageComponent,
    NotFoundComponent,
    MenuBarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MenubarModule,
    SharedModule,
    SlideMenuModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    })
  ],
  providers: [...SERVICES],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http);
}