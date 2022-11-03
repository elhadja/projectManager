import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { PMConstants } from '../common/PMConstants';
import { AppErrorHandler } from './app_error_handler.service';
import { MessageService } from './message.service';

@Injectable()
export class API {
  private baseURI: string;
  private httpOptions: {
        headers?: HttpHeaders;
    };

  constructor(private httpClient: HttpClient, private messageService: MessageService,
    private readonly appErrorHandler: AppErrorHandler) {
    this.baseURI = environment.api_base_uri;
    this.httpOptions =  {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'Accept-Language': PMConstants.DEFAULT_LANG,
      })
    };

    const token = localStorage.getItem(PMConstants.SESSION_TOKEN_ID_KEY);
    this.setHttpOptions(token ?? '');
  }

  public postWithoutHeaders(uri: string, body: any): Observable<any> {
    return this.httpClient.post(this.baseURI + uri, body, {headers: this.httpOptions?.headers?.delete('Authorization')})
      .pipe(
        catchError(this.appErrorHandler.handleApiRequestError)
      );
  }

  public post(uri: string, body: any): Observable<any> {
    return this.httpClient.post(this.baseURI + uri, body, this.httpOptions)
      .pipe(
        catchError(this.appErrorHandler.handleApiRequestError)
      );
  }


  public put(uri: string, body: any): Observable<any> {
    return this.httpClient.put(this.baseURI + uri, body, this.httpOptions).pipe(
      catchError(this.appErrorHandler.handleApiRequestError)
    );
  }

  public get(uri: string): Observable<any> {
    return this.httpClient.get(this.baseURI + uri, this.httpOptions).pipe(
      catchError(this.appErrorHandler.handleApiRequestError)
    );
  }

  public getWithoutAuthorization(uri: string): Observable<any> {
    return this.httpClient.get(this.baseURI + uri, {headers: this.httpOptions?.headers?.delete('Authorization')}).pipe(
      catchError(this.appErrorHandler.handleApiRequestError)
    );
  }
  public delete(uri: string): Observable<any> {
    return this.httpClient.delete(this.baseURI + uri, this.httpOptions).pipe(
      catchError(this.appErrorHandler.handleApiRequestError)
    );
  }

  public setHttpOptions(token: string) {
    if (this.httpOptions.headers == null) {
      this.httpOptions = {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
          'Accept-Language': 'fr',
        })
      };
    } 

    if (token != null && token != '') {
      this.httpOptions.headers = this.httpOptions.headers?.set('Authorization', 'Bearer ' + token);
    }
  }

  public setLang(lang: string): void {
    this.httpOptions.headers = this.httpOptions.headers?.set('Accept-Language', lang);
  }

  public clearHeader(): void {
    if (this.httpOptions != null) {
      this.httpOptions.headers?.delete('Authorization');
    }
  }

}