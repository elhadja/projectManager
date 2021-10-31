import { Summary } from "@angular/compiler";

export interface GetUserStoriesInputDTO {
    id: number,
    summary: string,
    description?: string,
    storyPoint?: string
}