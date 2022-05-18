import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { UserDTO as UserDTO } from '../dto/user.dto';
import { LoginInputDTO } from '../dto/login.input.interface';
import { API } from '../services/Api';

@Injectable()
export class UserApiService {
  private readonly baseURI = '/users';
  private readonly host = environment.host;

  constructor(private readonly api: API) {}

  public generateTokenForPasswordReinitialisation(email: string): Observable<void> {
    return this.api.post(this.baseURI + '/passwordReinitialisationToken', {email, url: this.host + '/password-update'});
  } 

  public reinitializePassword(password: string, token: string): Observable<void> {
    return this.api.post(this.baseURI + '/reinitializePassword/' + token, password);
  } 

  public loginWithGoogle(googleTokenId: string): Observable<LoginInputDTO> {
    return this.api.post(this.baseURI + '/loginWithGoogle', googleTokenId);
  }

  public getUserById(userId: number): Observable<UserDTO> {
    return this.api.get(this.baseURI + '/' + userId);
  }

  public updateUser(user: UserDTO, userId: number): Observable<void> {
    return this.api.put(this.baseURI + '/' + userId, user);
  }
}