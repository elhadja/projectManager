import { CustomRevisionEntityDTO } from "./custom-revision-entity.dto"

export interface GetTaskInputDTO {
    id: number,
    userStoriesIDs: number[],
    description: string,
    duration?: number,
    definitionOfDone?: string
    userPseudo: string,
    dependencies: {id: number}[]
    status: string,
    activities?: CustomRevisionEntityDTO[]
}