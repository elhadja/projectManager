import { HttpErrorResponse } from '@angular/common/http';
import { Injectable, Injector } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { MessageService } from './message.service';
import { sessionManagerService } from './sessionManager.service';

@Injectable()
export class AppErrorHandler {
  public constructor(private readonly messageService: MessageService,
    private readonly injector: Injector) {}
    
  public handleApiRequestError = (error: HttpErrorResponse): Observable<never> => {
    if (error.status === 401) {
      const sessionService: sessionManagerService = this.injector.get(sessionManagerService);
      if (sessionService.hasExpired()) {
        this.messageService.showErrorMessage('Your session expired, please reconnecte');
        sessionService.logout();
      } else {
        this.messageService.showErrorMessage(error.error.message);
      }
    } else if (error.status === 500) {
      this.messageService.showErrorMessage('Internal server error, please contact an administrator');
    } else if (error.status === 0) {
      console.log(error.error);
      this.messageService.showErrorMessage('Server is down, contact an administrator');
    }
    else {
      this.messageService.showErrorMessage(error.error.message);
    }

    return throwError(error);
  };
}