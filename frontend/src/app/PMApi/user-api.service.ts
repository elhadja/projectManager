import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { API } from '../services/Api';

@Injectable()
export class UserApiService {
  private readonly baseURI = '/users';

  constructor(private readonly api: API) {}

  public generateTokenForPasswordReinitialisation(email: string): Observable<void> {
    return this.api.post(this.baseURI + '/passwordReinitialisationToken', {email, url: 'http://localhost:4200/password-update'});
  } 

  public reinitializePassword(password: string, token: string): Observable<void> {
    return this.api.post(this.baseURI + '/reinitializePassword/' + token, password);
  } 
}