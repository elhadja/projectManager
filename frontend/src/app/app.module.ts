import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MessageComponent } from './components/message/message.component';
import { API } from './services/Api';
import { MessageService } from './services/message.service';
import { HttpClientModule } from '@angular/common/http';
import { RouteSecureService } from './services/routeSecure.service';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { sessionManagerService } from './services/sessionManager.service';

const SERVICES = [API, MessageService, RouteSecureService, sessionManagerService];

@NgModule({
  declarations: [
    AppComponent,
    MessageComponent,
    NotFoundComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule
  ],
  providers: [...SERVICES],
  bootstrap: [AppComponent]
})
export class AppModule { }
