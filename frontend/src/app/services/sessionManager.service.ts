import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { PMConstants } from "../common/PMConstants";
import { API } from "./Api";

@Injectable()
export class sessionManagerService {
    public userLoggedEmitter: Subject<boolean>;
    public projectSelectedSubject: Subject<void>;
    private readonly invalidId: number;

    constructor(private readonly api: API) {
        this.userLoggedEmitter = new Subject<boolean>();
        this.projectSelectedSubject = new Subject<void>();
        this.invalidId = -1;
    }

    public setUserid(id: number): void {
        localStorage.setItem(PMConstants.SESSION_USER_ID_KEY, `${id}`);
        this.userLoggedEmitter.next(true);
    }

    public getUserId(): number {
        const id = localStorage.getItem(PMConstants.SESSION_USER_ID_KEY);
        return id != null ? +id : -1;
    }

    public setProjectId(id: number): void {
        localStorage.setItem(PMConstants.SESSION_PROJECT_ID_KEY, `${id}`);
        this.projectSelectedSubject.next();
    }

    public getProjectId(): number {
        const id = localStorage.getItem(PMConstants.SESSION_PROJECT_ID_KEY);
        return id != null ? +id : -1;
    }

    public closeSession(): void {
        this.userLoggedEmitter.next(false);
        localStorage.clear();
        this.api.clearHeader();
    }
}