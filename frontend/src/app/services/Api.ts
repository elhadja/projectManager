import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
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
    } | undefined;
  private accept_language = PMConstants.DEFAULT_LANG;

  constructor(private httpClient: HttpClient, private messageService: MessageService,
    private readonly appErrorHandler: AppErrorHandler) {
    this.baseURI = environment.api_base_uri;
    this.httpOptions = undefined;
    const token = localStorage.getItem(PMConstants.SESSION_TOKEN_ID_KEY);
    this.setHttpOptions(token ?? '');
  }

  public postWithoutHeaders(uri: string, body: any): Observable<any> {
    return this.httpClient.post(this.baseURI + uri, body)
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
  public delete(uri: string): Observable<any> {
    return this.httpClient.delete(this.baseURI + uri, this.httpOptions).pipe(
      catchError(this.appErrorHandler.handleApiRequestError)
    );
  }

  public setHttpOptions(token: string) {
    if (this.httpOptions == null || this.httpOptions.headers == null) {
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
    this.accept_language = lang;
  }

  public clearHeader(): void {
    if (this.httpOptions != null) {
      this.httpOptions.headers = undefined;
    }
  }

}