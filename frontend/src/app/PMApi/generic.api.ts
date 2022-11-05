import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProjectManagerOutputDTO } from '../dto/projectManagerOutput.dto';
import { API } from '../services/Api';

@Injectable()
export class GenericApi {
  private readonly baseURI = '/generic';

  constructor(private readonly api: API) {}

  public getApiInfos(): Observable<ProjectManagerOutputDTO> {
    return this.api.getWithoutAuthorization(this.baseURI);
  } 

}