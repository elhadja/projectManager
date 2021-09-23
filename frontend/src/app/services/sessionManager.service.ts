import { Injectable } from "@angular/core";

@Injectable()
export class sessionManagerService {
    private userId: number

    constructor() {
        this.userId = -1;
    }

    public setUserid(id: number): void {
        this.userId = id;
    }

    public getUserId(): number {
        return this.userId;
    }
}