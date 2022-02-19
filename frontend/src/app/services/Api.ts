import { HttpClient, HttpErrorResponse, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, throwError } from "rxjs";
import { catchError } from 'rxjs/operators';
import { environment } from "src/environments/environment";
import { PMConstants } from "../common/PMConstants";
import { MessageService } from "./message.service";

@Injectable()
export class API {
    private baseURI: string;
    private httpOptions: {
        headers?: HttpHeaders;
    };

    constructor(private httpClient: HttpClient, private messageService: MessageService) {
        this.baseURI = environment.api_base_uri;
        this.httpOptions = {
            headers: new HttpHeaders({
                'Content-Type': 'application/json',
                'Accept-Language': 'fr', // TODO allow user to set language
                'Authorization': ''
            })
        };
        let token = localStorage.getItem(PMConstants.SESSION_TOKEN_ID_KEY);
        if (token !== null) {
            this.setHttpOptions(token);
        }
    }

    public postWithoutHeaders(uri: string, body: any): Observable<any> {
        return this.httpClient.post(this.baseURI + uri, body)
                    .pipe(
                        catchError(this.f)
                    );
    }

    public post(uri: string, body: any): Observable<any> {
        return this.httpClient.post(this.baseURI + uri, body, this.httpOptions)
                    .pipe(
                        catchError(this.f)
                    );
    }


    public put(uri: string, body: any): Observable<any> {
        return this.httpClient.put(this.baseURI + uri, body, this.httpOptions).pipe(
            catchError(this.f)
        );
    }

    public get(uri: string): Observable<any> {
        return this.httpClient.get(this.baseURI + uri, this.httpOptions).pipe(
            catchError(this.f)
        );
    }
    public delete(uri: string): Observable<any> {
        return this.httpClient.delete(this.baseURI + uri, this.httpOptions).pipe(
            catchError(this.f)
        );
    }

    public setHttpOptions(token: string) {
        this.httpOptions.headers = this.httpOptions.headers?.set("Authorization", "Bearer " + token);
    }

    private f = (error: HttpErrorResponse): Observable<never> => {
        if (error.status === 401 && !!error.error) {
            this.messageService.showErrorMessage(error.error.message);
        } else if (error.status === 500) {
            this.messageService.showErrorMessage("Internal server error, please contact an administrator");
        } else {
            this.messageService.showErrorMessage(error.error.message);
        }

        return throwError(error);
    }
}