import { Summary } from "@angular/compiler";
import { GetTaskInputDTO } from "./getTask.input.dto";

export interface GetUserStoriesInputDTO {
    id: number,
    summary: string,
    description?: string,
    storyPoint?: number
    status?: string,
    importance?: string,
    tasks: GetTaskInputDTO[]
}